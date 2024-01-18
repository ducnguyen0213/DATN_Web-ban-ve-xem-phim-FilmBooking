package com.example.filmBooking.service;

import com.example.filmBooking.model.Service;
import com.example.filmBooking.model.Promotion;
import org.springframework.data.repository.query.Param;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PromotionService {
    List<Promotion> fillAll();

    Promotion save(Promotion promotion);

    Promotion update(String id, Promotion promotion);

    void delete(String id);

    Promotion findById(String id);

    List<Promotion> listVoucherCustomer(String customerId);
    
    Page<Promotion> getAll(Integer currentPage);
    
    Pageable pagePromotion(Integer pageNumber);

    Page<Promotion> searchByNamePromotion(String keyword, Integer currentPage);
}
