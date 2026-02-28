package com.rev.app.repository;

import com.rev.app.entity.Product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IProductRepository extends JpaRepository<Product, Long> {
	long count();
	@Query("""
		       SELECT p FROM Product p
		       WHERE (:keyword IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%',:keyword,'%')))
		       AND (:categoryId IS NULL OR p.category.categoryId = :categoryId)
		       """)
		List<Product> searchProducts(@Param("keyword") String keyword,
		                             @Param("categoryId") Long categoryId);
}