package com.example.filmBooking.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DtoBillList implements DtoBill{
    private String billCode;
    private String customerName;
    private String movieImage;
    private String movieName;
    private String serviceName;
    private Date dateCreate;
    private String ticketCode;
    private String ticketStatus;
    private String seatCode;
    private BigDecimal totalMoney;

    private List<DtoBill> dtoBillList;

    public List<DtoBill> getDtoBillList() {
        return dtoBillList;
    }

    public void setDtoBillList(List<DtoBill> dtoBillList) {
        this.dtoBillList = dtoBillList;
    }

}
