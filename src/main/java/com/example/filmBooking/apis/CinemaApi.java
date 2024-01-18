package com.example.filmBooking.apis;

import com.example.filmBooking.model.Cinema;
import com.example.filmBooking.repository.CinemaRepository;
import com.example.filmBooking.service.impl.CinemaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping(value="/api/cinema")
public class CinemaApi {
    @Autowired
    private CinemaServiceImpl service;


    @GetMapping
    private ResponseEntity<List<Cinema>> getCinemaThatShowTheMovie(@RequestParam String movieId){
        return new ResponseEntity<>(service.getCinema(movieId), HttpStatus.OK);
    }


//    @GetMapping("/cinema_name")
//    private ResponseEntity<List<Cinema>> getCinema(@RequestParam String movieId, @RequestParam String cinemaId){
//        return new ResponseEntity<>(cinemaRepository.getCinema(movieId, cinemaId), HttpStatus.OK);
//    }


}
