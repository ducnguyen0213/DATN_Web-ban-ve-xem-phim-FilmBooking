package com.example.filmBooking.controller;

import com.example.filmBooking.common.ResponseBean;
import com.example.filmBooking.model.*;
import com.example.filmBooking.model.dto.DtoMovie;
import com.example.filmBooking.repository.ScheduleRepository;
import com.example.filmBooking.service.*;
import com.example.filmBooking.service.impl.ScheduleServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import org.apache.logging.log4j.util.Strings;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/schedule")
@SessionAttributes("soldTicketsCount")
@Tag(name = "schedule")
public class ScheduleAdminController {
    @Autowired
    private ScheduleService service;
//    @Autowired
//    private TempScheduleService service;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private ScheduleServiceImpl scheduleService;

    @Autowired
    private MovieService movieService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private CinemaService cinemaService;

    @GetMapping("/find-all")
    public String viewSchedule(Model model) {
        return findAll(model, 1, null, null, null, null, null, null);
    }

    @GetMapping("/find-all/page/{pageNumber}")
    @Operation(summary = "[Hiển thị tất cả]")
    public String findAll(Model model, @PathVariable("pageNumber") Integer currentPage, @Param(value = "nameCinema") String nameCinema,
                          @Param(value = "nameMovie") String nameMovie,
                          @DateTimeFormat(pattern = "dd/MM/yyyy")
                          @Param(value = "startAt") LocalDate startAt,
                          @Param(value = "startTime") Integer startTime,
                          @Param(value = "status") String status,
                          @Param(value = "endTime") Integer endTime) {
        Page<Schedule> page = scheduleService.getAll(currentPage);
        nameCinema = Strings.isEmpty(nameCinema) ? null : nameCinema;
        nameMovie = Strings.isEmpty(nameMovie) ? null : nameMovie;
        status = Strings.isEmpty(status) ? null : status;
        if (nameCinema != null || nameMovie != null || startAt != null || startTime != null || endTime != null || status != null) {
            page = scheduleService.searchSchedule(nameCinema, startAt, nameMovie, startTime, endTime, status, currentPage);
        }
        model.addAttribute("nameCinema", nameCinema);
        model.addAttribute("nameMovie", nameMovie);
        model.addAttribute("startAt", startAt);
        model.addAttribute("startTime", startTime);
        model.addAttribute("endTime", endTime);
        model.addAttribute("status", status);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listSchedule", page.getContent());
        List<Movie> movieId = movieService.showPhishowPhimSapChieuAndDangChieumSapChieu();
        List<Room> roomId = roomService.fillAll();
        List<Room> roomCapacity = roomService.roomCapacity();
        List<Cinema> cinemaId = cinemaService.fillAll();
        model.addAttribute("roomCapacity", roomCapacity);
        model.addAttribute("cinemaId", cinemaId);
        model.addAttribute("movieId", movieId);
        model.addAttribute("roomId", roomId);
        model.addAttribute("schedule", new Schedule());
        return "admin/schedule";
    }

    @PostMapping("/batch-save/{pageNumber}")
    @Operation(summary = "[Thêm mới]")
    public String generateSchedule(Model model,
                                   @RequestParam("listRoomChecked") List<String> listRoomChecked,
                                   @RequestParam("listMovieChecked") List<String> listMovieChecked,
                                   @RequestParam(value = "startTime", required = false) LocalDateTime startTime,
                                   @RequestParam(value = "endTime", required = false) LocalDateTime endTime,
                                   @PathVariable("pageNumber") Integer currentPage, RedirectAttributes ra, Schedule schedule
    ) {
        try {
//            if (scheduleService.checkScheduleConflict(schedule, String.valueOf(listRoomChecked)) == false) {
//                ra.addFlashAttribute("Message", "Tạo suất chiếu thất bại ");
//            } else {
            System.out.println("tôi là:" + listMovieChecked);
            System.out.println("tôi là:" + listRoomChecked);
            System.out.println("tôi là:" + startTime);
            System.out.println("tôi là:" + endTime);

            service.generateSchedule(listRoomChecked, listMovieChecked, startTime, endTime);

            model.addAttribute("listRoomChecked", listRoomChecked);
            model.addAttribute("listMovieChecked", listMovieChecked);
            model.addAttribute("currentPage", currentPage);
            return "redirect:/schedule/find-all";
        } catch (Exception e) {
            e.printStackTrace();
            return "admin/schedule";
        }
    }

    @GetMapping("/findById/{pageNumber}/{id}")
//    /{currentPage}
    public String findById(Model model, @PathVariable(name = "id") String id, @PathVariable("pageNumber") Integer currentPage) {
        model.addAttribute("schedule", service.findById(id));
        List<Schedule> scheduleList = scheduleService.findAll();
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("listSchedule", scheduleList);
        return "admin/update-schedule";
    }

    @PostMapping("/update/{id}")
    public String updatePromotion(@PathVariable(name = "id") String id, Schedule updatedRoom, RedirectAttributes ra) {
        scheduleService.update1(id, updatedRoom);
        ra.addFlashAttribute("successMessage", "Sửa thành công!!!");

        return "redirect:/schedule/find-all";   // Redirect to the promotion list page after update
    }

    @GetMapping
    public String view1(Model model) {
        //lấy các suất chiếu của 1 phòng trong 1 ngày cụ thể
        findAll(model, 1, null, null, null, null, null, null);
        List<Room> getAll = roomService.fillAll();
        model.addAttribute("listRoom", getAll);
        return "admin/calendar";
    }

    @PostMapping("/update-all")
    @Operation(summary = "updateAll")
    public ResponseEntity<?> updateAll(@RequestBody List<Schedule> itemMapData) {
        ResponseBean responseBean = new ResponseBean();
        itemMapData.forEach(item -> System.out.println(item.getStartAt()));
        responseBean.setData(service.updateAll(itemMapData));
        responseBean.setMessage("success");
        return new ResponseEntity<>(responseBean, HttpStatus.OK);   // Redirect to the promotion list page after update
    }

    @GetMapping("/search-by-date")
    @ResponseBody
    public ResponseEntity<?> handleCalendarRequest(Model model, @RequestParam(name = "date1") String date1, @RequestParam(name = "room") String room) {
        // Xử lý logic ở đây với tham số date được truyền từ JavaScript
        List<Schedule> getAll = scheduleRepository.findByRoomAndFinishAt(date1, room);
        model.addAttribute("list", getAll);
        return new ResponseEntity<>(getAll, HttpStatus.OK);
    }
    @GetMapping("/chi-tiet-calendar-schedule/detail")
    @Operation(summary = "[tim kiếm theo ngày]")
    public ResponseEntity<?> viewDate(@RequestParam("date") String date, Model model){
        List<Schedule> schedule = scheduleService.findByDateStartAt(date);
        return new ResponseEntity<>(schedule, HttpStatus.OK);
    }
    @GetMapping("/chi-tiet-calendar-schedule")
    public String viewer(Model model){
        List<Schedule> schedule = scheduleService.getAll();
        model.addAttribute("list", schedule);
        return "admin/chi-tiet-schedule";
    }



    @GetMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") String id, RedirectAttributes ra) {
        try {
            service.delete(id);
            ra.addFlashAttribute("successMessage", "Xóa thành công!!!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Xóa thất bại!!!");
        }
        return "redirect:/schedule";
    }
    
    @PostMapping("/save")
    @Operation(summary = "[Thêm mới]")
    public String save(Model model, @RequestParam(name = "status") String status,@RequestParam(name = "id") String id, RedirectAttributes ra) {
        try {
            Schedule schedule = Schedule.builder()
                    .id(id)
                    .status(status)
                    .build();

            if (service.save(schedule) instanceof String) {
                ra.addFlashAttribute("successMessage", "Thêm thành công");
            } else {
                ra.addFlashAttribute("thaerrorMessagetBai", "Thêm thất bại");
            }
            model.addAttribute("schedule", new Schedule());
            return "redirect:/schedule/chi-tiet-calendar-schedule";
        } catch (Exception e) {
            e.printStackTrace();
            return "admin/chi-tiet-schedule";
        }
    }
}


