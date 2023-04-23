package io.hoon.blogsearch.support.searchclient.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * <h1>카카오 블로그 검색 API 응답 DTO</h1>
 */
@Getter
@ToString
@RequiredArgsConstructor
public class KakaoApiResponseDto {

    private final Meta meta;

    private final List<Document> documents;

    // JSON 직렬화/역직렬화를 위한 생성자
    private KakaoApiResponseDto() {
        this.meta = null;
        this.documents = null;
    }

    @Getter
    @ToString
    @RequiredArgsConstructor
    public static class Meta {

        /**
         * <h3>검색된 문서 수</h3>
         */
        @JsonProperty("total_count")
        private final long totalCount;

        /**
         * <h3>total_count 중 노출 가능 문서 수</h3>
         */
        @JsonProperty("pageable_count")
        private final long pageableCount;

        /**
         * <h3>현재 페이지가 마지막 페이지인지 여부</h3>
         * 값이 false면 page를 증가시켜 다음 페이지를 요청할 수 있음
         */
        @JsonProperty("is_end")
        private final boolean end;

        // JSON 직렬화/역직렬화를 위한 생성자
        private Meta() {
            this.totalCount = -1;
            this.pageableCount = -1;
            this.end = false;
        }
    }

    @Getter
    @ToString
    @RequiredArgsConstructor
    public static class Document {

        /**
         * <h3>블로그 글 제목</h3>
         */
        private final String title;

        /**
         * <h3>블로그 글 요약</h3>
         */
        private final String contents;

        /**
         * <h3>블로그 글 URL</h3>
         */
        private final String url;

        /**
         * <h3>블로그의 이름</h3>
         */
        private final String blogname;

        /**
         * <h3>검색 시스템에서 추출한 대표 미리보기 이미지 URL</h3>
         */
        private final String thumbnail;

        /**
         * <h3>블로그 글 작성시간</h3>
         * ISO 8601
         */
        private final ZonedDateTime datetime;

        // JSON 직렬화/역직렬화를 위한 생성자
        private Document() {
            this.title = null;
            this.contents = null;
            this.url = null;
            this.blogname = null;
            this.thumbnail = null;
            this.datetime = null;
        }
    }

}
