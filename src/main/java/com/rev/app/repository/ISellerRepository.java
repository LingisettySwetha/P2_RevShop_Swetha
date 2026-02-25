package com.rev.app.repository;

import com.rev.app.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ISellerRepository extends JpaRepository<Seller, Long> {

    Optional<Seller> findByUserUserId(Long userId);
}