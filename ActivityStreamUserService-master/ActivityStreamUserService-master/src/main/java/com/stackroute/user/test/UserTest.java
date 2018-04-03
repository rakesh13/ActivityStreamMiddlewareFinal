package com.stackroute.user.test;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.Arrays;
import java.util.List;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stackroute.ActivityStreamBackend.dao.UserDao;
import com.stackroute.ActivityStreamBackend.model.User;
import com.stackroute.user.main.ActivityStreamUserService;

import com.stackroute.user.restcontroller.UserController;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes={ActivityStreamUserService.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT,classes=ActivityStreamUserService.class)
public class UserTest {

	private MockMvc mockMvc;
	
	@Autowired
    private WebApplicationContext webApplicationContext;
	
	@Mock
	private UserDao userDao;
	@InjectMocks
	private UserController userController;


	@Before
	public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

	}
	@Ignore
	@Test
	public void allUserTest() throws Exception
	{
		mockMvc.perform(MockMvcRequestBuilders.get("/activityStream/api/user")).andExpect(status().isOk());
	}
	
	public void validateUser()throws Exception
	{

			MvcResult mvcResult =(MvcResult) this.mockMvc.perform(post("http://localhost:9012/api/user/authenticate").param("emailId", "sweta@gmail.com")
            .param("password", "password")).andDo(print()).andExpect(status().isOk())        
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.message").value("Hello Sweta!!!"))
            .andExpect(jsonPath("$.emailId").value("sweta@gmail.com"));
	}
	
	@Test
	public void getAllUsers() throws Exception {
		List<User> users = Arrays.asList(new User("rakesh", "india", "rakesh@gmail.com", "123456789", true),
				new User("sunil", "india", "sunil@gmail.com", "987654321", true));
		when(userDao.list()).thenReturn(users);
		mockMvc.perform(get("/api/user")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$", hasSize(2))).andExpect(jsonPath("$[0].name", is("rakesh")))
				.andExpect(jsonPath("$[0].emailId", is("rakesh@gmail.com")))
				.andExpect(jsonPath("$[1].name", is("sunil")))
				.andExpect(jsonPath("$[1].emailId", is("sunil@gmail.com")));
		verify(userDao, times(1)).list();
		verifyNoMoreInteractions(userDao);
	}

	@Ignore
	@Test
	public void createUser() throws Exception {
	    User user = new User("sujata", "india", "sujata@gmail.com", "9178111637", true);
	    ((ResultActions) ((MockHttpServletRequestBuilder) mockMvc.perform(
	            post("/api/user/insert").contentType(MediaType.APPLICATION_JSON)))
	         .content(asJsonString(user)))
            .andExpect(status().isCreated())
	            .andExpect(header().string("location", containsString("http://localhost:9011/activityStream/api/user/insert")));
	    verify(userDao, times(1));
	    verify(userDao, times(1)).save(user);
	    verifyNoMoreInteractions(userDao);
	}

	
	 public static String asJsonString(final Object obj) {
	        try {
	            return new ObjectMapper().writeValueAsString(obj);
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        }
	    }

	
}
