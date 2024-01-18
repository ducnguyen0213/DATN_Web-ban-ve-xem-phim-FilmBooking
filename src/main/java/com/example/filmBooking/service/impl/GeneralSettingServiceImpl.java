package com.example.filmBooking.service.impl;
import com.example.filmBooking.model.GeneralSetting;
import com.example.filmBooking.repository.GeneralSettingRepository;
import com.example.filmBooking.service.GeneralSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;


@Service
public class GeneralSettingServiceImpl implements GeneralSettingService {

    @Autowired
    private GeneralSettingRepository repository;

    @Override
    public List<GeneralSetting> fillAll() {
        return repository.findAll();
    }

    @Override
    public GeneralSetting save(GeneralSetting GeneralSetting) {
        return repository.save(GeneralSetting);
    }

    @Override
    public GeneralSetting update(LocalTime timeBeginsToChange,
                                 LocalTime businessHours,
                                 LocalTime closeTime,
                                 BigDecimal fixedTicketPrice,
                                 Integer percentDay,
                                 Integer percentWeekend,
                                 Integer breakTime,
                                 Integer waitingTime,Integer confirmationWaitingTime,
                                 Integer percentagePlusPoints,Integer pointsCompensationPercentage
    ) {
        GeneralSetting generalSettingNew = findById("hihi");
        generalSettingNew.setTimeBeginsToChange(timeBeginsToChange);
        generalSettingNew.setBusinessHours(businessHours);
        generalSettingNew.setCloseTime(closeTime);
        generalSettingNew.setFixedTicketPrice(fixedTicketPrice);
        generalSettingNew.setBreakTime(breakTime);
        generalSettingNew.setPercentDay(percentDay);
        generalSettingNew.setPercentWeekend(percentWeekend);
        generalSettingNew.setWaitingTime(waitingTime);
        generalSettingNew.setConfirmationWaitingTime(confirmationWaitingTime);
        generalSettingNew.setPercentagePlusPoints(percentagePlusPoints);
        generalSettingNew.setPointsCompensationPercentage(pointsCompensationPercentage);
        return repository.save(generalSettingNew);
    }

    @Override
    public GeneralSetting findById(String id) {
        return repository.findById(id).get();
    }

    @Override
    public void delete(String id) {
        repository.delete(findById(id));
    }
}
