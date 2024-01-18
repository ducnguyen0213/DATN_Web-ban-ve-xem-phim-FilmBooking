package com.example.filmBooking.model.dto;

import com.example.filmBooking.model.Customer;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BillDto {
    private String id;
    private LocalDateTime dateCreate;
    private Integer status;
    private BigDecimal totalMoney;
    private String tradingCode;
    private Customer customer;
}
