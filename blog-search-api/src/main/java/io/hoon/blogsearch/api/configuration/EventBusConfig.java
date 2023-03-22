package io.hoon.blogsearch.api.configuration;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import io.hoon.blogsearch.api.event.listener.GuavaEventListener;
import io.hoon.blogsearch.api.properties.EventBusProperties;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <h1>이벤트 버스 설정</h1>
 */
@Configuration
@RequiredArgsConstructor
public class EventBusConfig {

    private final EventBusProperties eventBusProperties;
    private final List<GuavaEventListener> guavaEventListeners;

    @Bean
    public EventBus guavaEventBus() {
        ExecutorService executor = Executors.newFixedThreadPool(eventBusProperties.getThreadCount());
        EventBus eventBus = new AsyncEventBus(eventBusProperties.getName(), executor);

        for (GuavaEventListener listener : guavaEventListeners) {
            eventBus.register(listener);
        }

        return eventBus;
    }

}
