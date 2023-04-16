package io.hoon.blogsearch.domain.keyword.domain.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.test.util.ReflectionTestUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class KeywordHistoryFixture {

    public static List<KeywordHistory> getList(int size) {
        long max = (long) (Math.random() * 100 * size);
        List<KeywordHistory> result = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            result.add(getWithCount(max--));
        }

        return result;
    }

    public static KeywordHistory getWithCount(long count) {
        KeywordHistory keywordHistory = KeywordHistory.of(UUID.randomUUID().toString());
        ReflectionTestUtils.setField(keywordHistory, "count", count);
        return keywordHistory;
    }
}
