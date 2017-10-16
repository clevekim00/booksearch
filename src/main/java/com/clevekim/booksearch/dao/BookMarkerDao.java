package com.clevekim.booksearch.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.clevekim.booksearch.model.entity.BookMarker;

public interface BookMarkerDao extends JpaRepository<BookMarker, String> {

}
