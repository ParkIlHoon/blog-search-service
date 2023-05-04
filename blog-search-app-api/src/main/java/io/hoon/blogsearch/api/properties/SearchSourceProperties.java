package io.hoon.blogsearch.api.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <h1>검색 소스 프로퍼티</h1>
 */
@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "application.search-source")
public class SearchSourceProperties {
    private final Kakao kakao;
    private final Naver naver;

    @Getter
    @RequiredArgsConstructor
    public static class Kakao {
        private final String apiKey;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Naver {
        private final String clientId;
        private final String clientSecret;
    }
}
