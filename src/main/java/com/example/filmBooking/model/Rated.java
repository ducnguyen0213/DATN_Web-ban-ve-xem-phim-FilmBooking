package com.example.filmBooking.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@Getter
@Setter
//@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rated")
public class Rated {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ID")
    private String id;

    @Column(name = "code")
    private String code;

    @OneToMany(mappedBy = "rated",
            cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Movie> listMovie;

    @Column(name = "description", length = 560)
    private String description;
}
