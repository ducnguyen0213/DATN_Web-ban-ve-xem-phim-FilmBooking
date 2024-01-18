package com.example.filmBooking.service.impl;


import com.example.filmBooking.model.MovieType;
import com.example.filmBooking.repository.DirectorRepository;
import com.example.filmBooking.repository.MovieTypeRepository;
import com.example.filmBooking.service.MovieTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.filmBooking.model.Movie;
import com.example.filmBooking.repository.MovieRepository;


import java.util.List;
import java.util.Random;

@Service
public class MovieTypeServiceImpl implements MovieTypeService {

    @Autowired
    private MovieTypeRepository movieTypeRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Override
    public List<MovieType> fillAll() {
        return movieTypeRepository.findAll();
    }

    @Override
    public MovieType save(MovieType movieType) {
        Random generator = new Random();
        int value = generator.nextInt((100000 - 1) + 1) + 1;
        movieType.setCode("TL" + value);
        return movieTypeRepository.save(movieType);
    }

    @Override
    public MovieType update(String id, MovieType movieType) {
        MovieType movieTypeUpdate = findById(id);
        movieTypeUpdate.setName(movieType.getName());
        return movieTypeRepository.save(movieTypeUpdate);
    }

    @Override
    public void delete(String id) {
        movieTypeRepository.delete(findById(id));
    }

    @Override
    public MovieType findById(String id) {
        return movieTypeRepository.findById(id).get();
    }

    @Override
    public List<MovieType> searchNameMovieType(String keycode) {
        return movieTypeRepository.findByNameContains(keycode);
    }
    
    @Override
    public List<MovieType> findMovieTyprbyMovieId(String movieId) {
        Movie movie = movieRepository.findById(movieId).get();
        List<MovieType> movieTypes = movie.getMovieTypes();
        return movieTypes;
    }

    @Override
    public MovieType findByNameLike(String name) {
        return movieTypeRepository.findByNameLike(name);
    }
}
