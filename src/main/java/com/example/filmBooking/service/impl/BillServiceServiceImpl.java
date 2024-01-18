package com.example.filmBooking.service.impl;

import com.example.filmBooking.model.BillService;
import com.example.filmBooking.repository.BillServiceRepository;
import com.example.filmBooking.service.BillServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;


@Service
public class BillServiceServiceImpl implements BillServiceService {

    @Autowired
    private BillServiceRepository repository;

    @Override
    public List<BillService> findAll() {
        return repository.findAll();
    }

    @Override
    public BillService save(BillService billService) {
        Random generator = new Random();
        int value = generator.nextInt((100000 - 1) + 1) + 1;
//        billService.setCode("code_" + value);
        return repository.save(billService);
    }

    @Override
    public BillService update(String id, BillService billService) {
        BillService BillServiceNew = findById(id);
//        BillServiceNew.setName(BillService.getName());
//        BillServiceNew.setPoint(BillService.getPoint());
//        BillServiceNew.setDescription(BillService.getDescription());
        return repository.save(BillServiceNew);
    }

    @Override
    public BillService findById(String id) {
        return repository.findById(id).get();
    }

    @Override
    public void delete(String id) {
        repository.delete(findById(id));
    }
}
