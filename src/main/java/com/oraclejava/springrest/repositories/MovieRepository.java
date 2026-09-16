package com.oraclejava.springrest.repositories;

import com.oraclejava.springrest.dtos.MovieSummaryDto;
import com.oraclejava.springrest.models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
//DB에서 데이터를 조회·저장하는 기능 제공
@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    @Query("""
        select new com.oraclejava.springrest.dtos.MovieSummaryDto(
                    m.id, m.name, m.genre.name, m.price, m.releaseYear
                    )
                from Movie m
    """)

    List<MovieSummaryDto> findMovieSummaries();
}