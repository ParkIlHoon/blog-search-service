package io.hoon.blogsearch.search.interfaces.model;

import io.hoon.blogsearch.search.interfaces.enums.Sort;
import io.hoon.blogsearch.search.interfaces.exception.IllegalArgumentException;
import java.util.Objects;
import lombok.Getter;

/**
 * <h1>블로그 검색 페이징 모델</h1>
 */
@Getter
public class BlogSearchPaging {

    /**
     * <h3>정렬 방식</h3>
     * 기본값 : {@link Sort#ACCURACY}
     */
    private final Sort sort;

    /**
     * <h3>검색 페이지</h3>
     * 1 이상 50 미만
     */
    private final int page;

    /**
     * <h3>페이지 사이즈</h3>
     * 기본값 : 10
     * 1 이상 50 미만
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

    private BlogSearchPaging(Sort sort, int page, int pageSize) {
        if (page < 1) throw new IllegalArgumentException("page는 최소 1 이어야합니다.");
        if (page > 50) throw new IllegalArgumentException("page는 최대 50 이어야합니다.");
        if (pageSize < 1) throw new IllegalArgumentException("pageSize는 최소 1 이어야합니다.");
        if (pageSize > 50) throw new IllegalArgumentException("pageSize는 최대 50 이어야합니다.");
        if (Objects.isNull(sort)) throw new IllegalArgumentException("sort는 필수 값입니다.");

        this.sort = sort;
        this.page = page;
        this.pageSize = pageSize;
    }
}
