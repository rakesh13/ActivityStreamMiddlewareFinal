package com.stackroute.message.test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.hamcrest.core.Is.is;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stackroute.ActivityStreamBackend.dao.MessageDao;
import com.stackroute.ActivityStreamBackend.dao.UserMessageDao;
import com.stackroute.ActivityStreamBackend.model.UserMessage;
import com.stackroute.message.main.ActivityStreamMessageService;

import com.stackroute.message.restcontroller.UserMessageController;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT,classes=ActivityStreamMessageService.class)
@ComponentScan(basePackages={"com.stackroute.message"})
@AutoConfigureMockMvc
public class MessageTest {

	private MockMvc mockMvc;

    @Mock
    private MessageDao messageDAO;
    
    @Mock
    private UserMessageDao userMessageDAO;

    @InjectMocks
    private UserMessageController userMessageController;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(userMessageController)
                .build();
    }
    
    @Test
    public void getAllMessagesTest() throws Exception {
       List<UserMessage> message = Arrays.asList(new UserMessage(1, "Hiii", "rakesh@gmail.com", new Date(), 100, 1000, "Text", null, "general"));

       when(userMessageDAO.getMyMessages("rakesh@gmail.com")).thenReturn(message);

        mockMvc.perform(get("/api/usermessage/getAllMessages", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.messageId", is(1)))
                .andExpect(jsonPath("$.messageText", is("Hiii")));

        verify(userMessageDAO, times(1)).getMyMessages("rakesh@gmail.com");
        verifyNoMoreInteractions(userMessageDAO);
    }
   
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
