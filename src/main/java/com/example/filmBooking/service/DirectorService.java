package com.example.filmBooking.service;

import com.example.filmBooking.model.Director;

import java.util.List;

public interface DirectorService {

    List<Director> fillAll();

    Director save(Director director);

    Director update(String id, Director director);

    void delete(String id);

    Director findById(String id);

    List<Director> searchNameDirector(String keycode);
    
    List<Director> findDireactorByMovieId(String movieId);

    Director findByNameLike(String name);
}
