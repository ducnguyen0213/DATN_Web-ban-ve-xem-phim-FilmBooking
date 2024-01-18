package com.example.filmBooking.controllerApi;

import com.example.filmBooking.apis.Api;
import com.example.filmBooking.model.*;
import com.example.filmBooking.model.dto.DtoSeat;
import com.example.filmBooking.repository.CustomerRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Controller
@RequestMapping("/show/booking")
@SessionAttributes("soldTicketsCountBill")

public class BookingControllerApi {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CustomerRepository repository;

    public static String apiGetSchedule = Api.baseURL + "/api/ticket/show/schedule";
    public static String apiGetSchedule1 = Api.baseURL + "/api/ticket/show/schedule1";
    public static String apiGetSeat = Api.baseURL + "/api/ticket/show/seat";
    public static String apiGetSeat1 = Api.baseURL + "/api/ticket/show/seat1";
    public static String apiGetFoot = Api.baseURL + "/api/ticket/show/Service";
    public static String apiGetSeatType = Api.baseURL + "/api/ticket/show/seatType";
    public static String apiVoucher = Api.baseURL + "/api/ticket/show/voucher";
    public static String apiGeneralSetting= Api.baseURL + "/api/ticket/show/generalSetting";
    public static String apiGetTicket= Api.baseURL + "/api/ticket/show/ticket";
    public static String apiGetTicket1= Api.baseURL + "/api/ticket/show/ticket1";



    @GetMapping("/schedule")
    public String booking(@RequestParam String cinemaId,
                          @RequestParam String movieId,
                          @RequestParam String startTime,
                          @RequestParam String startAt,
                          @RequestParam String nameRoom,
                          Model model,
                          HttpServletRequest request) {
        HttpSession session = request.getSession();
//        session.setAttribute("cinemaId", cinemaId);
//        model.addAttribute("cinemaId", cinemaId);
//
//        session.setAttribute("movieId", movieId);
//        model.addAttribute("movieId", movieId);

        session.setAttribute("startTime", request.getParameter("startTime"));
        model.addAttribute("startTime", startTime);

        session.setAttribute("startAt", request.getParameter("startAt"));
        model.addAttribute("startAt", startAt);

        session.setAttribute("nameRoom", request.getParameter("nameRoom"));
        model.addAttribute("nameRoom", nameRoom);
//        System.out.println(start_time);

        Customer customer = (Customer) session.getAttribute("customer");
        model.addAttribute("customer", customer);


        if (customer == null) {
            // Initialize the customer object if it doesn't exist in the session
            customer = new Customer();
            // Set the necessary attributes of the customer
            customer.setId("yourCustomerId");

            session.setAttribute("customer", customer);
        }else {
                Integer listPoint = repository.findPointById(customer.getId());
                model.addAttribute("listPoint", listPoint);
        }

        String customerId = customer.getId();
        HttpEntity<?> entity = new HttpEntity<>(customer);

        //lấy ra lịch chiếu
        String urlTemplateSchdeule = UriComponentsBuilder.fromHttpUrl(apiGetSchedule)
                .queryParam("movieId", "{movieId}")
                .queryParam("cinemaId", "{cinemaId}")
                .queryParam("startTime", "{startTime}")
                .queryParam("startAt", "{startAt}")
                .queryParam("nameRoom", "{nameRoom}")
                .encode()
                .toUriString();
        Map<String, String> listRequestParam = new HashMap<>();
        listRequestParam.put("movieId", movieId + "");
        listRequestParam.put("cinemaId", cinemaId + "");
        listRequestParam.put("startTime", startTime + "");
        listRequestParam.put("startAt", startAt + "");
        listRequestParam.put("nameRoom", nameRoom + "");
        ResponseEntity<Schedule[]> listSchedule = restTemplate.exchange(urlTemplateSchdeule,
                HttpMethod.GET,
                entity,
                Schedule[].class,
                listRequestParam);
        Schedule scheduleDTO =  listSchedule.getBody()[0];
        model.addAttribute("listSchedule", listSchedule.getBody());
        session.setAttribute("schedule", scheduleDTO);
//        System.out.println("tôi là :"+ scheduleDTO.getId());

        //lấy ra seat
        String urlTemplateSeat = UriComponentsBuilder.fromHttpUrl(apiGetSeat)
                .queryParam("movieId", "{movieId}")
                .queryParam("cinemaId", "{cinemaId}")
                .queryParam("startTime", "{startTime}")
                .queryParam("startAt", "{startAt}")
                .queryParam("nameRoom", "{nameRoom}")
                .encode()
                .toUriString();
        ResponseEntity<DtoSeat[]> listSeat = restTemplate.exchange(urlTemplateSeat,
                HttpMethod.GET,
                entity,
                DtoSeat[].class,
                listRequestParam);

        DtoSeat[] listSeatDTOS = listSeat.getBody();
        Map<Character, List<DtoSeat>> groupedSeats = new HashMap<>();

// Grouping seats by initial letter
        for (DtoSeat seat : listSeatDTOS) {
            char initialLetter = seat.getCode().charAt(0);
            groupedSeats.computeIfAbsent(initialLetter, k -> new ArrayList<>()).add(seat);
        }

// Custom comparator to handle alphanumeric sorting
        Comparator<DtoSeat> seatComparator = (s1, s2) -> {
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
//        model.addAttribute("listSeatDTOS", listSeatDTOS);



        //lấy ra foood
        String urlTemplateService = UriComponentsBuilder.fromHttpUrl(apiGetFoot)
                .encode()
                .toUriString();
        HttpEntity<Service[]> listService = restTemplate.exchange(urlTemplateService,
                HttpMethod.GET,
                entity,
                Service[].class);
        model.addAttribute("listService", listService.getBody());
        session.setAttribute("listService", listService.getBody());


        //lấy ra voucher
        String urlTemplateVoucher = UriComponentsBuilder.fromHttpUrl(apiVoucher)
                .queryParam("customerId", customerId)
                .encode()
                .toUriString();
        ResponseEntity<Promotion[]> listVoucher = restTemplate.exchange(urlTemplateVoucher,
                HttpMethod.GET,
                entity,
                Promotion[].class,
                listRequestParam);
        model.addAttribute("listVoucher", listVoucher.getBody());

        //lấy ra thời gian đặt vé
        String urlTemplateGeneralSetting = UriComponentsBuilder.fromHttpUrl(apiGeneralSetting)
                .queryParam("customerId", customerId)
                .encode()
                .toUriString();
        ResponseEntity<GeneralSetting[]> listGeneralSetting = restTemplate.exchange(urlTemplateGeneralSetting,
                HttpMethod.GET,
                entity,
                GeneralSetting[].class,
                listRequestParam);
        model.addAttribute("listGeneralSetting", listGeneralSetting.getBody());


        String urlTemplateSeatType = UriComponentsBuilder.fromHttpUrl(apiGetSeatType)
                .queryParam("customerId", customerId)
                .encode()
                .toUriString();
        ResponseEntity<SeatType[]> listSeatType = restTemplate.exchange(urlTemplateSeatType,
                HttpMethod.GET,
                entity,
                SeatType[].class,
                listRequestParam);
        model.addAttribute("listSeatType", listSeatType.getBody());

        String urlTemplateTicket = UriComponentsBuilder.fromHttpUrl(apiGetTicket)
                .queryParam("movieId", "{movieId}")
                .queryParam("cinemaId", "{cinemaId}")
                .queryParam("startTime", "{startTime}")
                .queryParam("startAt", "{startAt}")
                .queryParam("nameRoom", "{nameRoom}")
                .encode()
                .toUriString();
        ResponseEntity<Ticket[]> listTicket = restTemplate.exchange(urlTemplateTicket,
                HttpMethod.GET,
                entity,
                Ticket[].class,
                listRequestParam);
//        Ticket ticket =  listTicket.getBody()[0];
        model.addAttribute("listTicket", listTicket.getBody());
        return "users/DatVe";
    }

    @GetMapping("/schedule1")
    public String booking1(@RequestParam String movieName,
                           @RequestParam String startAt,
                           @RequestParam String nameRoom,
                           Model model,
                           HttpServletRequest request) {
        HttpSession session = request.getSession();

        session.setAttribute("startAt", request.getParameter("startAt"));
        model.addAttribute("startAt", startAt);

//        System.out.println(start_time);

        Customer customer = (Customer) session.getAttribute("customer");
        model.addAttribute("customer", customer);
        if (customer == null) {
            // Initialize the customer object if it doesn't exist in the session
            customer = new Customer();
            // Set the necessary attributes of the customer
            customer.setId("yourCustomerId");

            session.setAttribute("customer", customer);
        }else {
            Integer listPoint = repository.findPointById(customer.getId());
            model.addAttribute("listPoint", listPoint);
        }
        String customerId = customer.getId();
        HttpEntity<?> entity = new HttpEntity<>(customer);

        //lấy ra lịch chiếu
        String urlTemplateSchdeule = UriComponentsBuilder.fromHttpUrl(apiGetSchedule1)
                .queryParam("movieName", "{movieName}")
                .queryParam("startAt", "{startAt}")
                .queryParam("nameRoom", "{nameRoom}")

                .encode()
                .toUriString();
        Map<String, String> listRequestParam1 = new HashMap<>();
        listRequestParam1.put("movieName", movieName + "");
        listRequestParam1.put("startAt", startAt + "");
        listRequestParam1.put("nameRoom", nameRoom + "");
        ResponseEntity<Schedule[]> listSchedule = restTemplate.exchange(urlTemplateSchdeule,
                HttpMethod.GET,
                entity,
                Schedule[].class,
                listRequestParam1);
        Schedule scheduleDTO =  listSchedule.getBody()[0];
        model.addAttribute("listSchedule", listSchedule.getBody());
        session.setAttribute("schedule", scheduleDTO);

        //lấy ra seat
        String urlTemplateSeat = UriComponentsBuilder.fromHttpUrl(apiGetSeat1)
                .queryParam("movieName", "{movieName}")
                .queryParam("startAt", "{startAt}")
                .queryParam("nameRoom", "{nameRoom}")
                .encode()
                .toUriString();
        ResponseEntity<DtoSeat[]> listSeat = restTemplate.exchange(urlTemplateSeat,
                HttpMethod.GET,
                entity,
                DtoSeat[].class,
                listRequestParam1);

        DtoSeat[] listSeatDTOS = listSeat.getBody();
        Map<Character, List<DtoSeat>> groupedSeats = new HashMap<>();

// Grouping seats by initial letter
        for (DtoSeat seat : listSeatDTOS) {
            char initialLetter = seat.getCode().charAt(0);
            groupedSeats.computeIfAbsent(initialLetter, k -> new ArrayList<>()).add(seat);
        }

// Custom comparator to handle alphanumeric sorting
        Comparator<DtoSeat> seatComparator = (s1, s2) -> {
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
//

        //lấy ra foood
        String urlTemplateService = UriComponentsBuilder.fromHttpUrl(apiGetFoot)
                .encode()
                .toUriString();
        HttpEntity<Service[]> listService = restTemplate.exchange(urlTemplateService,
                HttpMethod.GET,
                entity,
                Service[].class);
        model.addAttribute("listService", listService.getBody());
        session.setAttribute("listService", listService.getBody());


        //lấy ra voucher
        String urlTemplateVoucher = UriComponentsBuilder.fromHttpUrl(apiVoucher)
                .queryParam("customerId", customerId)
                .encode()
                .toUriString();
        ResponseEntity<Promotion[]> listVoucher = restTemplate.exchange(urlTemplateVoucher,
                HttpMethod.GET,
                entity,
                Promotion[].class,
                listRequestParam1);
        model.addAttribute("listVoucher", listVoucher.getBody());

        String urlTemplateGeneralSetting = UriComponentsBuilder.fromHttpUrl(apiGeneralSetting)
                .queryParam("customerId", customerId)
                .encode()
                .toUriString();
        ResponseEntity<GeneralSetting[]> listGeneralSetting = restTemplate.exchange(urlTemplateGeneralSetting,
                HttpMethod.GET,
                entity,
                GeneralSetting[].class,
                listRequestParam1);
        model.addAttribute("listGeneralSetting", listGeneralSetting.getBody());


        String urlTemplateTicket1 = UriComponentsBuilder.fromHttpUrl(apiGetTicket1)
                .queryParam("movieName", "{movieName}")
                .queryParam("startAt", "{startAt}")
                .queryParam("nameRoom", "{nameRoom}")
                .encode()
                .toUriString();
        ResponseEntity<Ticket[]> listTicket = restTemplate.exchange(urlTemplateTicket1,
                HttpMethod.GET,
                entity,
                Ticket[].class,
                listRequestParam1);
//        Ticket ticket =  listTicket.getBody()[0];
        model.addAttribute("listTicket", listTicket.getBody());
        return "users/DatVe";
    }
}
