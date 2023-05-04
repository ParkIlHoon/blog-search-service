package io.hoon.blogsearch.support.searchclient.interfaces.client;

import io.hoon.blogsearch.support.searchclient.contants.KakaoApiConstants;
import io.hoon.blogsearch.support.searchclient.dto.KakaoApiResponseDto;
import io.hoon.blogsearch.support.searchclient.interfaces.exception.IllegalArgumentException;
import io.hoon.blogsearch.support.searchclient.interfaces.model.BlogSearchPaging;
import io.hoon.blogsearch.support.searchclient.interfaces.model.BlogSearchResponse;
import io.hoon.blogsearch.support.searchclient.mapper.BlogSearchResponseMapper;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Objects;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * <h1>카카오 블로그 검색 클라이언트</h1>
 */
public class KakaoBlogSearchClient implements BlogSearchClient {

    private final WebClient webClient;

    public KakaoBlogSearchClient(String apiKey) {
        this.webClient = WebClient.builder()
            .baseUrl(KakaoApiConstants.URL)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader(KakaoApiConstants.AUTH_HEADER, KakaoApiConstants.AUTH_HEADER_VALUE_PREFIX + apiKey)
            .build();
    }

    @Override
    public Mono<BlogSearchResponse> search(String keyword, BlogSearchPaging paging) throws IllegalArgumentException {
        if (!StringUtils.hasText(keyword)) throw new IllegalArgumentException("검색어는 필수 값입니다.");
        if (Objects.isNull(paging)) throw new IllegalArgumentException("페이징은 필수 값입니다.");

        BlogSearchPaging constrainedPaging = BlogSearchPaging.of(constraintPage(paging.getPage()), constraintPageSize(paging.getPageSize()), paging.getSort());
        return this.webClient.get()
            .uri(builder -> builder
                .queryParam(KakaoApiConstants.PARAM_QUERY, constraintKeyword(keyword))
                .queryParam(KakaoApiConstants.PARAM_SORT, constrainedPaging.getSort().getKakao())
                .queryParam(KakaoApiConstants.PARAM_PAGE, constrainedPaging.getPage())
                .queryParam(KakaoApiConstants.PARAM_SIZE, constrainedPaging.getPageSize())
                .build())
            .retrieve()
            .bodyToMono(KakaoApiResponseDto.class)
            .map(k -> BlogSearchResponseMapper.toBlogSearchResponse(k, constrainedPaging));
    }

    /**
     * 카카오 블로그 검색 API 질의어 제약 조건을 적용합니다.
     *
     * @param original 기존 값
     * @return 제약 조건이 적용된 값
     */
    private String constraintKeyword(String original) {
        try {
            return URLEncoder.encode(original, KakaoApiConstants.CONSTRAINT_QUERY_CHARSET);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("검색어를 UTF-8로 URL Encoding 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 카카오 블로그 검색 API 페이지 제약 조건을 적용합니다.
     *
     * @param original 기존 값
     * @return 제약 조건이 적용된 값
     */
    private int constraintPage(int original) {
        if (original < KakaoApiConstants.CONSTRAINT_PAGE_MIN) return KakaoApiConstants.CONSTRAINT_PAGE_MIN;
        if (original > KakaoApiConstants.CONSTRAINT_PAGE_MAX) return KakaoApiConstants.CONSTRAINT_PAGE_MAX;
        return original;
    }

    /**
     * 카카오 블로그 검색 API 페이지 사이즈 제약 조건을 적용합니다.
     *
     * @param original 기존 값
     * @return 제약 조건이 적용된 값
     */
    private int constraintPageSize(int original) {
        if (original < KakaoApiConstants.CONSTRAINT_SIZE_MIN) return KakaoApiConstants.CONSTRAINT_SIZE_MIN;
        if (original > KakaoApiConstants.CONSTRAINT_SIZE_MAX) return KakaoApiConstants.CONSTRAINT_SIZE_MAX;
        return original;
    }
}
