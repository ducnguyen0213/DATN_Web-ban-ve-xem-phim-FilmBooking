package com.example.filmBooking.repository;

import com.example.filmBooking.model.MovieType;
import com.example.filmBooking.model.Performer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MovieTypeRepository extends JpaRepository<MovieType, String> {
    List<MovieType> findByNameContains(@Param("name") String keyCode);

    MovieType findByNameLike(String name);
}
