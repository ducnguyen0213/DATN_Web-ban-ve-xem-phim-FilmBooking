package com.example.filmBooking.service;

import com.example.filmBooking.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface CustomerService {
    List<Customer> fillAll();

    Customer save(Customer customer);

    Customer update(String id, Customer customer);

    void delete(String id);

    Customer findById(String id);

    void autoCheckPoint();

    List<Customer> findByPromotion(String idPromotion);

    Customer findByEmail(String email);
    
    Page<Customer>getAll(Integer currentPage);

    Pageable pageCustomer(Integer pageNumber);

    Page<Customer>searchCustomer(String keyword, Integer pageNumber);

    Customer findCustomerByEmail(String email);

    Integer pointSetRank(String customerId);

}
