package io.hoon.blogsearch.domain.keyword.interfaces.model;

import io.hoon.blogsearch.domain.keyword.interfaces.exception.IllegalArgumentException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.StringUtils;

/**
 * <h1>인기 검색어</h1>
 */
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PopularKeyword {

    /**
     * <h3>검색어</h3>
     */
    private final String name;

    /**
     * <h3>검색 횟수</h3>
     */
    private final long count;

    public static PopularKeyword of(String name, long count) {
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("name은 필수값 입니다.");
        }
        if (count < 0) {
            throw new IllegalArgumentException("count는 0 미만일 수 없습니다.");
        }
        return new PopularKeyword(name, count);
    }
}
