package io.hoon.blogsearch.support.distributelocker.exception;

public class FailToLockException extends RuntimeException{
    public FailToLockException(Throwable cause) {
        super(cause);
    }
}
