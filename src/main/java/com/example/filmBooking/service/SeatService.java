package com.example.filmBooking.service;

import com.example.filmBooking.model.Room;
import com.example.filmBooking.model.Seat;
import com.example.filmBooking.model.dto.DtoSeat;
import com.example.filmBooking.model.dto.SeatDTO;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface SeatService {
    List<Seat> getAll();

    List<Seat> findAllByRoom(String roomId);

    Seat save(List<String> listLineCodes, List<String> listSeatTypeId, List<Integer> listNumberOfSeatPerLine, String roomId);

    Seat update(String id, Seat seat);

    void delete(String id);

    Seat findById(String id);

    List<SeatDTO> getSeatsByScheduleId(String scheduleId);

    List<Object[]> getSeatsByCustomerId(String customerId);

    List<DtoSeat> getSeats(String cinemaId,String movieId,String startAt, String startTime, String roomName);

    List<DtoSeat> getSeats1(String movieName,String startAt, String roomName);

    Page<Seat> findAll(Integer currentPage);

    List<Seat> listSeat(String roomName);

    Pageable pageSeat(Integer pagaNumber);

    Page<Seat> searchByRoom (String id, Integer currentPage);

    Seat readExcel(MultipartFile file, Room roomId);

    Seat saveAll(Seat seat);

    Seat saveSeat(Seat seat);
}
