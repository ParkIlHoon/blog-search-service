package io.hoon.blogsearch.api.event.publisher;

import com.google.common.eventbus.EventBus;
import io.hoon.blogsearch.api.event.BlogSearchEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * <h1>블로그 검색 이벤트 발행 빈</h1>
 */
@Component
@RequiredArgsConstructor
public class BlogSearchEventPublisher {

    private final EventBus eventBus;

    /**
     * 블로그 검색 이벤트를 발행합니다.
     *
     * @param keyword 검색어
     * @see io.hoon.blogsearch.api.event.listener.BlogSearchEventListener 이벤트 리스너
     */
    public void publishBlogSearchEvent(String keyword) {
        eventBus.post(BlogSearchEvent.of(keyword));
    }

}
