package io.hoon.blogsearch.api.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <h1>분산 락 프로퍼티</h1>
 */
@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "application.distribute-lock")
public class DistributeLockProperties {

    private final Redisson redisson;

    @Getter
    @RequiredArgsConstructor
    public static class Redisson {
        private final boolean enabled;
    }

}
