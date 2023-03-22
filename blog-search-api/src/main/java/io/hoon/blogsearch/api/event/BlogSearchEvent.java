package io.hoon.blogsearch.api.event;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <h1>블로그 검색 이벤트</h1>
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BlogSearchEvent {

    /**
     * <h3>검색어</h3>
     */
    private final String keyword;

    public static BlogSearchEvent of(String keyword) {
        return new BlogSearchEvent(keyword);
    }
}
