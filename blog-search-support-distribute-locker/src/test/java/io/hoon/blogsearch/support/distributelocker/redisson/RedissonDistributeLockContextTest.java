package io.hoon.blogsearch.support.distributelocker.redisson;

import static org.junit.jupiter.api.Assertions.*;

import io.hoon.blogsearch.support.distributelocker.mock.MockRLockFixture;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;

@Slf4j
@DisplayName("Redisson 분산 락 컨텍스트 테스트")
class RedissonDistributeLockContextTest {

    public static final ConcurrentMap<String, ContextAssertion> CONTEXT_MAP = new ConcurrentHashMap<>();

    @Nested
    @DisplayName("기본 환경")
    class Basic {

        @BeforeEach
        void setUp() {
            CONTEXT_MAP.clear();
        }

        @Test
        @DisplayName("정상 동작")
        void success() throws InterruptedException {
            // given
            final int threadSize = 10;
            final CountDownLatch countDownLatch = new CountDownLatch(threadSize);
            List<Thread> threads = Stream.generate(() -> new Thread(new TestThread(countDownLatch))).limit(threadSize).toList();

            // when
            threads.forEach(Thread::start);
            countDownLatch.await();

            // then
            Set<Integer> hashcodeSet = new HashSet<>();
            CONTEXT_MAP.forEach((threadName, assertion) -> {
                assertTrue(assertion.before);
                assertFalse(assertion.put);
                assertTrue(assertion.after);
                hashcodeSet.add(assertion.hashcode);
            });
            assertEquals(threadSize, hashcodeSet.size());
        }
    }

    @Nested
    @DisplayName("스레드풀 환경")
    class ThreadPool {
        @BeforeEach
        void setUp() {
            CONTEXT_MAP.clear();
        }

        @Test
        @DisplayName("정상 동작")
        void success() throws InterruptedException {
            // given
            final int threadPoolSize = 10;
            final int testCount = 40;
            final CountDownLatch countDownLatch = new CountDownLatch(testCount);
            final ExecutorService executorService = Executors.newFixedThreadPool(threadPoolSize);
            List<Thread> threads = Stream.generate(() -> new Thread(new TestThread(countDownLatch))).limit(testCount).toList();

            // when
            threads.forEach(executorService::execute);
            countDownLatch.await();

            // then
            Set<Integer> hashcodeSet = new HashSet<>();
            CONTEXT_MAP.forEach((threadName, assertion) -> {
                assertTrue(assertion.before);
                assertFalse(assertion.put);
                assertTrue(assertion.after);
                hashcodeSet.add(assertion.hashcode);
            });
            assertEquals(testCount, hashcodeSet.size());
        }
    }

    static class TestThread implements Runnable {
        private CountDownLatch countDownLatch;

        public TestThread(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            try {
                String threadName = Thread.currentThread().getName();
                RLock given = MockRLockFixture.get();

                // before
                boolean before = RedissonDistributeLockContext.isEmpty();
                log.trace("before ->> Thread : {} , isEmpty? {}", threadName, before);

                // put
                RedissonDistributeLockContext.put(given);
                boolean put = RedissonDistributeLockContext.isEmpty();
                log.trace("put ->> Thread : {} , isEmpty? {}", threadName, put);

                // after
                RLock pop = RedissonDistributeLockContext.pop();
                boolean after = RedissonDistributeLockContext.isEmpty();
                log.trace("after ->> Thread : {} , Context hash : {}", threadName, pop.hashCode());
                log.trace("after ->> Thread : {} , isEmpty? {}", threadName, after);

                CONTEXT_MAP.put(UUID.randomUUID().toString(), new ContextAssertion(before, put, after, pop.hashCode()));
            } finally {
                countDownLatch.countDown();
            }
        }
    }

    @RequiredArgsConstructor
    static class ContextAssertion {
        final boolean before;
        final boolean put;
        final boolean after;
        final int hashcode;
    }

}