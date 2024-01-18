package com.example.filmBooking.service.impl;

import com.example.filmBooking.model.Director;
import com.example.filmBooking.repository.DirectorRepository;
import com.example.filmBooking.service.DirectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.filmBooking.repository.MovieRepository;
import com.example.filmBooking.model.Movie;


import java.util.List;
import java.util.Random;

@Service
public class DirectorServiceImpl implements DirectorService {

    @Autowired
    private DirectorRepository directorRepository;
    
    @Autowired
    private MovieRepository movieRepository;

    @Override
    public List<Director> fillAll() {
        return directorRepository.findAll();
    }

    @Override
    public Director save(Director director) {
        Random generator = new Random();
        int value = generator.nextInt((100000 - 1) + 1) + 1;
        director.setCode("DD" + value);
        return directorRepository.save(director);
    }

    @Override
    public Director update(String id, Director director) {
        Director directorUpdate = findById(id);
        directorUpdate.setName(director.getName());
        return directorRepository.save(directorUpdate);
    }

    @Override
    public void delete(String id) {
        directorRepository.delete(findById(id));
    }

    @Override
    public Director findById(String id) {
        return directorRepository.findById(id).get();
    }

    @Override
    public List<Director> searchNameDirector(String keycode) {
        return directorRepository.findByNameContains(keycode);
    }
    
    @Override
    public List<Director> findDireactorByMovieId(String movieId) {
        Movie movie = movieRepository.findById(movieId).get();
        List<Director> directors = movie.getDirectors();
        return directors;
    }

    @Override
    public Director findByNameLike(String name) {
        return directorRepository.findByNameLike(name);
    }
}
