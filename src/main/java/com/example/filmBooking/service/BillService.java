package com.example.filmBooking.service;

import com.example.filmBooking.model.Bill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.filmBooking.model.dto.DtoBill;
import com.example.filmBooking.model.dto.DtoBillList;

import java.util.Date;
import java.util.List;
import java.math.BigDecimal;


public interface BillService {
    List<Bill> findAll();

    Bill save(Bill bill);

    Bill update(String id, Bill bill);

    void delete(String id);

    Bill findById(String id);

    Page<Bill>findStatusZero(Integer pageNumber);

    List<Bill> findStatusZero2();

    Page<Bill>findStatusOne(Integer pageNumber);

    Pageable pageBill(Integer pageNumber);

    Page<Bill>searchDateAndDate(Date startDate, Date endDate, Integer pageNumber);
    
    List<BigDecimal> revenueInTheLast7Days(String cinemaId);

    List<Object[]> listTop5Movie();
    
}
