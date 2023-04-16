package io.hoon.blogsearch.support.searchclient.dto;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * <h1>네이버 블로그 검색 API 응답 DTO</h1>
 */
@Getter
@ToString
@RequiredArgsConstructor
public class NaverApiResponseDto {

    /**
     * <h3>총 검색 결과 개수</h3>
     */
    private final long total;
    /**
     * <h3>검색 시작 위치</h3>
     */
    private final int start;
    /**
     * <h3>한 번에 표시할 검색 결과 개수</h3>
     */
    private final int display;
    /**
     * <h3>개별 검색 결과</h3>
     */
    private final List<Item> items;

    // JSON 직렬화/역직렬화를 위한 생성자
    private NaverApiResponseDto() {
        this.total = -1;
        this.start = -1;
        this.display = -1;
        this.items = null;
    }

    @Getter
    @ToString
    @RequiredArgsConstructor
    public static class Item {
        /**
         * <h3>블로그 포스트의 제목</h3>
         * 제목에서 검색어와 일치하는 부분은 <b> 태그로 감싸져 있습니다.
         */
        private final String title;
        /**
         * <h3>블로그 포스트의 URL</h3>
         */
        private final String link;
        /**
         * <h3>블로그 포스트의 내용을 요약한 패시지 정보</h3>
         * 패시지 정보에서 검색어와 일치하는 부분은 <b> 태그로 감싸져 있습니다.
         */
        private final String description;
        /**
         * <h3>블로그 포스트가 있는 블로그의 이름</h3>
         */
        private final String bloggername;
        /**
         * <h3>블로그 포스트가 있는 블로그의 주소</h3>
         */
        private final String bloggerlink;
        /**
         * <h3>블로그 포스트가 작성된 날짜</h3>
         */
        private final String postdate;

        // JSON 직렬화/역직렬화를 위한 생성자
        public Item() {
            this.title = null;
            this.link = null;
            this.description = null;
            this.bloggername = null;
            this.bloggerlink = null;
            this.postdate = null;
        }
    }
}
