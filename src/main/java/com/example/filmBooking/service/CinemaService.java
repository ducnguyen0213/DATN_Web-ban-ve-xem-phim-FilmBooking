package com.example.filmBooking.service;

import com.example.filmBooking.model.Cinema;

import java.util.List;


public interface CinemaService {
    List<Cinema> fillAll();


    Cinema save(Cinema cinema);

    Cinema update(String id, Cinema cinema);

    void delete(String id);

    Cinema findById(String id);

    List<Cinema> getCinema( String movieId);

    List<Cinema> getCinemaAndMovie( String movieId, String cinemaId);

    List<Cinema> searchCinema(String keyword);
}
