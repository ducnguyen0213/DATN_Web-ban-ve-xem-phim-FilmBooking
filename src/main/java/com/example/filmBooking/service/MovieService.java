package com.example.filmBooking.service;

import com.example.filmBooking.model.Movie;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

public interface MovieService {
    List<Movie> findAll();

    Movie save(Movie movie);

    Movie update(String id, Movie Movie);

    void delete(String id);

    Movie findById(String id);

    void exportExcel();

    Movie readExcel();

    List<Movie> showPhimDangChieu();
    
    List<Movie> showPhimSapChieu();

    List<Movie> showPhishowPhimSapChieuAndDangChieumSapChieu();

    List<Movie> getMovie(String cinemaId, String movieId);

    Page<Movie> getAll(Integer pageNumber);

    Pageable pageMovie(Integer pageNumber);

    Movie findByName(String name);

    Page<Movie> filterMovies(Integer pageNumber, String directors, String languages, String movieTypes,  String performers, String status, String keyword);
}
