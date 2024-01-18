package com.example.filmBooking.repository;

import com.example.filmBooking.model.Bill;
import com.example.filmBooking.model.BillService;
import com.example.filmBooking.model.BillTicket;
import com.example.filmBooking.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface BillTicketRepository extends JpaRepository<BillTicket, String> {
    @Query("from BillTicket bt where bt.bill.id = :billId ")
    List<BillTicket> findAllByBill(String billId);
}
