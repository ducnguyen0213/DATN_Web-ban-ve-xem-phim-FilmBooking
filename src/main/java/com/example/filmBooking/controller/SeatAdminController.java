package com.example.filmBooking.controller;

import com.example.filmBooking.common.ResponseBean;
import com.example.filmBooking.model.*;
import com.example.filmBooking.repository.RoomRepository;
import com.example.filmBooking.repository.SeatRepository;
import com.example.filmBooking.service.RoomService;
import com.example.filmBooking.service.SeatService;
import com.example.filmBooking.service.impl.RoomServiceImpl;
import com.example.filmBooking.service.impl.SeatServiceImpl;
import com.example.filmBooking.service.impl.SeatTypeServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Controller
@RequestMapping("/seat")
@Tag(name = "Seat")
@SessionAttributes("soldTicketsCount")

public class SeatAdminController {

    @Autowired
    private RoomServiceImpl roomServiceI;

    @Autowired
    private SeatService seatService;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private SeatServiceImpl saveCustomersToDatabase;

    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomRepository roomRepository;

    @GetMapping("/find-all")
    public String viewSeat(Model model) {
        List<Room> getAll = roomService.fillAll();
        model.addAttribute("getAll", getAll);

        return "admin/seat";
    }


    @PostMapping("/update/{id}")
    public String updatePromotion(Model model, @PathVariable(name = "id") String id, Seat updatedRoom, RedirectAttributes ra) {
        seatService.update(id, updatedRoom);

        ra.addFlashAttribute("successMessage", "Sửa thành công!!!");
        return "redirect:/seat/find-all";   // Redirect to the promotion list page after update
    }

    @GetMapping("/seat-manager")
    public String viewSeatCustomer(Model model) {

        return "admin/seat-manager";
    }

    @GetMapping("/view-upload")
    public String viewUpload() {
        return "admin/ghe";
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam(name = "roomId") Room roomId) {
//        System.out.println("tôi là Controller:"+roomId);
        seatService.readExcel(file, roomId);
        return "redirect:/seat/find-all";
    }

    @PostMapping("/save")
    public String save(Model model,
                       @RequestParam(value = "listLineCodes", required = false) String[] listLineCodes,
                       @RequestParam(value = "listSeatTypeId", required = false) String[] listSeatTypeId,
                       @RequestParam(value = "listNumberOfSeatPerLine", required = false) String[] listNumberOfSeatPerLine,
                       @RequestParam("roomId") String roomId,
                       RedirectAttributes ra, Room updatedRoom) {
        try {

            int totalNumberOfSeats = 0;
            if (listNumberOfSeatPerLine != null) {
                for (String numberOfSeats : listNumberOfSeatPerLine) {
                    try {
                        int seats = Integer.parseInt(numberOfSeats);
                        totalNumberOfSeats += seats;
                        updatedRoom.setCapacity(totalNumberOfSeats);
                        roomServiceI.updateSeat(roomId, updatedRoom);

                    } catch (NumberFormatException e) {
                        // Xử lý ngoại lệ nếu dữ liệu không thể chuyển thành số nguyên
                    }
                }
            }
            CompletableFuture<Void> saveTask = CompletableFuture.runAsync(() -> {
                List<String> lineCodes = new ArrayList<>(Arrays.asList(listLineCodes));
                List<String> seatTypeIds = new ArrayList<>(Arrays.asList(listSeatTypeId));
                List<Integer> numberOfSeatPerLine = new ArrayList<>();
                for (String numberOfSeat : listNumberOfSeatPerLine) {
                    numberOfSeatPerLine.add(Integer.parseInt(numberOfSeat));
                }
                seatService.save(lineCodes, seatTypeIds, numberOfSeatPerLine, roomId);
            });

            saveTask.get(); // Ensure save operation is completed before redirecting

            return "redirect:/seat/find-all";
        } catch (Exception e) {
            // Generic exception handling
            e.printStackTrace();
            return "admin/seat";
        }
    }

    @PostMapping("/saveUpdate")
    public String saveUpdate(Model model,
                             @RequestParam(value = "listLineCodes", required = false) String[] listLineCodes,
                             @RequestParam(value = "listSeatTypeId", required = false) String[] listSeatTypeId,
                             @RequestParam(value = "listNumberOfSeatPerLine", required = false) String[] listNumberOfSeatPerLine,
                             @RequestParam("roomId") String roomId,
                             RedirectAttributes ra, Room updatedRoom) {
        try {
            Room room = roomService.findById(roomId);
            int totalNumberOfSeats = 0;

            if (listNumberOfSeatPerLine != null) {
                for (String numberOfSeats : listNumberOfSeatPerLine) {

                    try {
                        int seats = Integer.parseInt(numberOfSeats);
                        totalNumberOfSeats += seats;

                        room.setCapacity(room.getCapacity() + seats);
                        roomServiceI.updateSeat(roomId, room);

                    } catch (NumberFormatException e) {
                        // Xử lý ngoại lệ nếu dữ liệu không thể chuyển thành số nguyên
                    }
                }
            }
            CompletableFuture<Void> saveTask = CompletableFuture.runAsync(() -> {
                List<String> lineCodes = new ArrayList<>(Arrays.asList(listLineCodes));
                List<String> seatTypeIds = new ArrayList<>(Arrays.asList(listSeatTypeId));
                List<Integer> numberOfSeatPerLine = new ArrayList<>();
                for (String numberOfSeat : listNumberOfSeatPerLine) {
                    numberOfSeatPerLine.add(Integer.parseInt(numberOfSeat));
                }
                seatService.save(lineCodes, seatTypeIds, numberOfSeatPerLine, roomId);
            });

            saveTask.get(); // Ensure save operation is completed before redirecting

            return "redirect:/seat/find-all";
        } catch (Exception e) {
            // Generic exception handling
            e.printStackTrace();
            return "admin/seat";
        }
    }

    @GetMapping("/checkDuplicateLineForRoom")
    public ResponseEntity<Map<String, Boolean>> checkDuplicateLineForRoom(
            @RequestParam("idRoom") String idRoom,
            @RequestParam("listLineCodes") String listLineCodes) {
        Room room = roomRepository.findById(idRoom).orElse(null);
        if (room == null) {
            return ResponseEntity.badRequest().build();
        }

        boolean isDuplicate = saveCustomersToDatabase.isLineUniqueForRoom(room, listLineCodes);
        Map<String, Boolean> response = new HashMap<>();
        response.put("isDuplicate", isDuplicate);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/saveSeat")
    public String addRoom(Model model, @RequestParam(name = "room") Room room,
                          @RequestParam(name = "line") String line,
                          @RequestParam(name = "number") Integer number,
                          @RequestParam(name = "seatType") SeatType seatType,
                          @RequestParam(name = "status") int status,
                          @RequestParam(name = "id") String id,
                          RedirectAttributes ra) {
        try {
            Seat seat = Seat.builder()
                    .id(id)
                    .room(room)
                    .line(line)
                    .number(number)
                    .seatType(seatType)
                    .status(status)
                    .build();
            boolean existingSeat = saveCustomersToDatabase.findByCodeLike(room,line + number);
//            System.out.println(existingSeat);
            Room roomm = roomService.findById(room.getId());


            if (!existingSeat) {
                // Handle the duplicate entry based on your business logic
                ra.addFlashAttribute("errorMessages", "Ghế đã tồn tại!!!");
            } else {
                if (saveCustomersToDatabase.saveSeat(seat) instanceof Seat) {
                    roomm.setCapacity(roomm.getCapacity() + 1);
                    roomServiceI.updateSeat(room.getId(), roomm);
                    ra.addFlashAttribute("successMessage", "Thêm thành công!!!");
                } else {
                    ra.addFlashAttribute("errorMessage", "Thêm thất bại");
                }
            }
            //            model.addAttribute("cinema", new Cinema());
            return "redirect:/room/" + room.getId();
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/room/" + room.getId();
        }
    }


    @GetMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") String id,
                         RedirectAttributes ra,
                         HttpServletRequest request) {
        HttpSession session = request.getSession();
        Room room = (Room) session.getAttribute("room");
        try {
            seatService.delete(id);
            ra.addFlashAttribute("successMessage", "Xóa thành công!!!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Xóa thất bại!!!");
        }
        return "redirect:/room/" + room.getId();
    }



    @PostMapping("/saveUpdateSeat")
    public String addSeat(Model model, @RequestParam(name = "room") Room room,
                          @RequestParam(name = "line") String line,
                          @RequestParam(name = "number") Integer number,
                          @RequestParam(name = "seatType") SeatType seatType,
                          @RequestParam(name = "status") int status,
                          @RequestParam(name = "id") String id,
                          RedirectAttributes ra) {
        try {
            Seat seat = Seat.builder()
                    .id(id)
                    .room(room)
                    .line(line)
                    .number(number)
                    .seatType(seatType)
                    .status(status)
                    .build();
            boolean existingSeat = saveCustomersToDatabase.findByCodeLike(room,line + number);
            Room roomm = roomService.findById(room.getId());


            if (!existingSeat) {
                // Handle the duplicate entry based on your business logic
                ra.addFlashAttribute("errorMessages", "Ghế đã tồn tại!!!");
            } else {
                if (saveCustomersToDatabase.saveSeat(seat) instanceof Seat) {
                    roomm.setCapacity(roomm.getCapacity() + 1);
                    roomServiceI.updateSeat(room.getId(), roomm);
                    ra.addFlashAttribute("successMessage", "Thêm thành công!!!");
                } else {
                    ra.addFlashAttribute("errorMessage", "Thêm thất bại");
                }
            }
            //            model.addAttribute("cinema", new Cinema());
            return "redirect:/seat/find-all";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/seat/find-all";
        }
    }
}
