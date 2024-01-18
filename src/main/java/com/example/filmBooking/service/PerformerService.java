package com.example.filmBooking.service;

import com.example.filmBooking.model.Performer;
import com.example.filmBooking.model.Rated;

import java.util.List;

public interface PerformerService {
    List<Performer> fillAll();

    Performer save(Performer performer);

    Performer update(String id, Performer performer);

    void delete(String id);

    Performer findById(String id);

    List<Performer> searchNamePerformer(String keycode);
    
    List<Performer> findPerformerByMovieId(String id);

    Performer findByNameLike(String name);
}
