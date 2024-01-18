package com.example.filmBooking.service.impl;

import com.example.filmBooking.model.Service;
import com.example.filmBooking.repository.FootRepository;
import com.example.filmBooking.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Random;

@org.springframework.stereotype.Service

public class ServiceServiceImpl implements ServiceService {
    @Autowired
    private FootRepository footRepository;

    @Override
    public List<Service> fillAll() {
        return footRepository.findAll();
    }

    @Override
    public Service save(Service Service) {
        Random generator = new Random();
        int value = generator.nextInt((100000 - 1) + 1) + 1;
        Service.setCode("F" + value);
        return footRepository.save(Service);
    }

    @Override
    public Service update(String id, Service Service) {
        Service Services = findById(id);
        Services.setDescription(Service.getDescription());
        Services.setName(Service.getName());
        Services.setPrice(Service.getPrice());
        Services.setServiceType(Service.getServiceType());
        Services.setImage(Service.getImage());
        return footRepository.save(Services);
    }

    @Override
    public void delete(String id) {
        footRepository.delete(findById(id));
    }

    @Override
    public Service findById(String id) {
        return footRepository.findById(id).get();
    }
    
    @Override
    public Page<Service> findAll(Integer currentPage) {
        return footRepository.findAll(pageService(currentPage));
    }
    
    @Override
    public Pageable pageService(Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber -1, 5);
        return pageable;
    }

    @Override
    public Page<Service> findByNameContains(String keyword, Integer currentPage) {
        return footRepository.findByNameContains(keyword, pageService(currentPage));
    }
}
