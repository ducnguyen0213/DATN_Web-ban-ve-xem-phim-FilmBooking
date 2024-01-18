package com.example.filmBooking.service;

import com.example.filmBooking.model.MovieType;

import java.util.List;

public interface MovieTypeService {

    List<MovieType> fillAll();

    MovieType save(MovieType movieType);

    MovieType update(String id, MovieType movieType);

    void delete(String id);

    MovieType findById(String id);

    List<MovieType> searchNameMovieType(String keycode);
    
    List<MovieType> findMovieTyprbyMovieId(String movieId);

    MovieType findByNameLike(String name);
}
