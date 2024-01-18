package com.example.filmBooking.controller;

import com.example.filmBooking.model.Service;
import com.example.filmBooking.model.Movie;
import com.example.filmBooking.model.Schedule;
import com.example.filmBooking.service.ServiceService;
import com.example.filmBooking.service.MovieService;
import com.example.filmBooking.service.ScheduleService;
import com.example.filmBooking.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/statistical")
public class StatisticalAdminController {
    @Autowired
    private MovieService movieService;

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/find-all")
    public String viewThongKe(Model model,
                              @RequestParam(value = "fromDate", required = false)
                              @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
                              @RequestParam(value = "toDate", required = false)
                                  @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate) {
        List<Object> revenueMovie= statisticsService.revenueMovie(fromDate,toDate);
        List<Movie> movieList= movieService.findAll();
        List<Service> serviceList= serviceService.fillAll();
        List<Schedule> scheduleList= scheduleService.findAll();
        model.addAttribute("revenueMovie", revenueMovie);
        model.addAttribute("movieList", movieList);
        model.addAttribute("ServiceList", serviceList);
        model.addAttribute("scheduleList", scheduleList);
        return "admin/thong-ke";
    }
}
