package io.hoon.blogsearch.api.controller;

import io.hoon.blogsearch.api.event.annotation.PublishBlogSearchEvent;
import io.hoon.blogsearch.api.manager.BlogSearchManager;
import io.hoon.blogsearch.domain.keyword.interfaces.model.PopularKeyword;
import io.hoon.blogsearch.domain.keyword.interfaces.service.KeywordService;
import io.hoon.blogsearch.support.searchclient.interfaces.enums.Sort;
import io.hoon.blogsearch.support.searchclient.interfaces.model.BlogSearchResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * <h1>블로그 검색 컨트롤러</h1>
 */
@RestController
@RequestMapping("/api/v1/blog")
@RequiredArgsConstructor
public class SearchController {
    private final BlogSearchManager blogSearchManager;
    private final KeywordService keywordService;

    @GetMapping("/search")
    @PublishBlogSearchEvent
    public Mono<BlogSearchResponse> search(@RequestParam String keyword,
                                           @RequestParam int page,
                                           @RequestParam(required = false, defaultValue = "10") int pageSize,
                                           @RequestParam(required = false, defaultValue = "ACCURACY") Sort sort) {
        return blogSearchManager.search(keyword, page, pageSize, sort);
    }

    @GetMapping("/popular")
    public List<PopularKeyword> popularKeyword(@RequestParam(required = false, defaultValue = "10") int size) {
        return keywordService.getPopularKeywords(size);
    }
}
