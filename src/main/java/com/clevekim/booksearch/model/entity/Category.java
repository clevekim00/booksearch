package com.clevekim.booksearch.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Column;

@Entity
public class Category {
	@Id
	int category;
	
	String firstSection;
	String secondSection;
	public int getCategory() {
		return category;
	}
	public void setCategory(int category) {
		this.category = category;
	}
	public String getFirstSection() {
		return firstSection;
	}
	public void setFirstSection(String firstSection) {
		this.firstSection = firstSection;
	}
	public String getSecondSection() {
		return secondSection;
	}
	public void setSecondSection(String secondSection) {
		this.secondSection = secondSection;
	}
	
	
}
