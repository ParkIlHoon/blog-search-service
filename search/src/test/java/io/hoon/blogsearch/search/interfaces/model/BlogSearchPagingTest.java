package io.hoon.blogsearch.search.interfaces.model;

import static org.junit.jupiter.api.Assertions.*;

import io.hoon.blogsearch.search.interfaces.enums.Sort;
import io.hoon.blogsearch.search.interfaces.exception.IllegalArgumentException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("블로그 검색 페이징 모델 단위 테스트")
class BlogSearchPagingTest {

    @Nested
    @DisplayName("of 생성")
    class of {

        private final Random random = new Random();

        @BeforeEach
        void setUp() {
            random.setSeed(System.currentTimeMillis());
        }

        @Test
        @DisplayName("성공 : page 인자")
        void success_only_page() {
            // given
            final int page = random.nextInt(50);

            // when
            BlogSearchPaging paging = BlogSearchPaging.of(page);

            // then
            assertEquals(page, paging.getPage());
        }

        @Test
        @DisplayName("성공 : page, sort 인자")
        void success_page_sort() {
            // given
            final int page = random.nextInt(50);
            final Sort sort = getRandom();

            // when
            BlogSearchPaging paging = BlogSearchPaging.of(page, sort);

            // then
            assertEquals(page, paging.getPage());
            assertEquals(sort, paging.getSort());
        }

        @Test
        @DisplayName("성공 : page, pageSize, sort 인자")
        void success_page_sort_size() {
            // given
            final int page = random.nextInt(50);
            final int pageSize = random.nextInt(50);
            final Sort sort = getRandom();

            // when
            BlogSearchPaging paging = BlogSearchPaging.of(page, pageSize, sort);

            // then
            assertEquals(page, paging.getPage());
            assertEquals(pageSize, paging.getPageSize());
            assertEquals(sort, paging.getSort());
        }

        @Test
        @DisplayName("실패 : page 최소값 미만")
        void fail_less_than_page_min() {
            // given
            final int page = 0;

            // when, then
            assertThrows(IllegalArgumentException.class, () -> BlogSearchPaging.of(page));
        }

        @Test
        @DisplayName("실패 : page 최대값 미만")
        void fail_more_than_page_min() {
            // given
            final int page = 51;

            // when, then
            assertThrows(IllegalArgumentException.class, () -> BlogSearchPaging.of(page));
        }

        @Test
        @DisplayName("실패 : sort is null")
        void fail_sort_null() {
            // given
            final int page = random.nextInt(50);
            final Sort sort = null;

            // when, then
            assertThrows(IllegalArgumentException.class, () -> BlogSearchPaging.of(page, sort));
        }

        @Test
        @DisplayName("실패 : pageSize 최소값 미만")
        void fail_less_than_pagesize_min() {
            // given
            final int page = random.nextInt(50);
            final int pageSize = 0;
            final Sort sort = getRandom();

            // when, then
            assertThrows(IllegalArgumentException.class, () -> BlogSearchPaging.of(page, pageSize, sort));
        }

        @Test
        @DisplayName("실패 : pageSize 최대값 미만")
        void fail_more_than_pagesize_min() {
            // given
            final int page = random.nextInt(50);
            final int pageSize = 51;
            final Sort sort = getRandom();

            // when, then
            assertThrows(IllegalArgumentException.class, () -> BlogSearchPaging.of(page, pageSize, sort));
        }

        private Sort getRandom() {
            List<Sort> sorts = Arrays.asList(Sort.values());
            Collections.shuffle(sorts);
            return sorts.get(0);
        }
    }
}