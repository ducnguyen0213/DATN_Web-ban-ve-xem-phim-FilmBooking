package com.example.filmBooking.service.impl;


import com.example.filmBooking.model.Room;
import com.example.filmBooking.model.Seat;
import com.example.filmBooking.model.SeatType;
import com.example.filmBooking.model.Ticket;
import com.example.filmBooking.model.dto.DtoSeat;
import com.example.filmBooking.model.dto.SeatDTO;
import com.example.filmBooking.repository.*;
import com.example.filmBooking.service.RoomService;
import com.example.filmBooking.service.SeatService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;


@Service
public class SeatServiceImpl implements SeatService {
    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private SeatTypeRepository seatTypeRepository;

    @Autowired
    private RoomServiceImpl roomServiceI;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<Seat> getAll() {
        return seatRepository.findAll();
    }

    @Override
    public List<Seat> findAllByRoom(String roomId) {
        return seatRepository.findAllByRoom(roomId);
    }

    @Override
    public Seat save(List<String> listLineCodes, List<String> listSeatTypeId, List<Integer> listNumberOfSeatPerLine, String roomId) {
        List<Seat> seatList = new ArrayList<>();
        for (int i = 0; i < listLineCodes.size(); i++) {
            for (int j = 1; j <= listNumberOfSeatPerLine.get(i); j++) {
                Seat seat = new Seat();
                seat.setId(UUID.randomUUID().toString());
                seat.setLine(listLineCodes.get(i));
                seat.setNumber(j);
                seat.setCode(seat.getLine() + seat.getNumber());
                seat.setRoom(roomRepository.findById(roomId).get());
                seat.setSeatType(seatTypeRepository.findById(listSeatTypeId.get(i)).get());
                seat.setStatus(0);
                seatList.add(seat);
            }
        }
        seatRepository.saveAll(seatList);
        return null;
    }

    @Override
    public Seat update(String id, Seat seat) {
        Seat seatNew = findById(id);
        seatNew.setStatus(seat.getStatus());
        seatNew.setSeatType(seat.getSeatType());
        return seatRepository.save(seatNew);
    }

    @Override
    public void delete(String id) {

        seatRepository.delete(findById(id));
    }

    @Override
    public Seat findById(String id) {
        return seatRepository.findById(id).get();
    }

    @Override
    public List<SeatDTO> getSeatsByScheduleId(String scheduleId) {
        Room room = scheduleRepository.getById(scheduleId).getRoom();

        List<Seat> listSeat = seatRepository.getSeatByRoomId(room.getId());

        List<Seat> occupiedSeats = ticketRepository.findTicketByScheduleId(scheduleId)
                .stream().map(ticket -> ticket.getSeat())
                .collect(Collectors.toList());
        List<SeatDTO> filteredSeats = listSeat.stream().map(seat -> {
                    SeatDTO seatDTO = modelMapper.map(seat, SeatDTO.class);
                    if (occupiedSeats.stream()
                            .map(occupiedSeat -> occupiedSeat.getId())
                            .collect(Collectors.toList()).contains(seat.getId())) {
                        seatDTO.setIsOccupied(1);
                    }
                    return seatDTO;
                }
        ).collect(Collectors.toList());
        return filteredSeats;
    }

    @Override
    public List<Object[]> getSeatsByCustomerId(String customerId) {
        return seatRepository.findSeatsByCustomerId(customerId);
    }

    @Override
    public List<DtoSeat> getSeats(String cinemaId, String movieId, String startAt, String startTime, String nameRoom) {

        List<Seat> listSeat = seatRepository.getSeat(cinemaId, movieId, startAt, startTime, nameRoom);
// Lấy ra các vé đã được đặt trong lịch đó rồi map sang các chỗ ngồi
        List<Seat> occupiedSeats = ticketRepository.findTicketsBySchedule_Id(cinemaId, movieId, startAt, startTime, nameRoom)
                .stream().map(ticket -> ticket.getSeat())
                .collect(Collectors.toList());
// Lấy ra các vé
        List<Ticket> tickets = ticketRepository.ticketShow(cinemaId, movieId, startAt, startTime, nameRoom);
//        System.out.println(tickets);
//        tickets.forEach(ticket -> {
//            String ticketId = ticket.getId();
//            System.out.println("Ticket ID: " + ticketId);
//        });
        // Map list chỗ ngồi của phòng ở bước 1 sang list dto
        List<DtoSeat> filteredSeats = listSeat.stream().map(seat -> {
            DtoSeat dtoSeat = modelMapper.map(seat, DtoSeat.class);

            if (occupiedSeats.stream()
                    .map(occupiedSeat -> occupiedSeat.getId())
                    .collect(Collectors.toList()).contains(seat.getId())) {
                dtoSeat.setIsOccupied("1"); // Nếu ghế nào nằm trong list ghế đã được occupied thì set = 1
            }
//            List<Integer> ticketIdsWithSeat = new ArrayList<>();

            for (Ticket ticket : tickets) {
                if (ticket.getSeat().getId() == seat.getId()) {
                    dtoSeat.setTicketId(ticket.getId());
                }
            }
            return dtoSeat;
        }).collect(Collectors.toList());

        return filteredSeats;
    }

    @Override
    public List<DtoSeat> getSeats1(String movieName, String startAt, String nameRoom) {

        List<Seat> listSeat = seatRepository.getSeat1(movieName, startAt, nameRoom);
// Lấy ra các vé đã được đặt trong lịch đó rồi map sang các chỗ ngồi
        List<Seat> occupiedSeats = ticketRepository.findTicketsBySchedule_Id1(movieName, startAt, nameRoom)
                .stream().map(ticket -> ticket.getSeat())
                .collect(Collectors.toList());

        List<Ticket> tickets = ticketRepository.ticketShow1(movieName, startAt, nameRoom);

        // Map list chỗ ngồi của phòng ở bước 1 sang list dto
        List<DtoSeat> filteredSeats = listSeat.stream().map(seat -> {
            DtoSeat seatDTO = modelMapper.map(seat, DtoSeat.class);
            if (occupiedSeats.stream()
                    .map(occupiedSeat -> occupiedSeat.getId())
                    .collect(Collectors.toList()).contains(seat.getId())) {
                seatDTO.setIsOccupied("1"); // Nếu ghế nào nằm trong list ghế đã được occupied thì set = 1
            }
            for (Ticket ticket : tickets) {
                if (ticket.getSeat().getId() == seat.getId()) {
                    seatDTO.setTicketId(ticket.getId());
                }
            }
            return seatDTO;
        }).collect(Collectors.toList());

        return filteredSeats;
    }

    @Override
    public Page<Seat> findAll(Integer currentPage) {
        return seatRepository.findAll(pageSeat(currentPage));
    }

    @Override
    public List<Seat> listSeat(String roomName) {
        return seatRepository.getSeatByRoom(roomName);
    }

    @Override
    public Pageable pageSeat(Integer pagaNumber) {
        Pageable pageable = PageRequest.of(pagaNumber - 1, 8);
        return pageable;
    }

    @Override
    public Page<Seat> searchByRoom(String id, Integer currentPage) {
        return seatRepository.searchByRoom(id, pageSeat(currentPage));
    }

    @Override
    public Seat readExcel(MultipartFile file, Room roomId) {
        Room room = new Room();
        try {
            InputStream excelFile = file.getInputStream();
            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            DataFormatter fmt = new DataFormatter();
            int totalSeatCount = 1;
            for (int rowNum = 1; rowNum <= datatypeSheet.getLastRowNum(); rowNum++) {
                Row currentRow = datatypeSheet.getRow(rowNum);

                if (currentRow == null) {
                    // Bỏ qua dòng trống
                    continue;
                }

                for (int colNum = 1; colNum < currentRow.getLastCellNum(); colNum++) {
                    Cell cell = currentRow.getCell(colNum, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    String cellValue = fmt.formatCellValue(cell);

                    if (!cellValue.trim().isEmpty()) {
                        // Giả sử thông tin ghế cần có ở đây
                        Color backgroundColor = cell.getCellStyle().getFillForegroundColorColor();

                        if (backgroundColor instanceof XSSFColor) {
                            byte[] rgb = ((XSSFColor) backgroundColor).getRGB();
                            String hexColor = String.format("#%02X%02X%02X", rgb[0], rgb[1], rgb[2]);

                            // Xác định seat_type dựa trên mã màu
                            String seatTypeId = determineSeatTypeByColor(hexColor);
                            Seat seat = new Seat();

                            // Xác định 'line' từ chỉ mục dòng
                            seat.setLine(Character.toString((char) ('A' + rowNum - 1)));
                            // Xác định 'number' từ chỉ mục cột
                            seat.setNumber(colNum);
//                            Room roomIdd = roomRepository.findById("roomId").orElse(null);

                            // TODO: Bạn cần logic ở đây để xác định 'seatType' dựa trên thông tin có sẵn
                            // Ví dụ: seat.setSeatType(determineSeatType(cellValue, seat.getLine(), seat.getNumber()));
                            SeatType seatType = seatTypeRepository.findIdByName(seatTypeId);
//                            System.out.println("tôi là Service1:"+roomIdd);

                            seat.setSeatType(seatType);
                            seat.setRoom(roomId);
                            // Mặc định 'status' là có sẵn (ví dụ: 1) hoặc bạn cần chỉ định logic tương tự như cho 'seatType'
                            seat.setStatus(0);

                            // 'description' và 'code' có thể được xác định nếu có sẵn thông tin
//                            seat.setDescription("không có");
                            seat.setCode(seat.getLine() + seat.getNumber());
                            int totalNumberOfSeats = 0;
                            totalNumberOfSeats += totalSeatCount++;
                            room.setCapacity(totalNumberOfSeats);
                            roomServiceI.updateSeat(roomId.getId(), room);
                            // Lưu vào DB
                            saveAll(seat);
                        }
                    }
                }
            }


            workbook.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return null;
    }

    @Override
    public Seat saveAll(Seat seat) {
        return seatRepository.save(seat);

    }

    @Override
    public Seat saveSeat(Seat seat) {
        seat.setCode(seat.getLine() + seat.getNumber());
        return seatRepository.save(seat);
    }

    public String determineSeatTypeByColor(String hexColor) {

        if ("#FF0000".equalsIgnoreCase(hexColor)) {
            return "Ghế đơn";
        } else if ("#0000FF".equalsIgnoreCase(hexColor)) {
            return "Ghế đôi";
        }
        // Handle undefined color or return null as per your requirements
        return null;
    }
    public boolean isLineUniqueForRoom(Room room, String line) {
        int count = seatRepository.countByRoomAndLine(room, line);
        return count == 0;
    }

    public boolean findByCodeLike(Room room, String code) {
        int count = seatRepository.findByCodeLike(room, code);
        return count == 0;
    }
}


