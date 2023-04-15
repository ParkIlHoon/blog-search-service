package io.hoon.blogsearch.support.locker.mock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import org.redisson.api.RFuture;
import org.redisson.api.RLock;

public class MockRLock implements RLock {

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void lockInterruptibly(long leaseTime, TimeUnit unit) throws InterruptedException {
        // just mock
    }

    @Override
    public boolean tryLock(long waitTime, long leaseTime, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void lock(long leaseTime, TimeUnit unit) {
        // just mock
    }

    @Override
    public boolean forceUnlock() {
        return false;
    }

    @Override
    public boolean isLocked() {
        return false;
    }

    @Override
    public boolean isHeldByThread(long threadId) {
        return false;
    }

    @Override
    public boolean isHeldByCurrentThread() {
        return false;
    }

    @Override
    public int getHoldCount() {
        return 0;
    }

    @Override
    public long remainTimeToLive() {
        return 0;
    }

    @Override
    public void lock() {
        // just mock
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        // just mock
    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {
        // just mock
    }

    @Override
    public Condition newCondition() {
        return null;
    }

    @Override
    public RFuture<Boolean> forceUnlockAsync() {
        return null;
    }

    @Override
    public RFuture<Void> unlockAsync() {
        return null;
    }

    @Override
    public RFuture<Void> unlockAsync(long threadId) {
        return null;
    }

    @Override
    public RFuture<Boolean> tryLockAsync() {
        return null;
    }

    @Override
    public RFuture<Void> lockAsync() {
        return null;
    }

    @Override
    public RFuture<Void> lockAsync(long threadId) {
        return null;
    }

    @Override
    public RFuture<Void> lockAsync(long leaseTime, TimeUnit unit) {
        return null;
    }

    @Override
    public RFuture<Void> lockAsync(long leaseTime, TimeUnit unit, long threadId) {
        return null;
    }

    @Override
    public RFuture<Boolean> tryLockAsync(long threadId) {
        return null;
    }

    @Override
    public RFuture<Boolean> tryLockAsync(long waitTime, TimeUnit unit) {
        return null;
    }

    @Override
    public RFuture<Boolean> tryLockAsync(long waitTime, long leaseTime, TimeUnit unit) {
        return null;
    }

    @Override
    public RFuture<Boolean> tryLockAsync(long waitTime, long leaseTime, TimeUnit unit, long threadId) {
        return null;
    }

    @Override
    public RFuture<Integer> getHoldCountAsync() {
        return null;
    }

    @Override
    public RFuture<Boolean> isLockedAsync() {
        return null;
    }

    @Override
    public RFuture<Long> remainTimeToLiveAsync() {
        return null;
    }
}
