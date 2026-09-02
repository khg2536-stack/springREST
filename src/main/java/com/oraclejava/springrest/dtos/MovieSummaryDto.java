package com.oraclejava.springrest.dtos;

public record MovieSummaryDto(
        long id,
        String name,
        String genre,
        long price,
        int releaseYear
) {
}
