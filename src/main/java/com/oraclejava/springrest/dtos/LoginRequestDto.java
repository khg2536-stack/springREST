package com.oraclejava.springrest.dtos;

public record LoginRequestDto(
        String username,
        String password
    ) {

}
