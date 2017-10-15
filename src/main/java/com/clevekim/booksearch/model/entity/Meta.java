package com.clevekim.booksearch.model.entity;

public class Meta {
	//"is_end":true,"pageable_count":3,"total_count":3}
	private boolean is_end;
	public boolean isIs_end() {
		return is_end;
	}
	public void setIs_end(boolean is_end) {
		this.is_end = is_end;
	}
	public int getPagable_count() {
		return pagable_count;
	}
	public void setPagable_count(int pagable_count) {
		this.pagable_count = pagable_count;
	}
	public int getTotal_count() {
		return total_count;
	}
	public void setTotal_count(int total_count) {
		this.total_count = total_count;
	}
	private int pagable_count;
	private int total_count;
	
	
}
