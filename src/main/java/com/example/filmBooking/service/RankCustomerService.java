package com.example.filmBooking.service;

import com.example.filmBooking.model.RankCustomer;

import java.util.List;


public interface RankCustomerService {
    List<RankCustomer> fillAll();

    RankCustomer save(RankCustomer rankCustomer);

    RankCustomer update(String id, RankCustomer rankCustomer);

    void delete(String id);

    RankCustomer findById(String id);
    
    List<RankCustomer> searchNameRank(String keyword);
}
