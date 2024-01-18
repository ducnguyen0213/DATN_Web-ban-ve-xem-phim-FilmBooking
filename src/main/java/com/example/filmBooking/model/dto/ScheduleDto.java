package com.example.filmBooking.model.dto;

import com.example.filmBooking.model.Cinema;
import com.example.filmBooking.model.Movie;
import com.example.filmBooking.model.Room;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class ScheduleDto {
    private String id;
    private LocalDateTime startAt;
    private LocalDateTime finishAt;

//    private LocalTime startTime;
    private Cinema cinema;
    private Room room;
    private Movie movie;
}
