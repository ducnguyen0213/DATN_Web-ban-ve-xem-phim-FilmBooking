package com.example.filmBooking.service.impl;

import com.example.filmBooking.model.*;
import com.example.filmBooking.repository.*;
import com.example.filmBooking.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.example.filmBooking.model.dto.DtoBill;
import com.example.filmBooking.model.dto.DtoBillList;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.Date;
import java.math.BigDecimal;

@Service
public class BillServiceImpl implements BillService {

    @Autowired
    private BillRepository repository;

    @Override
    public List<Bill> findAll() {
        return repository.findAll();
    }

    @Override
    public Bill save(Bill bill) {
        Random generator = new Random();
        int value = generator.nextInt((100000 - 1) + 1) + 1;
        bill.setCode("Bill" + value);
        bill.setName("Bill" + bill.getDateCreate());
        return repository.save(bill);
    }



//    @Scheduled(fixedRate = 60000)
    @Async
    @Scheduled(cron = "* 0/1 8-23,0-2  * * *")
//    @Scheduled(cron = "0 0 8-23 * * *") // Chạy từ 8 giờ đến 23 giờ
//    @Scheduled(cron = "0 0 0-2 * * *") // Chạy từ 0 giờ đến 2 giờ
    public void updateBill(){
        for (Bill bill: findStatusZero2()
             ) {
            if (bill.getWaitingTime() != null && bill.getWaitingTime().isBefore(LocalDateTime.now())){
                bill.setStatus(2);
                repository.save(bill);
            }
        }
    }

    @Override
    public Bill update(String id, Bill bill) {
        Bill BillNew = findById(id);
        return repository.save(BillNew);
    }

    @Override
    public Bill findById(String id) {
        return repository.findById(id).get();
    }

    @Override
    public void delete(String id) {
        repository.delete(findById(id));
    }
    
    @Override
    public Page<Bill> findStatusZero(Integer pageNumber) {
        return repository.billStatusZero(pageBill(pageNumber));
    }

     @Override
    public List<Bill> findStatusZero2() {
        return repository.billStatusZero2();
    }

    @Override
    public Page<Bill> findStatusOne(Integer pageNumber) {
        return repository.billStatusOne(pageBill(pageNumber));
    }

    @Override
    public Pageable pageBill(Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber -1, 5);
        return pageable;
    }

    @Override
    public Page<Bill> searchDateAndDate(Date startDate, Date endDate, Integer pageNumber) {
        return repository.findByDateCreateBetween(startDate, endDate, pageBill(pageNumber));
    }
    
    @Override
    public List<BigDecimal> revenueInTheLast7Days(String cinemaId) {
        return repository.revenueInTheLast7Days(cinemaId);
    }

    @Override
    public List<Object[]> listTop5Movie() {
        return repository.listTop5Movie();
    }


}
