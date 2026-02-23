package com.example.filmBooking.controllerApi;

import com.example.filmBooking.model.*;
import com.example.filmBooking.model.Service;
import com.example.filmBooking.repository.CustomerRepository;
import com.example.filmBooking.repository.GeneralSettingRepository;
import com.example.filmBooking.repository.TicketRepository;
import com.example.filmBooking.service.impl.VNPayService;
import com.example.filmBooking.service.impl.*;
import com.example.filmBooking.util.DisplayFormatUtil;
import com.example.filmBooking.util.EmailHtmlUtil;
import com.google.zxing.BarcodeFormat;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@SessionAttributes("soldTicketsCountBill")

public class VNPAYController {

    @Value("${vnpay.return-url-base:}")
    private String vnpayReturnUrlBase;

    @Value("${spring.mail.username:}")
    private String mailFrom;

    @Value("${app.mail.admin-to:}")
    private String adminEmail;

    @Autowired
    private JavaMailSender mailSender;

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

    @Autowired
    private TemplateEngine templateEngine;

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
                              @RequestParam(value = "seatCodesDon", required = false) String seatCodesDon,
                              @RequestParam(value = "seatCodesDoi", required = false) String seatCodesDoi,
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
        String baseUrl = (vnpayReturnUrlBase != null && !vnpayReturnUrlBase.isBlank())
                ? vnpayReturnUrlBase.trim().replaceAll("/$", "")
                : request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
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

        // Gửi mail sau khi VNPay callback thành công — lưu nội dung vào session
        String thongbao = "không có";
        String thongbaos = "0";
        Date currentTime = new Date();
        String email = customer.getEmail();

        try {
            Context ctx = new Context();
            ctx.setVariable("movieName", nameFiml);
            ctx.setVariable("cinema", roomm);
            ctx.setVariable("showDate", datedate);
            ctx.setVariable("showTime", timetime);
            String seatTypeDisplay = DisplayFormatUtil.formatSeatTypeWithCodes(seatCountCount, seatCodesDon, seatCodesDoi);
            if ((seatCodesDon == null || seatCodesDon.isBlank()) && (seatCodesDoi == null || seatCodesDoi.isBlank()) && seatseat != null && !seatseat.isBlank())
                seatTypeDisplay = seatTypeDisplay + " · Vị trí: " + formatSeatCodes(seatseat);
            ctx.setVariable("seatTypeDisplay", seatTypeDisplay);
            ctx.setVariable("seatCodes", formatSeatCodes(seatseat));
            ctx.setVariable("foodsDisplay", DisplayFormatUtil.formatFoodsForDisplay(ServiceService == null || ServiceService.isEmpty() ? thongbao : ServiceService));
            ctx.setVariable("ticketTotal", priceSeatSeat);
            ctx.setVariable("foodTotal", (priceServiceService == null || priceServiceService.isEmpty()) ? thongbaos : priceServiceService);
            ctx.setVariable("discount", (discountcount == null || discountcount.isEmpty()) ? thongbaos : discountcount);
            ctx.setVariable("finalAmount", formattedPriceVN);
            ctx.setVariable("paymentTime", new SimpleDateFormat("dd/MM/yyyy HH:mm").format(currentTime));
            String html1 = templateEngine.process("emails/payment-success", ctx);
            session1.setAttribute("pendingMailHtml", html1);
            session1.setAttribute("pendingMailSubject", "FilmBooking – Đơn hàng thanh toán thành công");
            session1.setAttribute("pendingMailTo", email);
        } catch (Exception e) {
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
                               @RequestParam(value = "seatCodesDon", required = false) String seatCodesDon,
                               @RequestParam(value = "seatCodesDoi", required = false) String seatCodesDoi,
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
        // Gửi mail từ cấu hình application.properties (Spring Mail)
        String thongbao = "không có";
        String thongbaos = "0";
        Date currentTime = new Date();
        String email = customer.getEmail();
        String adminTo = (adminEmail != null && !adminEmail.isBlank()) ? adminEmail : mailFrom;

        try {
            MimeMessage message = mailSender.createMimeMessage();
            message.setFrom(new InternetAddress(mailFrom));
            message.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("FilmBooking – Đơn hàng đang chờ thanh toán");
            String bodyPending = EmailHtmlUtil.paragraph("Đơn hàng của bạn đang chờ thanh toán. Chi tiết:")
                    + EmailHtmlUtil.row("Tên phim", orderInfor)
                    + EmailHtmlUtil.row("Rạp / Phòng chiếu", room)
                    + EmailHtmlUtil.row("Ngày chiếu", date)
                    + EmailHtmlUtil.row("Giờ chiếu", time)
                    + EmailHtmlUtil.row("Đồ ăn", DisplayFormatUtil.formatFoodsForDisplay(Serviced == null || Serviced.isEmpty() ? thongbao : Serviced))
                    + EmailHtmlUtil.rowHtml("Ghế", buildSeatDisplayForEmail(seatCount, seat, seatCodesDon, seatCodesDoi))
                    + EmailHtmlUtil.row("Tổng tiền vé", priceSeat)
                    + EmailHtmlUtil.row("Tổng tiền đồ ăn", (priceService == null || priceService.isEmpty()) ? thongbaos : priceService)
                    + EmailHtmlUtil.row("Tiền được giảm", (discount == null || discount.isEmpty()) ? thongbaos : discount)
                    + EmailHtmlUtil.row("Thành tiền", formattedPriceVN)
                    + EmailHtmlUtil.row("Mã đơn hàng", transactionCode)
                    + EmailHtmlUtil.row("Thời gian", new SimpleDateFormat("dd/MM/yyyy HH:mm").format(currentTime))
                    + EmailHtmlUtil.paragraph("Đơn hàng đang chờ xác nhận. Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi.");
            String htmlPending = EmailHtmlUtil.wrap("Đơn hàng đang chờ thanh toán", bodyPending, "— FilmBooking");
            message.setContent(htmlPending, "text/html; charset=utf-8");
            mailSender.send(message);

            MimeMessage message2 = mailSender.createMimeMessage();
            message2.setFrom(new InternetAddress(mailFrom));
            message2.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(adminTo));
            message2.setSubject("FilmBooking – Đơn hàng mới chờ xác nhận");
            String detailUrl = "http://localhost:8080/bill/detail/" + bill.getId();
            String bodyAdmin = EmailHtmlUtil.row("Khách hàng", customer.getName())
                    + EmailHtmlUtil.row("Số điện thoại", customer.getPhoneNumber())
                    + EmailHtmlUtil.row("Email", customer.getEmail())
                    + EmailHtmlUtil.row("Mã giao dịch", transactionCode)
                    + EmailHtmlUtil.row("Thời gian", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
                    + EmailHtmlUtil.buttonLink(detailUrl, "Xác nhận đơn hàng");
            String htmlAdmin = EmailHtmlUtil.wrap("Đơn hàng mới chờ xác nhận", bodyAdmin, "— FilmBooking");
            message2.setContent(htmlAdmin, "text/html; charset=utf-8");
            mailSender.send(message2);
        } catch (MessagingException e) {
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
            try {
                String pendingHtml = (String) session.getAttribute("pendingMailHtml");
                String pendingSubject = (String) session.getAttribute("pendingMailSubject");
                String pendingTo = (String) session.getAttribute("pendingMailTo");
                if (pendingHtml != null && pendingTo != null && mailFrom != null && !mailFrom.isBlank()) {
                    MimeMessage message = mailSender.createMimeMessage();
                    message.setFrom(new InternetAddress(mailFrom));
                    message.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(pendingTo));
                    message.setSubject(pendingSubject != null ? pendingSubject : "FilmBooking – Đơn hàng thanh toán thành công");
                    message.setContent(pendingHtml, "text/html; charset=utf-8");
                    mailSender.send(message);
                }
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
            return "users/ordersuccess";
        } else {
            return "users/orderfail";
        }
    }

    /** "(0ghế đơn,2ghế đôi)" → "0 ghế đơn, 2 ghế đôi" */
    private static String formatSeatTypeSummary(String seatCountCount) {
        if (seatCountCount == null || seatCountCount.isBlank()) return "—";
        String s = seatCountCount.trim().replaceAll("^[\\(\\[]+|[\\]\\)]+$", "").trim();
        s = s.replaceAll("(\\d+)(ghế)", "$1 $2");
        return s.replace(",", ", ");
    }

    /** "C6,C5" → "C5, C6" (thêm khoảng trắng, sắp xếp) */
    private static String formatSeatCodes(String seatseat) {
        if (seatseat == null || seatseat.isBlank()) return "—";
        String[] codes = seatseat.split("\\s*,\\s*");
        java.util.Arrays.sort(codes);
        return String.join(", ", codes);
    }

    private static String buildSeatDisplayForEmail(String seatCount, String seat, String seatCodesDon, String seatCodesDoi) {
        String s = DisplayFormatUtil.formatSeatTypeWithCodes(seatCount, seatCodesDon, seatCodesDoi);
        if ((seatCodesDon == null || seatCodesDon.isBlank()) && (seatCodesDoi == null || seatCodesDoi.isBlank()) && seat != null && !seat.isBlank())
            s = s + " · Vị trí: " + formatSeatCodes(seat);
        return s;
    }
}
