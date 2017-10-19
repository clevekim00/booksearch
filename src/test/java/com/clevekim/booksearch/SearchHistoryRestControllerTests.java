package com.clevekim.booksearch;

//import com.clevekim.booksearch.dao.BookDao;
//import com.clevekim.booksearch.dao.BookMarkerDao;
//import com.clevekim.booksearch.dao.CategoryDao;
import com.clevekim.booksearch.dao.SearchHistoryDao;
//import com.clevekim.booksearch.model.entity.BookSearchResponse;
//import com.clevekim.booksearch.model.entity.Document;
//import com.clevekim.booksearch.view.BookSearchRestController;
//import com.google.gson.Gson;
import com.clevekim.booksearch.view.SearchHistoryRestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//import java.util.List;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@WebMvcTest(SearchHistoryRestController.class)
public class SearchHistoryRestControllerTests {
	
	private static final Logger logger =
			LoggerFactory.getLogger(SearchHistoryRestControllerTests.class);
	
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
    		MvcResult result = mockMvc.perform(get("/history/delete?id=1234"))
                .andExpect(status().isOk())
                .andReturn();
    		
    		String res = result.getResponse().getContentAsString();
    		logger.debug("Response {}", res);
    		assertThat(res).isNotEmpty();
    }

    @Test
    public void historyDeleteAll() throws Exception {
    		MvcResult result = mockMvc.perform(get("/history/delete/all"))
                .andExpect(status().isOk())
                .andReturn();
    		
    		String res = result.getResponse().getContentAsString();
    		logger.debug("Response {}", res);
    		assertThat(res).isNotEmpty();
    }
}
