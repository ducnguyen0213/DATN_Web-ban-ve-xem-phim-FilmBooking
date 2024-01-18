package com.example.filmBooking.service.impl;

import com.example.filmBooking.model.SeatType;
import com.example.filmBooking.repository.SeatTypeRepository;
import com.example.filmBooking.service.SeatTypeService;
import com.example.filmBooking.service.SeatTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class SeatTypeServiceImpl implements SeatTypeService {

    @Autowired
    private SeatTypeRepository SeatTypeRepository;

    @Override
    public List<SeatType> findAll() {
        return SeatTypeRepository.findAll();
    }

    @Override
    public SeatType save(SeatType seatType) {
        Random generator = new Random();
        int value = generator.nextInt((100000 - 1) + 1) + 1;
        seatType.setCode("TL" + value);
        return SeatTypeRepository.save(seatType);
    }

    @Override
    public SeatType update(String id, SeatType seatType) {
        SeatType seatTypeUpdate = findById(id);
        seatTypeUpdate.setName(seatType.getName());
        seatTypeUpdate.setSurcharge(seatType.getSurcharge());
        return SeatTypeRepository.save(seatTypeUpdate);
    }

    @Override
    public void delete(String id) {
        SeatTypeRepository.delete(findById(id));
    }

    @Override
    public SeatType findById(String id) {
        return SeatTypeRepository.findById(id).get();
    }

}
