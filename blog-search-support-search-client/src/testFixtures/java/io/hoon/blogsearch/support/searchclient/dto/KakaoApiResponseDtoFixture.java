package io.hoon.blogsearch.support.searchclient.dto;

import io.hoon.blogsearch.support.searchclient.dto.KakaoApiResponseDto.Document;
import io.hoon.blogsearch.support.searchclient.dto.KakaoApiResponseDto.Meta;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class KakaoApiResponseDtoFixture {

    private static final Random RANDOM = new Random();

    public static KakaoApiResponseDto get(int pageSize) {
        Meta meta = new Meta(10000L, pageSize, false);
        KakaoApiResponseDto result = new KakaoApiResponseDto(meta, new ArrayList<>());
        for (int i = 0; i < pageSize; i++) {
            result.getDocuments().add(new Document(
                UUID.randomUUID().toString(),
                "contents" + i,
                "url" + i,
                "blogname" + i,
                "thumbnail" + i,
                ZonedDateTime.now()
            ));
        }
        return result;
    }

    public static KakaoApiResponseDto getEnd(int pageSize) {
        RANDOM.setSeed(System.currentTimeMillis());
        int count = RANDOM.nextInt(pageSize);
        Meta meta = new Meta(10000L, pageSize, true);
        KakaoApiResponseDto result = new KakaoApiResponseDto(meta, new ArrayList<>());
        for (int i = 0; i < count; i++) {
            result.getDocuments().add(new Document(
                UUID.randomUUID().toString(),
                "contents" + i,
                "url" + i,
                "blogname" + i,
                "thumbnail" + i,
                ZonedDateTime.now()
            ));
        }
        return result;
    }
}
