package com.clevekim.booksearch;

import com.clevekim.booksearch.BooksearchApplication;
import com.clevekim.booksearch.dao.BookDao;
import com.clevekim.booksearch.dao.BookMarkerDao;
import com.clevekim.booksearch.dao.CategoryDao;
import com.clevekim.booksearch.dao.SearchHistoryDao;
import com.clevekim.booksearch.view.BookSearchRestController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.Headers;

import org.apache.http.client.methods.RequestBuilder;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(SpringRunner.class)
@WebMvcTest(BookSearchRestController.class)
public class BookSearchRestControllerTests {
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
	
    @Test
    public void getSearch() throws Exception {
    		mockMvc.perform(get("/search?query=java&sort=accuray&page=1&size=10&target=title&category=33"))
                .andExpect(status().isOk())
                .andReturn();
    }
    
    @Test
    public void getSearchedList() throws Exception {
    		MvcResult result = mockMvc.perform(get("/search/list"))
                .andExpect(status().isOk())
                .andReturn();
    }
    
    @Test
    public void getSearchedListDelete() throws Exception {
    		MvcResult result = mockMvc.perform(get("/search/delete/all"))
                .andExpect(status().isOk())
                .andReturn();
    }
}
