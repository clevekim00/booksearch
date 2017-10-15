package com.clevekim.booksearch.model.entity;

import java.util.List;

public class Document {
	
	String title;//	도서 제목	String
	String contents;//	도서 소개	String

	String url;//	책 링크 (URL)	String

	String isbn	;//ISBN 번호. 국제 표준 도서번호(ISBN10,ISBN13) (ISBN10,ISBN13 중에 하나 이상 존재하면 ' '(공백)을 구분자로 출렴됨)	String

	String datetime;//	도서 출판날짜. ISO 8601. [YYYY]-[MM]-[DD]T[hh]:[mm]:[ss].000+[tz]	String

	List<String> authors;//	도서 저자 리스트	Array of String

	String publisher;//	출판사	String

	List<String> translators;//	번역자 리스트	Array of String

	String price;//	도서 정가	String

	String sale_price;//	도서 판매가	String
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public List<String> getAuthors() {
		return authors;
	}

	public void setAuthors(List<String> authors) {
		this.authors = authors;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public List<String> getTranslators() {
		return translators;
	}

	public void setTranslators(List<String> translators) {
		this.translators = translators;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getSale_price() {
		return sale_price;
	}

	public void setSale_price(String sale_price) {
		this.sale_price = sale_price;
	}

	public boolean isSale_yn() {
		return sale_yn;
	}

	public void setSale_yn(boolean sale_yn) {
		this.sale_yn = sale_yn;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getEbook_barcode() {
		return ebook_barcode;
	}

	public void setEbook_barcode(String ebook_barcode) {
		this.ebook_barcode = ebook_barcode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	boolean sale_yn;//	도서 판매 여부	Y or N
	public boolean getSale_yn() {
		return sale_yn;
	}
	
	String category	;//도서 카테고리 정보	String

	String thumbnail;//	도서 표지 썸네일	String

	String barcode;//	교보문고 바코드 정보	String

	String ebook_barcode;//	교보문고 전자책 바코드 정보	String

	String status;//	도서 상태 정보(정상, 품절, 절판 등). 변경 가능성이 있으니 노출용으로만 사용 권장.	String
}
