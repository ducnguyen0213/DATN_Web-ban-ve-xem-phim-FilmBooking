package com.example.filmBooking.controller;

import com.example.filmBooking.common.ResponseBean;
import com.example.filmBooking.model.Cinema;
import com.example.filmBooking.model.Room;
import com.example.filmBooking.model.Ticket;
import com.example.filmBooking.service.CinemaService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/cinema")
@SessionAttributes("soldTicketsCount")

public class CinemaAdminController {
    @Autowired
    private CinemaService service;

    @GetMapping("/find-all")
    @Operation(summary = "[Hiển thị tất cả]")
    public String findAll(Model model) {
        List<Cinema> listCinema = service.fillAll();
        model.addAttribute("listCinema", listCinema);
        model.addAttribute("cinema", new Cinema());
        return "admin/cinema";
    }

    @PostMapping("/save")
    @Operation(summary = "[Thêm mới]")
    public String save(@RequestParam(name = "address") String address,
                       @RequestParam(name = "description") String description,
                       @RequestParam(name = "name") String name,
                       @RequestParam(name = "id") String id,RedirectAttributes ra,
                       Model model) {
        try {
            Cinema cinema = Cinema.builder()
                    .id(id)
                    .name(name)
                    .address(address)
                    .description(description)
                    .build();
            if (service.save(cinema) instanceof Cinema) {
                ra.addFlashAttribute("successMessage", "Thêm thành công!!!");
            } else {
                ra.addFlashAttribute("errorMessage", "Thêm thất bại");
            }
            model.addAttribute("cinema", new Cinema());
            return "redirect:/cinema/find-all";
        } catch (Exception e) {
            e.printStackTrace();
            return "admin/cinema";
        }

    }

    //    @GetMapping("/update/{id}")
//    @Operation(summary = "[Chỉnh sửa]")
//    public String update(@PathVariable("id") String id, Model model) {
//        List<Cinema> listCinema = service.fillAll();
//        model.addAttribute("listCinema", listCinema);
//        model.addAttribute("cinema", service.findById(id));
//        return "admin/cinema";
//    }


    @GetMapping("/delete/{id}")
    @Operation(summary = "[Xóa]")
    public String delete(@PathVariable("id") String id, RedirectAttributes ra) {
        try {
            service.delete(id);
            ra.addFlashAttribute("successMessage", "Xóa thành công!!!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Xóa thất bại!!!");
        }
        return "redirect:/cinema/find-all";
    }

    @GetMapping("/search-cinema")
    public String searchCinema(Model model, @RequestParam("keyword") String keyword) {
        List<Cinema> searcCinema = service.searchCinema(keyword);
        model.addAttribute("cinema", new Cinema());
        model.addAttribute("listCinema", searcCinema);
        return "admin/cinema";
    }
    @PostMapping("/update/{id}")
    public String updateCinema(@PathVariable(name = "id") String id, Cinema cinema, RedirectAttributes ra) {
        service.update(id, cinema);
        ra.addFlashAttribute("successMessage", "Sửa thành công!!!");
        return "redirect:/cinema/find-all";   // Redirect to the promotion list page after update
    }

}
