package com.rev.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rev.app.entity.Category;

public interface ICategoryRepository extends JpaRepository<Category, Long>
 {



}
