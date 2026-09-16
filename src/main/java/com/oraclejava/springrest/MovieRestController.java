package com.oraclejava.springrest;

import com.oraclejava.springrest.dtos.MovieDetailDto;
import com.oraclejava.springrest.dtos.MovieSummaryDto;
import com.oraclejava.springrest.models.Genre;
import com.oraclejava.springrest.models.Movie;
import com.oraclejava.springrest.repositories.GenreRepository;
import com.oraclejava.springrest.repositories.MovieRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


//컨트롤러는 외부에서 실행된 값을
@RestController
@RequestMapping(value = "/api/movies")
public class MovieRestController {

    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private GenreRepository genreRepository;

    //---------------------------------------------------------------------
    //모든 영화 조회 select * from movie
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<MovieSummaryDto> getMovie(){
        List<MovieSummaryDto> movies = movieRepository.findMovieSummaries();
        return movies;
    }

    //---------------------------------------------------------------------
    //값 조회 select * from Table where id = :id
    //GET/movie/{id} {id}번 영화 조회
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ResponseEntity<MovieDetailDto> getMovie(@PathVariable long id) {
        //                      Repository에 “이 번호의 제품을 찾아줘”라고 요청
        Optional<Movie> movie = movieRepository.findById(id);
        if (movie.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else {
            Movie realMovie = movie.get();
            MovieDetailDto dto = new MovieDetailDto(
                    (int)realMovie.getId(), realMovie.getName(),
                    (int)realMovie.getGenre().getId(),
                    realMovie.getPrice(),
                    realMovie.getReleaseYear()
            );
            return new ResponseEntity<>(dto, HttpStatus.OK);
        }
    }

    //---------------------------------------------------------------------
    //새로운 영화 추가 Insert
    // POST/MOVIES        INSERT INTO movie (name, genre_id, price, release_year)
    //                    VALUES (:name, :genre_id, :price, :release_year);
    @Transactional
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<Movie> addMovie(@RequestBody Movie movie){
        Genre genre = genreRepository.findById(movie.getGenreId()).orElseThrow();
        movie.setGenre(genre);
        movieRepository.save(movie);
        return ResponseEntity.status(HttpStatus.CREATED).body(movie);
    }

    //---------------------------------------------------------------------
    //기존 영화정보 수정 Update
    //@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @Transactional
    @PutMapping(value = "/{id}")
    public ResponseEntity<Void> updateMovie(@PathVariable long id, @RequestBody Movie movie){

        Optional<Movie> foundMovie = movieRepository.findById(id);
        if (foundMovie.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Movie realFoundMovie = foundMovie.get();
        realFoundMovie.setName(movie.getName());
        realFoundMovie.setPrice(movie.getPrice());
        realFoundMovie.setReleaseYear(movie.getReleaseYear());

        Genre genre = genreRepository.findById(movie.getGenreId())
                .orElseThrow();
        realFoundMovie.setGenre(genre);
        movieRepository.save(realFoundMovie);

        //return new ResponseEntity<>(HttpStatus.OK);
        return ResponseEntity.noContent().build();
    }

    //DELETE /movies/{id}} -> 지정 {id}번호 영화를 삭제
    @Transactional
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable long id){
        movieRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}