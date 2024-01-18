package com.example.filmBooking.service.impl;

import com.example.filmBooking.model.Cinema;
import com.example.filmBooking.model.Room;
import com.example.filmBooking.model.Seat;
import com.example.filmBooking.repository.CinemaRepository;
import com.example.filmBooking.repository.RoomRepository;
import com.example.filmBooking.repository.SeatRepository;
import com.example.filmBooking.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;


@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepository repository;
    @Autowired
    private CinemaRepository repositoryCinema;
//    @Autowired
//    private SeatRepository seatRepository;

    @Override
    public Page<Room> getAll(Integer currentPage) {
        return repository.findAll(pageRoom(currentPage));
    }

    @Override
    public List<Room> fillAll() {
        return repository.findAll();
    }


    @Override
    public boolean saveAll(Cinema cinema, String description, Integer capacity, Integer width, Integer height, String other_equipment, String projector, Integer status) {
        try {

            Random generator = new Random();
            int value = generator.nextInt((100000 - 1) + 1) + 1;
            Room room = new Room();
            room.setId(UUID.randomUUID().toString());
            room.setCode("ROOM" + value);
            room.setCinema(cinema);
            room.setDescription(description);
            room.setCapacity(capacity);
            room.setWidth(width);
            room.setHeight(height);
            room.setProjector(projector);
            room.setOther_equipment(other_equipment);
            room.setStatus(status);
            room.setName("Room" + value + "_" + room.getCinema().getName());
            repository.save(room);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public void saveSeat(String roomId) {
//        List<Character> listLine = new ArrayList<>();
//        Room room= repository.findById(roomId).get();
//        //Thêm dữ liệu vào line
//        for (char i = 'A'; i <= 'Z'; i++) {
//            listLine.add(i);
//        }
//        char line = listLine.get(5);
//        Seat seat = new Seat();
//        for (char ch = 'A'; ch <line; ch++) {
//            for (int i = 1; i <9; i++) {
//                seat.setId(UUID.randomUUID().toString());
//                seat.setRoom(room);
//                seat.setCode(ch + "" + i);
//                seat.setLine(ch + "");
//                seat.setNumber(i);
//                seat.setStatus(1);
//                seatRepository.save(seat);
//            }
//        }
//        room.setCapacity(repository.findNumber(room.getId()));
//        save(room);
    }

    @Override
    public Room save(Room room) {
        Random generator = new Random();
        int value = generator.nextInt((100000 - 1) + 1) + 1;
        room.setId(UUID.randomUUID().toString());
        room.setCode("ROOM" + value);
        room.setName("Room" + value + "_" + room.getCinema().getName());
        room.setCapacity(0);

//        repository.save(room);
        return repository.save(room);
    }

    @Override
    public Room update(String id, Room room) {
        Room customerNew = findById(id);
//        customerNew.setName(room.getName());
//        customerNew.setCode(room.getCode());
        customerNew.setDescription(room.getDescription());
//        customerNew.setType(room.getType());
//        customerNew.setCapacity(room.getCapacity());
        customerNew.setHeight(room.getHeight());
        customerNew.setWidth(room.getWidth());
        customerNew.setOther_equipment(room.getOther_equipment());
        customerNew.setProjector(room.getProjector());
        customerNew.setStatus(room.getStatus());
        customerNew.setStatus(room.getStatus());

        return repository.save(customerNew);
    }

    @Override
    public Room updateSeat(String id, Room room) {
        Room customerNew = findById(id);

        customerNew.setCapacity(room.getCapacity());


        return repository.save(customerNew);
    }

    @Override
    public Room findById(String id) {
        return repository.findById(id).get();
    }

    @Override
    public Pageable pageRoom(Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1, 5);
        return pageable;
    }

    @Override
    public List<Room> finByRoom() {
        return repository.findByCapacityIsNull();
    }

    @Override
    public List<Room> roomCapacity() {
        return repository.roomCapcity();
    }

    @Override
    public Page<Room> serachRoom(String keyword, Integer currentPage) {
        return repository.findByNameContains(keyword, pageRoom(currentPage));
    }

    @Override
    public void delete(String id) {
        repository.delete(findById(id));
    }
}
