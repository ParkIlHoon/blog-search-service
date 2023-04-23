package io.hoon.blogsearch.support.searchclient.contants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * <h1>네이버 API 상수 모음</h1>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NaverApiContants {

    /**
     * <h3>API URL</h3>
     */
    public static final String URL = "https://openapi.naver.com/v1/search/blog.json";
    /**
     * <h3>클라이언트 아이디 헤더명</h3>
     */
    public static final String CLIENT_ID_HEADER = "X-Naver-Client-Id";
    /**
     * <h3>클라이언트 시크릿 헤더명</h3>
     */
    public static final String CLIENT_SECRET_HEADER = "X-Naver-Client-Secret";
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
    public static final String PARAM_START = "start";
    /**
     * <h3>한 페이지에 보여질 문서 수 파라미터명</h3>
     */
    public static final String PARAM_DISPLAY = "display";
    /**
     * <h3>질의어 파라미터 Charset 제약조건</h3>
     */
    public static final String CONSTRAINT_QUERY_CHARSET = "UTF-8";
    /**
     * <h3>페이지 파라미터 최소값 제약조건</h3>
     */
    public static final int CONSTRAINT_START_MIN = 1;
    /**
     * <h3>페이지 파라미터 최대값 제약조건</h3>
     */
    public static final int CONSTRAINT_START_MAX = 100;
    /**
     * <h3>페이지 사이즈 파라미터 최소값 제약조건</h3>
     */
    public static final int CONSTRAINT_DISPLAY_MIN = 1;
    /**
     * <h3>페이지 사이즈 파라미터 최대값 제약조건</h3>
     */
    public static final int CONSTRAINT_DISPLAY_MAX = 100;

}
