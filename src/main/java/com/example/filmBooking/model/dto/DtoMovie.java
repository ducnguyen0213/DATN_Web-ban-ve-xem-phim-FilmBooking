package com.example.filmBooking.model.dto;

import com.example.filmBooking.model.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


@Data

public class DtoMovie {
    private String id;
    private String name;
    private Integer movieDuration;
    private String trailer;
    private Date premiereDate;
    private String image;
    private Rated rated;
    private List<MovieType> movieTypeList = new ArrayList<>();
    private List<Language> languageList;
    private List<Performer> performerList;
    private List<Director> directorList;

}
