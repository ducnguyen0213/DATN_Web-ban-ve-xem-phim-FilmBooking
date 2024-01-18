package com.example.filmBooking.controller;

import com.example.filmBooking.model.Cinema;
import com.example.filmBooking.model.Room;
import com.example.filmBooking.model.SeatType;
import com.example.filmBooking.service.impl.SeatTypeServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/seatType")
@SessionAttributes("soldTicketsCount")
public class SeatTypeAdminController {

    @Autowired
    private SeatTypeServiceImpl seatTypeService;

    @GetMapping("/find-all")
    public String viewRoom(Model model) {
        List<SeatType> seatTypeList = seatTypeService.findAll();
        model.addAttribute("seatTypeList", seatTypeList);
        model.addAttribute("seatType", new SeatType());
        return "admin/seatType";
    }

    @PostMapping("/save")
    @Operation(summary = "[Thêm dữ liệu room]")
    public String addSeatType(Model model,
                              @RequestParam(name = "name") String name,
                              @RequestParam(name = "surcharge") int surcharge,
                              @RequestParam(name = "id") String id,
                              RedirectAttributes ra) {
        try {
            SeatType seatType = SeatType.builder()
                    .id(id)
                    .name(name)
                    .surcharge(surcharge)
                    .build();
            if (seatTypeService.save(seatType) instanceof SeatType) {
                ra.addFlashAttribute("successMessage", "Thêm thành công!!!");
            } else {
                ra.addFlashAttribute("errorMessage", "Thêm thất bại");
            }
//            model.addAttribute("cinema", new Cinema());
            return "redirect:/seatType/find-all";
        } catch (Exception e) {
            e.printStackTrace();
            return "admin/seatType";
        }

    }

    @GetMapping("/delete/{id}")
    @Operation(summary = "[Xóa dữ liệu room]")
    public String delete(@PathVariable(name = "id") String id, RedirectAttributes ra) {
        try {
            seatTypeService.delete(id);
            ra.addFlashAttribute("successMessage", "Xóa thành công!!!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Xóa thất bại!!!");
        }
        return "redirect:/seatType/find-all";
    }

    @PostMapping("/update/{id}")
    public String updatePromotion(@PathVariable(name = "id") String id, SeatType updatedRoom, RedirectAttributes ra) {
        seatTypeService.update(id, updatedRoom);
        ra.addFlashAttribute("successMessage", "Sửa thành công!!!");

        return "redirect:/seatType/find-all";   // Redirect to the promotion list page after update
    }
}
