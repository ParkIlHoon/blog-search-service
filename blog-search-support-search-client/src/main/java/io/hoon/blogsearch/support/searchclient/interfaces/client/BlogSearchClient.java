package io.hoon.blogsearch.support.searchclient.interfaces.client;

import io.hoon.blogsearch.support.searchclient.interfaces.exception.IllegalArgumentException;
import io.hoon.blogsearch.support.searchclient.interfaces.model.BlogSearchPaging;
import io.hoon.blogsearch.support.searchclient.interfaces.model.BlogSearchResponse;
import reactor.core.publisher.Mono;

/**
 * <h1>블로그 검색 클라이언트</h1>
 * 외부 검색 소스를 통해 블로그 검색을 수행합니다.
 */
public interface BlogSearchClient {

    /**
     * 블로그를 검색합니다.
     *
     * @param keyword 검색할 키워드
     * @param paging  페이징 정보
     * @return 키워드와 페이징 정보에 해당하는 검색 결과
     */
    Mono<BlogSearchResponse> search(final String keyword, final BlogSearchPaging paging) throws IllegalArgumentException;

}
