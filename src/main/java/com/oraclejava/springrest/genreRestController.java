package com.oraclejava.springrest;

import com.oraclejava.springrest.models.Genre;
import com.oraclejava.springrest.repositories.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/genres")
public class genreRestController {
    @Autowired
    private GenreRepository genreRepository;

    @GetMapping
    public List<Genre> getGenres(){
        return genreRepository.findAll();
    }
}
