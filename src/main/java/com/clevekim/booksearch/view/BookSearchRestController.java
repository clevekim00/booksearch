package com.clevekim.booksearch.view;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clevekim.booksearch.dao.UserDao;
import com.clevekim.booksearch.model.entity.User;

@RestController
public class BookSearchRestController {
	@Autowired
	private UserDao userDao;

	@RequestMapping("/create")
	public User add(User user) {

		User userData = userDao.save(user);

		return userData;
	}

	@RequestMapping("/list")
	public List<User> list(Model model) {

		List<User> users = userDao.findAll();

		return users;
	}

	@RequestMapping("/")
	public String index() {
		return "BookSearch!";
	}
}