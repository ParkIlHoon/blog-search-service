package io.hoon.blogsearch.api.event.listener;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import io.hoon.blogsearch.api.event.BlogSearchEvent;
import io.hoon.blogsearch.domain.keyword.interfaces.service.KeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * <h1>블로그 검색 이벤트 리스너</h1>
 */
@Component
@RequiredArgsConstructor
public class BlogSearchEventListener implements GuavaEventListener {

    private final KeywordService keywordService;

    @Subscribe
    @AllowConcurrentEvents
    public void receive(BlogSearchEvent event) {
        keywordService.createKeywordHistory(event.getKeyword());
    }

}
