package com.oraclejava.springrest;

import com.oraclejava.springrest.models.Genre;
import com.oraclejava.springrest.models.Movie;
import com.oraclejava.springrest.repositories.GenreRepository;
import com.oraclejava.springrest.repositories.MovieRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/movies")
public class movieRestController {

    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private GenreRepository genreRepository;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<Movie> getMovie(){
        List<Movie> movies = movieRepository.findAll();
        System.out.println(movies);
        return movies;
    }
    //GET/movie/1 1번 영화 조회
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Movie> getMovie(@PathVariable long id){
        Optional<Movie> movie = movieRepository.findById(id);
        if (movie.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else {
            return new ResponseEntity<>(movie.get(), HttpStatus.OK);
        }
    }
    // POST/MOVIES
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<Movie> addMovie(@RequestBody Movie movie){
        Genre genre = genreRepository.findById(movie.getGenreId()).orElseThrow();
        movie.setGenre(genre);
        movieRepository.save(movie);

//        HttpHeaders headers = new HttpHeaders();
//        headers.setLocation(URI.create("/movies/" + movie.getId()));
//        return new ResponseEntity<>(headers, HttpStatus.FOUND); // 302 redirect
        return ResponseEntity.status(HttpStatus.CREATED).body(movie);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateMovie(@PathVariable long id, @RequestBody Movie movie){

        Optional<Movie> foundMovie = movieRepository.findById(id);
        if (foundMovie.isEmpty()){
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Movie realFoundMovie = foundMovie.get();
        //System.out.println(realFoundMovie);
        realFoundMovie.setName(movie.getName());
        realFoundMovie.setPrice(movie.getPrice());
        realFoundMovie.setReleaseYear(movie.getReleaseYear());

        Genre genre = genreRepository.findById(movie.getGenreId()).orElseThrow();
        movie.setGenre(genre);
        movieRepository.save(realFoundMovie);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    //DELETE /movies/15 -> 15번 영화를 삭제
    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteMovie(@PathVariable long id){
        movieRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
