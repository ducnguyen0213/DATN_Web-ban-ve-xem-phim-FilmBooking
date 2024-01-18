package com.example.filmBooking.model.dto;

import lombok.Data;

@Data
public class SeatDTO {
    private String id;
    private String name;
    private int isOccupied;

}
