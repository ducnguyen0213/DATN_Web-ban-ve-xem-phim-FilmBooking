package com.example.filmBooking.apis;

import com.example.filmBooking.model.Cinema;

import com.example.filmBooking.model.Movie;
import com.example.filmBooking.model.Schedule;
import com.example.filmBooking.model.dto.DtoMovie;
import com.example.filmBooking.model.dto.ScheduleDto;
import com.example.filmBooking.repository.CinemaRepository;
import com.example.filmBooking.repository.MovieRepository;
import com.example.filmBooking.repository.ScheduleRepository;
import com.example.filmBooking.service.ScheduleService;
import com.example.filmBooking.service.impl.CinemaServiceImpl;
import com.example.filmBooking.service.impl.MovieServiceImpl;
import com.example.filmBooking.service.impl.ScheduleServiceImpl;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/api/schedule")
public class ScheduleApi {

    @Autowired
    private ScheduleServiceImpl scheduleService;

    @Autowired
    private ScheduleRepository repository;

    @Autowired
    private CinemaServiceImpl cinemaService;

    @Autowired
    private MovieServiceImpl movieService;

    @GetMapping("/start_at")
    public List<String> getStart_At(@RequestParam String movieId, @RequestParam String cinemaId ) {
        return scheduleService.getStart_At(movieId,cinemaId);
    }


    @GetMapping("/cinema_name")
    private ResponseEntity<List<Cinema>> getCinema(@RequestParam String movieId, @RequestParam String cinemaId){
        return new ResponseEntity<>(cinemaService.getCinemaAndMovie(movieId, cinemaId), HttpStatus.OK);
    }

    @GetMapping("/movie_name")
    private ResponseEntity<List<Movie>> getMovie(@RequestParam String movieId, @RequestParam String cinemaId){
        return new ResponseEntity<>(movieService.getMovie(movieId, cinemaId), HttpStatus.OK);
    }

    @GetMapping("/time")
    private List<Object[]> getTime(@RequestParam String movieId, @RequestParam String cinemaId, @RequestParam String start_at){
        return scheduleService.getStart_At_Time(movieId, cinemaId,start_at);
    }

}
