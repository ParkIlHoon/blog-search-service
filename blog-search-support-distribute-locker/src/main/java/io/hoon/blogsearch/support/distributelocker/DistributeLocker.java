package io.hoon.blogsearch.support.distributelocker;

import io.hoon.blogsearch.support.distributelocker.exception.FailToLockException;
import io.hoon.blogsearch.support.distributelocker.exception.FailToUnlockException;

/**
 * <h1>분산 락커</h1>
 */
public interface DistributeLocker {

    /**
     * 분산 락을 생성합니다.
     *
     * @param key 락 키
     * @throws FailToLockException 락 생성 실패 시
     */
    void lock(String key) throws FailToLockException;

    /**
     * 분산 락을 해제합니다.
     *
     * @throws FailToUnlockException 락 해제 실패 시
     */
    void unlock() throws FailToUnlockException;

}
