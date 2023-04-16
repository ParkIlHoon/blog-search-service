package io.hoon.blogsearch.support.distributelocker.exception;

public class FailToUnlockException extends RuntimeException {
    public FailToUnlockException(Throwable cause) {
        super(cause);
    }
}
