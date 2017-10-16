package com.clevekim.booksearch.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.clevekim.booksearch.model.entity.Category;

public interface CategoryDao extends JpaRepository <Category, Integer> {

}
