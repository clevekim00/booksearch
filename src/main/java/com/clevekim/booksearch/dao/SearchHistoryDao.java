package com.clevekim.booksearch.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.clevekim.booksearch.model.entity.SearchHistory;

public interface SearchHistoryDao extends JpaRepository <SearchHistory, Integer> {

}
