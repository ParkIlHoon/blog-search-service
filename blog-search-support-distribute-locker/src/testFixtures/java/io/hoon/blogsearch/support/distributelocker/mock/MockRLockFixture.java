package io.hoon.blogsearch.support.distributelocker.mock;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MockRLockFixture {

    public static MockRLock get() {
        return new MockRLock();
    }
}
