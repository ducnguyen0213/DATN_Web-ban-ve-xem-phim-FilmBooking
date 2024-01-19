package com.example.filmBooking.repository;

import com.example.filmBooking.model.BillTicket;
import com.example.filmBooking.model.Schedule;
import com.example.filmBooking.model.Seat;
import com.example.filmBooking.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TicketRepository extends JpaRepository<Ticket, String> {
    String str_findBySchedule =
            "select * from ticket where  (schedule_id = (:scheduleId) )";

    @Query(nativeQuery = true, value = str_findBySchedule)
    List<Ticket> findBySchedule(@Param("scheduleId") String scheduleId);

    List<Ticket> findTicketByScheduleId(String scheduleId);

    List<Ticket> findTicketByScheduleIdAndSeatId(String scheduleId, String ticketId);


    String ticket = ("SELECT DISTINCT  t.* \n" +
            "            FROM datn.cinema c\n" +
            "            JOIN datn.room r ON c.id = r.cinema_id\n" +
            "            JOIN datn.schedule s ON r.id = s.room_id\n" +
            "            JOIN datn.movie m ON s.movie_id = m.id\n" +
            "            join datn.ticket t on t.schedule_id = s.id\n" +
            "            join datn.seat se on se.id= t.seat_id\n" +
            "            WHERE c.id = :cinemaId AND m.id = :movieId\n" +
            "            AND DATE(s.start_at ) = :startAt \n" +
            "            AND DATE_FORMAT(s.start_at, '%H:%i') = :startTime" +
            "            AND r.name = :nameRoom and t.status like 'đã bán'");

    @Query(value = ticket, nativeQuery = true)
    List<Ticket> findTicketsBySchedule_Id(@Param("cinemaId") String cinemaId,
                                          @Param("movieId") String movieId,
                                          @Param("startAt") String startAt,
                                          @Param("startTime") String startTime,
                                          @Param("nameRoom") String nameRoom);

    String ticket1 = ("SELECT DISTINCT  t.* \n" +
            "            FROM datn.cinema c\n" +
            "            JOIN datn.room r ON c.id = r.cinema_id\n" +
            "            JOIN datn.schedule s ON r.id = s.room_id\n" +
            "            JOIN datn.movie m ON s.movie_id = m.id\n" +
            "            join datn.ticket t on t.schedule_id = s.id\n" +
            "            join datn.seat se on se.id= t.seat_id\n" +
            "             WHERE m.name = :movieName\n" +
            "            AND s.start_at  = :startAt   AND r.name = :nameRoom\n" +
            "            AND t.status like 'đã bán'");

    @Query(value = ticket1, nativeQuery = true)
    List<Ticket> findTicketsBySchedule_Id1(@Param("movieName") String movieName,
                                           @Param("startAt") String startAt,
                                           @Param("nameRoom") String nameRoom);


    String ticketShow = ("SELECT DISTINCT  t.* \n" +
            "            FROM datn.cinema c\n" +
            "            JOIN datn.room r ON c.id = r.cinema_id\n" +
            "            JOIN datn.schedule s ON r.id = s.room_id\n" +
            "            JOIN datn.movie m ON s.movie_id = m.id\n" +
            "            join datn.ticket t on t.schedule_id = s.id\n" +
            "            join datn.seat se on se.id= t.seat_id\n" +
            "            WHERE c.id = :cinemaId AND m.id = :movieId\n" +
            "            AND DATE(s.start_at ) = :startAt \n" +
            "            AND DATE_FORMAT(s.start_at, '%H:%i') = :startTime  AND r.name = :nameRoom");

    @Query(value = ticketShow, nativeQuery = true)
    List<Ticket> ticketShow(@Param("cinemaId") String cinemaId,
                            @Param("movieId") String movieId,
                            @Param("startAt") String startAt,
                            @Param("startTime") String startTime,
                            @Param("nameRoom") String nameRoom);

    String ticketShow1 = ("SELECT DISTINCT  t.* \n" +
            "            FROM datn.cinema c\n" +
            "            JOIN datn.room r ON c.id = r.cinema_id\n" +
            "            JOIN datn.schedule s ON r.id = s.room_id\n" +
            "            JOIN datn.movie m ON s.movie_id = m.id\n" +
            "            join datn.ticket t on t.schedule_id = s.id\n" +
            "            join datn.seat se on se.id= t.seat_id\n" +
            "             WHERE m.name = :movieName\n" +
            "            AND s.start_at  = :startAt  AND r.name = :nameRoom");

    @Query(value = ticketShow1, nativeQuery = true)
    List<Ticket> ticketShow1(@Param("movieName") String movieName,
                             @Param("startAt") String startAt,
                             @Param("nameRoom") String nameRoom);

    Page<Ticket> findByScheduleId(String id, Pageable pageable);


    Page<Ticket> findAllByStatus(String status, Pageable pageable);

    //
//    @Query("FROM Ticket t where t.schedule.room.id like %:roomName% and t.schedule.movie.id like %:movieName% and Date(t.schedule.startAt)= : dateSearch and t.status is null or t.status like %:status% ")
//    Page<Ticket> searchTicket(String roomName, String movieName, Date dateSearch, String status, Pageable pageable);
    @Query("FROM Ticket t WHERE t.schedule.id LIKE %:scheduleId% AND (t.status IS NULL OR t.status LIKE %:status%) order by t.seat.line ASC, t.seat.number ASC ")
    Page<Ticket> searchTicket(@Param("scheduleId") String scheduleId, @Param("status") String status, Pageable pageable);

}
