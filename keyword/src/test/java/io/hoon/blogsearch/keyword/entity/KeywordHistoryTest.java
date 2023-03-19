package io.hoon.blogsearch.keyword.entity;

import static org.junit.jupiter.api.Assertions.*;

import io.hoon.blogsearch.keyword.interfaces.exception.IllegalArgumentException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("검색어 이력 엔티티 단위 테스트")
class KeywordHistoryTest {

    @Nested
    @DisplayName("of 생성")
    class of {
        @Test
        @DisplayName("성공")
        void of_success() {
            // given
            final String name = "테스트";

            // when
            KeywordHistory keywordHistory = KeywordHistory.of(name);

            // then
            assertEquals(name, keywordHistory.getName());
            assertEquals(1, keywordHistory.getCount());
        }

        @Test
        @DisplayName("실패 : name is 빈문자열")
        void of_fail_blank() {
            // given
            final String name = "";

            // when, then
            assertThrows(IllegalArgumentException.class, () -> KeywordHistory.of(name));
        }

        @Test
        @DisplayName("실패 : name is null")
        void of_fail_null() {
            // given
            final String name = null;

            // when, then
            assertThrows(IllegalArgumentException.class, () -> KeywordHistory.of(name));
        }
    }

}