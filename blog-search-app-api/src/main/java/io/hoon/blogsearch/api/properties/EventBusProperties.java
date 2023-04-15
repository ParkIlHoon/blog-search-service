package io.hoon.blogsearch.api.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <h1>이벤트 버스 프로퍼티</h1>
 */
@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "application.event-bus")
public class EventBusProperties {

    private final String name;

    private final int threadCount;

}
