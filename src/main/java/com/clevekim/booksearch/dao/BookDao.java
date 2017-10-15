package com.clevekim.booksearch.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.clevekim.booksearch.model.entity.Book;

public interface BookDao extends JpaRepository<Book, String> {

}
