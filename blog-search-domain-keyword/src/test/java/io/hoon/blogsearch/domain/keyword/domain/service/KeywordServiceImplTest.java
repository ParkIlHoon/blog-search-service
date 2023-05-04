package io.hoon.blogsearch.domain.keyword.domain.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import io.hoon.blogsearch.domain.keyword.domain.entity.KeywordHistoryFixture;
import io.hoon.blogsearch.domain.keyword.interfaces.model.PopularKeyword;
import io.hoon.blogsearch.domain.keyword.domain.entity.KeywordHistory;
import io.hoon.blogsearch.domain.keyword.interfaces.exception.IllegalArgumentException;
import io.hoon.blogsearch.domain.keyword.interfaces.service.KeywordService;
import io.hoon.blogsearch.domain.keyword.domain.repository.KeywordHistoryRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DisplayName("검색어 서비스 단위 테스트")
@ExtendWith(MockitoExtension.class)
class KeywordServiceImplTest {

    @Mock
    KeywordHistoryRepository keywordHistoryRepository;

    KeywordService keywordService;

    @BeforeEach
    void setUp() {
        this.keywordService = new KeywordServiceImpl(keywordHistoryRepository);
    }

    @Nested
    @DisplayName("검색어 이력 생성")
    class createKeywordHistory {

        @Test
        @DisplayName("실패 : 검색어가 빈 문자열일 경우")
        void fail_keyword_blank() {
            // given
            final String keyword = "";

            // when, then
            assertThrows(IllegalArgumentException.class, () -> keywordService.createKeywordHistory(keyword));
        }

        @Test
        @DisplayName("실패 : 검색어가 null 일 경우")
        void fail_keyword_null() {
            // given
            final String keyword = null;

            // when, then
            assertThrows(IllegalArgumentException.class, () -> keywordService.createKeywordHistory(keyword));
        }
    }

    @Nested
    @DisplayName("인기 검색어 목록 조회")
    class getPopularKeywords {
        @Test
        @DisplayName("성공")
        void success() {
            // given
            final int size = 10;
            List<KeywordHistory> keywordHistories = KeywordHistoryFixture.getList(size);
            when(keywordHistoryRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(keywordHistories, PageRequest.ofSize(size), anyLong()));

            // when
            List<PopularKeyword> popularKeywords = keywordService.getPopularKeywords(size);

            // then
            assertTrue(popularKeywords.size() <= size);
            for (int i = 0; i < keywordHistories.size(); i++) {
                KeywordHistory keywordHistory = keywordHistories.get(i);
                PopularKeyword popularKeyword = popularKeywords.get(i);

                assertEquals(keywordHistory.getName(), popularKeyword.getName());
                assertEquals(keywordHistory.getCount(), popularKeyword.getCount());
            }
        }

        @Test
        @DisplayName("실패 : size 가 0 미만")
        void fail_under_zero_size() {
            // given
            final int size = -1;

            // when, then
            assertThrows(IllegalArgumentException.class, () -> keywordService.getPopularKeywords(size));
        }
    }

}