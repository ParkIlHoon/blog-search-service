package io.hoon.blogsearch.support.searchclient.mapper;

import static org.junit.jupiter.api.Assertions.*;

import io.hoon.blogsearch.support.searchclient.dto.KakaoApiResponseDto;
import io.hoon.blogsearch.support.searchclient.dto.KakaoApiResponseDtoFixture;
import io.hoon.blogsearch.support.searchclient.dto.NaverApiResponseDto;
import io.hoon.blogsearch.support.searchclient.dto.NaverApiResponseDtoFixture;
import io.hoon.blogsearch.support.searchclient.interfaces.model.BlogSearchPaging;
import io.hoon.blogsearch.support.searchclient.interfaces.model.BlogSearchResponse;
import io.hoon.blogsearch.support.searchclient.utils.NaverParamUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("블로그 검색 응답 매퍼 테스트")
class BlogSearchResponseMapperTest {

    @Test
    @DisplayName("카카오")
    void kakao() {
        // given
        final BlogSearchPaging paging = BlogSearchPaging.of(1);
        final KakaoApiResponseDto response = KakaoApiResponseDtoFixture.get(paging.getPageSize());

        // when
        BlogSearchResponse blogSearchResponse = BlogSearchResponseMapper.toBlogSearchResponse(response, paging);

        // then
        assertNotNull(blogSearchResponse);
        assertEquals(paging.getPage(), blogSearchResponse.getHeader().getPage());
        assertEquals(paging.getPageSize(), blogSearchResponse.getHeader().getPageSize());
    }

    @Test
    @DisplayName("네이버")
    void naver() {
        // given
        final BlogSearchPaging paging = BlogSearchPaging.of(1);
        final int start = NaverParamUtils.calculateStart(paging.getPage(), paging.getPageSize());
        final NaverApiResponseDto response = NaverApiResponseDtoFixture.get(10, start, paging.getPageSize());

        // when
        BlogSearchResponse blogSearchResponse = BlogSearchResponseMapper.toBlogSearchResponse(response);

        // then
        assertNotNull(blogSearchResponse);
        assertEquals(paging.getPage(), blogSearchResponse.getHeader().getPage());
        assertEquals(paging.getPageSize(), blogSearchResponse.getHeader().getPageSize());
    }
}