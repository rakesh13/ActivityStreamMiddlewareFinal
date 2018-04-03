package com.stackroute.usercircle.test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stackroute.ActivityStreamBackend.dao.UserCircleDao;
import com.stackroute.ActivityStreamBackend.model.UserCircle;
import com.stackroute.usercircle.main.ActivityStreamUserCircleServiceMain;

import com.stackroute.usercircle.restcontroller.UserCircleController;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = ActivityStreamUserCircleServiceMain.class)
@ComponentScan(basePackages={"com.stackroute.usercircle"})
@AutoConfigureMockMvc
public class UserCircleTest {

	private MockMvc mockMvc;

	@Mock
	private UserCircleDao userCircleDao;
    @Autowired
	@InjectMocks
	private UserCircleController userCircleController;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(userCircleController).build();

	}

    @Ignore
	@Test
	public void addUserToCircleTest() throws Exception {
		UserCircle circle = new UserCircle(3,"doubts","rakesh@gmail.com");
		mockMvc.perform(post("/api/usercircle/addUserToCircle").contentType("application/json")
				.content(asJsonString(circle))).andExpect(status().isCreated())
				.andExpect(header().string("location", containsString("http://localhost:9012/activityStream/api/usercircle/addUserToCircle")));
		verify(userCircleDao, times(1)).addUserToCircle("rakesh@gmail.com", "doubts");
		verifyNoMoreInteractions(userCircleDao);
	}

    @Ignore
   	@Test
   	public void addUserToCircleNegativeTest() throws Exception {
   		UserCircle circle = new UserCircle(3,"doubts","rakesh@gmail.com");
   		mockMvc.perform(post("/api/usercircle/addUserToCircle").contentType("application/json")
   				.content(asJsonString(circle))).andExpect(status().isConflict())
   				.andExpect(header().string("location", containsString("http://localhost:9012/activityStream/api/usercircle/addUserToCircle")));
   		verify(userCircleDao, times(1)).addUserToCircle("rakesh@gmail.com", "doubts");
   		verifyNoMoreInteractions(userCircleDao);
   	}
	private static String asJsonString(final Object obj) {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			final String jsonContent = mapper.writeValueAsString(obj);
			return jsonContent;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
