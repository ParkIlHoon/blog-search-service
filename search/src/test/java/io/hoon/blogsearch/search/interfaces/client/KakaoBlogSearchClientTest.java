package io.hoon.blogsearch.search.interfaces.client;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.hoon.blogsearch.search.contants.KakaoApiConstants;
import io.hoon.blogsearch.search.dto.KakaoApiResponseDto;
import io.hoon.blogsearch.search.dto.KakaoApiResponseDtoFixture;
import io.hoon.blogsearch.search.interfaces.exception.IllegalArgumentException;
import io.hoon.blogsearch.search.interfaces.model.BlogSearchPaging;
import io.hoon.blogsearch.search.interfaces.model.BlogSearchResponse;
import java.io.IOException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;

@DisplayName("Kakao 블로그 검색 API Client 단위 테스트")
@ExtendWith(MockitoExtension.class)
class KakaoBlogSearchClientTest {

    public static MockWebServer mockWebServer;

    public BlogSearchClient kakaoBlogSearchClient;
    public final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void beforeAll() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    static void afterAll() throws IOException {
        mockWebServer.shutdown();
    }

    @BeforeEach
    void setUp() {
        // ZonedDateTime Serialization 을 위한 세팅
        this.objectMapper.registerModule(new JavaTimeModule());
        // 엔드포인트를 MockWebServer 로 변경
        this.kakaoBlogSearchClient = new KakaoBlogSearchClient("foo");
        ReflectionTestUtils.setField(this.kakaoBlogSearchClient, "webClient", WebClient.builder()
            .baseUrl(mockWebServer.url("/blog").toString())
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader(KakaoApiConstants.AUTH_HEADER, KakaoApiConstants.AUTH_HEADER_VALUE_PREFIX)
            .build());
    }

    @Test
    @DisplayName("성공 : 첫 페이지 ~ 마지막 직전 페이지")
    void success() throws JsonProcessingException {
        // given
        final String keyword = "테스트";
        final BlogSearchPaging paging = BlogSearchPaging.of(1);
        final KakaoApiResponseDto apiResponseDto = KakaoApiResponseDtoFixture.get(paging.getPageSize());

        mockWebServer.enqueue(
            new MockResponse().setResponseCode(HttpStatus.OK.value())
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(this.objectMapper.writeValueAsString(apiResponseDto))
        );

        // when
        BlogSearchResponse blogSearchResponse = this.kakaoBlogSearchClient.search(keyword, paging).block();

        // then
        assertNotNull(blogSearchResponse);
        assertEquals(paging.getPage(), blogSearchResponse.getHeader().getPage());
        assertEquals(paging.getPageSize(), blogSearchResponse.getHeader().getPageSize());
        assertTrue(paging.getPageSize() >= blogSearchResponse.getContents().size());
        assertFalse(blogSearchResponse.getHeader().isEnd());
    }

    @Test
    @DisplayName("성공 : 마지막 페이지")
    void success_last() throws JsonProcessingException {
        // given
        final String keyword = "테스트";
        final BlogSearchPaging paging = BlogSearchPaging.of(1);
        final KakaoApiResponseDto apiResponseDto = KakaoApiResponseDtoFixture.getEnd(paging.getPageSize());

        mockWebServer.enqueue(
            new MockResponse().setResponseCode(HttpStatus.OK.value())
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(this.objectMapper.writeValueAsString(apiResponseDto))
        );

        // when
        BlogSearchResponse blogSearchResponse = this.kakaoBlogSearchClient.search(keyword, paging).block();

        // then
        assertNotNull(blogSearchResponse);
        assertEquals(paging.getPage(), blogSearchResponse.getHeader().getPage());
        assertEquals(paging.getPageSize(), blogSearchResponse.getHeader().getPageSize());
        assertTrue(paging.getPageSize() >= blogSearchResponse.getContents().size());
        assertTrue(blogSearchResponse.getHeader().isEnd());
    }

    @Test
    @DisplayName("실패 : 검색어 is null")
    void fail_keyword_null() {
        // given
        final String keyword = null;
        final BlogSearchPaging paging = BlogSearchPaging.of(1);

        // when, then
        assertThrows(IllegalArgumentException.class, () -> this.kakaoBlogSearchClient.search(keyword, paging));
    }

    @Test
    @DisplayName("실패 : 검색어 is 빈 문자열")
    void fail_keyword_blank() {
        // given
        final String keyword = "";
        final BlogSearchPaging paging = BlogSearchPaging.of(1);

        // when, then
        assertThrows(IllegalArgumentException.class, () -> this.kakaoBlogSearchClient.search(keyword, paging));
    }

    @Test
    @DisplayName("실패 : 페이징 is null")
    void fail_paging_null() {
        // given
        final String keyword = "검색어";
        final BlogSearchPaging paging = null;

        // when, then
        assertThrows(IllegalArgumentException.class, () -> this.kakaoBlogSearchClient.search(keyword, paging));
    }

    @Test
    @DisplayName("제약조건 위반 : 페이지, 페이지 사이즈 최소값")
    void success_min() throws JsonProcessingException {
        // given
        final String keyword = "테스트";
        final BlogSearchPaging paging = BlogSearchPaging.of(KakaoApiConstants.CONSTRAINT_PAGE_MIN);
        ReflectionTestUtils.setField(paging, "page", KakaoApiConstants.CONSTRAINT_PAGE_MIN - 1);
        ReflectionTestUtils.setField(paging, "pageSize", KakaoApiConstants.CONSTRAINT_SIZE_MIN - 1);
        final KakaoApiResponseDto apiResponseDto = KakaoApiResponseDtoFixture.get(KakaoApiConstants.CONSTRAINT_SIZE_MIN);

        mockWebServer.enqueue(
            new MockResponse().setResponseCode(HttpStatus.OK.value())
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(this.objectMapper.writeValueAsString(apiResponseDto))
        );

        // when
        BlogSearchResponse blogSearchResponse = this.kakaoBlogSearchClient.search(keyword, paging).block();

        // then
        assertNotNull(blogSearchResponse);
        assertNotEquals(KakaoApiConstants.CONSTRAINT_PAGE_MIN, paging.getPage());
        assertNotEquals(KakaoApiConstants.CONSTRAINT_SIZE_MIN, paging.getPageSize());
        assertEquals(KakaoApiConstants.CONSTRAINT_PAGE_MIN, blogSearchResponse.getHeader().getPage());
        assertEquals(KakaoApiConstants.CONSTRAINT_SIZE_MIN, blogSearchResponse.getHeader().getPageSize());
        assertTrue(KakaoApiConstants.CONSTRAINT_SIZE_MIN >= blogSearchResponse.getContents().size());
        assertFalse(blogSearchResponse.getHeader().isEnd());
    }

    @Test
    @DisplayName("제약조건 위반 : 페이지, 페이지 사이즈 최대값")
    void success_max() throws JsonProcessingException {
        // given
        final String keyword = "테스트";
        final BlogSearchPaging paging = BlogSearchPaging.of(KakaoApiConstants.CONSTRAINT_PAGE_MAX);
        ReflectionTestUtils.setField(paging, "page", KakaoApiConstants.CONSTRAINT_PAGE_MAX + 1);
        ReflectionTestUtils.setField(paging, "pageSize", KakaoApiConstants.CONSTRAINT_SIZE_MAX + 1);
        final KakaoApiResponseDto apiResponseDto = KakaoApiResponseDtoFixture.getEnd(KakaoApiConstants.CONSTRAINT_SIZE_MAX);

        mockWebServer.enqueue(
            new MockResponse().setResponseCode(HttpStatus.OK.value())
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(this.objectMapper.writeValueAsString(apiResponseDto))
        );

        // when
        BlogSearchResponse blogSearchResponse = this.kakaoBlogSearchClient.search(keyword, paging).block();

        // then
        assertNotNull(blogSearchResponse);
        assertNotEquals(KakaoApiConstants.CONSTRAINT_PAGE_MAX, paging.getPage());
        assertNotEquals(KakaoApiConstants.CONSTRAINT_SIZE_MAX, paging.getPageSize());
        assertEquals(KakaoApiConstants.CONSTRAINT_PAGE_MAX, blogSearchResponse.getHeader().getPage());
        assertEquals(KakaoApiConstants.CONSTRAINT_SIZE_MAX, blogSearchResponse.getHeader().getPageSize());
        assertTrue(KakaoApiConstants.CONSTRAINT_SIZE_MAX >= blogSearchResponse.getContents().size());
        assertTrue(blogSearchResponse.getHeader().isEnd());
    }
}