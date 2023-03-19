package io.hoon.blogsearch.keyword.entity;

import io.hoon.blogsearch.keyword.interfaces.exception.IllegalArgumentException;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.util.StringUtils;

/**
 * <h1>검색 이력 엔티티</h1>
 */
@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class KeywordHistory {

    /**
     * <h3>검색어</h3>
     */
    @Id
    private String name;

    /**
     * <h3>검색 횟수</h3>
     */
    private long count;

    public static KeywordHistory of(String name) {
        if (!StringUtils.hasText(name)) throw new IllegalArgumentException("name은 필수값입니다.");
        return new KeywordHistory(name, 1);
    }

}
