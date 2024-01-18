package com.example.filmBooking.service.impl;

import com.example.filmBooking.model.Rated;
import com.example.filmBooking.repository.RatedRepository;
import com.example.filmBooking.service.RatedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class RatedServiceImpl implements RatedService {

    @Autowired
    private RatedRepository repository;

    @Override
    public List<Rated> fillAll() {
        return repository.findAll();
    }

    @Override
    public Rated save(Rated Rated) {
//        Random generator = new Random();
//        int value = generator.nextInt((100000 - 1) + 1) + 1;
//        Rated.setCode("code_" + value);
        return repository.save(Rated);
    }

    @Override
    public Rated update(String id, Rated rated) {
        Rated RatedNew = findById(id);
        RatedNew.setDescription(rated.getDescription());
        return repository.save(RatedNew);
    }

    @Override
    public Rated findById(String id) {
        return repository.findById(id).get();
    }

    @Override
    public void delete(String id) {
        repository.delete(findById(id));
    }
     
    @Override
    public List<Rated> searchCodeRated(String keycode) {
        return repository.findByCodeContains(keycode);
    }
}
