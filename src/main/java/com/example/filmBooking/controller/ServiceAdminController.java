package com.example.filmBooking.controller;

import com.example.filmBooking.model.Service;
import com.example.filmBooking.model.ServiceType;
import com.example.filmBooking.service.ServiceService;
import com.example.filmBooking.service.ServiceTypeService;
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
import java.util.List;

@Controller
@RequestMapping("/service")
@SessionAttributes("soldTicketsCount")

public class ServiceAdminController {
    @Autowired
    private ServiceService serviceService;

    @Autowired
    private UploadImage uploadImage;

    @Autowired
    private ServiceTypeService serviceTypeService;

    @PostMapping("/save/{pageNumber}")
    @Operation(summary = "[Thêm dữ liệu Service]")
    public String addService(Model model, @RequestParam(name = "id") String id, @RequestParam(name = "name") String name, @RequestParam(name = "price") BigDecimal priceService, @RequestParam(name = "description") String description, @RequestParam(name = "image") MultipartFile multipartFile, @PathVariable("pageNumber") Integer currentPage, RedirectAttributes ra, @RequestParam(name = "service") ServiceType serviceType) {
        try {
            Service service = Service.builder().id(id).name(name).price(priceService).description(description).serviceType(serviceType).image(multipartFile.getOriginalFilename()).build();
            uploadImage.handerUpLoadFile(multipartFile);
            if (serviceService.save(service) instanceof Service) {
                ra.addFlashAttribute("successMessage", "Thêm thành công");
            } else {
                ra.addFlashAttribute("errorMessage", "Thêm thất bại");
            }
            model.addAttribute("Service", new Service());
            model.addAttribute("currentPage", currentPage);
            return "redirect:/service/find-all";
        } catch (Exception e) {
            e.printStackTrace();
            return "admin/service";
        }
    }

    @GetMapping("/find-all")
    public String viewService(Model model) {
        return findAll(model, 1, null);
    }

    @GetMapping("/find-all/page/{pageNumber}")
    public String findAll(Model model, @PathVariable("pageNumber") Integer currentPage, @RequestParam(value = "keyword", required = false) String name) {
        Page<Service> page = serviceService.findAll(currentPage);
        if (name != null) {
            page = serviceService.findByNameContains(name, currentPage);
        }
        model.addAttribute("keyword", name);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listService", page.getContent());
        List<ServiceType> listService = serviceTypeService.findAll();
        model.addAttribute("cbbServiceType", listService);
        model.addAttribute("Service", new Service());
        return "admin/service";
    }

    @GetMapping("/test")
    public String test() {
        return "admin/movie";
    }

    @GetMapping("/delete/{id}")
    @Operation(summary = "[Xóa dữ liệu Service]")
    public String delete(@PathVariable(name = "id") String id, RedirectAttributes ra) {

        try {
            serviceService.delete(id);
            ra.addFlashAttribute("successMessage", "Xóa thành công!!!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Xóa thất bại!!!");
        }
        return "redirect:/service/find-all";
    }

    //    @GetMapping("/update/{pageNumber}/{id}")
//    public String detailUpdateService(Model model,
//                                   @PathVariable(name = "id") String id,
//                                   @PathVariable("pageNumber") Integer currentPage) {
//
//        Page<Service> listService = ServiceService.findAll(currentPage);
//        model.addAttribute("currentPage", currentPage);
//        model.addAttribute("totalPages", listService.getTotalPages());
//        model.addAttribute("totalItems", listService.getTotalElements());
//        model.addAttribute("Service", ServiceService.findById(id));
//        model.addAttribute("listService", listService.getContent());
//        return "admin/Service";
//    }
    @PostMapping("/update/{id}")
    public String updatePromotion(@PathVariable(name = "id") String id, Service updatedRoom, RedirectAttributes ra) {
        serviceService.update(id, updatedRoom);
        ra.addFlashAttribute("successMessage", "Sửa thành công!!!");

        return "redirect:/service/find-all";   // Redirect to the promotion list page after update
    }

    @PostMapping("/service-type/save")
    public String addServiceType(@RequestParam("name") String name, Model model, RedirectAttributes ra) {
        try {
            ServiceType serviceType = ServiceType.builder().name(name).build();
            if (serviceTypeService.addServiceType(serviceType) instanceof ServiceType) {
                ra.addFlashAttribute("successMessage", "Thêm thành công");
            } else {
                ra.addFlashAttribute("errorMessage", "Thêm thất bại");
            }
            model.addAttribute("serviceType", new ServiceType());
            return "redirect:/service/service-type";
        } catch (Exception e) {
//            e.printStackTrace();
            return "admin/service-type";
        }
    }

    @PostMapping("/service-type/update/{id}")
    public String updateServiceType(@PathVariable(name = "id") String id, Model model, ServiceType serviceType, RedirectAttributes ra) {
        serviceTypeService.updateServiceType(serviceType, id);
        ra.addFlashAttribute("successMessage", "Sửa thành công!!!");
        return "redirect:/service/service-type";
    }

    @GetMapping("/service-type/delete/{id}")
    public String deleteServiceType(Model model, @PathVariable("id") String id, RedirectAttributes ra) {
        try {
            serviceTypeService.deleteServiceType(id);
            ra.addFlashAttribute("successMessage", "Xóa thành công!!!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Xóa thất bại!!!");
        }
        return "redirect:/service/service-type";
    }

    @GetMapping("/service-type")
    public String getAll(Model model) {
        List<ServiceType> listServiceType = serviceTypeService.findAll();
        model.addAttribute("listServiceType", listServiceType);
        return "admin/service-type";
    }
}
