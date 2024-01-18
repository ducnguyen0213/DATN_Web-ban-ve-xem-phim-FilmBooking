package com.example.filmBooking.service.impl;

import com.example.filmBooking.model.RankCustomer;
import com.example.filmBooking.repository.RankCustomerRepository;
import com.example.filmBooking.service.RankCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;


@Service
public class RankCustomerServiceImpl implements RankCustomerService {

    @Autowired
    private RankCustomerRepository repository;

    @Override
    public List<RankCustomer> fillAll() {
        return repository.findAll();
    }

    @Override
    public RankCustomer save(RankCustomer rankCustomer) {
        Random generator = new Random();
        int value = generator.nextInt((100000 - 1) + 1) + 1;
        rankCustomer.setCode("code_" + value);
        return repository.save(rankCustomer);
    }

    @Override
    public RankCustomer update(String id, RankCustomer rankCustomer) {
        RankCustomer rankNew = findById(id);
        rankNew.setName(rankCustomer.getName());
        rankNew.setPoint(rankCustomer.getPoint());
        rankNew.setDescription(rankCustomer.getDescription());
        return repository.save(rankNew);
    }

    @Override
    public RankCustomer findById(String id) {
        return repository.findById(id).get();
    }

    @Override
    public void delete(String id) {
        repository.delete(findById(id));
    }
     
    @Override
    public List<RankCustomer> searchNameRank(String keyword) {
        return repository.findByNameContains(keyword);
    }
}
