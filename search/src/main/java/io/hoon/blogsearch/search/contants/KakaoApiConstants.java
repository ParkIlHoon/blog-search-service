package io.hoon.blogsearch.search.contants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * <h1>카카오 API 상수 모음</h1>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class KakaoApiConstants {

    /**
     * <h3>API URL</h3>
     */
    public static final String URL = "https://dapi.kakao.com/v2/search/blog";
    /**
     * <h3>인증인가 헤더명</h3>
     */
    public static final String AUTH_HEADER = "Authorization";
    /**
     * <h3>인증인가 헤더 Prefix</h3>
     */
    public static final String AUTH_HEADER_VALUE_PREFIX = "KakaoAK ";
    /**
     * <h3>질의어 파라미터명</h3>
     */
    public static final String PARAM_QUERY = "query";
    /**
     * <h3>정렬 파라미터명</h3>
     */
    public static final String PARAM_SORT = "sort";
    /**
     * <h3>페이지 파라미터명</h3>
     */
    public static final String PARAM_PAGE = "page";
    /**
     * <h3>한 페이지에 보여질 문서 수 파라미터명</h3>
     */
    public static final String PARAM_SIZE = "size";
    /**
     * <h3>질의어 파라미터 Charset 제약조건</h3>
     */
    public static final String CONSTRAINT_QUERY_CHARSET = "UTF-8";
    /**
     * <h3>페이지 파라미터 최소값 제약조건</h3>
     */
    public static final int CONSTRAINT_PAGE_MIN = 1;
    /**
     * <h3>페이지 파라미터 최대값 제약조건</h3>
     */
    public static final int CONSTRAINT_PAGE_MAX = 50;
    /**
     * <h3>페이지 사이즈 파라미터 최소값 제약조건</h3>
     */
    public static final int CONSTRAINT_SIZE_MIN = 1;
    /**
     * <h3>페이지 사이즈 파라미터 최대값 제약조건</h3>
     */
    public static final int CONSTRAINT_SIZE_MAX = 50;
}
