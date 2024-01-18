package com.example.filmBooking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;


@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bill_ticket")
public class BillTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ID")
    private String id;

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    //    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "bill_id")
    private Bill bill;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "total_money")
    private BigDecimal totalMoney;

    @Column(name = "status")
    private Integer status;
}
