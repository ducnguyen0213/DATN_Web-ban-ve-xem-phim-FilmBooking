package com.example.filmBooking.repository;

import com.example.filmBooking.model.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;


public interface ScheduleRepository extends JpaRepository<Schedule, String> {
    String Start_at = ("SELECT DISTINCT  DATE(s.start_at)\n"
            + "FROM datn.cinema c\n"
            + "JOIN datn.room r ON c.id = r.cinema_id\n"
            + "JOIN datn.schedule s ON r.id = s.room_id\n"
            + "JOIN datn.movie m ON s.movie_id = m.id\n"
            + "where c.id=:cinemaId and m.id=:movieId  and  s.status  IN ('Sắp chiếu', 'Đang chiếu')  ORDER BY  DATE(s.start_at) ASC");

    @Query(value = Start_at, nativeQuery = true)
    List<String> getstartAtAndFinishAt(@Param("movieId") String movieId, @Param("cinemaId") String cinemaId);

    //   lấy ra thời gian
    String time = ("\n" + "\tselect distinct   DATE_FORMAT(s.start_at , '%H:%i'), r.name, s.price\n"
            + "\tfrom datn.cinema c\n"
            + "\tjoin datn.room r on r.cinema_id = c.id\n"
            + "\tjoin datn.schedule s on s.room_id = r.id\n"
            + "\tjoin datn.movie m on m.id = s.movie_id\n"
            + "where m.id=:movieId\n"
            + " and c.id =:cinemaId and  s.status like 'Sắp chiếu' and  s.operating_status like '1' and  DATE(s.start_at)=:start_at   ORDER BY  DATE_FORMAT(s.start_at , '%H:%i') ASC");

    @Query(value = time, nativeQuery = true)
    List<Object[]> getTime(@Param("movieId") String movieId, @Param("cinemaId") String cinemaId, @Param("start_at") String start_at);

    //   lấy ra schedule
    String schedule = ("SELECT DISTINCT   s.*\n"
            + "   FROM datn.cinema c\n"
            + "" +
            " JOIN datn.room r ON c.id = r.cinema_id\n"
            + "  JOIN datn.schedule s ON r.id = s.room_id\n"
            + " JOIN datn.movie m ON s.movie_id = m.id\n"
            + " join datn.ticket t on t.schedule_id = s.id\n"
            + "   join datn.seat se on se.id= t.seat_id\n"
            + "    WHERE c.id = :cinemaId AND m.id = :movieId\n"
            + "     AND DATE(s.start_at ) = :startAt \n"
            + "            AND DATE_FORMAT(s.start_at, '%H:%i') = :startTime AND r.name = :nameRoom");

    @Query(value = schedule, nativeQuery = true)
    List<Schedule> getSchedule(@Param("cinemaId") String cinemaId,
                               @Param("movieId") String movieId,
                               @Param("startAt") String startAt,
                               @Param("startTime") String startTime,
                               @Param("nameRoom") String nameRoom);


    String schedule1 = ("SELECT DISTINCT   s.*\n"
            + "   FROM datn.cinema c\n"
            + "" +
            " JOIN datn.room r ON c.id = r.cinema_id\n"
            + "  JOIN datn.schedule s ON r.id = s.room_id\n"
            + " JOIN datn.movie m ON s.movie_id = m.id\n"
            + " join datn.ticket t on t.schedule_id = s.id\n"
            + "   join datn.seat se on se.id= t.seat_id\n"
            + "    WHERE m.name = :movieName\n"
            + "     AND s.start_at  = :startAt AND r.name = :nameRoom");

    @Query(value = schedule1, nativeQuery = true)
    List<Schedule> getSchedule1(@Param("movieName") String movieName,
                                @Param("startAt") String startAt,
                                @Param("nameRoom") String nameRoom);


    List<Schedule> findAllByOrderByStartAtAsc();

    @Query(value = "select *from schedule where room_id= ?1", nativeQuery = true)
    List<Schedule> findByRoom(String id);

    @Query(value = " select * from schedule s where s.operating_status like '0' and s.room_id= ?2 and date(s.start_at) = Date(?1)  order by s.start_at asc ", nativeQuery = true)
    List<Schedule> findByRoomAndFinishAt(String date, String room);


    @Query("FROM Schedule s WHERE " +
            " s.status like 'Sắp chiếu'" +
            " AND   s.operatingStatus like '1'" +
            "AND ((?1 is NULL) OR (s.room.cinema.name like ?1)) " +
            "AND ((?2 is NULL) OR (date(s.startAt) = Date(?2))) " +
            "AND ((?4 is NULL) OR (HOUR(s.startAt) >= ?4)) " +
            "AND ((?5 is NULL) OR (HOUR(s.startAt) < ?5)) " +
            "AND ((?3 is NULL) OR (s.movie.name like ?3)) " +
            "ORDER BY s.startAt ASC"
    )
    List<Schedule> findByConditions(String name, LocalDate startAt, String movieName, Integer startTime, Integer endTime);

    @Query("FROM Schedule s WHERE " +
            " ((?1 is NULL) OR (s.room.cinema.name like ?1)) " +
            "AND ((?2 is NULL) OR (date(s.startAt) = Date(?2))) " +
            "AND ((?4 is NULL) OR (HOUR(s.startAt) >= ?4)) " +
            "AND ((?5 is NULL) OR (HOUR(s.startAt) < ?5)) " +
            "AND ((?3 is NULL) OR (s.movie.name like ?3)) " +
            "AND ((?6 is NULL) OR (s.status like ?6)) " +
            "ORDER BY s.startAt ASC"
    )
    Page<Schedule> searchBySchedule(String name, LocalDate startAt, String movieName, Integer startTime, Integer endTime,String status, Pageable pageable);

    List<Schedule> findAllByStatus(String status);

    @Query(value = "SELECT DISTINCT b.id " +
            "FROM bill b " +
            "INNER JOIN bill_ticket bt ON b.id = bt.bill_id " +
            "INNER JOIN ticket t ON bt.ticket_id = t.id " +
            "INNER JOIN schedule s ON t.schedule_id = s.id " +
            "WHERE s.id = ?1 and b.status ='1' ", nativeQuery = true)
    List<String> findBillByStatusSchedule(String scheduleId);

    @Query(" select distinct s.name from Schedule s ")
    List<String> listSchedule();

    @Query("from Schedule s where s.name like %:name%")
    List<Schedule> findAllByName(String name);
    
    @Query(value = "select * from datn.schedule s where DATE(s.start_at) = Date(?1)  and s.operating_status = 1", nativeQuery = true)
    List<Schedule> findByDateStartAt(String date);

}

