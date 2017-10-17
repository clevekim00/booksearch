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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@WebMvcTest(SearchHistoryRestController.class)
public class SearchHistoryRestController {
	
	private static final Logger logger =
			LoggerFactory.getLogger(BookSearchRestControllerTests.class);
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	SearchHistoryDao searchDao;
	
	@Test
    public void getHistoryList() throws Exception {
    		MvcResult result = mockMvc.perform(get("/history/list"))
                .andExpect(status().isOk())
                .andReturn();

    		String res = result.getResponse().getContentAsString();
    		logger.debug("Response {}", res);
    		assertThat(res).isNotEmpty();
    }

    @Test
    public void historyDelete() throws Exception {
	    	mockMvc.perform(get("/search?query=java&sort=accuray&page=1&size=10&target=title&category=33"))
	        .andExpect(status().isOk())
	        .andReturn();
    		MvcResult result = mockMvc.perform(get("/history/delete"))
                .andExpect(status().isOk())
                .andReturn();
    		
    		String res = result.getResponse().getContentAsString();
    		logger.debug("Response {}", res);
    		assertThat(res).isNotEmpty();
    }
}
