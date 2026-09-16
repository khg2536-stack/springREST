package com.oraclejava.springrest.dtos;

public record MovieDetailDto(
        //요청이나 응답으로 전달할 데이터를 담음
        int id,
        String name,
        int genreId,
        int price,
        int releaseYear
) {
}
