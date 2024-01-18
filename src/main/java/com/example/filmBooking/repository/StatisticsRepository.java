package com.example.filmBooking.repository;

import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface StatisticsRepository {
    @Query(value = "SELECT DATE(date_create) AS day, SUM(total_money) AS revenue\n" +
            "FROM bill\n" +
            "WHERE MONTH(date_create) = [month] AND YEAR(date_create) = [year]\n" +
            "GROUP BY DATE(date_create)\n" +
            "ORDER BY DATE(date_create);", nativeQuery = true)
    List<BigDecimal> revenueEveryDayOfTheMonth();

}
