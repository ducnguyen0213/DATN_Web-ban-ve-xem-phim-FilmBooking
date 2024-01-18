package com.example.filmBooking.service;

import com.example.filmBooking.model.GeneralSetting;

import java.time.LocalTime;
import java.util.List;
import java.math.BigDecimal;


public interface GeneralSettingService {
    List<GeneralSetting> fillAll();

    GeneralSetting save(GeneralSetting GeneralSetting);

    GeneralSetting update(LocalTime timeBeginsToChange,
                          LocalTime businessHours,
                          LocalTime closeTime,
                          BigDecimal fixedTicketPrice,
                          Integer percentDay,
                          Integer percentWeekend,
                          Integer breakTime,
                          Integer waitingTime,
                          Integer confirmationWaitingTime,
                          Integer percentagePlusPoints,
                          Integer pointsCompensationPercentage);

    void delete(String id);

    GeneralSetting findById(String id);

}
