package com.clevekim.booksearch;

import com.clevekim.booksearch.dao.BookDao;
import com.clevekim.booksearch.dao.BookMarkerDao;
import com.clevekim.booksearch.dao.CategoryDao;
import com.clevekim.booksearch.dao.SearchHistoryDao;
import com.clevekim.booksearch.model.entity.BookSearchResponse;
import com.clevekim.booksearch.model.entity.Document;
import com.clevekim.booksearch.view.BookSearchRestController;
import com.google.gson.Gson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@WebMvcTest(BookSearchRestController.class)
@EnableSpringDataWebSupport
public class BookSearchRestControllerTests {
	
	private static final Logger logger =
			LoggerFactory.getLogger(BookSearchRestControllerTests.class);
	
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	BookDao bookDao;
	@MockBean
	SearchHistoryDao searchDao;
	@MockBean
	CategoryDao categoryDao;
	@MockBean
	BookMarkerDao bookMarkerDao;
	
	BookSearchResponse bookSearchResponse;
	String searchResult;
    @Test
    public void getSearch() throws Exception {
    		MvcResult result = mockMvc.perform(get("/search?query=java&sort=accuray&page=1&size=10&target=title&category=33"))
                .andExpect(status().isOk())
                .andReturn();
    		
    		searchResult = result.getResponse().getContentAsString();
    		assertThat(searchResult).isNotEmpty();
    		
    		Gson gson = new Gson();
    		bookSearchResponse = gson.fromJson(searchResult, BookSearchResponse.class);
    }
    
    @Test
    public void getSearchedList() throws Exception {
	    	mockMvc.perform(get("/search?query=java&sort=accuray&page=1&size=10&target=title&category=33"))
	        .andExpect(status().isOk())
	        .andReturn();
	    	
    		MvcResult result = mockMvc.perform(get("/search/list"))
                .andExpect(status().isOk())
                .andReturn();
    		
    		String res = result.getResponse().getContentAsString();
    		logger.debug("Response {}", res);
    		assertThat(res).isNotEmpty();
    }
    
    @Test
    public void getSearchedListPage() throws Exception {
    		try {

	    	    	mockMvc.perform(get("/search?query=java&sort=accuray&page=1&size=10&target=title&category=33"))
	    	        .andExpect(status().isOk())
	    	        .andReturn();
	        	
	    	    	MvcResult result = mockMvc.perform(get("/search/listpage?page=1&size=10&sort=title,asc"))
	                        .andExpect(status().isOk())
	                        .andReturn();
	        		
	        		String res = result.getResponse().getContentAsString();
	        		logger.debug("Response {}", res);
	        		assertThat(res).isNotEmpty();
    		} catch(Exception e) {
    			logger.error(e.toString(), e);
    			throw e;
    		}
    }
    
    @Test
    public void getSearchedListDelete() throws Exception {
    		MvcResult result = mockMvc.perform(get("/search/delete/all"))
                .andExpect(status().isOk())
                .andReturn();

    		String res = result.getResponse().getContentAsString();
    		logger.debug("Response {}", res);
    		assertThat(res).isNotEmpty();
    }
    
    @Test
    public void bookMarkerAdd() throws Exception {
    		String data = "8909081325 9788909081320,8988379799 9788988379790,";
    		MvcResult result = mockMvc.perform(get("/bookmarker?bookMarkerInfo=" + data))
                .andExpect(status().isOk())
                .andReturn();
    		
    		String res = result.getResponse().getContentAsString();
    		logger.debug("Response {}", res);
    		assertThat(res).isNotEmpty();
    }
    
    @Test
    public void bookMarkerSelectedDelete() throws Exception {
		String data = "8909081325 9788909081320,";
    		
    		MvcResult result = mockMvc.perform(get("/bookmarker/delete?bookMarkerInfo=" + data))
                .andExpect(status().isOk())
                .andReturn();
    		
    		String res = result.getResponse().getContentAsString();
    		logger.debug("Response {}", res);
    		assertThat(res).isNotEmpty();
    }
    
    @Test
    public void bookMarkerAllDelete() throws Exception {
    		MvcResult result = mockMvc.perform(get("/bookmarker/delete/all"))
                .andExpect(status().isOk())
                .andReturn();
    		
    		String res = result.getResponse().getContentAsString();
    		logger.debug("Response {}", res);
    		assertThat(res).isEmpty();
    }

    @Test
    public void bookMarkerList() throws Exception {
    		MvcResult result = mockMvc.perform(get("/bookmarker/list"))
                .andExpect(status().isOk())
                .andReturn();
    		
    		String res = result.getResponse().getContentAsString();
    		logger.debug("Response {}", res);
    		assertThat(res).isNotEmpty();
    }
}
