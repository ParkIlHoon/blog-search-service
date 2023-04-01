package io.hoon.blogsearch.support.locker.redisson;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import redis.embedded.RedisServer;

@DisplayName("Redisson 분산 락 테스트")
class RedissonDistributeLockerTest {

    private static RedisServer redisServer;

    @BeforeAll
    static void beforeAll() {
        redisServer = new RedisServer(16379);
        redisServer.start();
    }

    @AfterAll
    static void afterAll() {
        redisServer.stop();
    }

    @Test
    @DisplayName("멀티 쓰레드")
    void success() throws InterruptedException {
        // given
        final String key = "iam-lock-test-key";
        final int threadSize = 10;

        final CountDownLatch countDownLatch = new CountDownLatch(threadSize);
        List<Thread> threads = Stream.generate(() -> new Thread(new TestThread(key, countDownLatch))).limit(threadSize).toList();

        // when, then
        assertDoesNotThrow(() -> {
            threads.forEach(Thread::start);
            countDownLatch.await();
        });
    }

    @Test
    @DisplayName("쓰레드 풀")
    void threadPool() {
        // given
        final String key = "iam-lock-test-key";
        final int threadPoolSize = 3;
        final int testCount = 10;

        final CountDownLatch countDownLatch = new CountDownLatch(testCount);
        final ExecutorService executorService = Executors.newFixedThreadPool(threadPoolSize);
        List<Thread> threads = Stream.generate(() -> new Thread(new TestThread(key, countDownLatch))).limit(testCount).toList();

        // when, then
        assertDoesNotThrow(() -> {
            threads.forEach(executorService::execute);
            countDownLatch.await();
        });
    }


    @RequiredArgsConstructor
    static class TestThread implements Runnable {

        private final String key;
        private final CountDownLatch countDownLatch;

        @Override
        public void run() {
            Config config = new Config();
            config.useSingleServer().setAddress("redis://localhost:16379");
            RedissonClient redissonClient = Redisson.create(config);

            RedissonDistributeLocker locker = new RedissonDistributeLocker(redissonClient);
            locker.lock(key);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                locker.unlock();
            }

            countDownLatch.countDown();
        }
    }
}