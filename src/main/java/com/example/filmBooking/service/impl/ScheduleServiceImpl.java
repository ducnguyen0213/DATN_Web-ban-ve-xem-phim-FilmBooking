package com.example.filmBooking.service.impl;

import com.example.filmBooking.model.*;
import com.example.filmBooking.repository.*;
import com.example.filmBooking.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.*;
import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleRepository repository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private GeneralSettingRepository settingRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private SeatTypeRepository seatTypeRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private BillTicketRepository billTicketRepository;

    @Autowired
    private BillServiceRepository billServiceRepository;

    @Override
    public List<Schedule> findAll() {
        return repository.findAllByOrderByStartAtAsc();
    }

    // lấy thông tin cài đặt
    public GeneralSetting findByIdSetting() {
        String id = "hihi";
        GeneralSetting setting = settingRepository.findById(id).get();
        return setting;
    }

    @Override
    public String save(Schedule schedule) {
        List<Schedule> scheduleList = new ArrayList<>();
        //tạo mã suất chiếu
        Random generator = new Random();
        int value = generator.nextInt((100000 - 1) + 1) + 1;
        schedule.setCode("SCD" + value);
        // lấy thông tin phim của suất chiếu
        Movie movie = movieRepository.findById(schedule.getMovie().getId()).get();
        // lấy thông tin phòng chiếu
        Room room = roomRepository.findById(schedule.getRoom().getId()).get();
        //tạo tên suất chiếu = tên phim + tên phòng
        schedule.setName(movie.getName() + "__" + room.getName());
        // tính thời gian kết thúc = thời gian bắt đầu+ thời lượng phim(phút*60000= millisecond) + 900000(15 phút)
        //lấy thời gian bắt đầu
        LocalDateTime startAt = schedule.getStartAt();
        //lấy thời lượng phim( đơn vị phút)
        Integer movieDuration = movie.getMovieDuration();
        Timestamp timestamp = Timestamp.valueOf(startAt);
        timestamp.setTime(timestamp.getTime() + movieDuration * 60000 + findByIdSetting().getBreakTime() * 60000);
        LocalDateTime finishAt = timestamp.toLocalDateTime();
        schedule.setFinishAt(finishAt);
        schedule.setPrice(checkTheDayOfTheWeek(schedule));
        schedule.setOperatingStatus(0);
        if (startAt.isAfter(LocalDateTime.now())) {
            schedule.setStatus("Sắp chiếu");
        } else if (finishAt.isBefore(LocalDateTime.now())) {
            schedule.setStatus("Đã chiếu");
        } else {
            schedule.setStatus("Đang chiếu");
        }
        String id = null;
        if (checkScheduleConflict(schedule, schedule.getRoom().getId()) && timeSchedule(schedule, findByIdSetting().getBusinessHours(), findByIdSetting().getCloseTime()) && dateSchedule(schedule.getMovie().getId(), schedule)) {
            // Lưu suất chiếu mới vào cơ sở dữ liệu
            id = repository.save(schedule).getId();
//            scheduleList.add(schedule);
//            autoSave(id);
            System.out.println("Lưu suất chiếu mới thành công.");
        } else {
            System.out.println("Xung đột suất chiếu hoặc suất chiếu nằm ngoài khoảng ngày chiếu của phim. Không thể lưu.");
            return schedule.getId();
        }
        System.out.println(scheduleList);
        return id;

    }

    @Override
    public Object updateAll(List<Schedule> scheduleList) {
        repository.saveAll(scheduleList);
        for (Schedule schedule : scheduleList
        ) {
            autoSave(schedule);
        }
        return null;
    }

    @Async
    @Scheduled(fixedRate = 60000)
    public void scheduleFixedRate() {
        // danh sách lịch chiếu
        List<Schedule> listSchedule = repository.findAll();
        for (Schedule schedule : listSchedule) {
            //lấy thời gian bắt đầu
            LocalDateTime startAt = schedule.getStartAt();
            //lấy thời gian kết thúc
            LocalDateTime finishAt = schedule.getFinishAt();
            if (schedule.getStatus().equals("Hủy")) {
                continue;
            } else if (startAt.isAfter(LocalDateTime.now()) && schedule.getStatus() != "Hủy") {
                schedule.setStatus("Sắp chiếu");
                repository.save(schedule);
            } else if (finishAt.isBefore(LocalDateTime.now()) && schedule.getStatus() != "Hủy") {
                schedule.setStatus("Đã chiếu");
                repository.save(schedule);
            } else {
                schedule.setStatus("Đang chiếu");
                repository.save(schedule);
            }
        }
    }


    @Override
    public Schedule update(String id, Schedule schedule) {
        Movie movie = movieRepository.findById(schedule.getMovie().getId()).get();
        Schedule scheduleNew = findById(id);
        scheduleNew.setRoom(schedule.getRoom());
        scheduleNew.setMovie(schedule.getMovie());
        scheduleNew.setName(schedule.getName());
        scheduleNew.setStartAt(schedule.getStartAt());
        scheduleNew.setPrice(schedule.getPrice());
        //lấy thời gian bắt đầu
        LocalDateTime startAt = schedule.getStartAt();
        //lấy thời gian kết thúc
        LocalDateTime finishAt = schedule.getFinishAt();
        //lấy thời lượng phim( đơn vị phút)
        Integer movieDuration = movie.getMovieDuration();
        Timestamp timestamp = Timestamp.valueOf(startAt);
//        Set thời gian kết thúc= thời gian bắt đầu+ thời lượng phim(phút*60000= millisecond) + 900000(15 phút)
        timestamp.setTime(timestamp.getTime() + movieDuration * 60000 + findByIdSetting().getBreakTime() * 60000);
        finishAt = timestamp.toLocalDateTime();
        scheduleNew.setFinishAt(finishAt);
        return repository.save(scheduleNew);
    }

    @Override
    public Schedule update1(String id, Schedule schedule) {
        Schedule scheduleNew = findById(id);
        scheduleNew.setPrice(schedule.getPrice());
        scheduleNew.setStatus(schedule.getStatus());
        if (scheduleNew.getStatus().equals("Hủy")) {
//            System.out.println("trạng thái suất chiếu: " + scheduleNew.getStatus());
            updateTicket(id);
            updatePointCustomer(id);
            return repository.save(scheduleNew);
        }
        return null;
    }

    // cập nhật vé khi suất chiếu bị hủy
    private void updateTicket(String scheduleId) {
        List<Ticket> ticketList = new ArrayList<>();
        for (Ticket ticket : ticketRepository.findBySchedule(scheduleId)) {
            ticket.setStatus("Bị hủy do rạp");
            System.out.println("trạng thái vé" + ticket.getStatus());
            ticketList.add(ticket);
        }
        ticketRepository.saveAll(ticketList);
    }

    // trả điểm cho khách hàng khi suất chiếu bị hủy
    private void updatePointCustomer(String scheduleId) {
        List<String> listBillId = repository.findBillByStatusSchedule(scheduleId);
        for (String id : listBillId) {
            //lấy ra hóa đơn
            Bill bill = billRepository.findById(id).get();
            // dodoirr trạng thái bill tổng
            bill.setStatus(2);
            System.out.println("trạng thái bill " + bill.getStatus());
            //đổi trạng thái bill Service
            System.out.println(id);
            List<BillService> listBillService = billServiceRepository.findAllByBill(id);
//            System.out.println(listBillService);
            for (BillService billService : listBillService
            ) {
                billService.setStatus(2);
                System.out.println("trạng thái bill Service: " + billService.getStatus());
                billServiceRepository.save(billService);
            }
            // đổi trạng thái bill ticket
            List<BillTicket> listBillTicket = billTicketRepository.findAllByBill(id);
            for (BillTicket billTicket : listBillTicket
            ) {
                billTicket.setStatus(2);
                System.out.println("trạng thái bill ticket" + billTicket.getStatus());
                billTicketRepository.save(billTicket);
            }
            Customer customer = bill.getCustomer();
            System.out.println(customer.getName() + " điểm cũ: " + customer.getPoint());
            BigDecimal totalMoney = bill.getTotalMoney();
            Integer pointsCompensationPercentage = findByIdSetting().getPointsCompensationPercentage();
            System.out.println("số phần trăm cộng thêm:  " + pointsCompensationPercentage);
            System.out.println("Tổng tiền: " + totalMoney.intValue());
            Integer point = totalMoney.intValue() * pointsCompensationPercentage / 100;
            System.out.println(" số điểm cộng thêm: " + point);
            Integer point2 = customer.getPoint() + point;
            System.out.println("Điểm mới của khách hàng: " + point2);
            customer.setPoint(point2);
            customerRepository.save(customer);
        }
    }

    @Override
    public Schedule findById(String id) {
        return repository.findById(id).get();
    }

    @Override
    public void delete(String id) {
        repository.delete(findById(id));
    }

    //check trùng suất chiếu
    public boolean checkScheduleConflict(Schedule newSchedule, String idRoom) {
        // Lấy ra các suất chiếu của phòng
        List<Schedule> roomSchedules = repository.findByRoom(idRoom);
        for (Schedule schedule : roomSchedules) {
            if ( //thời gian bắt đầu của suất chiếu mới trước thời gian kết thúc của suất chiếu cũ
                    roomSchedules != null && newSchedule.getStartAt().isBefore(schedule.getFinishAt()) &&
                            //thời gian bắt đầu của suất chiếu cũ trước thời gian kết thúc của suất chiếu mới
                            schedule.getStartAt().isBefore(newSchedule.getFinishAt())) {
                return false; // không có xung đột
            }
        }
        return true; // Có xung đột
    }

    //     Lịch chiếu nằm trong khoảng bắt đầu và kết thúc của phim
    public boolean dateSchedule(String movieId, Schedule schedule) {
        // lấy ra khoảng thời gian phim chiếu
        Movie movie = movieRepository.findById(movieId).get();
        //ngày bắt đầu
        Date premiereDate = movie.getPremiereDate();
        //ngày kết thúc
        Date endDate = movie.getEndDate();
        //lấy ngày chiếu
        LocalDateTime startAt = schedule.getStartAt();
        //chuyển từ datetime sang date
        Date scheduleDate = Date.from(startAt.atZone(ZoneId.systemDefault()).toInstant());

        //     Lịch chiếu nằm trong khoảng bắt đầu và kết thúc của phim
        if (premiereDate.before(scheduleDate) && scheduleDate.before(endDate)) {
            return true;
        }
        return false;
    }

    //     Lịch chiếu nằm trong khoảng thời gian mở rạp
    public boolean timeSchedule(Schedule schedule, LocalTime startDay, LocalTime endDay) {
        LocalDateTime startAt = schedule.getStartAt();
        LocalDateTime finishAt = schedule.getFinishAt();
        LocalTime scheduleStart = LocalTime.from(startAt.atZone(ZoneId.systemDefault()));
        LocalTime scheduleFinish = LocalTime.from(finishAt.atZone(ZoneId.systemDefault()));
        if (scheduleStart.isBefore(endDay) && scheduleFinish.isBefore(endDay)) {
            return true;
        } else if (scheduleStart.isAfter(startDay) || scheduleStart.equals(startDay) && scheduleFinish.isAfter(startDay)) {
            return true;
        } else if (scheduleStart.isBefore(endDay) && scheduleFinish.isAfter(startDay)) {
            return true;
        }
        return false;
    }

    //Kiểm tra và tăng giá suất chiếu
    public BigDecimal checkTheDayOfTheWeek(Schedule schedule) {
        BigDecimal fixedPrice = findByIdSetting().getFixedTicketPrice();
        System.out.println(fixedPrice);
        // Lấy mốc thời gian bắt đầu tăng giá trong ngày
        LocalTime timeBeginsToChange = findByIdSetting().getTimeBeginsToChange();
        // Mốc thời gian kết thúc tăng giá
        LocalTime endDay = findByIdSetting().getCloseTime();
        // Lấy tỷ lệ phần trăm tăng giá sau 17 giờ
        double percentDay = (double) findByIdSetting().getPercentDay() / 100;
        BigDecimal priceDay = fixedPrice.multiply(BigDecimal.valueOf(percentDay));
        // Lấy tỷ lệ phần trăm tăng giá cuối tuần
        double percentWeekend = (double) findByIdSetting().getPercentWeekend() / 100;
        BigDecimal priceWeekend = fixedPrice.multiply(BigDecimal.valueOf(percentWeekend));
        BigDecimal newPrice = null;
        // Cho ngày dạng LocalDateTime
//        LocalDateTime currentDate = LocalDateTime.of(2023, 8, 20, 15, 0); // ví dụ 20/08/2023 22:00
        LocalDateTime currentDate = schedule.getStartAt();
        // Kiểm tra xem ngày hiện tại có phải là cuối tuần hay không
        if (currentDate.getDayOfWeek() == DayOfWeek.SATURDAY || currentDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
            // Kiểm tra xem giờ bắt đầu của lịch có lớn hơn hoặc bằng thời gian bắt đầu thay đổi không
            if (currentDate.getHour() >= timeBeginsToChange.getHour() || currentDate.getHour() <= endDay.getHour()) {
                // Thay đổi giá theo tỷ lệ phần trăm tăng giá cuối tuần sau 17 giờ
                newPrice = fixedPrice.add(priceWeekend).add(priceDay);
                System.out.println("Giá vé cuối tuần sau 17:00: " + newPrice);
            } else {
                // Thay đổi giá theo tỷ lệ phần trăm tăng giá cuối tuần
                newPrice = fixedPrice.add(priceWeekend);
                System.out.println("Giá vé cuối tuần: " + newPrice);
            }
        } else {
            // Kiểm tra xem giờ bắt đầu của lịch có lớn hơn hoặc bằng thời gian bắt đầu thay đổi và nhỏ hơn hoặc bằng 2 không
            if (currentDate.getHour() >= timeBeginsToChange.getHour() || currentDate.getHour() <= endDay.getHour()) {
                // Thay đổi giá theo tỷ lệ phần trăm tăng giá sau 17 giờ
                newPrice = fixedPrice.add(priceDay);
                System.out.println("Giá vé sau 17:00: " + newPrice);
            } else {
                // In giá cố định
                newPrice = fixedPrice;
                System.out.println("Giá vé cố định: " + fixedPrice);
            }
        }
        return newPrice;
    }

    @Override
    public List<Schedule> generateSchedule(List<String> listRoom, List<String> listMovieId, LocalDateTime startTime, LocalDateTime endTime) {
        boolean shouldContinue = true;
        for (String roomId : listRoom) {
            Room room = roomRepository.findById(roomId).get();
            LocalDateTime currentStartTime = startTime;
            LocalDateTime currentEndTime = null;
            while (shouldContinue) {
                //xáo trộn danh sách các phim
                Collections.shuffle(listMovieId);
                for (String movieId : listMovieId) {
                    Movie movie = movieRepository.findById(movieId).get();
                    Schedule schedule = new Schedule();
                    schedule.setId(UUID.randomUUID().toString());
                    schedule.setMovie(movie);
                    schedule.setRoom(room);
                    schedule.setStartAt(currentStartTime);
                    long movieDuration = movie.getMovieDuration(); // Thời lượng (tính bằng phút)
//        Set thời gian kết thúc= thời gian bắt đầu+ thời lượng phim(phút*60000= millisecond) + 900000(15 phút)
                    Timestamp timestamp = Timestamp.valueOf(currentStartTime);
                    timestamp.setTime(timestamp.getTime() + movieDuration * 60000 + findByIdSetting().getBreakTime() * 60000);
                    currentEndTime = timestamp.toLocalDateTime();
                    System.out.println(currentEndTime);
                    if (currentEndTime.isAfter(endTime)) {
                        shouldContinue = false;
                        break; // Nếu thời gian kết thúc của suất chiếu sau vượt quá thời gian kết thúc của ngày, thoát khỏi vòng lặp
                    } else {
                        schedule.setFinishAt(currentEndTime);
                        // Lưu vào cơ sở dữ liệu hoặc thực hiện các xử lý khác tại đây
                        currentStartTime = currentEndTime; // Lấy thời gian kết thúc của suất chiếu này làm thời gian bắt đầu cho suất chiếu tiếp theo
                        save(schedule);
                        if (currentEndTime.isBefore(endTime)) {
                            continue; // Nếu thời gian kết thúc của suất chiếu sau vẫn nhỏ hơn thời gian kết thúc của ngày, tiếp tục vòng lặp
                        } else {
                            shouldContinue = false;
                            break; // Nếu không, thoát khỏi vòng lặp
                        }
                    }
                }
            }
            shouldContinue = true;
        }
        return findAll();
    }

    @Override
    public List<String> listSchedule() {
        return repository.listSchedule();
    }

    @Override
    public List<Schedule> getScheduleByName(String name) {
        return repository.findAllByName(name);
    }

    @Override
    public List<String> getStart_At(String movieId, String cinemaId) {
        return repository.getstartAtAndFinishAt(movieId, cinemaId);
    }

    @Override
    public List<Object[]> getStart_At_Time(String movieId, String cinemaId, String start_at) {

        return repository.getTime(movieId, cinemaId, start_at);
    }

    @Override
    public List<Schedule> getSchedule(String movieId, String cinemaId, String startAt, String startTime, String nameRoom) {
        return repository.getSchedule(movieId, cinemaId, startAt, startTime, nameRoom);
    }

    @Override
    public List<Schedule> getSchedule1(String movieName, String startAt, String nameRoom) {
        return repository.getSchedule1(movieName, startAt, nameRoom);
    }

    @Override
    public Pageable pageSchedule(Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1, 20);
        return pageable;
    }

    @Override
    public Page<Schedule> getAll(Integer currentPage) {
        return repository.findAll(pageSchedule(currentPage));
    }

    @Override
    public Page<Schedule> searchSchedule(String name, LocalDate startAt, String movieName, Integer startTime, Integer endTime, String status, Integer currentPage) {
        return repository.searchBySchedule(name, startAt, movieName, startTime, endTime, status, pageSchedule(currentPage));
    }

    public void autoSave(Schedule schedule) {
        List<Ticket> ticketList = new ArrayList<>();
        Room room = roomRepository.findById(schedule.getRoom().getId()).get();
        List<Seat> seats = seatRepository.findAllByRoom(room.getId());
        for (Seat seat : seats) {
            Ticket ticket = new Ticket();
            ticket.setId(UUID.randomUUID().toString());
            Random generator = new Random();
            int value = generator.nextInt((1000 - 1) + 1) + 1;
            ticket.setCode("TK" + value);
            ticket.setSchedule(schedule);
            ticket.setSeat(seat);
            double giaTang= (double) seat.getSeatType().getSurcharge() / 100;
            ticket.setPrice(schedule.getPrice().add((schedule.getPrice().multiply(BigDecimal.valueOf(giaTang)))));
            ticket.setStatus("Chưa bán");
            ticketList.add(ticket);
        }
        ticketRepository.saveAll(ticketList);
    }
    
    @Override
    public List<Schedule> findByDateStartAt(String date) {
        return repository.findByDateStartAt(date);
    }
    
    @Override
    public List<Schedule> getAll() {
        return repository.findAll();
    }
}


