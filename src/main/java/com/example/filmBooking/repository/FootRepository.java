package com.example.filmBooking.repository;

import com.example.filmBooking.model.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FootRepository extends JpaRepository<Service, String> {
    Page<Service> findByNameContains(String keyword, Pageable pageable);

//    List<Service> findAll(Integer curentPage);
}
