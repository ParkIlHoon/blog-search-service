package io.hoon.blogsearch.support.searchclient.mapper;

import io.hoon.blogsearch.support.searchclient.dto.KakaoApiResponseDto;
import io.hoon.blogsearch.support.searchclient.dto.NaverApiResponseDto;
import io.hoon.blogsearch.support.searchclient.interfaces.model.BlogSearchPaging;
import io.hoon.blogsearch.support.searchclient.interfaces.model.BlogSearchResponse;
import io.hoon.blogsearch.support.searchclient.interfaces.model.BlogSearchResponse.Content;
import io.hoon.blogsearch.support.searchclient.interfaces.model.BlogSearchResponse.Header;
import io.hoon.blogsearch.support.searchclient.utils.NaverParamUtils;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * <h1>블로그 검색 응답 매퍼</h1>
 * 외부에 노출하는 모델과 내부에서 사용하는 DTO 간의 의존을 줄이기 위한 매퍼
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BlogSearchResponseMapper {

    private static final DateTimeFormatter NAVER_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    /**
     * 카카오 API 응답 DTO 를 BlogSearchResponse 로 매핑합니다.
     *
     * @param dto    카카오 API 응답 DTO
     * @param paging 페이징 정보
     */
    public static BlogSearchResponse toBlogSearchResponse(KakaoApiResponseDto dto, BlogSearchPaging paging) {
        Header header = Header.builder()
            .totalCount(dto.getMeta().getTotalCount())
            .page(paging.getPage())
            .pageSize(paging.getPageSize())
            .end(dto.getMeta().isEnd())
            .build();

        List<Content> contents = dto.getDocuments()
            .stream()
            .map(d -> Content.builder()
                .postTitle(d.getTitle())
                .postContents(d.getContents())
                .postUrl(d.getUrl())
                .postDate(d.getDatetime())
                .blogTitle(d.getBlogname())
                .build())
            .toList();

        return BlogSearchResponse.builder()
            .header(header)
            .contents(contents)
            .build();
    }

    /**
     * 네이버 API 응답 DTO 를 BlogSearchResponse 로 매핑합니다.
     *
     * @param dto    네이버 API 응답 DTO
     */
    public static BlogSearchResponse toBlogSearchResponse(NaverApiResponseDto dto) {
        int page = NaverParamUtils.calculatePage(dto.getStart(), dto.getDisplay());

        Header header = Header.builder()
            .totalCount(dto.getTotal())
            .page(page)
            .pageSize(dto.getDisplay())
            .end(NaverParamUtils.calculateEnd(dto.getStart(), dto.getDisplay(), dto.getTotal()))
            .build();

        List<Content> contents = dto.getItems()
            .stream()
            .map(i -> Content.builder()
                .postTitle(i.getTitle())
                .postContents(i.getDescription())
                .postUrl(i.getLink())
                .postDate(ZonedDateTime.of(LocalDate.parse(i.getPostdate(), NAVER_DATE_FORMATTER), LocalTime.MIN, ZoneId.of("Asia/Seoul")))
                .blogTitle(i.getBloggername())
                .build())
            .toList();

        return BlogSearchResponse.builder()
            .header(header)
            .contents(contents)
            .build();
    }

}
