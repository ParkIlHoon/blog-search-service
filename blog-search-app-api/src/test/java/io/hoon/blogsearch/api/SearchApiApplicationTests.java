package io.hoon.blogsearch.api;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
class SearchApiApplicationTests {

    @BeforeAll
    static void beforeAll() {
        GenericContainer<?> REDIS_CONTAINER =
            new GenericContainer<>(DockerImageName.parse("bitnami/redis:latest"))
                .withEnv("ALLOW_EMPTY_PASSWORD", "yes")
                .withExposedPorts(6379);

        REDIS_CONTAINER.start();

        System.setProperty("spring.data.redis.host", REDIS_CONTAINER.getHost());
        System.setProperty("spring.data.redis.port", REDIS_CONTAINER.getMappedPort(6379).toString());
    }

    @Test
    void contextLoads() {
    }

}
