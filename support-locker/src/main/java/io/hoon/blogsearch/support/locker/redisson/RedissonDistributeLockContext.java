package io.hoon.blogsearch.support.locker.redisson;

import java.util.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.redisson.api.RLock;

/**
 * <h1>Redisson 분산 락 컨텍스트</h1>
 * {@link org.redisson.api.RedissonClient}를 통해 획득한 {@link RLock}을 {@link ThreadLocal}에 저장하기 위한 클래스.
 * Thread 에 객체를 저장할 수 있으므로 thread-safe 하다.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RedissonDistributeLockContext {

    /**
     * <h3>RLock ThreadLocal 변수</h3>
     * 클라이언트에 사용 가능한 인터페이스를 제한하기 위해 접근 제어자를 private 로 선언
     */
    private static ThreadLocal<RLock> lockThreadLocal = new ThreadLocal<>();

    /**
     * {@link RLock Redisson Lock} 객체를 저장합니다.
     * @param rLock 획득한 Redisson Lock 객체
     */
    public static void put(RLock rLock) {
        lockThreadLocal.set(rLock);
    }

    /**
     * ThreadLocal에 저장된 {@link RLock Redisson Lock} 객체를 반환합니다.
     * @return 저장된 Redisson Lock 객체
     */
    public static RLock pop() {
        RLock rLock = lockThreadLocal.get();
        // 스레드풀 사용 환경에서 스레드 재활용 시 이전 값을 참조하지 않도록 하기 위해 remove 해야함
        lockThreadLocal.remove();
        return rLock;
    }

    /**
     * ThreadLocal이 비어있는지 여부를 반환합니다.
     * @return ThreadLocal에 RLock이 저장되어있는 경우 false, 그렇지 않은 경우 true
     */
    public static boolean isEmpty() {
        return Objects.isNull(lockThreadLocal.get());
    }
}
