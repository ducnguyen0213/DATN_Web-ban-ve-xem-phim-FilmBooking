package com.example.filmBooking.service;

import com.example.filmBooking.model.BillService;

import java.util.List;


public interface BillServiceService {
    List<BillService> findAll();

    BillService save(BillService billService);

    BillService update(String id, BillService billService);

    void delete(String id);

    BillService findById(String id);

}
