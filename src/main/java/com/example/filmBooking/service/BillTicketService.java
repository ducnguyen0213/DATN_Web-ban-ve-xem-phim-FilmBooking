package com.example.filmBooking.service;

import com.example.filmBooking.model.BillTicket;

import java.util.List;


public interface BillTicketService {
    List<BillTicket> findAll();

    BillTicket save(BillTicket billTicket);

    BillTicket update(String id, BillTicket billTicket);

    void delete(String id);

    BillTicket findById(String id);

}
