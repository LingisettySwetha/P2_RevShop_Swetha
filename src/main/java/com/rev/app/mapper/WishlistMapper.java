package com.rev.app.mapper;

import com.rev.app.dto.WishlistDTO;
import com.rev.app.entity.Wishlist;

public class WishlistMapper {

   
    public static WishlistDTO toDTO(Wishlist wishlist) {

        WishlistDTO dto = new WishlistDTO();

        dto.setWishlistId(wishlist.getWishlistId());

        dto.setProductId(
                wishlist.getProduct().getProductId()
        );

        dto.setProductName(
                wishlist.getProduct().getName()
        );

        dto.setPrice(
                wishlist.getProduct().getPrice()
        );

        dto.setCategoryName(
                wishlist.getProduct()
                        .getCategory()
                        .getCategoryName()
        );

        return dto;
    }
}