package com.clevekim.booksearch.model.entity;

import java.util.List;

public class BookSearchResponse {
	private List<Document> documents;
	private Meta meta;
	public List<Document> getDocuments() {
		return documents;
	}
	public void setBooks(List<Document> documents) {
		this.documents = documents;
	}
	public Meta getMeta() {
		return meta;
	}
	public void setMeta(Meta meta) {
		this.meta = meta;
	}
}
