package io.hoon.blogsearch.support.locker.redisson;

import io.hoon.blogsearch.support.locker.exception.FailToLockException;
import io.hoon.blogsearch.support.locker.exception.FailToUnlockException;
import io.hoon.blogsearch.support.locker.DistributeLocker;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * <h1>Redisson 분산 락커</h1>
 * Redisson을 통해 분산 락을 수행합니다.
 */
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "application.distribute-lock.redisson", name = "enabled", havingValue = "true")
public class RedissonDistributeLocker implements DistributeLocker {

    private static final Long WAIT_TIME = 5L;

    private static final Long LEASE_TIME = 3L;

    private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;

    private final RedissonClient redissonClient;

    @Override
    public void lock(String key) throws FailToLockException {
        try {
            RLock rLock = redissonClient.getLock(key);
            /**
             * STUDY
             * 락을 획득할 때까지 계속해서 시도합니다.
             * 한 번 시도후 실패 시 false를 반환시키거나 예외를 던지면, 락 잡은 쓰레드의 작업이 길어지면 다른 쓰레드에서 계속해서 예외를 던질 수 밖에 없기 때문입니다.
             */
            while (!rLock.tryLock(WAIT_TIME, LEASE_TIME, TIME_UNIT)) {
                Thread.sleep(50);
            }
            RedissonDistributeLockContext.put(rLock);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new FailToLockException(e);
        }
    }

    @Override
    public void unlock() throws FailToUnlockException {
        try {
            if (RedissonDistributeLockContext.isEmpty()) {
                return;
            }

            RedissonDistributeLockContext.pop().unlock();
        } catch (Exception e) {
            throw new FailToUnlockException(e);
        }
    }
}
