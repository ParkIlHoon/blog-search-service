package io.hoon.blogsearch.keyword.interfaces.model;

import static org.junit.jupiter.api.Assertions.*;

import io.hoon.blogsearch.keyword.interfaces.exception.IllegalArgumentException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("인기 검색어 모델 단위 테스트")
class PopularKeywordTest {

    @Nested
    @DisplayName("of 생성")
    class of {
        @Test
        @DisplayName("성공")
        void success() {
            // given
            final String name = "테스트";
            final long count = 100_000L;

            // when
            PopularKeyword popularKeyword = PopularKeyword.of(name, count);

            // then
            assertEquals(name, popularKeyword.getName());
            assertEquals(count, popularKeyword.getCount());
        }

        @Test
        @DisplayName("실패 : name is 빈 문자열")
        void fail_blank() {
            // given
            final String name = "";
            final long count = 100_000L;

            // when, then
            assertThrows(IllegalArgumentException.class, () -> PopularKeyword.of(name, count));
        }

        @Test
        @DisplayName("실패 : name is null")
        void fail_null() {
            // given
            final String name = null;
            final long count = 100_000L;

            // when, then
            assertThrows(IllegalArgumentException.class, () -> PopularKeyword.of(name, count));
        }

        @Test
        @DisplayName("실패 : 0 미만 count")
        void fail_under_zero_count() {
            // given
            final String name = "테스트";
            final long count = -1L;

            // when, then
            assertThrows(IllegalArgumentException.class, () -> PopularKeyword.of(name, count));
        }
    }

}