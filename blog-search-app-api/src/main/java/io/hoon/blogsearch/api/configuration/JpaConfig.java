package io.hoon.blogsearch.api.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * <h1>JPA 설정</h1>
 */
@EnableJpaRepositories(basePackages = {"io.hoon.blogsearch"})
@EntityScan(basePackages = {"io.hoon.blogsearch"})
@Configuration
public class JpaConfig {

}
