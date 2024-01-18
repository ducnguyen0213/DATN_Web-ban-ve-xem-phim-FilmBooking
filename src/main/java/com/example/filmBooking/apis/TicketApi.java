package com.example.filmBooking.apis;

import com.example.filmBooking.model.*;
import com.example.filmBooking.model.dto.BillDto;
import com.example.filmBooking.model.dto.DtoMovie;
import com.example.filmBooking.model.dto.DtoSeat;

import com.example.filmBooking.repository.*;
import com.example.filmBooking.service.impl.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/api/ticket")
public class TicketApi {

    @Autowired
    private TicketServiceImpl ticketService;

    @Autowired
    private ScheduleServiceImpl scheduleService;

    @Autowired
    private SeatServiceImpl seatService;

    @Autowired
    private FootRepository ServiceService;

    @Autowired
    private PromotionServiceImpl promotionService;

    @Autowired
    private GeneralSettingServiceImpl generalSettingService;

    @Autowired
    private BillRepository repository;

    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private SeatTypeServiceImpl seatTypeService;

    @GetMapping("/show/schedule")
    private ResponseEntity<List<Schedule>> getSchedule(@RequestParam String cinemaId,
                                                       @RequestParam String movieId,
                                                       @RequestParam String startAt,
                                                       @RequestParam String startTime,
                                                       @RequestParam String nameRoom) {
        return new ResponseEntity<>(scheduleService.getSchedule(cinemaId, movieId, startAt, startTime, nameRoom), HttpStatus.OK);
    }

    @GetMapping("/show/schedule1")
    private ResponseEntity<List<Schedule>> getSchedule1(@RequestParam String movieName,
                                                        @RequestParam String startAt,
                                                        @RequestParam String nameRoom) {
        return new ResponseEntity<>(scheduleService.getSchedule1(movieName, startAt, nameRoom), HttpStatus.OK);
    }

    @GetMapping("/show/seat")
    private ResponseEntity<List<DtoSeat>> getSeat(@RequestParam String cinemaId,
                                                  @RequestParam String movieId,
                                                  @RequestParam String startAt,
                                                  @RequestParam String startTime,
                                                  @RequestParam String nameRoom) {
        return new ResponseEntity<>(seatService.getSeats(cinemaId, movieId, startAt, startTime, nameRoom), HttpStatus.OK);
    }

    @GetMapping("/show/ticket")
    private ResponseEntity<List<Ticket>> getSTicket(@RequestParam String cinemaId,
                                                    @RequestParam String movieId,
                                                    @RequestParam String startAt,
                                                    @RequestParam String startTime,
                                                    @RequestParam String nameRoom) {
        return new ResponseEntity<>(ticketRepository.ticketShow(cinemaId, movieId, startAt, startTime, nameRoom), HttpStatus.OK);
    }
    @GetMapping("/show/ticket1")
    private ResponseEntity<List<Ticket>> getSTicket1(@RequestParam String movieName,
                                                    @RequestParam String startAt,
                                                    @RequestParam String nameRoom) {
        return new ResponseEntity<>(ticketRepository.ticketShow1(movieName, startAt, nameRoom), HttpStatus.OK);
    }

    @GetMapping("/show/seat1")
    private ResponseEntity<List<DtoSeat>> getSeat1(@RequestParam String movieName,
                                                   @RequestParam String startAt,
                                                   @RequestParam String nameRoom) {
        return new ResponseEntity<>(seatService.getSeats1(movieName, startAt, nameRoom), HttpStatus.OK);
    }

    @GetMapping("/show/Service")
    private ResponseEntity<List<Service>> getAllService() {
        return new ResponseEntity<>(ServiceService.findAll(), HttpStatus.OK);
    }
    @GetMapping("/show/seatType")
    private ResponseEntity<List<SeatType>> getAllSeatType() {
        return new ResponseEntity<>(seatTypeService.findAll(), HttpStatus.OK);
    }
    @GetMapping("/show/voucher")
    private ResponseEntity<List<Promotion>> getAllVoucher(@RequestParam String customerId) {
        return new ResponseEntity<>(promotionService.listVoucherCustomer(customerId), HttpStatus.OK);
    }

    @GetMapping("/show/generalSetting")
    private ResponseEntity<List<GeneralSetting>> getAllGeneralSetting() {
        return new ResponseEntity<>(generalSettingService.fillAll(), HttpStatus.OK);
    }

    @GetMapping("/show/bill")
    private ResponseEntity<List<Object[]>> getAllGeneralSetting(@RequestParam String customerId) {
        return new ResponseEntity<>(repository.findBillDetailsByCustomer(customerId), HttpStatus.OK);
    }
//    @GetMapping("/find/bill")
//    private List<BillDto> FindBill(@RequestParam String tradingCode, @RequestParam LocalDate dateCreate ) {
//        return repository.findBillsByTradingCodeAndDateCho(tradingCode,dateCreate)
//                .stream().map(bill -> modelMapper.map(bill, BillDto.class)).collect(Collectors.toList());
//    }


//    @GetMapping("/show/movie")
//    private ResponseEntity<List<Movie>> getMovie(@RequestParam String directors,
//                                                       @RequestParam String languages,
//                                                       @RequestParam String movieTypes,
//                                                       @RequestParam String performers) {
//
//        return new ResponseEntity<>(movieRepository.filterMovies(directors, languages, movieTypes, performers), HttpStatus.OK);
//    }
}
