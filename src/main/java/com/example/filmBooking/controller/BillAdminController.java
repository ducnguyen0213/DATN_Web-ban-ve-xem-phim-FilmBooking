package com.example.filmBooking.controller;

import com.example.filmBooking.common.ResponseBean;
import com.example.filmBooking.model.*;
import com.example.filmBooking.model.dto.BillDto;
import com.example.filmBooking.repository.BillRepository;
import com.example.filmBooking.repository.BillTicketRepository;
import com.example.filmBooking.repository.CustomerRepository;
import com.example.filmBooking.repository.GeneralSettingRepository;
import com.example.filmBooking.service.BillService;
import com.example.filmBooking.service.BillTicketService;
import com.example.filmBooking.service.CustomerService;
import com.example.filmBooking.service.TicketService;
import com.oracle.wls.shaded.org.apache.xpath.operations.Mod;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.logging.log4j.util.Strings;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.filmBooking.model.dto.DtoBill;
import com.example.filmBooking.model.dto.DtoBillList;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/bill")
@SessionAttributes("soldTicketsCount")
public class BillAdminController {
    @Autowired
    private BillService service;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private BillTicketService billTicketService;

    @Autowired
    private TicketService ticketService;


    @Autowired
    private BillRepository repository;

    @Autowired
    private ModelMapper modelMapper;


    @Autowired
    private GeneralSettingRepository generalSettingRepository;
//    @GetMapping("/find-all/page/{pageNumber}")
//    @Operation(summary = "[Hiển thị tất cả]")
//    public String findAll(te") Date endDate) {
//
//        return "admin/bill";
//    }

    @GetMapping("/xac-nhan")
    public String viewCho(Model model) {
        return "admin/xac-nhan";

    }

    @ModelAttribute("soldTicketsCount")
    public String getSoldTicketsCount(@RequestParam(value = "dateCreate", required = false) LocalDate dateCreate) {
        LocalDate currentDate = LocalDate.now();
        System.out.println(currentDate);
        return repository.countSoldTicketsWithStatusZero(currentDate);
    }

    //
    @GetMapping("/update/{id}")
    public String updateStatus(Model model, @PathVariable(name = "id") String id) {
        Bill bill = service.findById(id);
        bill.setStatus(1);
        Customer customer = customerService.findById(bill.getCustomer().getId());
        Integer percentagePlusPoints = generalSettingRepository.findPercentagePlusPoints();

        BigDecimal phantram = BigDecimal.valueOf(percentagePlusPoints).divide(BigDecimal.valueOf(100));
        BigDecimal orderTotalDecimal = bill.getTotalMoney();
        BigDecimal diemKhachHang = orderTotalDecimal.multiply(phantram);
        customer.setPoint(customer.getPoint() + diemKhachHang.intValue());
        if (bill.getUsepoints() == null){

        }else {
            customer.setPoint(customer.getPoint() - bill.getUsepoints());
        }
        customerService.update(bill.getCustomer().getId(), customer);

        try {
            if (service.update(id, bill) instanceof Bill) {
                String email = bill.getCustomer().getEmail();
                final String username = "ducnguyen1302cat@gmail.com";
                final String password = "vtzo bcyi iefk qtvj"; // Replace <your-password> with your actual password

                Properties props = new Properties();
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", "587");

                Session session = Session.getInstance(props, new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

                try {
                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress("ducnguyen1302cat@gmail.com"));

                    // Populate multiple recipients
                    String[] recipients = {email}; // Replace with actual recipient emails
                    InternetAddress[] recipientAddresses = new InternetAddress[recipients.length];
                    for (int i = 0; i < recipients.length; i++) {
                        recipientAddresses[i] = new InternetAddress(recipients[i]);
                    }
                    message.setRecipients(Message.RecipientType.TO, recipientAddresses);
                    message.setSubject("Đơn hàng đã được xác nhận!");
                    StringBuilder emailContent = new StringBuilder();
                    emailContent.append("Đơn hàng của bạn đã được xác nhận  ").append(LocalDateTime.now()).append("\n");
//                    emailContent.append("Mã bil: ").append(bill.getCode()).append("\n");
                    emailContent.append("Mã giao dịch đơn hàng : ").append(bill.getTradingCode()).append("\n");
                    message.setText(emailContent.toString());

                    Transport.send(message);
                } catch (MessagingException e) {
                    // Handle the exception, for example:
                }
            } else {
                model.addAttribute("thatBai", "Xác nhận hóa đơn thất bại");
            }
            return "redirect:/bill/find-all";
        } catch (Exception e) {
            e.printStackTrace();
            return "admin/xac-nhan";
        }

    }

//    @GetMapping("/delete/{id}")
//    public String delete(@PathVariable("id") String id) {
//        service.delete(id);
//        return "redirect:/bill/xac-nhan";
//    }

    @GetMapping("/detail/{id}")
    public String detailBill(Model model, @PathVariable("id") String id) {
        List<Object[]> detailBill = repository.findBillDetailId(id);
        Map<String, List<Object[]>> groupedBillDetails = detailBill.stream()
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

        model.addAttribute("groupedBillDetails", groupedBillDetails);

        return "admin/chi-tiet-hoa-don";
    }

    @GetMapping("/search/bill")
    public String FindByBill(Model model,
                             @RequestParam(value = "dateCreate", required = false) LocalDate dateCreate,
                             @RequestParam(value = "tradingCode", required = false) String tradingCode,
                             @RequestParam(value = "status", required = false) Integer status) {
//        dateCreate = (dateCreate == null) ? null : dateCreate;
        tradingCode = Strings.isEmpty(tradingCode) ? null : tradingCode;
        List<BillDto> billList = repository.findBillsByTradingCodeAndDate(tradingCode, dateCreate, status).stream().map(bill -> modelMapper.map(bill, BillDto.class)).collect(Collectors.toList());
        model.addAttribute("billList", billList);
        return "admin/viewbill";
    }

    @GetMapping("/find-all")
    public String viewBill() {
        return "admin/bill";
    }


    @GetMapping("/search/bill/xacnhan")
    public String FindByBillCho(Model model,
                                @RequestParam(value = "dateCreate1", required = false) LocalDate dateCreate1,
                                @RequestParam(value = "tradingCode1", required = false) String tradingCode1) {
        tradingCode1 = Strings.isEmpty(tradingCode1) ? null : tradingCode1;

        List<BillDto> billList = repository.findBillsByTradingCodeAndDateCho(tradingCode1, dateCreate1).stream().map(bill -> modelMapper.map(bill, BillDto.class)).collect(Collectors.toList());

        model.addAttribute("billList", billList);
        return "admin/viewbillcho";
    }

    @GetMapping("/hoadonhuy")
    public String viewBillHuy(Model model) {
        return "admin/hoadonhuy";
    }
}
