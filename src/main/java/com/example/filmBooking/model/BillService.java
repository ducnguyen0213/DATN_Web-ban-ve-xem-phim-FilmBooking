package com.example.filmBooking.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;


@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bill_Service")
public class BillService {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ID")
    private String id;

//    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "bill_id")
    private Bill bill;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service Service;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "total_money")
    private BigDecimal totalMoney;

    @Column(name = "status")
    private Integer status;
}
