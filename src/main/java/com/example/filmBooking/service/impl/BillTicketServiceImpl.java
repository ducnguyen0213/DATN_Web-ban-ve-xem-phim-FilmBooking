package com.example.filmBooking.service.impl;

import com.example.filmBooking.model.BillTicket;
import com.example.filmBooking.repository.BillTicketRepository;
import com.example.filmBooking.service.BillTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;


@Service
public class BillTicketServiceImpl implements BillTicketService {

    @Autowired
    private BillTicketRepository repository;

    @Override
    public List<BillTicket> findAll() {
        return repository.findAll();
    }

    @Override
    public BillTicket save(BillTicket billTicket) {
        Random generator = new Random();
        int value = generator.nextInt((100000 - 1) + 1) + 1;
//        BillTicket.setCode("code_" + value);
        return repository.save(billTicket);
    }

    @Override
    public BillTicket update(String id, BillTicket billTickets) {
        BillTicket BillTicketNew = findById(id);
//        BillTicketNew.setName(BillTicket.getName());
//        BillTicketNew.setPoint(BillTicket.getPoint());
//        BillTicketNew.setDescription(BillTicket.getDescription());
        BillTicketNew.setQuantity(billTickets.getQuantity());
        return repository.save(BillTicketNew);
    }

    @Override
    public BillTicket findById(String id) {
        return repository.findById(id).get();
    }

    @Override
    public void delete(String id) {
        repository.delete(findById(id));
    }
}
