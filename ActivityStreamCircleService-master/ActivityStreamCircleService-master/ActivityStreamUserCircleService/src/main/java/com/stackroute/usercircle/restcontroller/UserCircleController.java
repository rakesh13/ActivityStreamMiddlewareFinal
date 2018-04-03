package com.stackroute.usercircle.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.stackroute.ActivityStreamBackend.dao.UserCircleDao;
import com.stackroute.ActivityStreamBackend.model.UserCircle;


@RestController
@RequestMapping("/api/usercircle")
public class UserCircleController {

	@Autowired
	UserCircleDao userCircleDao;
	/*
	public static final String REST_SERVICE_URI = "http://localhost:9012/api/usercircle";
	@Autowired
	RestTemplate restTemplate;
	
*/	
	@GetMapping("/")
	public String serverStarted()
	{
		return "server started";
	}
	
	/*
	@RequestMapping(value="/addUserToCircle",method=RequestMethod.POST)
	public ResponseEntity addUserToCircle(@RequestBody UserCircle userCircle)
	{
		userCircleDao.addUserToCircle(userCircle.getEmailId(), userCircle.getCircleName());
		return new ResponseEntity("User "+userCircle.getEmailId()+" added Successfully",HttpStatus.OK);
	}
	
	@RequestMapping(value="/deleteUserFromCircle",method=RequestMethod.POST)
	public ResponseEntity deleteUserFromCircle(@RequestBody UserCircle userCircle)
	{
		userCircleDao.deleteUserFromCircle(userCircle.getEmailId(), userCircle.getCircleName());
		return new ResponseEntity("User "+userCircle.getEmailId()+" deleted Successfully",HttpStatus.OK);
	}
	
	@RequestMapping(value="/getUsersOfCircle/{circleName}",method=RequestMethod.GET)
	public List<String> getUsersOfCircle(@PathVariable("circleName") String circleName)
	{
		List<String> result=userCircleDao.getUsersOfCircle(circleName);
		//restTemplate.getForObject(REST_SERVICE_URI, List.class);
		return result;
	}*/
}
