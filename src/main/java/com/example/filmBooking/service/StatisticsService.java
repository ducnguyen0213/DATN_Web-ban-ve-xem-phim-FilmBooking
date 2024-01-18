package com.example.filmBooking.service;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface StatisticsService {
    List<BigDecimal> revenueInTheLast7DaysThanhXuan();

    List<BigDecimal> revenueInTheLast7DaysMyDinh();

    List<BigDecimal> revenueInTheLast7DaysMipec();

    List<Object> revenueTicket(Date startDate, Date endDate);

    List<Object> revenueService(Date startDate, Date endDate);

    List<Object> revenueMovie(Date fromDate, Date toDate);
}
