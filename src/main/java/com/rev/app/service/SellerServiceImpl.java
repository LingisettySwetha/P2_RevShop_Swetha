package com.rev.app.service;

import com.rev.app.entity.Seller;
import com.rev.app.entity.User;
import com.rev.app.repository.ISellerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SellerServiceImpl implements ISellerService {

    @Autowired
    private ISellerRepository sellerRepository;

    @Override
    public void createSeller(User user, String businessName) {

        Seller seller = new Seller();

        seller.setUser(user);
        seller.setBusinessName(businessName);

        sellerRepository.save(seller);
    }
}