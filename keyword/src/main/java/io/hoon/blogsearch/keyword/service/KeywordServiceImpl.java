package io.hoon.blogsearch.keyword.service;

import io.hoon.blogsearch.keyword.entity.KeywordHistory;
import io.hoon.blogsearch.keyword.interfaces.exception.IllegalArgumentException;
import io.hoon.blogsearch.keyword.interfaces.model.PopularKeyword;
import io.hoon.blogsearch.keyword.interfaces.service.KeywordService;
import io.hoon.blogsearch.keyword.repository.KeywordHistoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class KeywordServiceImpl implements KeywordService {

    private final KeywordHistoryRepository keywordHistoryRepository;

    @Override
    @Transactional
    public boolean createKeywordHistory(String keyword) throws IllegalArgumentException {
        if (!StringUtils.hasText(keyword)) {
            throw new IllegalArgumentException("keyword는 필수값입니다.");
        }

        boolean existsByName = keywordHistoryRepository.existsByName(keyword);
        if (existsByName) {
            keywordHistoryRepository.increaseCount(keyword);
        } else {
            keywordHistoryRepository.save(KeywordHistory.of(keyword));
        }
        return !existsByName;
    }

    @Override
    public List<PopularKeyword> getPopularKeywords(int size) throws IllegalArgumentException {
        if (size < 1) {
            throw new IllegalArgumentException("size 는 최소 1 이상이어야합니다.");
        }
        return keywordHistoryRepository.findAll(PageRequest.of(0, size, Sort.by(Direction.DESC, "count")))
            .map(k -> PopularKeyword.of(k.getName(), k.getCount()))
            .getContent();
    }

}
