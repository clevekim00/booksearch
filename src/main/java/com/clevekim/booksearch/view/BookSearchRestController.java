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
import com.clevekim.booksearch.dao.BookMarkerDao;
import com.clevekim.booksearch.dao.SearchHistoryDao;
import com.clevekim.booksearch.dao.CategoryDao;

import com.clevekim.booksearch.model.entity.Book;
import com.clevekim.booksearch.model.entity.BookMarker;
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
	private static final int MAX_PAGE = 50;
	private static final int MAX_SIZE = 50;
	
	@Autowired
	private BookDao bookDao;
	@Autowired
	private SearchHistoryDao searchHistoryDao;
	@Autowired
	private CategoryDao categoryDao;

	@RequestMapping("/search")
	public BookSearchResponse insertBook(@RequestParam("query") String query, 
			@RequestParam("sort") String sort,
			@RequestParam("page") int page,
			@RequestParam("size") int size,
			@RequestParam("target") String target,
			@RequestParam("category") String category
			) {

		logger.debug("size {}", size);
		logger.debug("sort {}", sort);
		logger.debug("target {}", target);
		logger.debug("category {}", category);
		logger.debug("page {}", page);
		
		if (page > MAX_PAGE)
			page = MAX_PAGE;
		
		if (size > MAX_SIZE)
			size = MAX_SIZE;
		
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
		StringBuffer response = new StringBuffer();
		List<Document> documents = null;
		BookSearchResponse res = null;
		try {
			result = new ArrayList<Book>();
			
			httpClient = HttpClients.createDefault();

			HttpGet httpGet = new HttpGet(GET_URL);
			httpGet.addHeader("User-Agent", USER_AGENT);
			httpGet.addHeader("Authorization", API_KEY);
			
			URI uri = new URIBuilder(httpGet.getURI())
					.addParameter("query",query)
					.addParameter("size", String.valueOf(size))
					.addParameter("page", String.valueOf(page))
					.addParameter("sort",sort)
					.addParameter("category",category)
					.addParameter("target",target)
					.build();
			httpGet.setURI(uri);
			
			CloseableHttpResponse httpResponse = httpClient.execute(httpGet);

			logger.debug("GET Response Status:: {}", httpResponse.getStatusLine().getStatusCode());

			reader = new BufferedReader(new InputStreamReader(
					httpResponse.getEntity().getContent()));

			String inputLine;

			while ((inputLine = reader.readLine()) != null) {
				response.append(inputLine);
			}
			
			// print result
			logger.debug("Response: {}", response.toString());
			
			res = parsing(response.toString());
			documents = res.getDocuments();
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
		
		return res;
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
	
	private BookSearchResponse parsing(String list) {
		Gson gson = new Gson();
		BookSearchResponse res = gson.fromJson(list, BookSearchResponse.class);
		
		logger.debug("Meta.getPagable_count(): {}", res.getMeta().getPagable_count());
		logger.debug("List<Document> size: {}", res.getDocuments().size());
		return res;
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
	
	@Autowired
	private BookMarkerDao bookMarkerDao;
	
	@RequestMapping("/bookmarker")
	public List<BookMarker> addBookMarker(@RequestParam("bookMarkerInfo") String bookMarkerInfo) {
		if (bookMarkerInfo == null || bookMarkerInfo.length() == 0)
			return null;
		
		bookMarkerInfo = bookMarkerInfo.substring(0, bookMarkerInfo.length() - 1);
		
		String []isbns = bookMarkerInfo.split(",");
		
		List<BookMarker> result = new ArrayList<BookMarker>();
		for (int i = 0; i < isbns.length; i++) {
			BookMarker bookMarker = new BookMarker();
			bookMarker.setIsbn(isbns[i]);
			
			result.add(bookMarkerDao.saveAndFlush(bookMarker));
		}
		
		return result;
	}
	
	@RequestMapping("/bookmarker/list")
	public List<Book> bookMarkerList() {
		return getBookMarkedList();
	}
	
	private List<Book> getBookMarkedList() {
		List<BookMarker> bookMarkers = bookMarkerDao.findAll();
		
		List<String> isbns = new ArrayList<String>();
		for (int i = 0; i < bookMarkers.size(); i++) {
			isbns.add(bookMarkers.get(i).getIsbn());
		}
		
		return bookDao.findAll(isbns.subList(0, isbns.size()));
	}
	
	@RequestMapping("/bookmarker/delete")
	public List<Book> deleteBookMarker(@RequestParam("bookMarkerInfo") String bookMarkerInfo) {
		if (bookMarkerInfo == null || bookMarkerInfo.length() == 0) {
			return getBookMarkedList();
		}
		bookMarkerInfo = bookMarkerInfo.substring(0, bookMarkerInfo.length() - 1);
		
		String []isbns = bookMarkerInfo.split(",");
		for (int i = 0; i < isbns.length; i++) {
			BookMarker bookMarker = new BookMarker();
			bookMarker.setIsbn(isbns[i]);
			
			bookMarkerDao.delete(bookMarker);
		}
		
		return getBookMarkedList();
	}
	
	@RequestMapping("/bookmarker/delete/all")
	public void deleteAllBookMarker() {
		bookMarkerDao.deleteAll();
	}
}
