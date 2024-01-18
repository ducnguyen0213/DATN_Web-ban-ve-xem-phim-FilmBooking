package com.example.filmBooking.service.impl;

import com.example.filmBooking.model.RankCustomer;

import java.util.Comparator;

public class RankCustomerComparator implements Comparator<RankCustomer> {
    @Override
    public int compare(RankCustomer customer1, RankCustomer customer2) {
        return Integer.compare(customer1.getPoint(), customer2.getPoint());
    }
}
