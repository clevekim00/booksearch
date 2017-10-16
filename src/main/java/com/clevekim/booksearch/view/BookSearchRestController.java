package com.clevekim.booksearch.view;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
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
import com.google.gson.reflect.TypeToken;
import com.clevekim.booksearch.dao.BookDao;
import com.clevekim.booksearch.dao.SearchHistoryDao;
import com.clevekim.booksearch.dao.CategoryDao;

import com.clevekim.booksearch.model.entity.Book;
import com.clevekim.booksearch.model.entity.Document;
import com.clevekim.booksearch.model.entity.SearchHistory;
import com.clevekim.booksearch.model.entity.BookSearchResponse;
import com.clevekim.booksearch.model.entity.Category;

@RestController
public class BookSearchRestController {
	
	private static final Logger logger =
			LoggerFactory.getLogger(BookSearchRestController.class);
	
	private static final String USER_AGENT = "Mozilla/5.0";
	private static final String GET_URL = "https://dapi.kakao.com/v2/search/book";
	private static final String API_KEY = "KakaoAK e0f21ef8a75dde9089cc8d1feac3bb55";
	private static final String CATEGORY_DATA_FILE = "data/category.txt";
	
	@Autowired
	private BookDao bookDao;
	@Autowired
	private SearchHistoryDao searchHistoryDao;
	@Autowired
	private CategoryDao categoryDao;

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
		BufferedReader reader = null;
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

			reader = new BufferedReader(new InputStreamReader(
					httpResponse.getEntity().getContent()));

			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = reader.readLine()) != null) {
				response.append(inputLine);
			}
			
			// print result
			logger.debug("Response: {}", response.toString());
			
			List<Document> documents = parsing(response.toString());
			result = saveBookData(documents);
		} catch(Exception e) {
			logger.error("HttpGet Fail", e);
		} finally {
			try {
				if (reader != null)
					reader.close();
				if (httpClient != null)
					httpClient.close();
			} catch(Exception io) {
				
			}
			reader = null;
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
		BookSearchResponse res = gson.fromJson(list, BookSearchResponse.class);
		
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
	
	@RequestMapping("/search/category")
	public List<Category> categoryList() {
		List<Category> categories = categoryDao.findAll();
		
		if (categories == null || categories.size() == 0) {
			categories = insertCategoryData();
			for (int i = 0; i < categories.size(); i++) {
				categoryDao.saveAndFlush(categories.get(i));
			}
		}
		
		if (categories == null || categories.size() == 0)
			return categories;
		
		Collections.sort(categories, new CategoryAsc());
		
		return categories;
	}
	
	private List<Category> insertCategoryData() {
		
		StringBuilder result = new StringBuilder("");

		//Get file from resources folder
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(CATEGORY_DATA_FILE).getFile());

		try {
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				result.append(line).append("\n");
			}
			scanner.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		logger.debug("Read {}", result);

		Gson gson = new Gson();
		TypeToken<List<Category>> typeToken = new TypeToken<List<Category>>() {};
		List<Category> categoryData = gson.fromJson(result.toString(), typeToken.getType());
		
		return categoryData;
	}
	
	static class CategoryAsc implements Comparator<Category> {
		@Override
		public int compare(Category arg0, Category arg1) {
			return arg0.getCategory() < arg1.getCategory() ? -1 : arg0.getCategory() > arg1.getCategory() ? 1:0;
		}
	}
}
