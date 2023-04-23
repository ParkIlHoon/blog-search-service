package io.hoon.blogsearch.support.searchclient.interfaces.client;

import io.hoon.blogsearch.support.searchclient.contants.NaverApiContants;
import io.hoon.blogsearch.support.searchclient.dto.NaverApiResponseDto;
import io.hoon.blogsearch.support.searchclient.interfaces.exception.IllegalArgumentException;
import io.hoon.blogsearch.support.searchclient.interfaces.model.BlogSearchPaging;
import io.hoon.blogsearch.support.searchclient.interfaces.model.BlogSearchResponse;
import io.hoon.blogsearch.support.searchclient.mapper.BlogSearchResponseMapper;
import io.hoon.blogsearch.support.searchclient.utils.NaverParamUtils;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Objects;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * <h1>네이버 블로그 검색 클라이언트</h1>
 */
public class NaverBlogSearchClient implements BlogSearchClient {

    private final WebClient webClient;

    public NaverBlogSearchClient(String clientId, String clientSecret) {
        this.webClient = WebClient.builder()
            .baseUrl(NaverApiContants.URL)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader(NaverApiContants.CLIENT_ID_HEADER, clientId)
            .defaultHeader(NaverApiContants.CLIENT_SECRET_HEADER, clientSecret)
            .build();
    }

    @Override
    public Mono<BlogSearchResponse> search(String keyword, BlogSearchPaging paging) throws IllegalArgumentException {
        if (!StringUtils.hasText(keyword)) throw new IllegalArgumentException("검색어는 필수 값입니다.");
        if (Objects.isNull(paging)) throw new IllegalArgumentException("페이징은 필수 값입니다.");

        int display = constraintDisplay(paging.getPageSize());
        int start = NaverParamUtils.calculateStart(paging.getPage(), display);
        BlogSearchPaging constrainedPaging = BlogSearchPaging.of(constraintStart(start), display, paging.getSort());
        return this.webClient.get()
            .uri(builder -> builder
                .queryParam(NaverApiContants.PARAM_QUERY, constraintKeyword(keyword))
                .queryParam(NaverApiContants.PARAM_SORT, constrainedPaging.getSort().getNaver())
                .queryParam(NaverApiContants.PARAM_START, constrainedPaging.getPage())
                .queryParam(NaverApiContants.PARAM_DISPLAY, constrainedPaging.getPageSize())
                .build())
            .retrieve()
            .bodyToMono(NaverApiResponseDto.class)
            .map(BlogSearchResponseMapper::toBlogSearchResponse);
    }

    /**
     * 네이버 블로그 검색 API 질의어 제약 조건을 적용합니다.
     *
     * @param original 기존 값
     * @return 제약 조건이 적용된 값
     */
    private String constraintKeyword(String original) {
        try {
            return URLEncoder.encode(original, NaverApiContants.CONSTRAINT_QUERY_CHARSET);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("검색어를 UTF-8로 URL Encoding 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 네이버 블로그 검색 API 검색 시작 위치 제약 조건을 적용합니다.
     *
     * @param original 기존 값
     * @return 제약 조건이 적용된 값
     */
    private int constraintStart(int original) {
        if (original < NaverApiContants.CONSTRAINT_START_MIN) return NaverApiContants.CONSTRAINT_START_MIN;
        if (original > NaverApiContants.CONSTRAINT_START_MAX) return NaverApiContants.CONSTRAINT_START_MAX;
        return original;
    }

    /**
     * 네이버 블로그 검색 API 한 번에 표시할 검색 결과 개수 제약 조건을 적용합니다.
     *
     * @param original 기존 값
     * @return 제약 조건이 적용된 값
     */
    private int constraintDisplay(int original) {
        if (original < NaverApiContants.CONSTRAINT_DISPLAY_MIN) return NaverApiContants.CONSTRAINT_DISPLAY_MIN;
        if (original > NaverApiContants.CONSTRAINT_DISPLAY_MAX) return NaverApiContants.CONSTRAINT_DISPLAY_MAX;
        return original;
    }
}
