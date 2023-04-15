package io.hoon.blogsearch.search.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

@DisplayName("네이버 파라미터 유틸 단위 테스트")
class NaverParamUtilsTest {

    @Nested
    @DisplayName("검색 시작 위치-페이지 계산")
    class calculateStartAndPage {

        @RepeatedTest(100)
        @DisplayName("성공")
        void success() {
            // given
            final int page = new Random().nextInt(10000);
            final int pageSize = new Random().nextInt(10000);

            // when
            int start = NaverParamUtils.calculateStart(page, pageSize);
            int checking = NaverParamUtils.calculatePage(start, pageSize);

            // then
            assertEquals(page, checking);
        }
    }


    @Nested
    @DisplayName("마지막 페이지 여부 계산")
    class calculateEnd {
        @Test
        @DisplayName("마지막 페이지일 경우")
        void lastPage() {
            // given
            final int display = 3;
            final long total = 28;
            final int page = 10;
            final int start = NaverParamUtils.calculateStart(page, display);

            // when
            boolean end = NaverParamUtils.calculateEnd(start, display, total);

            // then
            assertTrue(end);
        }

        @Test
        @DisplayName("마지막 페이지가 아닐 경우")
        void notLastPage() {
            // given
            final int display = 3;
            final long total = 28;
            final int page = 9;
            final int start = NaverParamUtils.calculateStart(page, display);

            // when
            boolean end = NaverParamUtils.calculateEnd(start, display, total);

            // then
            assertFalse(end);
        }
    }
}