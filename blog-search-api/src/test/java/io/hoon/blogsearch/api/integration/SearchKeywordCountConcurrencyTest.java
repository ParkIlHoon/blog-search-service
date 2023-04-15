package io.hoon.blogsearch.api.integration;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import io.hoon.blogsearch.keyword.domain.repository.KeywordHistoryRepository;
import io.hoon.blogsearch.keyword.interfaces.model.PopularKeyword;
import io.hoon.blogsearch.keyword.interfaces.service.KeywordService;
import java.util.List;
import java.util.Random;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("검색어 카운트 동시성 통합 테스트")
class SearchKeywordCountConcurrencyTest {

    @Autowired
    KeywordService keywordService;

    @Autowired
    KeywordHistoryRepository keywordHistoryRepository;

    @Autowired
    MockMvc mockMvc;


    @BeforeEach
    void setUp() {
        keywordHistoryRepository.deleteAll();
    }

    @Nested
    @DisplayName("시작점 : Service")
    class FromService {
        @Test
        @DisplayName("성공 - 멀티 쓰레드")
        void success() throws InterruptedException {
            // given
            final String keyword = "검색어";
            final int searchCount = new Random().nextInt(1, 100);
            final CountDownLatch countDownLatch = new CountDownLatch(searchCount);
            List<Thread> threads = Stream.generate(() -> new Thread(new TestThread(keyword, keywordService, countDownLatch))).limit(searchCount).toList();

            // when
            threads.forEach(Thread::start);
            countDownLatch.await();

            // then
            List<PopularKeyword> popularKeywords = keywordService.getPopularKeywords(10);
            assertEquals(1, popularKeywords.size());
            PopularKeyword popularKeyword = popularKeywords.get(0);
            assertEquals(keyword, popularKeyword.getName());
            assertEquals(searchCount, popularKeyword.getCount());

            log.debug("keyword : {}, actual : {}", keyword, popularKeyword.getName());
            log.debug("searchCount : {}, actual : {}", searchCount, popularKeyword.getCount());
        }

        @Test
        @DisplayName("성공 - 쓰레드풀")
        void success_threadPool() throws InterruptedException {
            // given
            final String keyword = "검색어";
            final int searchCount = new Random().nextInt(1, 100);
            final int threadPoolSize = 5;

            final ExecutorService executorService = Executors.newFixedThreadPool(threadPoolSize);
            final CountDownLatch countDownLatch = new CountDownLatch(searchCount);
            List<Thread> threads = Stream.generate(() -> new Thread(new TestThread(keyword, keywordService, countDownLatch))).limit(searchCount).toList();

            // when
            threads.forEach(executorService::execute);
            countDownLatch.await();

            // then
            List<PopularKeyword> popularKeywords = keywordService.getPopularKeywords(10);
            assertEquals(1, popularKeywords.size());
            PopularKeyword popularKeyword = popularKeywords.get(0);
            assertEquals(keyword, popularKeyword.getName());
            assertEquals(searchCount, popularKeyword.getCount());

            log.debug("keyword : {}, actual : {}", keyword, popularKeyword.getName());
            log.debug("searchCount : {}, actual : {}", searchCount, popularKeyword.getCount());
        }

        @RequiredArgsConstructor
        static class TestThread implements Runnable {

            private final String keyword;
            private final KeywordService keywordService;
            private final CountDownLatch countDownLatch;

            @Override
            public void run() {
                try {
                    log.debug("Thread : {}, Keyword : {}", Thread.currentThread().getName(), keyword);
                    keywordService.createKeywordHistory(keyword);
                } finally {
                    countDownLatch.countDown();
                }
            }
        }
    }

    @Nested
    @DisplayName("시작점 : API Call")
    class FromApiCall {
        @Test
        @DisplayName("성공")
        void success() throws Exception {
            // given
            final String keyword = "키워드";
            final int searchCount = new Random().nextInt(1, 100);
            final CountDownLatch countDownLatch = new CountDownLatch(searchCount);
            List<Thread> threads = Stream.generate(() -> new Thread(new TestThread(keyword, mockMvc, countDownLatch))).limit(searchCount).toList();

            // when
            threads.forEach(Thread::start);
            countDownLatch.await();

            // then
            List<PopularKeyword> popularKeywords = keywordService.getPopularKeywords(10);
            assertEquals(1, popularKeywords.size());
            PopularKeyword popularKeyword = popularKeywords.get(0);
            assertEquals(keyword, popularKeyword.getName());
            assertEquals(searchCount, popularKeyword.getCount());

            log.debug("keyword : {}, actual : {}", keyword, popularKeyword.getName());
            log.debug("searchCount : {}, actual : {}", searchCount, popularKeyword.getCount());
        }

        @RequiredArgsConstructor
        static class TestThread implements Runnable {

            private final String keyword;
            private final MockMvc mockMvc;
            private final CountDownLatch countDownLatch;

            @Override
            public void run() {
                try {
                    log.debug("Thread : {}, Keyword : {}", Thread.currentThread().getName(), keyword);
                    mockMvc.perform(get("/api/v1/blog/search").queryParam("keyword", keyword).queryParam("page", "1"));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    countDownLatch.countDown();
                }
            }
        }
    }
}
