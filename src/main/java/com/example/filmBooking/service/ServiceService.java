package com.example.filmBooking.service;

import com.example.filmBooking.model.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ServiceService {
    List<Service> fillAll();

    Page<Service> findAll(Integer curentPage);

    Service save(Service Service);

    Service update(String id, Service Service);

    void delete(String id);

    Service findById(String id);

    Pageable pageService(Integer pageNumber);

    Page<Service>findByNameContains(String keyword, Integer currentPage);
}
