package io.hoon.blogsearch.search.dto;

import io.hoon.blogsearch.search.dto.NaverApiResponseDto.Item;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NaverApiResponseDtoFixture {

    private static final Random RANDOM = new Random();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");


    public static NaverApiResponseDto get(int total, int start, int display) {
        NaverApiResponseDto result = new NaverApiResponseDto(
            total,
            start,
            display,
            new ArrayList<>()
        );

        for (int i = 0; i < display; i++) {
            result.getItems().add(new Item(
                UUID.randomUUID().toString(),
                "url" + i,
                "description" + i,
                "bloggername" + i,
                "bloggerlink" + i,
                LocalDate.now().format(FORMATTER)
            ));
        }
        return result;
    }
    public static NaverApiResponseDto getEnd(int display) {
        RANDOM.setSeed(System.currentTimeMillis());
        int totalCount = RANDOM.nextInt(display);

        NaverApiResponseDto result = new NaverApiResponseDto(
            totalCount,
            1,
            display,
            new ArrayList<>()
        );

        for (int i = 0; i < totalCount; i++) {
            result.getItems().add(new Item(
                UUID.randomUUID().toString(),
                "url" + i,
                "description" + i,
                "bloggername" + i,
                "bloggerlink" + i,
                LocalDate.now().format(FORMATTER)
            ));
        }
        return result;
    }
}
