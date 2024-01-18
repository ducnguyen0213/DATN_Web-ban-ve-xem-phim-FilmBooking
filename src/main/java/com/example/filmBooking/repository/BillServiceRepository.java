package com.example.filmBooking.repository;

import com.example.filmBooking.model.BillService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface BillServiceRepository extends JpaRepository<BillService, String> {
    @Query(value = "from BillService bf where bf.bill.id = :billId ")
    List<BillService> findAllByBill(String billId);
}
