package com.example.filmBooking.repository;

import com.example.filmBooking.model.Bill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.filmBooking.model.dto.DtoBill;
import com.example.filmBooking.model.dto.DtoBillList;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.math.BigDecimal;


public interface BillRepository extends JpaRepository<Bill, String> {
    @Query(value = "SELECT * FROM datn.bill b where b.status = 0;", nativeQuery = true)
    Page<Bill> billStatusZero(Pageable pageable);

    @Query(value = "SELECT * FROM datn.bill b where b.status = 1;", nativeQuery = true)
    Page<Bill> billStatusOne(Pageable pageable);

    //    Page<Bill> findByDateCreateBetweenAndDateCreate
    Page<Bill> findByDateCreateBetween(Date startDate, Date endDate, Pageable pageable);

    @Query("SELECT COUNT(b) FROM Bill b WHERE b.status = 0 and DATE(b.dateCreate)= :dateCreate")
    String countSoldTicketsWithStatusZero(@Param("dateCreate") LocalDate dateCreate);

    @Query("SELECT COUNT(b) FROM Bill b WHERE b.status = 0 and b.customer.id = :customerId")
    String countSoldTicket(@Param("customerId") String customerId);

    @Query(value = "select sum(total_money) from bill b join" +
            "(SELECT DISTINCT bill_id, c.id, c.name FROM bill_ticket bt " +
            "JOIN ticket t ON t.id = bt.ticket_id" +
            " JOIN schedule s ON t.schedule_id = s.id" +
            " JOIN room r ON s.room_id = r.id" +
            " JOIN cinema c ON c.id = r.cinema_id) d on d.bill_id = b.id" +
            " WHERE " +
            " d.id=?1 and" +
            "    b.date_create >= DATE_SUB(CURDATE(), INTERVAL 7 DAY) " +
            "GROUP BY DATE(b.date_create), d.id = ?1" +
            " ORDER BY DATE(b.date_create) DESC "
            , nativeQuery = true)
    List<BigDecimal> revenueInTheLast7Days(String cinemaId);

    @Query(value = " SELECT m.name, COUNT(bt.id) AS bill_ticket_count " +
            " FROM bill_ticket bt" +
            " JOIN  bill b ON bt.bill_id = b.id" +
            " JOIN ticket t ON bt.ticket_id = t.id" +
            " JOIN schedule s ON t.schedule_id = s.id" +
            " JOIN movie m ON s.movie_id = m.id" +
            " WHERE MONTH(b.date_create) = MONTH(CURDATE()) " +
            "    AND YEAR(b.date_create) = YEAR(CURDATE())" +
            " GROUP BY m" +
            " ORDER BY bill_ticket_count DESC" +
            " LIMIT 5;", nativeQuery = true)
    List<Object[]> listTop5Movie();

    String bill = ("SELECT \n" +
            "    b.trading_code,\n" +
            "    m.name,\n" +
            "    m.image,\n" +
            "    c.name,\n" +
            "    s.start_at,\n" +
            "    GROUP_CONCAT(DISTINCT se.code) AS seat_codes,\n" +
            "    COUNT(DISTINCT se.code) AS seat_code_count,\n" +
            "    GROUP_CONCAT(DISTINCT CONCAT_WS('  ',\n" +
            "                f.name,\n" +
            "                CONCAT('(', bf.quantity, ')'),\n" +
            "                FORMAT(bf.total_money, 0, 'vi_VN'))) AS formatted_values,\n" +
            "    b.date_create,\n" +
            "    bt.total_money,\n" +
            "    r.name,\n" +
            "    b.status,b.total_money, b.point,b.use_points\n" +
            "FROM\n" +
            "    datn.bill b\n" +
            "        JOIN\n" +
            "    datn.customer cu ON cu.id = b.customer_id\n" +
            "        JOIN\n" +
            "    datn.bill_ticket bt ON b.id = bt.bill_id\n" +
            "        JOIN\n" +
            "    datn.ticket t ON bt.ticket_id = t.id\n" +
            "        JOIN\n" +
            "    datn.seat se ON se.id = t.seat_id\n" +
            "        JOIN\n" +
            "    datn.schedule s ON t.schedule_id = s.id\n" +
            "        JOIN\n" +
            "    datn.movie m ON s.movie_id = m.id\n" +
            "        JOIN\n" +
            "    datn.room r ON s.room_id = r.id\n" +
            "        JOIN\n" +
            "    datn.cinema c ON r.cinema_id = c.id\n" +
            "        LEFT JOIN\n" +
            "    datn.bill_service bf ON b.id = bf.bill_id\n" +
            "        LEFT JOIN\n" +
            "    datn.service f ON bf.service_id = f.id\n" +
            "WHERE\n" +
            "    cu.id = :customerId\n" +
            "        AND b.status = 1\n" +
            "GROUP BY b.trading_code , m.name , m.image , c.name , s.start_at , b.date_create , bt.total_money , r.name , b.status , b.id , b.total_money, b.point,b.use_points\n" +
            "ORDER BY DATE(b.date_create)  DESC");

    @Query(value = bill, nativeQuery = true)
    List<Object[]> findBillDetailsByCustomer(@Param("customerId") String customerId);

    String billCho = ("SELECT \n" +
            "    b.trading_code,\n" +
            "    m.name,\n" +
            "    m.image,\n" +
            "    c.name,\n" +
            "    s.start_at,\n" +
            "    GROUP_CONCAT(DISTINCT se.code) AS seat_codes,\n" +
            "    COUNT(DISTINCT se.code) AS seat_code_count,\n" +
            "    GROUP_CONCAT(DISTINCT CONCAT_WS('  ',\n" +
            "                f.name,\n" +
            "                CONCAT('(', bf.quantity, ')'),\n" +
            "                FORMAT(bf.total_money, 0, 'vi_VN'))) AS formatted_values,\n" +
            "    b.date_create,\n" +
            "    bt.total_money,\n" +
            "    r.name,\n" +
            "    b.status,b.total_money, b.point,b.use_points\n" +
            "FROM\n" +
            "    datn.bill b\n" +
            "        JOIN\n" +
            "    datn.customer cu ON cu.id = b.customer_id\n" +
            "        JOIN\n" +
            "    datn.bill_ticket bt ON b.id = bt.bill_id\n" +
            "        JOIN\n" +
            "    datn.ticket t ON bt.ticket_id = t.id\n" +
            "        JOIN\n" +
            "    datn.seat se ON se.id = t.seat_id\n" +
            "        JOIN\n" +
            "    datn.schedule s ON t.schedule_id = s.id\n" +
            "        JOIN\n" +
            "    datn.movie m ON s.movie_id = m.id\n" +
            "        JOIN\n" +
            "    datn.room r ON s.room_id = r.id\n" +
            "        JOIN\n" +
            "    datn.cinema c ON r.cinema_id = c.id\n" +
            "        LEFT JOIN\n" +
            "    datn.bill_service bf ON b.id = bf.bill_id\n" +
            "        LEFT JOIN\n" +
            "    datn.service f ON bf.service_id = f.id\n" +
            "WHERE\n" +
            "    cu.id = :customerId\n" +
            "        AND b.status = 0\n" +
            "GROUP BY b.trading_code , m.name , m.image , c.name , s.start_at , b.date_create , bt.total_money , r.name , b.status , b.id , b.total_money, b.point,b.use_points\n" +
            "ORDER BY DATE(b.date_create)  DESC");

    @Query(value = billCho, nativeQuery = true)
    List<Object[]> findBillDetailsByCustomerCho(@Param("customerId") String customerId);

    String billDetail = ("SELECT \n" +
            "    b.trading_code,\n" +
            "    m.name,\n" +
            "    m.image,\n" +
            "    c.name,\n" +
            "    s.start_at,\n" +
            "    GROUP_CONCAT(DISTINCT se.code) AS seat_codes,\n" +
            "    COUNT(DISTINCT se.code) AS seat_code_count,\n" +
            "    GROUP_CONCAT(DISTINCT CONCAT_WS('  ',\n" +
            "                f.name,\n" +
            "                CONCAT('(', bf.quantity, ')'),\n" +
            "                FORMAT(bf.total_money, 0, 'vi_VN'))) AS formatted_values,\n" +
            "    b.date_create,\n" +
            "    bt.total_money,\n" +
            "    r.name,\n" +
            "    b.status, b.total_money, cu.name, cu.phone_number, b.point, b.waiting_time,b.use_points,b.id\n" +
            "FROM\n" +
            "    datn.bill b\n" +
            "        JOIN\n" +
            "    datn.customer cu ON cu.id = b.customer_id\n" +
            "        JOIN\n" +
            "    datn.bill_ticket bt ON b.id = bt.bill_id\n" +
            "        JOIN\n" +
            "    datn.ticket t ON bt.ticket_id = t.id\n" +
            "        JOIN\n" +
            "    datn.seat se ON se.id = t.seat_id\n" +
            "        JOIN\n" +
            "    datn.schedule s ON t.schedule_id = s.id\n" +
            "        JOIN\n" +
            "    datn.movie m ON s.movie_id = m.id\n" +
            "        JOIN\n" +
            "    datn.room r ON s.room_id = r.id\n" +
            "        JOIN\n" +
            "    datn.cinema c ON r.cinema_id = c.id\n" +
            "        LEFT JOIN\n" +
            "    datn.bill_service bf ON b.id = bf.bill_id\n" +
            "        LEFT JOIN\n" +
            "    datn.service f ON bf.service_id = f.id\n" +
            "WHERE\n" +
            "    b.id = :idBill\n" +
            "GROUP BY b.trading_code , m.name , m.image , c.name , s.start_at , b.date_create , bt.total_money , r.name , " +
            "b.status , b.id , b.total_money, cu.name, cu.phone_number, b.point, b.waiting_time,b.use_points,b.id\n" +
            "ORDER BY DATE(b.date_create)  DESC");
    @Query(value = billDetail, nativeQuery = true)
    List<Object[]> findBillDetailId(@Param("idBill") String idBill);


    @Query("SELECT b FROM Bill b WHERE " +
            "(:tradingCode IS NULL OR b.tradingCode = :tradingCode) " +
            "AND (:dateCreate IS NULL OR DATE(b.dateCreate) = :dateCreate) " +
            "AND (:status IS NULL OR b.status = :status)")
    List<Bill> findBillsByTradingCodeAndDate(@Param("tradingCode") String tradingCode,
                                             @Param("dateCreate") LocalDate dateCreate,
                                             @Param("status") Integer status);


    @Query("SELECT b FROM Bill b WHERE " +
            "(:tradingCode IS NULL OR b.tradingCode = :tradingCode) " +
            "AND (:dateCreate IS NULL OR DATE(b.dateCreate) = :dateCreate) " +
            "AND b.status = 0")
    List<Bill> findBillsByTradingCodeAndDateCho(@Param("tradingCode") String tradingCode,
                                                @Param("dateCreate") LocalDate dateCreate);

    @Query(value = "SELECT * FROM datn.bill b where b.status = 0", nativeQuery = true)
    List<Bill> billStatusZero2();


    String billHuy = ("SELECT \n" +
            "    b.trading_code,\n" +
            "    m.name,\n" +
            "    m.image,\n" +
            "    c.name,\n" +
            "    s.start_at,\n" +
            "    GROUP_CONCAT(DISTINCT se.code) AS seat_codes,\n" +
            "    COUNT(DISTINCT se.code) AS seat_code_count,\n" +
            "    GROUP_CONCAT(DISTINCT CONCAT_WS('  ',\n" +
            "                f.name,\n" +
            "                CONCAT('(', bf.quantity, ')'),\n" +
            "                FORMAT(bf.total_money, 0, 'vi_VN'))) AS formatted_values,\n" +
            "    b.date_create,\n" +
            "    bt.total_money,\n" +
            "    r.name,\n" +
            "    b.status,b.total_money, b.point, b.waiting_time,b.use_points\n" +
            "FROM\n" +
            "    datn.bill b\n" +
            "        JOIN\n" +
            "    datn.customer cu ON cu.id = b.customer_id\n" +
            "        JOIN\n" +
            "    datn.bill_ticket bt ON b.id = bt.bill_id\n" +
            "        JOIN\n" +
            "    datn.ticket t ON bt.ticket_id = t.id\n" +
            "        JOIN\n" +
            "    datn.seat se ON se.id = t.seat_id\n" +
            "        JOIN\n" +
            "    datn.schedule s ON t.schedule_id = s.id\n" +
            "        JOIN\n" +
            "    datn.movie m ON s.movie_id = m.id\n" +
            "        JOIN\n" +
            "    datn.room r ON s.room_id = r.id\n" +
            "        JOIN\n" +
            "    datn.cinema c ON r.cinema_id = c.id\n" +
            "        LEFT JOIN\n" +
            "    datn.bill_service bf ON b.id = bf.bill_id\n" +
            "        LEFT JOIN\n" +
            "    datn.service f ON bf.service_id = f.id\n" +
            "WHERE\n" +
            "    cu.id = :customerId\n" +
            "        AND b.status = 2\n" +
            "GROUP BY b.trading_code , m.name , m.image , c.name , s.start_at , b.date_create ," +
            " bt.total_money , r.name , b.status , b.id , b.total_money, b.point, b.waiting_time,b.use_points\n" +
            "ORDER BY DATE(b.date_create)  DESC");

    @Query(value = billHuy, nativeQuery = true)
    List<Object[]> findBillDetailsByCustomerHuy(@Param("customerId") String customerId);


    String billCustomer = ("SELECT \n" +
            "    b.trading_code,\n" +
            "    m.name,\n" +
            "    m.image,\n" +
            "    c.name,\n" +
            "    s.start_at,\n" +
            "    GROUP_CONCAT(DISTINCT se.code) AS seat_codes,\n" +
            "    COUNT(DISTINCT se.code) AS seat_code_count,\n" +
            "    GROUP_CONCAT(DISTINCT CONCAT_WS('  ',\n" +
            "                f.name,\n" +
            "                CONCAT('(', bf.quantity, ')'),\n" +
            "                FORMAT(bf.total_money, 0, 'vi_VN'))) AS formatted_values,\n" +
            "    b.date_create,\n" +
            "    bt.total_money,\n" +
            "    r.name,\n" +
            "    b.status,b.total_money, b.point,b.use_points\n" +
            "    FROM\n" +
            "    datn.bill b\n" +
            "        JOIN\n" +
            "    datn.customer cu ON cu.id = b.customer_id\n" +
            "        JOIN\n" +
            "    datn.bill_ticket bt ON b.id = bt.bill_id\n" +
            "        JOIN\n" +
            "    datn.ticket t ON bt.ticket_id = t.id\n" +
            "        JOIN\n" +
            "    datn.seat se ON se.id = t.seat_id\n" +
            "        JOIN\n" +
            "    datn.schedule s ON t.schedule_id = s.id\n" +
            "        JOIN\n" +
            "    datn.movie m ON s.movie_id = m.id\n" +
            "        JOIN\n" +
            "    datn.room r ON s.room_id = r.id\n" +
            "        JOIN\n" +
            "    datn.cinema c ON r.cinema_id = c.id\n" +
            "        LEFT JOIN\n" +
            "    datn.bill_service bf ON b.id = bf.bill_id\n" +
            "        LEFT JOIN\n" +
            "    datn.service f ON bf.service_id = f.id\n" +
            "     WHERE\n" +
            "    cu.id = :customerId\n" +
            "     GROUP BY b.trading_code , m.name , m.image , c.name , s.start_at , b.date_create , bt.total_money , r.name , b.status , b.id , b.total_money, b.point,b.use_points\n" +
            "     ORDER BY DATE(b.date_create)  DESC");

    @Query(value = billCustomer, nativeQuery = true)
    List<Object[]> findBillDetailsByCustomerbillCustomer(@Param("customerId") String customerId);
}
