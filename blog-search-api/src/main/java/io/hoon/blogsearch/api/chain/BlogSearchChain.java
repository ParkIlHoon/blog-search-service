package io.hoon.blogsearch.api.chain;

import io.hoon.blogsearch.search.interfaces.client.BlogSearchClient;
import io.hoon.blogsearch.search.interfaces.model.BlogSearchPaging;
import io.hoon.blogsearch.search.interfaces.model.BlogSearchResponse;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

/**
 * <h1>블로그 검색 체인</h1>
 */
@RequiredArgsConstructor
public class BlogSearchChain {

    private final BlogSearchClient client;
    private BlogSearchChain next = null;

    public BlogSearchChain next(BlogSearchClient nextClient) {
        this.next = new BlogSearchChain(nextClient);
        return this.next;
    }

    public Mono<BlogSearchResponse> search(String keyword, BlogSearchPaging paging) {
        if (hasNext()) {
            return this.client.search(keyword, paging)
                .onErrorResume(fallback -> this.next.search(keyword, paging));
        }

        return this.client.search(keyword, paging);
    }

    public boolean hasNext() {
        return !Objects.isNull(this.next);
    }

}
