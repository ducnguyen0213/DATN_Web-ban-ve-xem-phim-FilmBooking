package com.example.filmBooking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bill")
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ID")
    private String id;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "bill",
            cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BillTicket> listBillTicket;

    @OneToMany(mappedBy = "bill",
            cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BillService> listBillService;

    @ManyToOne
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    @Column(name = "date_create")
    private LocalDateTime dateCreate;

    @Column(name = "status")
    private Integer status;

    @Column(name = "total_money")
    private BigDecimal totalMoney;

    @Column(name = "trading_code")
    private String tradingCode;

    @Column(name = "waiting_time")
    private LocalDateTime waitingTime;

    @Column(name = "point")
    private Integer point;

    @Column(name = "use_points")
    private Integer usepoints ;
}
