package io.hoon.blogsearch.search.interfaces.client;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.hoon.blogsearch.search.contants.NaverApiContants;
import io.hoon.blogsearch.search.dto.NaverApiResponseDto;
import io.hoon.blogsearch.search.dto.NaverApiResponseDtoFixture;
import io.hoon.blogsearch.search.interfaces.exception.IllegalArgumentException;
import io.hoon.blogsearch.search.interfaces.model.BlogSearchPaging;
import io.hoon.blogsearch.search.interfaces.model.BlogSearchResponse;
import io.hoon.blogsearch.search.utils.NaverParamUtils;
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

@DisplayName("Naver 블로그 검색 API Client 단위 테스트")
@ExtendWith(MockitoExtension.class)
class NaverBlogSearchClientTest {

    public static MockWebServer mockWebServer;

    public BlogSearchClient naverBlogSearchClient;
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
        this.naverBlogSearchClient = new NaverBlogSearchClient("foo", "bar");
        ReflectionTestUtils.setField(this.naverBlogSearchClient, "webClient", WebClient.builder()
            .baseUrl(mockWebServer.url("/blog").toString())
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader(NaverApiContants.CLIENT_ID_HEADER, "foo")
            .defaultHeader(NaverApiContants.CLIENT_SECRET_HEADER, "bar")
            .build());
    }

    @Test
    @DisplayName("성공 : 첫 페이지 ~ 마지막 직전 페이지")
    void success() throws JsonProcessingException {
        // given
        final String keyword = "테스트";
        final BlogSearchPaging paging = BlogSearchPaging.of(1);
        final NaverApiResponseDto apiResponseDto = NaverApiResponseDtoFixture.get(paging.getPageSize() + 1, paging.getPage(), paging.getPageSize());

        mockWebServer.enqueue(
            new MockResponse().setResponseCode(HttpStatus.OK.value())
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(this.objectMapper.writeValueAsString(apiResponseDto))
        );

        // when
        BlogSearchResponse blogSearchResponse = this.naverBlogSearchClient.search(keyword, paging).block();

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
        final NaverApiResponseDto apiResponseDto = NaverApiResponseDtoFixture.getEnd(paging.getPageSize());

        mockWebServer.enqueue(
            new MockResponse().setResponseCode(HttpStatus.OK.value())
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(this.objectMapper.writeValueAsString(apiResponseDto))
        );

        // when
        BlogSearchResponse blogSearchResponse = this.naverBlogSearchClient.search(keyword, paging).block();

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
        assertThrows(IllegalArgumentException.class, () -> this.naverBlogSearchClient.search(keyword, paging));
    }

    @Test
    @DisplayName("실패 : 검색어 is 빈 문자열")
    void fail_keyword_blank() {
        // given
        final String keyword = "";
        final BlogSearchPaging paging = BlogSearchPaging.of(1);

        // when, then
        assertThrows(IllegalArgumentException.class, () -> this.naverBlogSearchClient.search(keyword, paging));
    }

    @Test
    @DisplayName("실패 : 페이징 is null")
    void fail_paging_null() {
        // given
        final String keyword = "검색어";
        final BlogSearchPaging paging = null;

        // when, then
        assertThrows(IllegalArgumentException.class, () -> this.naverBlogSearchClient.search(keyword, paging));
    }

    @Test
    @DisplayName("제약조건 위반 : 페이지, 페이지 사이즈 최소값")
    void success_min() throws JsonProcessingException {
        // given
        final String keyword = "테스트";
        final BlogSearchPaging paging = BlogSearchPaging.of(NaverApiContants.CONSTRAINT_DISPLAY_MIN);
        int calculatePage = NaverParamUtils.calculatePage(NaverApiContants.CONSTRAINT_START_MIN, NaverApiContants.CONSTRAINT_DISPLAY_MIN);
        ReflectionTestUtils.setField(paging, "page",  calculatePage - 1);
        ReflectionTestUtils.setField(paging, "pageSize", NaverApiContants.CONSTRAINT_DISPLAY_MIN - 1);
        final NaverApiResponseDto apiResponseDto = NaverApiResponseDtoFixture.get(10000, NaverApiContants.CONSTRAINT_START_MIN, NaverApiContants.CONSTRAINT_DISPLAY_MIN);

        mockWebServer.enqueue(
            new MockResponse().setResponseCode(HttpStatus.OK.value())
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(this.objectMapper.writeValueAsString(apiResponseDto))
        );

        // when
        BlogSearchResponse blogSearchResponse = this.naverBlogSearchClient.search(keyword, paging).block();

        // then
        assertNotNull(blogSearchResponse);
        assertNotEquals(calculatePage, paging.getPage());
        assertNotEquals(NaverApiContants.CONSTRAINT_DISPLAY_MIN, paging.getPageSize());
        assertEquals(calculatePage, blogSearchResponse.getHeader().getPage());
        assertEquals(NaverApiContants.CONSTRAINT_DISPLAY_MIN, blogSearchResponse.getHeader().getPageSize());
        assertTrue(NaverApiContants.CONSTRAINT_DISPLAY_MIN >= blogSearchResponse.getContents().size());
        assertFalse(blogSearchResponse.getHeader().isEnd());
    }
}