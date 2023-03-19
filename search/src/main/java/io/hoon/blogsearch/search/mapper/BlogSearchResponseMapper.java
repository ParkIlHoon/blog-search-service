package io.hoon.blogsearch.search.mapper;

import io.hoon.blogsearch.search.dto.KakaoApiResponseDto;
import io.hoon.blogsearch.search.dto.NaverApiResponseDto;
import io.hoon.blogsearch.search.interfaces.model.BlogSearchPaging;
import io.hoon.blogsearch.search.interfaces.model.BlogSearchResponse;
import io.hoon.blogsearch.search.interfaces.model.BlogSearchResponse.Content;
import io.hoon.blogsearch.search.interfaces.model.BlogSearchResponse.Header;
import io.hoon.blogsearch.search.utils.NaverParamUtils;
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

    /**
     * 카카오 API 응답 DTO 를 BlogSearchResponse 로 매핑합니다.
     *
     * @param dto    카카오 API 응답 DTO
     * @param paging 페이징 정보
     */
    public static BlogSearchResponse toBlogSearchResponse(KakaoApiResponseDto dto, BlogSearchPaging paging) {
        Header header = Header.builder()
            .totalCount(dto.getMeta().getPageableCount())
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

        List<Content> contents = dto.getItem()
            .stream()
            .map(i -> Content.builder()
                .postTitle(i.getTitle())
                .postContents(i.getDescription())
                .postUrl(i.getLink())
                .postDate(ZonedDateTime.parse(i.getPostdate(), DateTimeFormatter.ISO_DATE))
                .blogTitle(i.getBloggername())
                .build())
            .toList();

        return BlogSearchResponse.builder()
            .header(header)
            .contents(contents)
            .build();
    }

}
