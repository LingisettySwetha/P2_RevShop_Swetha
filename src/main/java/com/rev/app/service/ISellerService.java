package com.rev.app.service;

import com.rev.app.entity.User;

public interface ISellerService {

    void createSeller(User user, String businessName);
}