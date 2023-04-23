package io.hoon.blogsearch.api.manager;

import io.hoon.blogsearch.api.chain.BlogSearchChain;
import io.hoon.blogsearch.support.searchclient.interfaces.enums.Sort;
import io.hoon.blogsearch.support.searchclient.interfaces.model.BlogSearchPaging;
import io.hoon.blogsearch.support.searchclient.interfaces.model.BlogSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * <h1>블로그 검색 매니저</h1>
 */
@Component
@RequiredArgsConstructor
public class BlogSearchManager {

    /**
     * <h3>블로그 검색 체인</h3>
     * @see io.hoon.blogsearch.api.configuration.BlogSearchSourceConfig 체인 설정
     */
    private final BlogSearchChain blogSearchChain;

    /**
     * 블로그를 검색합니다.
     *
     * @param keyword  검색어
     * @param page     페이지
     * @param pageSize 페이지 사이즈
     * @param sort     정렬
     * @return 검색 결과
     */
    public Mono<BlogSearchResponse> search(String keyword, int page, int pageSize, Sort sort) {
        return blogSearchChain.search(keyword, BlogSearchPaging.of(page, pageSize, sort));
    }

}
