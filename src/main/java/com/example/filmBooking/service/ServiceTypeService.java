package com.example.filmBooking.service;

import com.example.filmBooking.model.ServiceType;

import java.util.List;

public interface ServiceTypeService {
    ServiceType addServiceType(ServiceType serviceType);

    ServiceType updateServiceType(ServiceType serviceType, String id);

    List<ServiceType> findAll();

    void deleteServiceType(String id);

    ServiceType findById(String id);
}
