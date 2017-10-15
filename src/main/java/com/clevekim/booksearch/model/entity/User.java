package com.clevekim.booksearch.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Column;

@Entity
public class User {
	
	@Id
	@GeneratedValue
	int id;
	
	String name;
	
	@Column(length=255)
	String barCode;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setBarCode(String barcode) {
		this.barCode = barcode;
	}
	
	public String getBarCode() {
		return barCode;
	}
}
