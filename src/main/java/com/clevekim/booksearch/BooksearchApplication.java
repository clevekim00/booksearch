package com.clevekim.booksearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.h2.server.web.WebServlet;

@SpringBootApplication
public class BooksearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(BooksearchApplication.class, args);
	}
	
	@Bean
	public ServletRegistrationBean h2servletRegistration() {
	    ServletRegistrationBean registration = new ServletRegistrationBean(new WebServlet());
	    registration.addUrlMappings("/console/booksearch");
	    return registration;
	}
}
