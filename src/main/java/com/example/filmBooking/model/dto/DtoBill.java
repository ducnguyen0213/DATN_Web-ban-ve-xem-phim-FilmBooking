package com.example.filmBooking.model.dto;

import java.math.BigDecimal;
import java.util.Date;

public interface DtoBill {
     String getBillCode();
     String getCustomerName();
     String getMovieImage();
     String getMovieName();
     String getServiceName();
     Date getDateCreate();
     String getTicketCode();
     String getTicketStatus();
     String getSeatCode();
     BigDecimal getTotalMoney();
}
