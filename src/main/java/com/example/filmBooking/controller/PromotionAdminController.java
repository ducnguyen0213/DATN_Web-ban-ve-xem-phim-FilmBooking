package com.example.filmBooking.controller;

import com.example.filmBooking.model.Promotion;
import com.example.filmBooking.model.RankCustomer;
import com.example.filmBooking.service.PromotionService;
import com.example.filmBooking.service.RankCustomerService;
import com.example.filmBooking.util.UploadImage;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/promotion")
@SessionAttributes("soldTicketsCount")

public class PromotionAdminController {
    @Autowired
    private PromotionService promotionService;
    @Autowired
    private RankCustomerService rankCustomerService;

    @PostMapping("/save")
    @Operation(summary = "[Thêm dữ liệu promotion]")
    public String addPromotion(Model model, @RequestParam(name = "id") String id,
                               @RequestParam(name = "name") String name,
                               @RequestParam(name = "code") String code,
                               @RequestParam(name = "percent") Integer percent,
                               @RequestParam(name = "rankCustomer") RankCustomer rankCustomer,
                               @RequestParam(name = "startDate") LocalDateTime startDate,
                               @RequestParam(name = "endDate") LocalDateTime endDate,
                               @RequestParam(name = "quantity") Integer quantity,
                               @RequestParam(name = "description") String description, RedirectAttributes ra
    ) {
        try {
            Promotion promotion = Promotion.builder()
                    .id(id)
                    .code(code)
                    .name(name)
                    .description(description)
                    .percent(percent)
                    .rankCustomer(rankCustomer)
                    .startDate(startDate)
                    .endDate(endDate)
                    .quantity(quantity)
                    .build();
            if (promotionService.save(promotion) instanceof Promotion) {
                ra.addFlashAttribute("successMessage", "Thêm thành công!!!");

            } else {
                ra.addFlashAttribute("errorMessage", "Thêm thất bại!!!");
            }
            return "redirect:/promotion/find-all";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("promotion", new Promotion());
            return "admin/promotion";
        }

    }

//    @GetMapping("/find-all")
//    public String viewPromotion(Model model) {
//        return findAll(model, 1);
//    }
//
//    @GetMapping("/find-all/page/{pageNumber}")
//    public String findAll(Model model, @PathVariable("pageNumber") Integer currentPage) {
//        Page<Promotion> page = promotionService.getAll(currentPage);
//        model.addAttribute("currentPage", currentPage);
//        model.addAttribute("totalPages", page.getTotalPages());
//        model.addAttribute("totalItems", page.getTotalElements());
//        model.addAttribute("listPromotion", page.getContent());
//        List<RankCustomer> rankCustomer = rankCustomerService.fillAll();
//        model.addAttribute("rankCustomer", rankCustomer);
//        model.addAttribute("promotion", new Promotion()); // bắt buộc. không có là lỗi
//        return "admin/promotion";
//    }

    @GetMapping("/find-all")
    public String viewPromotion(Model model) {
        List<Promotion> listPromotion = promotionService.fillAll();
        List<RankCustomer> rankCustomer = rankCustomerService.fillAll();
        model.addAttribute("rankCustomer", rankCustomer);
        model.addAttribute("promotion", new Promotion());
        model.addAttribute("listPromotion", listPromotion);
        return "admin/promotion";
    }

    @GetMapping("/delete/{id}")
    @Operation(summary = "[Xóa dữ liệu promotion]")
    public String delete(@PathVariable(name = "id") String id, RedirectAttributes ra) {

        try {
            promotionService.delete(id);
            ra.addFlashAttribute("successMessage", "Xóa thành công!!!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Xóa thất bại!!!");
        }
        return "redirect:/promotion/find-all";
    }

//    @GetMapping("/update/{id}")
//    public String updatePromotionForm(Model model, @PathVariable(name = "id") String id) {
//        Promotion promotion = promotionService.findById(id);
//        // Add any necessary data to the model for populating the update form
//        model.addAttribute("promotion", promotion);  // Assuming there's a method to retrieve all promotions
//
//        return "redirect:/promotion/find-all";    }

    @PostMapping("/update/{id}")
    public String updatePromotion(@PathVariable(name = "id") String id, Promotion updatedPromotion, RedirectAttributes ra) {
        promotionService.update(id, updatedPromotion);
        ra.addFlashAttribute("successMessage", "Sửa thành công!!!");

        return "redirect:/promotion/find-all";   // Redirect to the promotion list page after update
    }

    @GetMapping("/findById/{pageNumber}/{id}")
    @Operation(summary = "[Tìm kiếm theo id]")
    public String findById(Model model, @PathVariable("id") String id, @PathVariable("pageNumber") Integer currentPage) {
        Page<Promotion> listPromotion = promotionService.getAll(currentPage);
        List<RankCustomer> rankCustomer = rankCustomerService.fillAll();
        model.addAttribute("rankCustomer", rankCustomer);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", listPromotion.getTotalPages());
        model.addAttribute("totalItems", listPromotion.getTotalElements());
        model.addAttribute("listPromotion", listPromotion.getContent());
        model.addAttribute("promotion", promotionService.findById(id));
        return "admin/promotion";

    }

//    @GetMapping("/search-by-name-promotion/{pageNumber}")
//    public String searchPromotion(@RequestParam("keyword") String keyword, Model model, @PathVariable("pageNumber") Integer currentPage) {
//        Page<Promotion> page = promotionService.searchByNamePromotion(keyword, currentPage);
//        model.addAttribute("keyword", keyword);
//        model.addAttribute("currentPage", currentPage);
//        model.addAttribute("totalPages", page.getTotalPages());
//        model.addAttribute("totalItems", page.getTotalElements());
//        model.addAttribute("listPromotion", page.getContent());
//        model.addAttribute("promotion", new Promotion());
//        return "admin/promotion";
//    }
}

