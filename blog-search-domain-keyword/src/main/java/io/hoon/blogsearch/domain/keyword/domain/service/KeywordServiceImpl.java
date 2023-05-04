package io.hoon.blogsearch.domain.keyword.domain.service;

import io.hoon.blogsearch.domain.keyword.interfaces.exception.IllegalArgumentException;
import io.hoon.blogsearch.domain.keyword.interfaces.model.PopularKeyword;
import io.hoon.blogsearch.domain.keyword.interfaces.service.KeywordService;
import io.hoon.blogsearch.domain.keyword.domain.entity.KeywordHistory;
import io.hoon.blogsearch.domain.keyword.domain.repository.KeywordHistoryRepository;
import io.hoon.blogsearch.support.distributelocker.annotation.DistributeLock;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.framework.AopContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class KeywordServiceImpl implements KeywordService {

    private final KeywordHistoryRepository keywordHistoryRepository;

    @DistributeLock
    @Override
    public boolean createKeywordHistory(String keyword) throws IllegalArgumentException {
        if (!StringUtils.hasText(keyword)) {
            throw new IllegalArgumentException("keyword는 필수값입니다.");
        }
        /**
         * STUDY
         * AopContext 를 통해 같은 클래스의 메서드를 호출해 프록시 객체를 통한 진입을 만들어냅니다.
         * = AopContext 를 통해 self-invocation 을 우회
         * -> @Transactional 적용 가능
         */
        return ((KeywordServiceImpl) AopContext.currentProxy()).upsertKeywordHistory(keyword);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PopularKeyword> getPopularKeywords(int size) throws IllegalArgumentException {
        if (size < 1) {
            throw new IllegalArgumentException("size 는 최소 1 이상이어야합니다.");
        }
        return keywordHistoryRepository.findAll(PageRequest.of(0, size, Sort.by(Direction.DESC, "count")))
            .map(k -> PopularKeyword.of(k.getName(), k.getCount()))
            .getContent();
    }

    @Transactional
    public boolean upsertKeywordHistory(String keyword) {
        boolean existsByName = keywordHistoryRepository.existsByName(keyword);
        if (existsByName) {
            keywordHistoryRepository.increaseCount(keyword);
        } else {
            keywordHistoryRepository.save(KeywordHistory.of(keyword));
        }
        return !existsByName;
    }

}
