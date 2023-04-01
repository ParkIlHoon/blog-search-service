package io.hoon.blogsearch.support.locker.exception;

public class FailToLockException extends RuntimeException{
    public FailToLockException(Throwable cause) {
        super(cause);
    }
}
