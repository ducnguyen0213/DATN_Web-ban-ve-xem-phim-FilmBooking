package com.example.filmBooking.service.impl;

import com.example.filmBooking.model.Room;
import com.example.filmBooking.model.Schedule;
import com.example.filmBooking.model.Seat;
import com.example.filmBooking.model.Ticket;
import com.example.filmBooking.repository.RoomRepository;
import com.example.filmBooking.repository.SeatRepository;
import com.example.filmBooking.repository.TicketRepository;
import com.example.filmBooking.service.ScheduleService;
import com.example.filmBooking.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketRepository repository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private ScheduleService scheduleRepository;
    @Autowired
    private SeatRepository seatRepository;

    @Override
    public List<Ticket> fillAll() {
        return repository.findAll();
    }


    @Override
    public Ticket autoSave(String idSchedule) {
        Schedule schedule = scheduleRepository.findById(idSchedule);
        Room room = roomRepository.findById(schedule.getRoom().getId()).get();
        Ticket ticket = new Ticket();
        List<Seat> numberSeat1 = new ArrayList<>();
        List<Seat> seats = seatRepository.findAllByRoom(room.getId());
        for (Seat seat : seats) {
            numberSeat1.add(seat);
        }
        for (int i = 0; i < numberSeat1.size(); i++) {
            ticket.setId(UUID.randomUUID().toString());
            Random generator = new Random();
            int value = generator.nextInt((1000 - 1) + 1) + 1;
            ticket.setCode("TK" + value);
            ticket.setSchedule(schedule);
            ticket.setSeat(numberSeat1.get(i));
            repository.save(ticket);
        }
        return null;
    }

    // Đổi trạng thái vé
//    @Async
//    @Scheduled(fixedRate = 60000)
    public void scheduleFixedRate() {
        // danh sách lịch chiếu
        List<Ticket> listTicket = repository.findAll();
        for (Ticket ticket : listTicket) {
            Schedule schedule = scheduleRepository.findById(ticket.getSchedule().getId());
            System.out.println(ticket.getSchedule().getId());
            if (schedule.getStatus() == "Đã chiếu") {
                ticket.setStatus("Hết hạn");
                repository.save(ticket);
            } else {
                ticket.setStatus("Hạn sử dụng đến: " + schedule.getFinishAt());
                repository.save(ticket);
            }
        }
    }

    @Override
    public Ticket update(String id, Ticket ticket) {
        Ticket ticketNew = findById(id);
        ticketNew.setSchedule(ticket.getSchedule());
        ticketNew.setSeat(ticket.getSeat());
        return repository.save(ticketNew);
    }

    @Override
    public Ticket findById(String id) {
        return repository.findById(id).get();
    }

    @Override
    public List<Ticket> getTicket(String cinemaId, String movieId, String startAt, String startTime, String nameRoom) {
        return repository.findTicketsBySchedule_Id(cinemaId, movieId, startAt, startTime, nameRoom);
    }

    @Override
    public void delete(String id) {
        repository.delete(findById(id));
    }

    public static void main(String[] args) {
        // Giá ban đầu
    }


    // hàm check

    public String checkSchedule() {
        LocalDateTime date = LocalDateTime.now();
        // list schedule
        List<Schedule> listSchedule = scheduleRepository.findAll();
        for (Schedule dto : listSchedule) {
            if (date.isAfter(dto.getFinishAt())) {
                //tìm kiếm tất cả vé
                List<Ticket> tickets = repository.findBySchedule(dto.getId());
                for (Ticket ticket : tickets) {
                    // thực hiện update
                    ticket.setStatus("Đã chiếu");
                    repository.save(ticket);
                }

            }
        }

        return null;
    }

    @Override
    public Page<Ticket> getAll(Integer pageNumber) {
        return repository.findAll(pageTicket(pageNumber));
    }

    @Override
    public Pageable pageTicket(Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1, 10);
        return pageable;
    }

    @Override
    public Page<Ticket> findByScheduleId(String scheduleId, Integer pageable) {
        return repository.findByScheduleId(scheduleId, pageTicket(pageable));
    }

//    @Override
//    public Page<Ticket> findAllByStatus(String status, Integer pageNumber) {
//        return repository.findAllByStatus(status, pageTicket(pageNumber));
//    }

    @Override
    public Page<Ticket> findAllByStatus(String scheduleId, String status, Integer pageNumber) {
        return repository.searchTicket(scheduleId, status, pageTicket(pageNumber));
    }

}
