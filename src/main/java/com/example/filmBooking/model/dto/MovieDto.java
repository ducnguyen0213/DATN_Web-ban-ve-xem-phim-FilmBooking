package com.example.filmBooking.model.dto;

import com.example.filmBooking.model.Movie;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;

import java.sql.Date;


@Data
@Getter
@Setter
public class MovieDto {
    private String id;
    private String code;
    private String name;
    private Integer movieDuration;
    private String trailer;
    private Date premiereDate;
    private Date endDate;
    private String director;
    private String performers;
    private String languages;
    private String image;
    private String typeIds;
    private String description;
    private String movieType;
    private String status = status(premiereDate, endDate);

    private String status(Date premiereDate, Date endDate) {
        java.util.Date date = new java.util.Date();
        if (date.after(endDate)) {
            return "Đã chiếu";
        } else if (date.before(premiereDate)) {
            return "Sắp chiếu";
        } else {
            return "Đang chiếu";
        }
    }


}
