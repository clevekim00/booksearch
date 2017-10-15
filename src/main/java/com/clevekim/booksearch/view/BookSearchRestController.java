package com.clevekim.booksearch.view;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.google.gson.*;

import com.clevekim.booksearch.dao.UserDao;
import com.clevekim.booksearch.dao.BookDao;
import com.clevekim.booksearch.model.entity.User;
import com.clevekim.booksearch.model.entity.Book;

@RestController
public class BookSearchRestController {
	@Autowired
	private UserDao userDao;
	@Autowired
	private BookDao bookDao;

	@RequestMapping("/user/create")
	public User add(User user) {
		User userData = userDao.save(user);

		return userData;
	}

	@RequestMapping("/user/list")
	public List<User> list(Model model) {
		List<User> users = userDao.findAll();

		return users;
	}

	@RequestMapping("/")
	public String index() {
		return "BookSearch!";
	}
	
	@RequestMapping("/book/insert")
	public List<Book> insertBook(String bookList) {
		List<Book> bookDatas = new ArrayList<Book>();
		List<Book> parsingBooks = parsing(bookList);
		for (int i = 0; i < parsingBooks.size(); i++) {
			Book book = bookDao.save(parsingBooks.get(i));
			bookDatas.add(book);
		}
		
		return bookDatas;
	}
	
	private List<Book> parsing(String list) {
		Gson gson = new Gson();
		Book[] result = gson.fromJson(list, Book[].class);
		
		return Arrays.asList(result);
	}
}