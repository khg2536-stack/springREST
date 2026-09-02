package com.oraclejava.springrest.dtos;

public record MovieDetailDto(
        int id,
        String name,
        int genreId,
        int price,
        int releaseYear
) {
}
