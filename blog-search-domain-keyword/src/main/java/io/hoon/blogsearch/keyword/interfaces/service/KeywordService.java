package io.hoon.blogsearch.keyword.interfaces.service;

import io.hoon.blogsearch.keyword.interfaces.exception.IllegalArgumentException;
import io.hoon.blogsearch.keyword.interfaces.model.PopularKeyword;
import java.util.List;

/**
 * <h1>검색어 서비스</h1>
 */
public interface KeywordService {

    /**
     * 검색 기록을 생성합니다.
     *
     * @param keyword 검색어
     * @throws IllegalArgumentException keyword 파라미터가 null 이거나 빈 문자열일 경우
     * @return 신규 검색어 여부. 이전에 검색한 이력이 없을 경우 true, 이전에 검색한 이력이 존재하는 경우 false
     */
    boolean createKeywordHistory(final String keyword) throws IllegalArgumentException;

    /**
     * 인기 검색어 목록을 조회합니다.
     *
     * @param size 조회할 개수 - 최소 1
     * @return 조회할 개수에 해당하는 검색 횟수 기준 인기 검색어 목록
     * @throws IllegalArgumentException size 파라미터가 1 미만일 경우
     */
    List<PopularKeyword> getPopularKeywords(final int size) throws IllegalArgumentException;

}
