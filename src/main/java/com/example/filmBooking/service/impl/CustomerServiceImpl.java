package com.example.filmBooking.service.impl;

import com.example.filmBooking.model.Customer;
import com.example.filmBooking.model.RankCustomer;
import com.example.filmBooking.repository.CustomerRepository;
import com.example.filmBooking.repository.RankCustomerRepository;
import com.example.filmBooking.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository repository;

    @Autowired
    private RankCustomerRepository rankRepository;

    @Override
    public List<Customer> fillAll() {
        return repository.findAll();
    }

    @Override
    public Customer save(Customer customer) {
        Random generator = new Random();
        int value = generator.nextInt((100000 - 1) + 1) + 1;
        customer.setCode("code_" + value);
        customer.setPoint(0);
        List<RankCustomer> listRank = rankRepository.findAll();
        listRank.sort((o1, o2) -> {
            return o1.getPoint().compareTo(o2.getPoint());
        });
        if (customer.getPoint() > listRank.get(listRank.size() - 1).getPoint()) {
            //chuyển thành  goi cao nhat
            customer.setRankCustomer(rankRepository.findById(listRank.get(listRank.size() - 1).getId()).get());
        } else {
            for (int i = 0; i < listRank.size(); i++) {
                if (customer.getPoint() >= listRank.get(i).getPoint() && customer.getPoint() < listRank.get(i + 1).getPoint()) {
                    customer.setRankCustomer(rankRepository.findById(listRank.get(i).getId()).get());
                    break;
                }
            }
        }
        return repository.save(customer);
    }


    //tự động cập nhật rank cho khách hàng
    @Scheduled(fixedRate = 60000)
    public void autoCheckPoint() {
        List<RankCustomer> listRank = rankRepository.findAll();
        listRank.sort((o1, o2) -> {
            return o1.getPoint().compareTo(o2.getPoint());
        });
        System.out.println(listRank);
        for (Customer customer : repository.findAll()
        ) {
            System.out.println(customer.getId());
            System.out.println(pointSetRank(customer.getId()));
            if(pointSetRank(customer.getId())==null){
                customer.setRankCustomer(listRank.get(0));
                repository.save(customer);
            }
            else if (pointSetRank(customer.getId()) != null && pointSetRank(customer.getId()) > listRank.get(listRank.size() - 1).getPoint()) {
                //chuyển thành  goi cao nhat
                customer.setRankCustomer(rankRepository.findById(listRank.get(listRank.size() - 1).getId()).get());
                repository.save(customer);
            } else {
                for (int i = 0; i < listRank.size(); i++) {
                    if (pointSetRank(customer.getId()) >= listRank.get(i).getPoint() && pointSetRank(customer.getId()) < listRank.get(i + 1).getPoint()) {
                        customer.setRankCustomer(rankRepository.findById(listRank.get(i).getId()).get());
                        repository.save(customer);
                        break;
                    }
                }
            }
//            System.out.println(customer.getName() + customer.getRankCustomer().getName());
        }
    }

    @Override
    public List<Customer> findByPromotion(String idPromotion) {
        return repository.findByPromotion(idPromotion);
    }

    @Override
    public Customer findByEmail(String email) {
        return repository.findEmail(email);
    }


    @Override
    public Customer update(String id, Customer customer) {
        Customer customerNew = findById(id);
        customerNew.setName(customer.getName());
        customerNew.setPhoneNumber(customer.getPhoneNumber());
        customerNew.setEmail(customer.getEmail());
        customerNew.setPassword(customer.getPassword());
        return repository.save(customerNew);
    }

    @Override
    public Customer findById(String id) {
        return repository.findById(id).get();
    }

    @Override
    public void delete(String id) {
        repository.delete(findById(id));
    }

  @Override
    public Pageable pageCustomer(Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber -1, 5);
        return pageable;
    }

    @Override
    public Page<Customer> searchCustomer(String keyword, Integer pageNumber) {
        return repository.findByNameContains(keyword, pageCustomer(pageNumber));
    }

    @Override
    public Customer findCustomerByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public Integer pointSetRank(String customerId) {
//        System.out.println(repository.pointSetRank("c2710df2-fee7-44e1-a757-b381ec4303a3") +"hahahahahhah");
        return repository.pointSetRank(customerId);
    }

    @Override
    public Page<Customer> getAll(Integer currentPage) {
        return repository.findAll(pageCustomer(currentPage));
    }

}
