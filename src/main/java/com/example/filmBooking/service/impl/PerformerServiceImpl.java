package com.example.filmBooking.service.impl;

import com.example.filmBooking.model.Performer;
import com.example.filmBooking.model.Rated;
import com.example.filmBooking.repository.PerformerRepository;
import com.example.filmBooking.service.PerformerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.filmBooking.model.Movie;
import com.example.filmBooking.repository.MovieRepository;


import java.util.List;
import java.util.Random;

@Service
public class PerformerServiceImpl implements PerformerService {

    @Autowired
    private PerformerRepository performerRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Override
    public List<Performer> fillAll() {
        return performerRepository.findAll();
    }

    @Override
    public Performer save(Performer performer) {
        Random generator = new Random();
        int value = generator.nextInt((100000 - 1) + 1) + 1;
        performer.setCode("DV" + value);
        return performerRepository.save(performer);
    }

    @Override
    public Performer update(String id, Performer performer) {
        Performer performerUpdate = findById(id);
        performerUpdate.setName(performer.getName());
        return performerRepository.save(performerUpdate);
    }

    @Override
    public void delete(String id) {
        performerRepository.delete(findById(id));
    }

    @Override
    public Performer findById(String id) {
        return performerRepository.findById(id).get();
    }

    @Override
    public List<Performer> searchNamePerformer(String keycode) {
        return performerRepository.findByNameContains(keycode);
    }
    
    @Override
    public List<Performer> findPerformerByMovieId(String id) {
        Movie movie = movieRepository.findById(id).get();
        List<Performer> listPerformer = movie.getPerformers();
        return listPerformer;
    }

    @Override
    public Performer findByNameLike(String name) {
        return performerRepository.findByNameLike(name);
    }
}
