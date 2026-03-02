package com.rev.app.mapper;

import com.rev.app.dto.ProductRequest;
import com.rev.app.entity.*;

public class ProductMapper {

    public static Product toEntity(ProductRequest request,
                                   User seller,
                                   Category category) {

        Product product = new Product();

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setDiscountPrice(request.getDiscountPrice());
        product.setQuantity(request.getQuantity());

        product.setSeller(seller);
        product.setCategory(category);

        return product;
    }
}