package com.example.filmBooking.repository;

import com.example.filmBooking.model.Cinema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CinemaRepository extends JpaRepository<Cinema, String> {

    String findNumberOfRoom = "select COUNT(*) from room WHERE cinema_id =:id";

    @Query(value = findNumberOfRoom, nativeQuery = true)
    Integer findNumberOfRoom(String id);

    String cinema = ("SELECT DISTINCT c.name, c.id, c.address, c.description, c.code, c.capacity\n" +
            "FROM datn.cinema c\n" +
            "JOIN datn.room r ON c.id = r.cinema_id\n" +
            "JOIN datn.schedule s ON r.id = s.room_id\n" +
            "JOIN datn.movie m ON s.movie_id = m.id\n" +
            "where c.id=:cinemaId and m.id=:movieId");
    @Query(value = cinema, nativeQuery = true)
    List<Cinema> getCinema(@Param("movieId") String movieId
            , @Param("cinemaId") String cinemaId);

    
    @Query("SELECT c FROM Cinema c  where c.id in " +
            "(SELECT s.room.cinema.id FROM Schedule s JOIN s.movie m JOIN s.room r WHERE s.movie.id = :movieId  )")
    List<Cinema> getCinemaThatShowTheMovie(@Param("movieId") String movieId);

//    @Query(value = "SELECT c.name, m.id FROM cinema c JOIN room r on c.id = r.cinema_id JOIN schedule s on r.id = s.room_id JOIN movie m on m.id = s.movie_id", nativeQuery = true)
//    List<CinemaDto> fillCinema();
    
    List<Cinema> findByNameContains(String keyword);

}
