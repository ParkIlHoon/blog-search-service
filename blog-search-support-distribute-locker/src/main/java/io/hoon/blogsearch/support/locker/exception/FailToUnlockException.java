package io.hoon.blogsearch.support.locker.exception;

public class FailToUnlockException extends RuntimeException {
    public FailToUnlockException(Throwable cause) {
        super(cause);
    }
}
