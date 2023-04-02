package io.hoon.blogsearch.support.locker.mock;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MockRLockFixture {

    public static MockRLock get() {
        return new MockRLock();
    }
}
