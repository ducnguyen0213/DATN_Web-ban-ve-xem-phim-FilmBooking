package com.example.filmBooking.model.dto;

import com.example.filmBooking.model.Room;
import com.example.filmBooking.model.SeatType;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class DtoSeat {
    private String id;
    private Room room;
    private String code;
    private String line;
    private String description;
    private Integer status;
    private Integer number;
    private String isOccupied;
    private String ticketId;
    private SeatType seatType;

}
