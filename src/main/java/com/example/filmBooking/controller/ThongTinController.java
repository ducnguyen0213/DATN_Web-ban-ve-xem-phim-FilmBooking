package com.example.filmBooking.controller;

import com.example.filmBooking.apis.Api;
import com.example.filmBooking.model.Bill;
import com.example.filmBooking.model.Customer;
import com.example.filmBooking.model.RankCustomer;
import com.example.filmBooking.repository.BillRepository;
import com.example.filmBooking.repository.CustomerRepository;
import com.example.filmBooking.repository.ScheduleRepository;
import com.example.filmBooking.service.CustomerService;
import com.example.filmBooking.service.impl.CustomerServiceImpl;
import com.example.filmBooking.service.impl.RankCustomerServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/filmbooking")
@SessionAttributes("soldTicketsCountBill")

public class ThongTinController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private CustomerRepository repository;

    @Autowired
    private RankCustomerServiceImpl rankCustomerService;
//    public static String apiGetCinema = Api.baseURL + "/api/ticket/show/test";

    @GetMapping("/thongtincanhan")
    public String showThongTinCaNhan(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Customer customerSession = (Customer) session.getAttribute("customer");


        Customer customer = customerService.findByEmail(customerSession.getEmail());
        if (customer == null) {
            return "redirect:/filmbooking/login";
        }
        List<Object[]> listCustomer = repository.getCustommerById(customer.getId());
        List<Integer> rankCustomerList = repository.getPoint(customer.getId());

        String soldTicketsCountBill = billRepository.countSoldTicket(customer.getId());
//        System.out.println(soldTicketsCountBill);
        List<Object[]> listBill = billRepository.findBillDetailsByCustomer(customer.getId());
        Map<String, List<Object[]>> groupedBillDetails = listBill.stream()
                .collect(Collectors.groupingBy(bill -> (String) bill[0])); // Assuming the transaction ID is at index 0 in the Object array

// Separate unique and duplicate records
        Map<String, List<Object[]>> uniqueRecords = new HashMap<>();
        Map<String, List<Object[]>> duplicateRecords = new HashMap<>();
        groupedBillDetails.forEach((transactionId, details) -> {
            if (details.size() > 1) {
                duplicateRecords.put(transactionId, details);
            } else {
                uniqueRecords.put(transactionId, details);
            }
        });
        uniqueRecords.forEach((transactionId, details) -> {
            details.forEach(detail -> System.out.println(Arrays.toString(detail)));
        });

        duplicateRecords.forEach((transactionId, details) -> {
            details.forEach(detail -> System.out.println(Arrays.toString(detail)));
        });


// bill chờ
        List<Object[]> listBillCho = billRepository.findBillDetailsByCustomerCho(customer.getId());
        Map<String, List<Object[]>> groupedBillDetailsCho = listBillCho.stream()
                .collect(Collectors.groupingBy(bill -> (String) bill[0])); // Assuming the transaction ID is at index 0 in the Object array


        Map<String, List<Object[]>> uniqueRecordsCho = new HashMap<>();
        Map<String, List<Object[]>> duplicateRecordsCho = new HashMap<>();
        groupedBillDetailsCho.forEach((transactionId, details) -> {
            if (details.size() > 1) {
                uniqueRecordsCho.put(transactionId, details);
            } else {
                duplicateRecordsCho.put(transactionId, details);
            }
        });
        uniqueRecordsCho.forEach((transactionId, details) -> {
            details.forEach(detail -> System.out.println(Arrays.toString(detail)));
        });

        duplicateRecordsCho.forEach((transactionId, details) -> {
            details.forEach(detail -> System.out.println(Arrays.toString(detail)));
        });
        // bill huy
        List<Object[]> listBillHuy = billRepository.findBillDetailsByCustomerHuy(customer.getId());
        Map<String, List<Object[]>> groupedBillDetailsHuy = listBillHuy.stream()
                .collect(Collectors.groupingBy(bill -> (String) bill[0])); // Assuming the transaction ID is at index 0 in the Object array


        Map<String, List<Object[]>> uniqueRecordsHuy = new HashMap<>();
        Map<String, List<Object[]>> duplicateRecordsHuy = new HashMap<>();
        groupedBillDetailsCho.forEach((transactionId, details) -> {
            if (details.size() > 1) {
                uniqueRecordsHuy.put(transactionId, details);
            } else {
                duplicateRecordsHuy.put(transactionId, details);
            }
        });
        uniqueRecordsHuy.forEach((transactionId, details) -> {
            details.forEach(detail -> System.out.println(Arrays.toString(detail)));
        });

        duplicateRecordsHuy.forEach((transactionId, details) -> {
            details.forEach(detail -> System.out.println(Arrays.toString(detail)));
        });
        model.addAttribute("customer", customer);
        model.addAttribute("listCustomer", listCustomer);
        model.addAttribute("rankCustomerList", rankCustomerList);
        model.addAttribute("groupedBillDetails", groupedBillDetails);
        model.addAttribute("groupedBillDetailsCho", groupedBillDetailsCho);
        model.addAttribute("groupedBillDetailsHuy", groupedBillDetailsHuy);
        model.addAttribute("soldTicketsCountBill", soldTicketsCountBill);
        return "users/ThongTinCaNhan";
    }

    @PostMapping("/update-thongtin/{id}")
    public String updateAcc(
            @PathVariable(name = "id") String id,
            @RequestParam(name = "name") String name,
            @RequestParam(name = "email") String email,
            @RequestParam(name = "phoneNumber") String phoneNumber) {

        Customer customer = customerService.findById(id);
        if (customer != null) {
            customer.setEmail(email);
            customer.setName(name);
            customer.setPhoneNumber(phoneNumber);
//            customer.setPassword(password);
            customerService.update(customer.getId(), customer);
        } else {
            System.out.println("không tìm thấy account");
        }

        return "redirect:/thongtincanhan";
    }


}