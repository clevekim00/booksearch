package com.clevekim.booksearch.model.entity;

import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
import javax.persistence.Id;
//import javax.persistence.Column;

@Entity
public class BookMarker {
	@Id
	String isbn;

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	
}
