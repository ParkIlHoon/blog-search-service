package io.hoon.blogsearch.support.searchclient.interfaces.model;

import java.time.ZonedDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * <h1>블로그 검색 응답 모델</h1>
 */
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BlogSearchResponse {

    private final Header header;

    private final List<Content> contents;

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Header {

        /**
         * <h3>검색된 총 문서 수</h3>
         */
        private final long totalCount;
        /**
         * <h3>현재 페이지</h3>
         */
        private final int page;
        /**
         * <h3>페이지 사이즈</h3>
         */
        private final int pageSize;
        /**
         * <h3>마지막 페이지 여부</h3>
         */
        private final boolean end;
    }

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Content {
        /**
         * <h3>포스트 제목</h3>
         */
        private final String postTitle;
        /**
         * <h3>포스트 내용</h3>
         */
        private final String postContents;
        /**
         * <h3>포스트 게시일</h3>
         */
        private final ZonedDateTime postDate;
        /**
         * <h3>포스트 링크</h3>
         */
        private final String postUrl;
        /**
         * <h3>블로그 제목</h3>
         */
        private final String blogTitle;
    }
}
