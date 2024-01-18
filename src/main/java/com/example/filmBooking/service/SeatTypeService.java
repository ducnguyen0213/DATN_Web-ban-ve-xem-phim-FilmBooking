package com.example.filmBooking.service;

import com.example.filmBooking.model.SeatType;

import java.util.List;

public interface SeatTypeService {
    List<SeatType> findAll();

    SeatType save(SeatType seatType);

    SeatType update(String id, SeatType seatType);

    void delete(String id);

    SeatType findById(String id);

}
