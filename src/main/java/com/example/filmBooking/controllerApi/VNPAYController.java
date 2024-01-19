package com.example.filmBooking.controllerApi;

import com.example.filmBooking.model.*;
import com.example.filmBooking.model.Service;
import com.example.filmBooking.repository.CustomerRepository;
import com.example.filmBooking.repository.GeneralSettingRepository;
import com.example.filmBooking.repository.TicketRepository;
import com.example.filmBooking.service.impl.VNPayService;
import com.example.filmBooking.service.impl.*;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@SessionAttributes("soldTicketsCountBill")

public class VNPAYController {
    @Autowired
    private VNPayService vnPayService;

    @Autowired
    private BillServiceImpl billService;

    @Autowired
    private BillTicketServiceImpl billTicketService;

    @Autowired
    private BillServiceServiceImpl billServiceService;

    @Autowired
    private TicketRepository ticketService;

    @Autowired
    private PromotionServiceImpl promotionService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private GeneralSettingRepository generalSettingRepository;

    @GetMapping("/index")
    public String home() {
        return "users/orderfail";
    }

    @PostMapping("/submitOrder")
    public String submidOrder(@RequestParam("amount") int orderTotal,
                              @RequestParam("orderInfo") String orderInfo,
                              @RequestParam("nameFiml") String nameFiml,
                              @RequestParam("roomm") String roomm,
                              @RequestParam("datedate") String datedate,
                              @RequestParam("timetime") String timetime,
                              @RequestParam("ServiceService") String ServiceService,
                              @RequestParam("seatseat") String seatseat,
                              @RequestParam("seatCountCount") String seatCountCount,
                              @RequestParam("priceSeatSeat") String priceSeatSeat,
                              @RequestParam("priceServiceService") String priceServiceService,
                              @RequestParam("discountcount") String discountcount,
                              @RequestParam(value = "point", required = false) Integer point,
                              HttpServletRequest request,
                              RedirectAttributes ra,
                              @RequestParam("selectedSeats") List<Ticket> selectedSeats,
                              @RequestParam("priceTicket") BigDecimal priceTicket,
                              @RequestParam(value = "selectedService", required = false) List<Service> selectedService,
                              @RequestParam(value = "selectedQuantity", required = false) List<Integer> selectedQuantity,
                              @RequestParam(value = "selectedPrice", required = false) List<BigDecimal> selectedPrice,
                              @RequestParam(value = "selectedPromition", required = false) Promotion selectedPromition) {
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayUrl = vnPayService.createOrder(orderTotal, orderInfo, baseUrl);
        HttpSession session = request.getSession();
        Schedule schedule = (Schedule) session.getAttribute("schedule");
        BigDecimal orderTotalDecimal = new BigDecimal(orderTotal);
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
        String formattedPriceVN = currencyVN.format(orderTotal);
//        System.out.println(selectedPromition.);
        HttpSession session1 = request.getSession();
        String roomName = schedule.getRoom().getName();
        String formattedRoomName = null;
        try {
            formattedRoomName = URLEncoder.encode(roomName, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //        check xem vé được bán chưa
        for (Ticket ticket : selectedSeats) {
            String status = ticket.getStatus();
            if (status.equalsIgnoreCase("đã bán")) {
                // Display message if the ticket status is "sold"
                String message = "Có người nhanh tay hơn đã chọn vào ghế mà bạn đã chọn, vui lòng chọn lại chỗ ngồi!";
                ra.addFlashAttribute("Message", message);
                return "redirect:/show/booking/schedule?movieId=" + schedule.getMovie().getId() +
                        "&cinemaId=" + schedule.getRoom().getCinema().getId() + "&startTime=" +
                        schedule.getStartAt().format(DateTimeFormatter.ofPattern("HH:mm")) + "&nameRoom=" +
                        formattedRoomName + "&startAt=" +
                        schedule.getStartAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            }
        }

//      thêm bill
        Bill bill = new Bill();
        bill.setStatus(1);
        bill.setDateCreate(LocalDateTime.now());

        Customer customer = (Customer) session.getAttribute("customer");
        bill.setCustomer(customer);
        bill.setTotalMoney(orderTotalDecimal);
        System.out.println(point);
        Integer percentagePlusPoints = generalSettingRepository.findPercentagePlusPoints();

        BigDecimal phantram = BigDecimal.valueOf(percentagePlusPoints).divide(BigDecimal.valueOf(100));

        BigDecimal diemKhachHang = orderTotalDecimal.multiply(phantram);
        System.out.println("điểm cộng"+ diemKhachHang);
        System.out.println("điểm sẵn"+ customer.getPoint());

        customer.setPoint(customer.getPoint() + diemKhachHang.intValue());

        bill.setPoint(diemKhachHang.intValue());
        bill.setUsepoints(point);
        if (point == null) {
            // Xử lý khi selectedService là null hoặc rỗng
        } else {
            customer.setPoint(customer.getPoint() - point);
        }

        if (selectedPromition == null) {

        } else {
            bill.setPromotion(selectedPromition);
            selectedPromition.setQuantity(selectedPromition.getQuantity() - 1);
        }
        session1.setAttribute("bill", bill);
        session1.setAttribute("selectedPromition", selectedPromition);
        session1.setAttribute("customer", customer);

//thêm bill_ticket
        List<BillTicket> billTickets = new ArrayList<>();
        List<Ticket> tickets = new ArrayList<>();
        for (Ticket ticket : selectedSeats) {
            // Lưu seatId vào cơ sở dữ liệu
            BillTicket billTicket = new BillTicket();
            billTicket.setBill(bill);
            billTicket.setTotalMoney(priceTicket);
            billTicket.setStatus(0);
            billTicket.setTicket(ticket);
            billTickets.add(billTicket);
            tickets.add(ticket);
            session1.setAttribute("billTickets", billTickets);
            session1.setAttribute("tickets", tickets);
        }
////thêm bill_Service
        List<BillService> billServices = new ArrayList<>();
        if (selectedService == null || selectedService.isEmpty()) {
            // Xử lý khi selectedService là null hoặc rỗng
        } else {
            for (int i = 0; i < selectedService.size() && i < selectedQuantity.size() && i < selectedPrice.size(); i++) {
                Service Service = selectedService.get(i);
                Integer quantity = selectedQuantity.get(i);
                BigDecimal price = selectedPrice.get(i);
                BillService billService = new BillService();

                billService.setBill(bill);
                billService.setStatus(0);
                billService.setService(Service);
                billService.setQuantity(quantity);
                billService.setTotalMoney(price);
//            billServiceService.save(billService);
                billServices.add(billService);
                session1.setAttribute("billServices", billServices);
            }
        }

        //        gửi về mail
        String thongbao = "không có";
        String thongbaos = "0";
        Date currentTime = new Date();

        String email = customer.getEmail();
        final String username = "ducnguyen1302cat@gmail.com";
        final String password = "vtzo bcyi iefk qtvj"; // Replace <your-password> with your actual password

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session2 = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session2);
            message.setFrom(new InternetAddress("ducnguyen1302cat@gmail.com"));

            // Populate multiple recipients
            String[] recipients = {email}; // Replace with actual recipient emails
            InternetAddress[] recipientAddresses = new InternetAddress[recipients.length];
            for (int i = 0; i < recipients.length; i++) {
                recipientAddresses[i] = new InternetAddress(recipients[i]);
            }
            message.setRecipients(Message.RecipientType.TO, recipientAddresses);
            StringBuilder emailContent = new StringBuilder();
            message.setSubject("FilmBooking_Thông tin đơn hàng của bạn(Đơn hàng thanh toán thành công)");
            emailContent.append("Tên phim : ").append(nameFiml).append("\n");
            emailContent.append("Rạp/Phòng chiếu : ").append(roomm).append("\n");
            emailContent.append("Ngày chiếu : ").append(datedate).append("\n");
            emailContent.append("Giờ chiếu : ").append(timetime).append("\n");
            if (ServiceService == null || ServiceService.isEmpty()) {
                emailContent.append("Đồ ăn : ").append(thongbao).append("\n");
            } else {
                emailContent.append("Đồ ăn : ").append(ServiceService).append("\n");
            }
            emailContent.append("Ghế : ").append("(" + seatCountCount + ")" + seatseat).append("\n");
            emailContent.append("Tổng tiền vé : ").append(priceSeatSeat).append("\n");
            if (priceServiceService == null || priceServiceService.isEmpty()) {
                emailContent.append("Tổng tiền đồ ăn : ").append(thongbaos).append("\n");
            } else {
                emailContent.append("Tổng tiền đồ ăn : ").append(priceServiceService).append("\n");
            }
            if (discountcount == null || discountcount.isEmpty()) {
                emailContent.append("Tiền được giảm : ").append(thongbaos).append("\n");
            } else {
                emailContent.append("Tiền được giảm : ").append(discountcount).append("\n");
            }
            emailContent.append("Thành tiền : ").append(formattedPriceVN).append("\n");
            emailContent.append("Thời gian thanh toán : ").append(currentTime).append("\n");
            emailContent.append("Đơn hàng của bạn được đặt thành công! Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi.");
            message.setText(emailContent.toString());
//            Transport.send(message);
            session1.setAttribute("message", message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        return "redirect:" + vnpayUrl;
    }

    @PostMapping("/filmbooking/thongtinthanhtoan")
    public String submidOrder1(@RequestParam("amount1") int orderTotal,
                               @RequestParam("orderInfor") String orderInfor,
                               @RequestParam("room") String room,
                               @RequestParam("date") String date,
                               @RequestParam("time") String time,
                               @RequestParam("Serviced") String Serviced,
                               @RequestParam("seat") String seat,
                               @RequestParam("seatCount") String seatCount,
                               @RequestParam("priceSeat") String priceSeat,
                               @RequestParam("priceService") String priceService,
                               @RequestParam("discount") String discount,
                               Model model,
                               HttpServletRequest request,
                               RedirectAttributes ra,
                               @RequestParam("selectedSeats1") List<Ticket> selectedSeats1,
                               @RequestParam("priceTicket1") BigDecimal priceTicket1,
                               @RequestParam(value = "selectedService1", required = false) List<Service> selectedService1,
                               @RequestParam(value = "selectedQuantity1", required = false) List<Integer> selectedQuantity1,
                               @RequestParam(value = "selectedPrice1", required = false) List<BigDecimal> selectedPrice1,
                               @RequestParam(value = "selectedPromition1", required = false) Promotion selectedPromition1, @RequestParam(value = "point", required = false) Integer point,
                               @RequestParam(value = "pointt", required = false) Integer pointt) {

        HttpSession session = request.getSession();
        Customer customer = (Customer) session.getAttribute("customer");
        Schedule schedule = (Schedule) session.getAttribute("schedule");
        model.addAttribute("customer", customer);
        String transactionCode = generateTransactionCode();
        BigDecimal orderTotalDecimal = new BigDecimal(orderTotal);
        String thongtin = "Giao dịch được thực hiện tại Web FimlBooking";
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
        String formattedPriceVN = currencyVN.format(orderTotal);
        model.addAttribute("orderId", thongtin);
        model.addAttribute("totalPrice", formattedPriceVN);
        model.addAttribute("paymentTime", LocalDateTime.now());
        model.addAttribute("transactionId", transactionCode);
        String roomName = schedule.getRoom().getName();
        String formattedRoomName = null;
        try {
            formattedRoomName = URLEncoder.encode(roomName, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
//        check xem vé được bán chưa
        for (Ticket ticket : selectedSeats1) {
            String status = ticket.getStatus();
            if (status.equalsIgnoreCase("đã bán")) {
                // Display message if the ticket status is "sold"
                String message = "Có người nhanh tay hơn đã chọn vào ghế mà bạn đã chọn, vui lòng chọn lại chỗ ngồi!";
                ra.addFlashAttribute("Message", message);
                return "redirect:/show/booking/schedule?movieId=" + schedule.getMovie().getId() +
                        "&cinemaId=" + schedule.getRoom().getCinema().getId() + "&startTime=" +
                        schedule.getStartAt().format(DateTimeFormatter.ofPattern("HH:mm")) + "&nameRoom=" +
                        formattedRoomName + "&startAt=" +
                        schedule.getStartAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            }
        }
//thêm bill
        Bill bill = new Bill();

//        Customer customer1 = new Customer();
        bill.setStatus(0);
        bill.setDateCreate(LocalDateTime.now());
        bill.setCustomer(customer);
        bill.setTotalMoney(orderTotalDecimal);
        bill.setTradingCode(transactionCode);

        Integer percentagePlusPoints = generalSettingRepository.findPercentagePlusPoints();

        BigDecimal phantram = BigDecimal.valueOf(percentagePlusPoints).divide(BigDecimal.valueOf(100));

        BigDecimal diemKhachHang = orderTotalDecimal.multiply(phantram);
        bill.setPoint(diemKhachHang.intValue());
        bill.setUsepoints(pointt);

        if (selectedPromition1 == null) {
            // Xử lý khi selectedService là null hoặc rỗng
        } else {
            Promotion promotion = promotionService.findById(selectedPromition1.getId());
            bill.setPromotion(selectedPromition1);
            promotion.setQuantity(promotion.getQuantity() - 1);
            promotionService.save(selectedPromition1);
        }

        Bill createdBill = billService.save(bill);
        customerRepository.save(customer);


//thêm bill_ticket

        for (Ticket ticket : selectedSeats1) {
            // Lưu seatId vào cơ sở dữ liệu
            BillTicket billTicket = new BillTicket();
            billTicket.setBill(createdBill);
            billTicket.setTotalMoney(priceTicket1);
            billTicket.setStatus(0);
            billTicket.setTicket(ticket);
            billTicketService.save(billTicket);
            ticket.setStatus("đã bán");
            ticketService.save(ticket);
        }
////thêm bill_Service
        if (selectedService1 == null || selectedService1.isEmpty()) {
            // Xử lý khi selectedService là null hoặc rỗng
        } else {
            for (int i = 0; i < selectedService1.size() && i < selectedQuantity1.size() && i < selectedPrice1.size(); i++) {
                Service Service = selectedService1.get(i);
                Integer quantity = selectedQuantity1.get(i);
                BigDecimal price = selectedPrice1.get(i);
                BillService billService = new BillService();

                billService.setBill(createdBill);
                billService.setStatus(0);
                billService.setService(Service);
                billService.setQuantity(quantity);
                billService.setTotalMoney(price);
                billServiceService.save(billService);

            }
        }
//        gửi về mail
        String thongbao = "không có";
        String thongbaos = "0";
        Date currentTime = new Date();

        String email = customer.getEmail();
        final String username = "ducnguyen1302cat@gmail.com";
        final String password = "vtzo bcyi iefk qtvj"; // Replace <your-password> with your actual password

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session1 = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session1);
            message.setFrom(new InternetAddress("ducnguyen1302cat@gmail.com"));

            // Populate multiple recipients
            String[] recipients = {email}; // Replace with actual recipient emails
            InternetAddress[] recipientAddresses = new InternetAddress[recipients.length];
            for (int i = 0; i < recipients.length; i++) {
                recipientAddresses[i] = new InternetAddress(recipients[i]);
            }
            message.setRecipients(Message.RecipientType.TO, recipientAddresses);
            StringBuilder emailContent = new StringBuilder();
            message.setSubject("Thông tin đơn hàng của bạn(Đơn hàng đang chờ thanh toán)");
            emailContent.append("Tên phim : ").append(orderInfor).append("\n");
            emailContent.append("Rạp/Phòng chiếu : ").append(room).append("\n");
            emailContent.append("Ngày chiếu : ").append(date).append("\n");
            emailContent.append("Giờ chiếu : ").append(time).append("\n");
            if (Serviced == null || Serviced.isEmpty()) {
                emailContent.append("Đồ ăn : ").append(thongbao).append("\n");
            } else {
                emailContent.append("Đồ ăn : ").append(Serviced).append("\n");
            }
            emailContent.append("Ghế : ").append("(" + seatCount + ")" + seat).append("\n");
            emailContent.append("Tổng tiền vé : ").append(priceSeat).append("\n");
            if (priceService == null || priceService.isEmpty()) {
                emailContent.append("Tổng tiền đồ ăn : ").append(thongbaos).append("\n");
            } else {
                emailContent.append("Tổng tiền đồ ăn : ").append(priceService).append("\n");
            }
            if (discount == null || discount.isEmpty()) {
                emailContent.append("Tiền được giảm : ").append(thongbaos).append("\n");
            } else {
                emailContent.append("Tiền được giảm : ").append(discount).append("\n");
            }
            emailContent.append("Thành tiền : ").append(formattedPriceVN).append("\n");
            emailContent.append("Mã đơn hàng : ").append(transactionCode).append("\n");
            emailContent.append("Thời gian thanh toán : ").append(currentTime).append("\n");

            emailContent.append("Đơn hàng của bạn đang chờ xác nhận! Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi.");
            message.setText(emailContent.toString());
            Transport.send(message);
// Create and send the second email
            Message message2 = new MimeMessage(session1);
            message2.setFrom(new InternetAddress("ducnguyen1302cat@gmail.com"));
            message2.setRecipients(Message.RecipientType.TO, InternetAddress.parse("ducnguyen1302cat@gmail.com")); // Replace with the second recipient's email
            message2.setSubject("Có đơn hànng mới chờ xác nhận!");
            StringBuilder emailContent1 = new StringBuilder();
            emailContent1.append("Khách hàng : ").append(customer.getName()).append("\n");
            emailContent1.append("Số điện thoại : ").append(customer.getPhoneNumber()).append("\n");
            emailContent1.append("Email : ").append(customer.getEmail()).append("\n");
            emailContent1.append("Mã giao dịch : ").append(transactionCode).append("\n");
            emailContent1.append("Đơn hàng thanh toán lúc: ").append(LocalDateTime.now()).append("\n");
            emailContent1.append("Xác nhận đơn hàng:  ").append("http://localhost:8080/bill/detail/").append(bill.getId());
            message2.setText(emailContent1.toString());
            Transport.send(message2);
        } catch (
                MessagingException e) {
            throw new RuntimeException(e);
        }
        return "users/ordersuccessfull";
    }

    public static String generateTransactionCode() {
        Random random = new Random();
        StringBuilder transactionCode = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            transactionCode.append(random.nextInt(10)); // Sinh số ngẫu nhiên từ 0 đến 9 và thêm vào chuỗi
        }
        return transactionCode.toString();
    }

    @GetMapping("/vnpay-payment")
    public String GetMapping(HttpServletRequest request, Model model) {
        int paymentStatus = vnPayService.orderReturn(request);
        HttpSession session = request.getSession();
        Customer customer = (Customer) session.getAttribute("customer");
        model.addAttribute("customer", customer);
        HttpEntity<?> entity = new HttpEntity<>(customer);
        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");

        model.addAttribute("orderId", orderInfo);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("paymentTime", paymentTime);
        model.addAttribute("transactionId", transactionId);

        if (paymentStatus == 1) {
//            HttpSession session = request.getSession();
            Bill bill = (Bill) session.getAttribute("bill");
            Customer customerr = (Customer) session.getAttribute("customer");
            Promotion promotion = (Promotion) session.getAttribute("selectedPromition");
            List<BillTicket> billTickets = (List<BillTicket>) session.getAttribute("billTickets");
            List<BillService> billServices = (List<BillService>) session.getAttribute("billServices");
            List<Ticket> tickets = (List<Ticket>) session.getAttribute("tickets");
// Lưu đối tượng Bill vào cơ sở dữ liệu
            bill.setTradingCode(transactionId);
            billService.save(bill);
            customerRepository.save(customerr);
            if (promotion == null) {
                // Xử lý khi selectedService là null hoặc rỗng
            } else {
                promotionService.save(promotion);
            }
            for (BillTicket billTicket : billTickets) {
                billTicketService.save(billTicket);
            }
            for (Ticket ticket : tickets) {
                ticket.setStatus("đã bán");
                ticketService.save(ticket);
            }
            if (billServices == null || billServices.isEmpty()) {
                // Xử lý khi selectedService là null hoặc rỗng
            } else {
                for (BillService billService : billServices) {
                    billServiceService.save(billService);
                }
            }
//
            try {
                Message message = (Message) session.getAttribute("message");
                Transport.send(message);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
            return "users/ordersuccess";
        } else {
            return "users/orderfail";
        }
    }
}
