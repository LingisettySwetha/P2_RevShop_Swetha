package com.rev.app.repository;

import com.rev.app.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IOrderItemRepository
        extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrder_OrderId(Long orderId);

    List<OrderItem> findByProduct_Seller_User_UserId(Long userId);

    @Query("""
           SELECT oi
           FROM OrderItem oi
           JOIN FETCH oi.order o
           JOIN FETCH oi.product p
           JOIN FETCH o.user
           LEFT JOIN FETCH o.payment
           WHERE p.seller.user.userId = :sellerUserId
           ORDER BY o.orderDate DESC, oi.orderItemId DESC
           """)
    List<OrderItem> findSellerOrderItems(@Param("sellerUserId") Long sellerUserId);
}
