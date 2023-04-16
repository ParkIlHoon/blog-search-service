package io.hoon.blogsearch.support.distributelocker.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <h1>분산 락 어노테이션</h1>
 * 이 어노테이션이 붙은 메서드는 Aspect 를 통해 메서드 전후로 분산 락을 수행합니다.
 * @see io.hoon.blogsearch.support.distributelocker.DistributeLockAspect
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributeLock {

}
