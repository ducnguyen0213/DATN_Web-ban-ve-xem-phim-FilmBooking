package com.example.filmBooking.repository;

import com.example.filmBooking.model.Rated;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface RatedRepository extends JpaRepository<Rated, String> {
    List<Rated> findByCodeContains(String keycode);
}
