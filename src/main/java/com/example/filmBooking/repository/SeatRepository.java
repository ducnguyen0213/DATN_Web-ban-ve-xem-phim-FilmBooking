package com.example.filmBooking.repository;

import com.example.filmBooking.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;


public interface SeatRepository extends JpaRepository<Seat, String> {

    String findAllByRoomValue =
            "select * from seat where  (room_id = (:roomId) ) ORDER BY line ASC, number ASC";

    @Query(nativeQuery = true, value = findAllByRoomValue)
    List<Seat> findAllByRoom(@Param("roomId") String roomId);

    String findAllByRoom =
            "select * from seat where  (room_id = (:roomId) )";

    @Query(nativeQuery = true, value = findAllByRoom)
    Page<Seat> searchByRoom(@Param("roomId") String roomId, Pageable pageable);

    @Query(value = "SELECT s, b.dateCreate FROM Seat s \n" +
            "               JOIN datn.ticket t ON t.seat.id = s.id\n" +
            "               JOIN datn.bill_ticket bt ON bt.ticket.id = t.id \n" +
            "               JOIN datn.bill b ON bt.bill.id = b.id \n" +
            "               JOIN datn.customer c ON b.customer.id = c.id\n" +
            "               WHERE c.id = :customerId", nativeQuery = true)
    List<Object[]> findSeatsByCustomerId(@Param("customerId") String customerId);

    List<Seat> getSeatByRoomId(String roomId);


    String seat = ("SELECT DISTINCT se.*\n" +
            "            FROM datn.cinema c\n" +
            "            JOIN datn.room r ON c.id = r.cinema_id\n" +
            "            JOIN datn.schedule s ON r.id = s.room_id\n" +
            "            JOIN datn.movie m ON s.movie_id = m.id\n" +
            "            join datn.ticket t on t.schedule_id = s.id\n" +
            "            join datn.seat se on se.id= t.seat_id\n" +
            "            WHERE c.id = :cinemaId AND m.id = :movieId\n" +
            "            AND DATE(s.start_at ) = :startAt \n" +
            "            AND DATE_FORMAT(s.start_at, '%H:%i') = :startTime " +
            "            AND r.name = :nameRoom ORDER BY se.code ASC");

    @Query(value = seat, nativeQuery = true)
    List<Seat> getSeat(@Param("cinemaId") String cinemaId,
                       @Param("movieId") String movieId,
                       @Param("startAt") String startAt,
                       @Param("startTime") String startTime,
                       @Param("nameRoom") String nameRoom);

    @Query("SELECT COUNT(s) FROM Seat s WHERE s.room = :room AND s.line LIKE %:line%")
    int countByRoomAndLine(@Param("room") Room room, @Param("line") String line);

    @Query("SELECT COUNT(s) FROM Seat s WHERE s.room = :room AND s.code LIKE %:code%")
    int findByCodeLike(@Param("room") Room room, @Param("code") String code);

//    Seat findByCodeLike(String code);

    String seat1 = ("SELECT DISTINCT se.*\n" +
            "            FROM datn.cinema c\n" +
            "            JOIN datn.room r ON c.id = r.cinema_id\n" +
            "            JOIN datn.schedule s ON r.id = s.room_id\n" +
            "            JOIN datn.movie m ON s.movie_id = m.id\n" +
            "            join datn.ticket t on t.schedule_id = s.id\n" +
            "            join datn.seat se on se.id= t.seat_id\n" +
            "            WHERE  m.name = :movieName\n" +
            "            AND s.start_at  = :startAt AND r.name = :nameRoom\n" +
            "            ORDER BY se.code ASC");

    @Query(value = seat1, nativeQuery = true)
    List<Seat> getSeat1(@Param("movieName") String movieName,
                        @Param("startAt") String startAt,
                        @Param("nameRoom") String nameRoom);


    String SeatByRoom = ("select s.* from datn.seat s\n" +
            "join datn.room r on s.room_id = r.id\n" +
            "where r.name=:roomName\n" +
            "ORDER BY s.code ASC");

    @Query(value = SeatByRoom, nativeQuery = true)
    List<Seat> getSeatByRoom(@Param("roomName") String roomName);

//    @Query(value = "select s.* from datn.seat s\n" +
//            "Join datn.room r where r.name =:roomname;", nativeQuery = true)
//    List<Seat> SeatRoom(@Param("roomname") String roomname);


    String seatSeatAll = ("select s.* from datn.seat s\n" +
            "join datn.room r on s.room_id = r.id\n" +
            "where r.id=:idRoom\n" +
            "ORDER BY s.code ASC");

    @Query(value = seatSeatAll, nativeQuery = true)
    List<Seat> seatSeatAll(@Param("idRoom") String idRoom);
//}
}
