package io.hoon.blogsearch.api.controller;

import io.hoon.blogsearch.api.event.publisher.BlogSearchEventPublisher;
import io.hoon.blogsearch.api.manager.BlogSearchManager;
import io.hoon.blogsearch.search.interfaces.enums.Sort;
import io.hoon.blogsearch.search.interfaces.model.BlogSearchResponse;
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
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class SearchController {
    private final BlogSearchManager blogSearchManager;
    private final BlogSearchEventPublisher blogSearchEventPublisher;

    @GetMapping
    public Mono<BlogSearchResponse> search(@RequestParam String keyword,
                                           @RequestParam int page,
                                           @RequestParam(required = false, defaultValue = "10") int pageSize,
                                           @RequestParam(required = false, defaultValue = "ACCURACY") Sort sort) {
        blogSearchEventPublisher.publishBlogSearchEvent(keyword);
        return blogSearchManager.search(keyword, page, pageSize, sort);
    }
}
