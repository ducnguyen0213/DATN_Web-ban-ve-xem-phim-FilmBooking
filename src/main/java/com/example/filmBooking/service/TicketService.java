package com.example.filmBooking.service;

import com.example.filmBooking.model.Seat;
import com.example.filmBooking.model.Ticket;

import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TicketService {
    List<Ticket> fillAll();

    Ticket autoSave(String idSchedule);

    Ticket update(String id, Ticket ticket);

    void delete(String id);

    Ticket findById(String id);

    List<Ticket> getTicket(String cinemaId, String movieId, String startAt, String startTime, String nameRoom);
            
    Page<Ticket> getAll(Integer pageNumber);
    
    Pageable pageTicket(Integer pageNumber);

    Page<Ticket> findByScheduleId(String scheduleId, Integer pageable);

    Page<Ticket> findAllByStatus(String scheduleId, String status, Integer pageNumber);

//    Page<Ticket> findAllByStatus(String roomName, String movieName, Date dateSearch, String status, Integer pageNumber);

}
