package com.example.filmBooking.service.impl;

import com.example.filmBooking.model.Promotion;
import com.example.filmBooking.repository.PromotionRepository;
import com.example.filmBooking.service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


@Service
public class PromotionServiceImpl implements PromotionService {
    @Autowired
    private PromotionRepository promotionRepository;

    @Override
    public List<Promotion> fillAll() {
        return promotionRepository.findAll();
    }

    @Override
    public Promotion save(Promotion promotion) {
//        Random generator = new Random();
//        int value = generator.nextInt((100000 - 1) + 1) + 1;
//        promotion.setCode("code_promotion_" + value);
        return promotionRepository.save(promotion);
    }

    @Override
    public Promotion update(String id, Promotion promotion) {
        Promotion promotionNew = findById(id);
        promotionNew.setCode(promotion.getCode());
        promotionNew.setName(promotion.getName());
        promotionNew.setDescription(promotion.getDescription());
        promotionNew.setRankCustomer(promotion.getRankCustomer());
        promotionNew.setEndDate(promotion.getEndDate());
        promotionNew.setStartDate(promotion.getStartDate());
        promotionNew.setPercent(promotion.getPercent());
        promotionNew.setQuantity(promotion.getQuantity());
//        promotionNew.setType(promotion.getType());
        return promotionRepository.save(promotion);
    }

    @Override
    public void delete(String id) {
        promotionRepository.deleteById(id);
    }

    @Override
    public Promotion findById(String id) {
        return promotionRepository.findById(id).get();
    }

    @Override
    public List<Promotion> listVoucherCustomer(String customerId) {
        return promotionRepository.listVoucherCustomer(customerId);
    }
     
    @Override
    public Page<Promotion> getAll(Integer currentPage) {
        return promotionRepository.findAll(pagePromotion(currentPage));
    }
     
    @Override
    public Pageable pagePromotion(Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber -1, 5);
        return pageable;
    }

    @Override
    public Page<Promotion> searchByNamePromotion(String keyword, Integer currentPage) {
        return promotionRepository.findByCodeContains(keyword, pagePromotion(currentPage));
    }
}
