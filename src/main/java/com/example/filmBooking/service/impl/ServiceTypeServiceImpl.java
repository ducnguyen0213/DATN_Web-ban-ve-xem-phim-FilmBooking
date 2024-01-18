package com.example.filmBooking.service.impl;

import com.example.filmBooking.model.ServiceType;
import com.example.filmBooking.repository.ServiceTypeRepository;
import com.example.filmBooking.service.ServiceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
@Service
public class ServiceTypeServiceImpl implements ServiceTypeService {
    @Autowired
    private ServiceTypeRepository serviceTypeRepository;

    @Override
    public ServiceType addServiceType(ServiceType serviceType) {
        Random generator = new Random();
        int value = generator.nextInt((100000 - 1) + 1) + 1;
        serviceType.setCode("ST" + value);
        return serviceTypeRepository.save(serviceType);
    }

    @Override
    public ServiceType updateServiceType(ServiceType serviceType, String id) {
        ServiceType serviceTypeNew = findById(id);
        serviceTypeNew.setName(serviceType.getName());
        return serviceTypeRepository.save(serviceTypeNew);
    }

    @Override
    public List<ServiceType> findAll() {
        return serviceTypeRepository.findAll();
    }

    @Override
    public void deleteServiceType(String id) {
        serviceTypeRepository.delete(findById(id));
    }

    @Override
    public ServiceType findById(String id) {
        return serviceTypeRepository.findById(id).get();
    }
}
