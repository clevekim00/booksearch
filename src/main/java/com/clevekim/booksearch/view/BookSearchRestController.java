package com.clevekim.booksearch.view;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.net.URI;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.utils.URIBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.*;

import com.clevekim.booksearch.dao.BookDao;
import com.clevekim.booksearch.dao.SearchHistoryDao;

import com.clevekim.booksearch.model.entity.Book;
import com.clevekim.booksearch.model.entity.Document;
import com.clevekim.booksearch.model.entity.Meta;
import com.clevekim.booksearch.model.entity.SearchHistory;

@RestController
public class BookSearchRestController {
	
	private static final Logger logger =
			LoggerFactory.getLogger(BookSearchRestController.class);
	
	private static final String USER_AGENT = "Mozilla/5.0";
	private static final String GET_URL = "https://dapi.kakao.com/v2/search/book";
	private static final String API_KEY = "KakaoAK e0f21ef8a75dde9089cc8d1feac3bb55";
	
	@Autowired
	private BookDao bookDao;
	@Autowired
	private SearchHistoryDao searchHistoryDao;

	@RequestMapping("/search")
	public List<Book> insertBook(@RequestParam("query") String query, 
			@RequestParam("sort") String sort,
			@RequestParam("page") int page,
			@RequestParam("size") int size,
			@RequestParam("target") String target,
			@RequestParam("category") String category
			) {
		
		SearchHistory history = new SearchHistory();
		if (category == null || category.length() == 0)
			history.setCategory(0);
		else
			history.setCategory(Integer.parseInt(category));
		history.setPage(page);
		history.setQuery(query);
		history.setSize(size);
		history.setSort(sort);
		history.setTitle(target);
		saveSearchHistory(history);
		
		List<Book> result = null;
		
		CloseableHttpClient httpClient = null;
		try {
			result = new ArrayList<Book>();
			
			httpClient = HttpClients.createDefault();

			HttpGet httpGet = new HttpGet(GET_URL);
			httpGet.addHeader("User-Agent", USER_AGENT);
			httpGet.addHeader("Authorization", API_KEY);
			
			URI uri = new URIBuilder(httpGet.getURI())
					.addParameter("query",query).build();
			httpGet.setURI(uri);
			
			CloseableHttpResponse httpResponse = httpClient.execute(httpGet);

			logger.debug("GET Response Status:: {}", httpResponse.getStatusLine().getStatusCode());

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					httpResponse.getEntity().getContent()));

			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = reader.readLine()) != null) {
				response.append(inputLine);
			}
			reader.close();
			
			// print result
			logger.debug("Response: {}", response.toString());
			
			List<Document> documents = parsing(response.toString());
			saveBookData(documents);
		} catch(Exception e) {
			logger.error("HttpGet Fail", e);
		} finally {
			try {
				httpClient.close();
			} catch(Exception io) {
				
			}
			httpClient = null;
		}
		
		return result;
	}
	
	private List<Book> saveBookData(List<Document> documents) {
		List<Book> result = new ArrayList<Book>();
		
		for (int i = 0; i < documents.size(); i++) {
			Book book = convertToBook(documents.get(i));
			Book bookData = bookDao.saveAndFlush(book);
			
			result.add(bookData);
		}
		
		return result;
	}
	
	private void saveSearchHistory(SearchHistory history) {
		searchHistoryDao.saveAndFlush(history);
	}
	
	private List<Document> parsing(String list) {
		Gson gson = new Gson();
		Response res = gson.fromJson(list, Response.class);
		
		logger.debug("Meta.getPagable_count(): {}", res.getMeta().getPagable_count());
		logger.debug("List<Document> size: {}", res.getDocuments().size());
		return res.getDocuments();
	}
	
	private Book convertToBook(Document document) {
		Book book = new Book();
		StringBuilder authors = new StringBuilder();
		for (int j = 0; j < document.getAuthors().size(); j++)
			authors.append(document.getAuthors().get(j)).append(",");
		book.setAuthors(authors.toString());
		book.setBarcode(document.getBarcode());
		book.setCategory(document.getCategory());
		book.setContents(document.getContents());
		book.setDatetime(document.getDatetime());
		book.setEbook_barcode(document.getEbook_barcode());
		book.setIsbn(document.getIsbn());
		book.setPrice(document.getPrice());
		book.setPublisher(document.getPublisher());
		book.setSale_price(document.getSale_price());
		book.setSale_yn(document.getSale_yn());
		book.setStatus(document.getStatus());
		book.setThumbnail(document.getThumbnail());
		book.setTitle(document.getTitle());
		book.setUrl(document.getUrl());
		
		StringBuilder translators = new StringBuilder();
		for (int j = 0; j < document.getTranslators().size(); j++)
			translators.append(document.getTranslators().get(j)).append(",");
		book.setTranslators(translators.toString());
		
		return book;
	}
	
	@RequestMapping("/search/list")
	public List<Book> searchedList(Model model) {
		List<Book> books = bookDao.findAll();
		
		return books;
	}
	
	@RequestMapping("/search/delete")
	public List<Book> deleteSearchList(@RequestParam("isbn") String isbn) {
		Book book = bookDao.findOne(isbn);

		bookDao.delete(book);
		
		List<Book> books = bookDao.findAll();
		return books;
	}
	
	@RequestMapping("/search/delete/all")
	public List<Book> deleteAllSearchList() {
		bookDao.deleteAll();
		
		List<Book> books = bookDao.findAll();
		return books;
	}
}

class Response {
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
