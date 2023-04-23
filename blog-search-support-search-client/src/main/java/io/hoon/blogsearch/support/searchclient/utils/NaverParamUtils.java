package io.hoon.blogsearch.support.searchclient.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NaverParamUtils {

    /**
     * 페이지와 페이지 사이즈를 통해 검색 시작 위치를 계산합니다.
     *
     * @param page     페이지
     * @param pageSize 페이지 사이즈
     * @return 검색 시작 위치
     */
    public static int calculateStart(int page, int pageSize) {
        if (page == 1) return 1;
        return (page - 1) * pageSize + 1;
    }

    /**
     * 검색 시작 위치와 페이지 사이즈를 통해 페이지를 계산합니다.
     *
     * @param start   검색 시작 위치
     * @param display 한 번에 표시할 검색 결과 개수
     * @return 페이지
     */
    public static int calculatePage(int start, int display) {
        return (start / display) + ((start % display > 0) ? 1 : 0);
    }
    public static long calculatePage(long start, long display) {
        return (start / display) + ((start % display > 0) ? 1 : 0);
    }

    /**
     * 마지막 페이지 여부를 계산합니다.
     *
     * @param start   검색 시작 위치
     * @param display 한 번에 표시할 검색 결과 개수
     * @param total   총 검색 결과 개수
     * @return 마지막 페이지 여부
     */
    public static boolean calculateEnd(int start, int display, long total) {
        long lastPage = calculatePage(total, display);
        long currentPage = calculatePage(start, display);
        return lastPage == currentPage;
    }
}
