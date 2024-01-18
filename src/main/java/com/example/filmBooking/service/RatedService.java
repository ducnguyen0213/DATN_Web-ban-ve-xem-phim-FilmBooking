package com.example.filmBooking.service;

import com.example.filmBooking.model.Rated;

import java.util.List;


public interface RatedService {
    List<Rated> fillAll();

    Rated save(Rated rated);

    Rated update(String id, Rated rated);

    void delete(String id);

    Rated findById(String id);
    
    List<Rated> searchCodeRated(String keycode);
}
