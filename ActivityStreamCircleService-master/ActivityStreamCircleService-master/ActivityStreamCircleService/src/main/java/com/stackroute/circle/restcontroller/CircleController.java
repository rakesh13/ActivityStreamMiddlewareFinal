package com.stackroute.circle.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.stackroute.ActivityStreamBackend.dao.CircleDao;
import com.stackroute.ActivityStreamBackend.model.Circle;



@RestController
@RequestMapping("/api/circle")
@CrossOrigin("http://localhost:4200")
public class CircleController {

	@Autowired
	CircleDao circleDao;
	
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public ResponseEntity addCircle(@RequestBody Circle circle) {
		circleDao.addCircle(circle);
		return new ResponseEntity(circle, HttpStatus.OK);
	}
	
	@RequestMapping
	public List<Circle> getAllCircles()
	{
		return circleDao.getAllCircles();
	}
	@RequestMapping(value="/getCircleByUser",method=RequestMethod.POST)
	public ResponseEntity getCircleByUser(@RequestBody Circle circle)
	{
		List<Circle> circleList=circleDao.getCircleByUser(circle.getCreatedBy());
		if(!(circleList.isEmpty()))
		{
			return new ResponseEntity(circleList,HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity("User Not Added in any Circle",HttpStatus.OK);
		}
	}
}