package com.example.filmBooking.controller;

import com.example.filmBooking.model.*;
import com.example.filmBooking.model.dto.DtoSeat;
import com.example.filmBooking.repository.RoomRepository;
import com.example.filmBooking.repository.SeatRepository;
import com.example.filmBooking.service.CinemaService;
import com.example.filmBooking.service.RoomService;
import com.example.filmBooking.service.impl.RoomServiceImpl;
import com.example.filmBooking.service.impl.SeatServiceImpl;
import com.example.filmBooking.service.impl.SeatTypeServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
@RequestMapping("/room")
@SessionAttributes("soldTicketsCount")

public class RoomAdminController {
    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomServiceImpl roomServiceI;

    @Autowired
    private SeatServiceImpl seatService;

    @Autowired
    private CinemaService cinemaService;

    @Autowired
    private SeatTypeServiceImpl seatTypeService;

    @Autowired
    private SeatRepository repository;

    @PostMapping("/save")
    @Operation(summary = "[Thêm dữ liệu room]")
    public String addRoom(Model model, @RequestParam(name = "cinema") Cinema cinema,
                          @RequestParam(name = "width") int width,
                          @RequestParam(name = "height") int height,
                          @RequestParam(name = "projector") String projector,
                          @RequestParam(name = "other_equipment") String other_equipment,
                          @RequestParam(name = "status") int status,
                          @RequestParam(name = "description") String description,
                          @RequestParam(name = "id") String id,
                          RedirectAttributes ra) {

        try {
            Room room = Room.builder()
                    .id(id)
                    .cinema(cinema)
                    .height(height)
                    .width(width)
                    .projector(projector)
                    .other_equipment(other_equipment)
                    .status(status)
                    .description(description)
                    .build();
            if (roomService.save(room) instanceof Room) {
                ra.addFlashAttribute("successMessage", "Thêm thành công!!!");
            } else {
                ra.addFlashAttribute("errorMessage", "Thêm thất bại");
            }
//            model.addAttribute("cinema", new Cinema());
            return "redirect:/room/find-all";
        } catch (Exception e) {
            e.printStackTrace();
            return "admin/room";
        }

    }

    @GetMapping("/find-all")
    public String viewRoom(Model model) {
        List<Room> roomList = roomService.fillAll();
        model.addAttribute("roomList", roomList);

        List<SeatType> seatTypeList = seatTypeService.findAll();
        Collections.sort(seatTypeList, Comparator.comparing(SeatType::getSurcharge));
        List<Cinema> cinemaList = cinemaService.fillAll();
        model.addAttribute("seatTypeList", seatTypeList);
        model.addAttribute("cinemaList", cinemaList);
        model.addAttribute("room", new Room());

        return "admin/room";
    }


    @GetMapping("/delete/{id}")
    @Operation(summary = "[Xóa dữ liệu room]")
    public String delete(@PathVariable(name = "id") String id, RedirectAttributes ra) {
        try {
            roomService.delete(id);
            ra.addFlashAttribute("successMessage", "Xóa thành công!!!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Xóa thất bại!!!");
        }
        return "redirect:/room/find-all";
    }

    @GetMapping("/update/{pageNumber}/{id}")
    @Operation(summary = "[Sửa theo id]")
    public String findById(Model model, @PathVariable("id") String id, @PathVariable("pageNumber") Integer currentPage) {
        Page<Room> page = roomService.getAll(currentPage);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("room", roomService.findById(id));
        model.addAttribute("listRoom", page.getContent());
        List<Cinema> cinemaId = cinemaService.fillAll();
        model.addAttribute("cinemaId", cinemaId);
        return "admin/room";
    }


    @PostMapping("/update/{id}")
    public String updatePromotion(@PathVariable(name = "id") String id, Room updatedRoom, RedirectAttributes ra) {
        roomServiceI.update(id, updatedRoom);
        ra.addFlashAttribute("successMessage", "Sửa thành công!!!");

        return "redirect:/room/find-all";   // Redirect to the promotion list page after update
    }

    @GetMapping("/search-room/{pageNumber}")
    public String searchRoom(@RequestParam(name = "keyword") String keyword, @PathVariable("pageNumber") Integer currentPage, Model model) {
        Page<Room> page = roomService.serachRoom(keyword, currentPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listRoom", page.getContent());
        List<Cinema> cinemaId = cinemaService.fillAll();
        model.addAttribute("cinemaId", cinemaId);
        model.addAttribute("room", new Room());
        return "admin/room";
    }


    @GetMapping("/search/seat")
    public String SearchSeat(Model model, @RequestParam(value = "roomName", required = false) String roomName) {
        List<Room> getAll = roomService.fillAll();
        model.addAttribute("getAll", getAll);
        List<SeatType> seatTypeList = seatTypeService.findAll();
//        System.out.println(seatTypeList);
        model.addAttribute("seatTypeList", seatTypeList);
//        List<Seat> listSeat = seatService.getAll();
        List<Seat> seatList = seatService.listSeat(roomName);
        Map<Character, List<Seat>> groupedSeats = new HashMap<>();

// Grouping seats by initial letter
        for (Seat seat : seatList) {
            char initialLetter = seat.getCode().charAt(0);
            groupedSeats.computeIfAbsent(initialLetter, k -> new ArrayList<>()).add(seat);
        }

// Custom comparator to handle alphanumeric sorting
        Comparator<Seat> seatComparator = (s1, s2) -> {
            String code1 = s1.getCode();
            String code2 = s2.getCode();
            String numericPart1 = code1.replaceAll("\\D", "");
            String numericPart2 = code2.replaceAll("\\D", "");
            if (numericPart1.length() == numericPart2.length()) {
                return numericPart1.compareTo(numericPart2);
            } else {
                return Integer.compare(Integer.parseInt(numericPart1), Integer.parseInt(numericPart2));
            }
        };

// Sorting seat codes within each group using the custom comparator
        groupedSeats.values().forEach(seats -> seats.sort(seatComparator));
        model.addAttribute("groupedSeats", groupedSeats);
        model.addAttribute("seatList", seatList);
        model.addAttribute("seat", new Seat());

//        System.out.println("Tôi là :" + seatList);

        return "admin/seat-manager";
    }

    @GetMapping("/{id}")
    public String showEditForm(@PathVariable("id") String id, Model model, HttpServletRequest request) {
        Room room = roomServiceI.findById(id);
        List<Seat> seatList = repository.seatSeatAll(id);
        model.addAttribute("room", room);
        List<SeatType> seatTypeList = seatTypeService.findAll();
        model.addAttribute("seatTypeList", seatTypeList);
        model.addAttribute("seatList", seatList);
        HttpSession session = request.getSession();
        session.setAttribute("room", room);
        model.addAttribute("seat", new Seat());
        return "admin/creatSeat";
    }
}
