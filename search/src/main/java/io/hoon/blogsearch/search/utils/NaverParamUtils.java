package io.hoon.blogsearch.search.utils;

import io.hoon.blogsearch.search.contants.NaverApiContants;
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
        return (page - 1) * pageSize + 1;
    }

    /**
     * 검색 시작 위치와 페이지 사이즈를 통해 페이지를 계산합니다.
     *
     * @param start    검색 시작 위치
     * @param display 한 번에 표시할 검색 결과 개수
     * @return 페이지
     */
    public static int calculatePage(int start, int display) {
        int left = start + 1;
        return (int) Math.round((double) left / display) + 1;
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
        int checksum = start + display - 1;
        return checksum >= total
            || checksum >= NaverApiContants.CONSTRAINT_START_MAX * NaverApiContants.CONSTRAINT_DISPLAY_MAX;
    }
}
