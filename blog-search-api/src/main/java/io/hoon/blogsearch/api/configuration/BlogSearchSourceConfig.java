package io.hoon.blogsearch.api.configuration;

import io.hoon.blogsearch.api.properties.SearchSourceProperties;
import io.hoon.blogsearch.search.interfaces.client.KakaoBlogSearchClient;
import io.hoon.blogsearch.search.interfaces.client.NaverBlogSearchClient;
import io.hoon.blogsearch.search.interfaces.client.BlogSearchClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <h1>블로그 검색소스 설정</h1>
 */
@Configuration
@RequiredArgsConstructor
public class BlogSearchSourceConfig {

    private final SearchSourceProperties searchSourceProperties;

    /**
     * 카카오
     */
    @Bean
    public BlogSearchClient kakaoBlogSearchClient() {
        return new KakaoBlogSearchClient(searchSourceProperties.getKakao().getApiKey());
    }

    /**
     * 네이버
     */
    @Bean
    public BlogSearchClient naverBlogSearchClient() {
        return new NaverBlogSearchClient(searchSourceProperties.getNaver().getClientId(), searchSourceProperties.getNaver().getClientSecret());
    }

}
