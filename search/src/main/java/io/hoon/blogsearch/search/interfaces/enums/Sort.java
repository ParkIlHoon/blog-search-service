package io.hoon.blogsearch.search.interfaces.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * <h1>정렬</h1>
 */
@Getter
@RequiredArgsConstructor
public enum Sort {
    // 정확도순
    ACCURACY("accuracy", "sim"),

    // 최신순
    RECENCY("recency", "date");

    private final String kakao;
    private final String naver;
}
