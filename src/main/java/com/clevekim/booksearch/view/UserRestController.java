package com.clevekim.booksearch.view;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.clevekim.booksearch.dao.UserDao;

import com.clevekim.booksearch.model.entity.User;

@RestController
public class UserRestController {
	
	private static final Logger logger =
			LoggerFactory.getLogger(UserRestController.class);
	
	@Autowired
	private UserDao userDao;
	
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
}
