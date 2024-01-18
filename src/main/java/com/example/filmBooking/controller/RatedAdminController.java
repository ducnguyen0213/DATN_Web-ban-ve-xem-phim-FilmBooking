package com.example.filmBooking.controller;

import com.example.filmBooking.common.ResponseBean;
import com.example.filmBooking.model.Rated;
import com.example.filmBooking.service.RatedService;
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
@RequestMapping("/rated")
@SessionAttributes("soldTicketsCount")

public class RatedAdminController {
    @Autowired
    private RatedService service;

    @GetMapping("/find-all")
    @Operation(summary = "[Hiển thị tất cả]")
    public String findAll(Model model) {
        List<Rated> listRated = service.fillAll();
        model.addAttribute("listRated", listRated);
        model.addAttribute("rated", new Rated());
        return "admin/rated";
    }

    @PostMapping("/save")
    @Operation(summary = "[Thêm mới]")
    public String save(Model model, @RequestParam(name = "code") String code,
                       @RequestParam(name = "id") String id,
                       @RequestParam(name = "description") String description, RedirectAttributes ra) {
        try {
            Rated rated = Rated.builder()
                    .id(id)
                    .code(code)
                    .description(description)
                    .build();

            if (service.save(rated) instanceof Rated) {
                ra.addFlashAttribute("successMessage", "Thêm thành công");
            } else {
                ra.addFlashAttribute("thaerrorMessagetBai", "Thêm thất bại");
            }
            model.addAttribute("rated", new Rated());
            return "redirect:/rated/find-all";
        } catch (Exception e) {
            e.printStackTrace();
            return "admin/rated";
        }
    }

    //    @GetMapping("/update/{id}")
//    @Operation(summary = "[Chỉnh sửa]")
//    public String update(@PathVariable("id") String id, Model model) {
//        model.addAttribute("rated", service.findById(id));
//        List<Rated> listRated = service.fillAll();
//        model.addAttribute("listRated", listRated);
//        return "admin/rated";
//    }
    @PostMapping("/update/{id}")
    public String updatePromotion(@PathVariable(name = "id") String id, Rated updatedRoom, RedirectAttributes ra) {
        service.update(id, updatedRoom);
        ra.addFlashAttribute("successMessage", "Sửa thành công!!!");

        return "redirect:/rated/find-all";   // Redirect to the promotion list page after update
    }

    @GetMapping("/delete/{id}")
    @Operation(summary = "[Xóa]")
    public String delete(@PathVariable("id") String id, RedirectAttributes ra) {

        try {
            service.delete(id);
            ra.addFlashAttribute("successMessage", "Xóa thành công!!!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Xóa thất bại!!!");
        }
        return "redirect:/rated/find-all";
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

    @GetMapping("/search-rated")
    public String searchRated(Model model, @RequestParam("keycode") String keycode) {
        List<Rated> searchRated = service.searchCodeRated(keycode);
        model.addAttribute("listRated", searchRated);
        model.addAttribute("rated", new Rated());
        return "admin/rated";
    }
}
