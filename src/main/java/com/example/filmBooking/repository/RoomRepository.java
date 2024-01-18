package com.example.filmBooking.repository;

import com.example.filmBooking.model.Room;
import com.example.filmBooking.model.SeatType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface RoomRepository extends JpaRepository<Room, String> {
    String findNumberOfSeat = "select COUNT(*) from seat WHERE room_id =:id";

    @Query(value = findNumberOfSeat, nativeQuery = true)
    Integer findNumber(String id);
    
    List<Room> findByCapacityIsNull();

    @Query(value = "select * from room where capacity = 40", nativeQuery = true)
    List<Room> roomCapcity();

    Page<Room> findByNameContains(String name, Pageable pageable);

    @Query("SELECT r FROM Room r WHERE r.name = :name")
    Room findIdByName(@Param("name") String name);
}
