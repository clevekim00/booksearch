package com.clevekim.booksearch.view;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.clevekim.booksearch.dao.SearchHistoryDao;
import com.clevekim.booksearch.model.entity.SearchHistory;

@RestController
public class SearchHistoryRestController {
	
	private static final Logger logger =
			LoggerFactory.getLogger(SearchHistoryRestController.class);
	
	@Autowired
	private SearchHistoryDao searchHistoryDao;
	
	@RequestMapping("/history/list")
	public List<SearchHistory> addSearchHistory() {
		List<SearchHistory> result = searchHistoryDao.findAll();
		
		return result;
	}
	
	@RequestMapping("/history/delete")
	public List<SearchHistory> deleteSearchHistory(@RequestParam("id") int id) {
		searchHistoryDao.delete(id);
		
		List<SearchHistory> result = searchHistoryDao.findAll();
		
		return result;
	}
	
	@RequestMapping("/history/delete/all")
	public List<SearchHistory> deleteAllSearchHistory() {
		searchHistoryDao.deleteAll();
		
		List<SearchHistory> result = searchHistoryDao.findAll();
		
		return result;
	}
}