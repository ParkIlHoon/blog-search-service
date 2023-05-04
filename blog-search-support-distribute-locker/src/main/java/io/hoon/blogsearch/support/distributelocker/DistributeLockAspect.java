package io.hoon.blogsearch.support.distributelocker;

import io.hoon.blogsearch.support.distributelocker.annotation.DistributeLock;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * <h1>분산 락 Aspect</h1>
 * @see DistributeLock
 */
@Aspect
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "application.distribute-lock", name = "enabled", havingValue = "true")
public class DistributeLockAspect {

    private final DistributeLocker distributeLocker;

    @Around("@annotation(distributeLockAnnotation)")
    public Object lock(ProceedingJoinPoint joinPoint, DistributeLock distributeLockAnnotation) throws Throwable {
        if (Objects.isNull(distributeLocker)) {
            return joinPoint.proceed();
        }

        // lock
        String joinPointName = joinPoint.getSignature().getName();
        distributeLocker.lock(joinPointName);

        Object result = null;
        try {
            // proceeding
            result = joinPoint.proceed();
        } finally {
            // unlock
            distributeLocker.unlock();
        }
        return result;
    }

}
