package com.stackroute.user.restcontroller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.stackroute.ActivityStreamBackend.dao.UserDao;
import com.stackroute.ActivityStreamBackend.model.User;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

//import com.stackroute.user.model.Circle;
//import com.stackroute.user.model.CircleInfoResponse;
//import com.stackroute.user.model.Message;

//import com.stackroute.user.vo.UserHomeVO;

@RestController
@RequestMapping("/api/user")
@EnableWebMvc
@CrossOrigin("http://localhost:4200")
public class UserController {
	
	//Why not private??

	@Autowired
	private UserDao userDao;

	@Autowired
	private User user;
	@Autowired
	RestTemplate restTemplate;
	
	//UserHomeVO userHomeVO;
	@Autowired
	HttpSession session;
	
	public static final String CIRCLE_SERVICE_URI = "http://172.23.238.132:9013/api/circle/";
	public static final String USER_CIRCLE_SERVICE_URI = "http://172.23.238.132:9013/api/userCircle/";
	public static final String MESSAGE_SERVICE_URI = "http://172.23.238.165:8089/getMessagesByCircleId/veggrp05";
	public static final String url="http://172.23.238.154:8888/activityStream/api/message/";
	@RequestMapping(produces = "application/json")
	
	//Use List<User>
	public List getUser() {
		//Better to use plural if it is list
		// i.., users , listOfUsers,  userList etc.,
		List<User> user=userDao.list();
		for(User usr:user)
		{
			Link link=linkTo(UserController.class).slash(usr.getEmailId()).withSelfRel();
			usr.add(link);
		}
		//Remove commented code.
		//restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		//List<Circle> u=(List<Circle>) restTemplate.getForObject(CIRCLE_SERVICE_URI, CircleInfoResponse.class).getCircles();
		/*Circle[] circles=restTemplate.getForObject(CIRCLE_SERVICE_URI, Circle[].class);
		for(Circle circle:circles)
		{
			System.out.println(circle.getCreatedBy());
		}*/
		//Message msg=restTemplate.getForObject(url, Message.class);
		//System.out.println(msg.getMessageText());
		return user;
	}

	@RequestMapping(value="/search",method=RequestMethod.POST)
	public ResponseEntity<User> getUser(@RequestBody String id) {
		
		//Whey this user1??
		User user1=userDao.get(id);
		
		if (user1 == null) {
			//If the user does not exist, why should add links?
			
			return new ResponseEntity("No Customer found for ID " + id, HttpStatus.NOT_FOUND);
		}
		user1.add(linkTo(UserController.class).slash(user.getEmailId()).withSelfRel());
		return new ResponseEntity(user1, HttpStatus.OK);
	}

	@RequestMapping(value="/insert",method=RequestMethod.POST)
	public ResponseEntity createUser(@RequestBody User user) {
		//what if the user already exist?
		userDao.save(user);
		return new ResponseEntity(user, HttpStatus.OK);
	}
	
	@PostMapping("/login")
	//Use ResponseEntity<UserHomeVO>
	public ResponseEntity authenticateUser(@RequestBody User loggedUser)
	{
		//userHomeVO=new UserHomeVO();
		//User user = userDao.get(loggedUser.getEmailId());
		System.out.println(loggedUser.getEmailId());
		user=userDao.validate(loggedUser.getEmailId(), loggedUser.getPassword());
		if(user==null)
		{
			
			return new ResponseEntity("Invalid Username and Password", HttpStatus.NOT_FOUND);
		}
		else
		{
			session.setAttribute("username", loggedUser.getEmailId());
			/*restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
			List<String> allCircleNames=new ArrayList<>();
			Circle[] circles=restTemplate.getForObject(CIRCLE_SERVICE_URI+"allcircles", Circle[].class);
			for(Circle circle:circles)
			{
				allCircleNames.add(circle.getCircleName());
			}
			//Why to create new circle here?  Every time whenever the user login, creating new circle?
			Circle newCircle=new Circle();
			//why to set created by when the user logging in.  
			newCircle.setCreatedBy(user.getEmailId());
			List<String> myCircleNames=new ArrayList<>();
			Circle[] circlesByUser=restTemplate.postForObject(CIRCLE_SERVICE_URI+"getCirclesByUser",newCircle, Circle[].class);
			for(Circle circle:circlesByUser)
			{
				myCircleNames.add(circle.getCircleName());
			}
			userHomeVO.setAllCircleNames(allCircleNames);
			userHomeVO.setEmailId(user.getEmailId());
			userHomeVO.setUserName(user.getName());
			userHomeVO.setMyCircleNames(myCircleNames);
			List<User> allUsers=getUser();
			List<String> allUserNames=new ArrayList<>();
			allUsers.forEach(username -> allUserNames.add(username.getEmailId()));
			userHomeVO.setAllUserEmailId(allUserNames);
			//whey this circle name general??
			userHomeVO.setCircleName("general");
			List<Message> allMessageByCircleName=new ArrayList<>();
			Message[] allMessages=restTemplate.getForObject(url+"getAllMessagesByCircleName/general",Message[].class);
			for(Message message:allMessages)
			{
				allMessageByCircleName.add(message);
			}
			userHomeVO.setSelectedCircleMessage(allMessageByCircleName);*/
			return new ResponseEntity(user,HttpStatus.OK);
		}
	}
	
	@RequestMapping(value="/logout",method=RequestMethod.GET)
	public ResponseEntity logout(HttpSession session)
	{
		String username=(String) session.getAttribute("username");
		if(username==null)
		{
			session.invalidate();
			return new ResponseEntity(" Logout Successfull!!",HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity("Already Logged Out!! Please Login to continue..",HttpStatus.OK);
		}
	}
}

