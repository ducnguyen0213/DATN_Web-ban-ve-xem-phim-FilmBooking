package com.example.filmBooking.controller;

import com.example.filmBooking.model.Room;
import com.example.filmBooking.model.Schedule;
import com.example.filmBooking.model.Ticket;
import com.example.filmBooking.service.RoomService;
import com.example.filmBooking.service.ScheduleService;
import com.example.filmBooking.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/ticket")
@SessionAttributes("soldTicketsCount")
public class TicketAdminController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private RoomService roomService;

    @GetMapping("/find-all")
    public String viewTicket(Model model) {
        return findAll(model, null,null, 1,null);
    }

    @GetMapping("/find-all/page/{pageNumber}")
    public String findAll(Model model,
//                          @RequestParam(value = "roomId", required = false) String roomId,
//                          @RequestParam(value = "movieId", required = false) String movieId,
                          @RequestParam(value = "id", required = false) String id,
                          @RequestParam(value = "dateSearch", required = false) Date dateSearch,
                          @PathVariable("pageNumber") Integer currentPage,
                          @RequestParam(value = "status", required = false) String status
    )
    {
        Page<Ticket> page = ticketService.findAllByStatus(id, status, currentPage);
        if (status != null) {
            page = ticketService.findAllByStatus(id, status,currentPage);
        }
        model.addAttribute("status", status);
        model.addAttribute("idSchedule", id);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listTicket", page.getContent());
        List<String> scheduleName = scheduleService.listSchedule();
        System.out.println(page.getContent().size()+"hihi");
        System.out.println(status+"hehe");
//        List<Room> roomList= roomService.
        model.addAttribute("scheduleNames", scheduleName);
        return "admin/ticket";
    }

    @RequestMapping("/schedule-date")
    public @ResponseBody List<Schedule> getOptions(@RequestParam("schedule-name") String scheduleName) {
            return scheduleService.getScheduleByName(scheduleName);
    }

}
