package com.example.filmBooking.repository;

import com.example.filmBooking.model.BillTicket;
import com.example.filmBooking.model.Promotion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@Repository
public interface PromotionRepository extends JpaRepository<Promotion, String> {
    String voucherCustomerValue = "select p.* from promotion p " +
            "join rank_customer r on p.rank_customer_id= r.id " +
            "join customer c on c.rank_customer_id= r.id " +
            "where c.id= ?1 and quantity>0";

    @Query(value = voucherCustomerValue, nativeQuery = true)
    List<Promotion> listVoucherCustomer(String customerId);
    
    Page<Promotion> findByCodeContains(String keyword, Pageable pageable);

    @Query(value = "SELECT bt" +
            "FROM bill b\n" +
            "JOIN bill_ticket bt on bt.bill_id= b.id\n" +
            "JOIN ticket t on bt.ticket_id= t.id\n" +
            "JOIN schedule s ON t.schedule_id= s.id\n" +
            "JOIN room r ON s.room_id = r.id\n" +
            "JOIN cinema c ON c.id = r.cinema_id\n" +
            "WHERE b.date_create BETWEEN ?1 AND ?2", nativeQuery = true)
    List<BillTicket> searchBillWithTicket();

    @Query(value = "select sum(total_money) from bill b join\n" +
            "            (SELECT DISTINCT bill_id, c.id, c.name FROM bill_ticket bt\n" +
            "            JOIN ticket t ON t.id = bt.ticket_id\n" +
            "            JOIN schedule s ON t.schedule_id = s.id\n" +
            "            JOIN room r ON s.room_id = r.id\n" +
            "            JOIN cinema c ON c.id = r.cinema_id) d on d.bill_id = b.id\n" +
            "            WHERE \n" +
            "            d.name='Mỹ Đình' and\n" +
            "                b.date_create >= DATE_SUB(CURDATE(), INTERVAL 7 DAY) \n" +
            "            GROUP BY DATE(b.date_create)\n" +
            "             ORDER BY DATE(b.date_create) DESC  "
            , nativeQuery = true)
    List<BigDecimal> revenueInTheLast7DaysMyDinh();

    @Query(value = "select sum(total_money) from bill b join\n" +
            "            (SELECT DISTINCT bill_id, c.id, c.name FROM bill_ticket bt\n" +
            "            JOIN ticket t ON t.id = bt.ticket_id\n" +
            "            JOIN schedule s ON t.schedule_id = s.id\n" +
            "            JOIN room r ON s.room_id = r.id\n" +
            "            JOIN cinema c ON c.id = r.cinema_id) d on d.bill_id = b.id\n" +
            "            WHERE \n" +
            "            d.name='Thanh Xuân' and\n" +
            "                b.date_create >= DATE_SUB(CURDATE(), INTERVAL 7 DAY) \n" +
            "            GROUP BY DATE(b.date_create)\n" +
            "             ORDER BY DATE(b.date_create) DESC  "
            , nativeQuery = true)
    List<BigDecimal> revenueInTheLast7DaysThanhXuan();

    @Query(value = "select sum(total_money) from bill b join\n" +
            "            (SELECT DISTINCT bill_id, c.id, c.name FROM bill_ticket bt\n" +
            "            JOIN ticket t ON t.id = bt.ticket_id\n" +
            "            JOIN schedule s ON t.schedule_id = s.id\n" +
            "            JOIN room r ON s.room_id = r.id\n" +
            "            JOIN cinema c ON c.id = r.cinema_id) d on d.bill_id = b.id\n" +
            "            WHERE \n" +
            "            d.name='Mipec Tower' and\n" +
            "                b.date_create >= DATE_SUB(CURDATE(), INTERVAL 7 DAY) \n" +
            "            GROUP BY DATE(b.date_create)\n" +
            "             ORDER BY DATE(b.date_create) DESC  "
            , nativeQuery = true)
    List<BigDecimal> revenueInTheLast7DaysMipec();

    @Query(value = "SELECT sum(bt.total_money) as totalMoney, c.name as cinemaName\n" +
            "FROM bill b\n" +
            "JOIN bill_ticket bt on bt.bill_id= b.id\n" +
            "JOIN ticket t on bt.ticket_id= t.id\n" +
            "JOIN schedule s ON t.schedule_id= s.id\n" +
            "JOIN room r ON s.room_id = r.id\n" +
            "JOIN cinema c ON c.id = r.cinema_id\n" +
            "WHERE " +
            "(:fromDate IS NULL OR b.date_create >= :fromDate) " +
            "AND (:toDate IS NULL OR b.date_create <= :toDate) \n" +
            "AND b.status =1 " +
            "group by c.id", nativeQuery = true)
    List<Object> revenueTicket(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

    @Query(value = "SELECT f.name AS service_name, SUM(bf.quantity * f.price) AS total_revenue\n" +
            "FROM bill_service bf \n" +
            "JOIN service f ON bf.service_id = f.id\n" +
            "JOIN bill b ON bf.bill_id = b.id\n" +
            "WHERE " +
            "(:fromDate IS NULL OR b.date_create >= :fromDate) " +
            "AND (:toDate IS NULL OR b.date_create <= :toDate) " +
            "AND b.status =1 " +
            "GROUP BY bf.service_id, f.name\n" +
            "LIMIT 7", nativeQuery = true)
    List<Object> revenueService(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

    @Query(value = "SELECT m.name AS movie_name, SUM(bt.total_money) AS total_revenue\n" +
            "FROM bill_ticket bt\n" +
            "join bill b on b.id= bt.bill_id\n" +
            "JOIN ticket t on bt.ticket_id= t.id\n" +
            "JOIN schedule s ON t.schedule_id= s.id\n" +
            "JOIN movie m ON m.id = s.movie_id\n" +
            "WHERE b.date_create BETWEEN ?1 AND ?2 \n" +
            "GROUP BY m.name\n" +
            "ORDER BY total_revenue DESC\n" +
            "LIMIT 5", nativeQuery = true)
    List<Object> revenueMovie(Date fromDate, Date toDate);
}
