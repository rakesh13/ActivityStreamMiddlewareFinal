package com.stackroute.circle.test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stackroute.ActivityStreamBackend.dao.CircleDao;
import com.stackroute.ActivityStreamBackend.model.Circle;
import com.stackroute.circle.main.ActivityStreamCircleService;

import com.stackroute.circle.restcontroller.CircleController;
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = ActivityStreamCircleService.class)
@ComponentScan(basePackages = { "com.stackroute.circle" })
@AutoConfigureMockMvc
public class CircleTestRest {

	private MockMvc mockMvc;

	@Mock
	private CircleDao circleDAO;

	@InjectMocks
	private CircleController circleController;

	@Autowired
	WebApplicationContext context;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(circleController)
                .build();

	}

	@Ignore
	@Test
	public void allCircleTest() throws Exception {
		List<Circle> userCircles = Arrays.asList(new Circle("general", "rakesh@gmail.com", true, new Date()),
				new Circle("random", "sunil@gmail.com", true, new Date()));
		when(circleDAO.getAllCircles()).thenReturn(userCircles);
		mockMvc.perform(get("/api/circle/getAllCircles")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$", hasSize(2))).andExpect(jsonPath("$[0].circleName", is("general")))
				.andExpect(jsonPath("$[0].createdBy", is("rakesh@gmail.com")))
				.andExpect(jsonPath("$[1].circleName", is("random")))
				.andExpect(jsonPath("$[1].createdBy", is("sunil@gmail.com")));
		verify(circleDAO, times(1)).getAllCircles();
		verifyNoMoreInteractions(circleDAO);
	}
	@Ignore
	@Test
	public void createCircle() throws Exception {
		Circle userCircle = new Circle("general", "rakesh@gmail.com", true, new Date());
		mockMvc.perform(post("/api/circle/add").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(userCircle))).andExpect(status().isCreated())
				.andExpect(header().string("location",
						containsString("http://localhost:9012/activityStream/api/circle/add")));
	}
	@Ignore
	@Test
	public void circleByUser() throws Exception {
		List<Circle> circle = Arrays.asList(new Circle("random", "rakesh@gmail.com", true, new Date()));

		when(circleDAO.getCircleByUser("rakesh@gmail.com")).thenReturn(circle);

		mockMvc.perform(get("/api/circle/getCircleByUser")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.circleName", is("random")))
				.andExpect(jsonPath("$.createdBy", is("rakesh@gmail.com")));

		verify(circleDAO, times(1)).getCircleByUser("rakesh@gmail.com");
		verifyNoMoreInteractions(circleDAO);
	}
	
	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
