package com.example.filmBooking.repository;

import com.example.filmBooking.model.RankCustomer;
import com.example.filmBooking.model.SeatType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository

public interface SeatTypeRepository extends JpaRepository<SeatType, String> {
    @Query("SELECT s FROM SeatType s WHERE s.name = :name")
    SeatType findIdByName(@Param("name") String name);

}
