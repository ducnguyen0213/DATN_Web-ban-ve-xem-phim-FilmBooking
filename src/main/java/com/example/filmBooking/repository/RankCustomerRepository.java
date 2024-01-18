package com.example.filmBooking.repository;

import com.example.filmBooking.model.RankCustomer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface RankCustomerRepository extends JpaRepository<RankCustomer, String> {
    List<RankCustomer> findByNameContains(String keyword);
}
