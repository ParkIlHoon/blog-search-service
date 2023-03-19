package io.hoon.blogsearch.search.interfaces.model;

import io.hoon.blogsearch.search.interfaces.enums.Sort;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * <h1>블로그 검색 페이징 모델</h1>
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class BlogSearchPaging {

    /**
     * <h3>정렬 방식</h3>
     * 기본값 : {@link Sort#ACCURACY}
     */
    private final Sort sort;

    /**
     * <h3>검색 페이지</h3>
     */
    private final int page;

    /**
     * <h3>페이지 사이즈</h3>
     * 기본값 : 10
     */
    private final int pageSize;

    public static BlogSearchPaging of(int page) {
        return new BlogSearchPaging(Sort.ACCURACY, page, 10);
    }

    public static BlogSearchPaging of(int page, Sort sort) {
        return new BlogSearchPaging(sort, page, 10);
    }

    public static BlogSearchPaging of(int page, int pageSize, Sort sort) {
        return new BlogSearchPaging(sort, page, pageSize);
    }

}
