package com.example.filmBooking.service;

import com.example.filmBooking.model.Cinema;
import com.example.filmBooking.model.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface RoomService {

    Page<Room> getAll(Integer currentPage);

    List<Room> fillAll();

    boolean saveAll(Cinema idCinema, String description, Integer capacity, Integer width,Integer height, String projector, String other_equipment, Integer status);

    Room save(Room room);

    Room update(String id, Room room);

    Room updateSeat(String id, Room room);

    void delete(String id);

    Room findById(String id);

    Pageable pageRoom(Integer pageNumber);

    List<Room> finByRoom();

    List<Room> roomCapacity();

    Page<Room> serachRoom(String keyword, Integer currentPage);
}
