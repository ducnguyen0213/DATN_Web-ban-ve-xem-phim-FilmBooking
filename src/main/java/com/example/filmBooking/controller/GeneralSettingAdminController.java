package com.example.filmBooking.controller;

import com.example.filmBooking.common.ResponseBean;
import com.example.filmBooking.model.GeneralSetting;
import com.example.filmBooking.service.GeneralSettingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

@Controller
@CrossOrigin("*")
@RequestMapping("/general-setting")
@Tag(name = "general-setting")
@SessionAttributes("soldTicketsCount")

public class GeneralSettingAdminController {
    @Autowired
    private GeneralSettingService service;

    @GetMapping("/find-view")
    @Operation(summary = "[Hiển thị tất cả]")
    public String findAll(Model model) {
        List<GeneralSetting> generalSettingList = service.fillAll();
        model.addAttribute("general", generalSettingList.get(0));
        model.addAttribute("listSetting", generalSettingList);
//        model.addAttribute("general", new GeneralSetting());
//        a
        return "admin/general-setting";
    }

    @PostMapping("/save")
    @Operation(summary = "[Thêm mới]")
    public ResponseEntity<Object> save(@RequestBody @Valid GeneralSetting GeneralSetting) {
        ResponseBean responseBean = new ResponseBean();
        responseBean.setCode(HttpStatus.OK.toString());
        responseBean.setMessage("SUCCESS");
        responseBean.setData(service.save(GeneralSetting));
        return new ResponseEntity<>(responseBean, HttpStatus.OK);
    }

    @PostMapping("/update")
    @Operation(summary = "[Chỉnh sửa]")
    public String update(Model model,
                         @RequestParam("timeBeginsToChange") LocalTime timeBeginsToChange,
                         @RequestParam("businessHours") LocalTime businessHours,
                         @RequestParam("closeTime") LocalTime closeTime,
                         @RequestParam("fixedTicketPrice") BigDecimal fixedTicketPrice,
                         @RequestParam("percentDay") Integer percentDay,
                         @RequestParam("percentWeekend") Integer percentWeekend,
                         @RequestParam("breakTime") Integer breakTime,
                         @RequestParam("waitingTime") Integer waitingTime,
                         @RequestParam("confirmationWaitingTime") Integer confirmationWaitingTime,
                         @RequestParam("percentagePlusPoints") Integer percentagePlusPoints,
                         @RequestParam("pointsCompensationPercentage") Integer pointsCompensationPercentage) {
        model.addAttribute("setting");
//        responseBean.setMessage("SUCCESS");
        try {
            service.update(timeBeginsToChange, businessHours, closeTime, fixedTicketPrice, percentDay, percentWeekend, breakTime, waitingTime,confirmationWaitingTime,percentagePlusPoints,pointsCompensationPercentage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/general-setting/find-view";
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "[Xóa]")
    public ResponseEntity<?> delete(@PathVariable("id") String id) {
        ResponseBean responseBean = new ResponseBean();
        responseBean.setCode(HttpStatus.OK.toString());
        responseBean.setMessage("SUCCESS");
        service.delete(id);
        responseBean.setData(id);
        return new ResponseEntity<>(responseBean, HttpStatus.OK);
    }

    @GetMapping("/findById/{id}")
    @Operation(summary = "[Tìm kiếm theo id]")
    public ResponseEntity<?> findById(@PathVariable("id") String id) {
        ResponseBean responseBean = new ResponseBean();
        responseBean.setCode(HttpStatus.OK.toString());
        responseBean.setMessage("SUCCESS");
        responseBean.setData(service.findById(id));
        return new ResponseEntity<>(responseBean, HttpStatus.OK);
    }
}
