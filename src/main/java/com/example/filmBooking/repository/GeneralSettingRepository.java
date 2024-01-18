package com.example.filmBooking.repository;

import com.example.filmBooking.model.GeneralSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface GeneralSettingRepository extends JpaRepository<GeneralSetting, String> {
    @Query("SELECT gs.percentagePlusPoints FROM GeneralSetting gs")
    Integer findPercentagePlusPoints();
}
