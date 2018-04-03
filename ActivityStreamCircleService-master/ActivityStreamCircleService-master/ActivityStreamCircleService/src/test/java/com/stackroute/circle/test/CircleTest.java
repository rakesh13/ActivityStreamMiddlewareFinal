package com.stackroute.circle.test;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.assertEquals;

import com.stackroute.ActivityStreamBackend.dao.CircleDao;
import com.stackroute.ActivityStreamBackend.model.Circle;
import com.stackroute.circle.main.ActivityStreamCircleService;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT,classes=ActivityStreamCircleService.class)
public class CircleTest {

	@Autowired
	private CircleDao circleDao;
	
	private Circle circle;
	
	@Before
	public void init()
	{
		circle=new Circle();
	}
	@Ignore
	@Test
	public void addCircleTest()
	{
		circle.setCircleName("Friends");
		circle.setCreatedBy("pankaj@gmail.com");
		circle.setCreationDate(new Date());
		circle.setStatus(true);
		boolean result=circleDao.addCircle(circle);
		assertEquals(true,result);
		
	}
	@Ignore
	@Test
	public void getAllCircles()
	{
		List<Circle> circleList=circleDao.getAllCircles();
		/*for(Circle circle:circleList)
		{
			System.out.println(circle.getCircleName()+"  "+circle.getCreatedBy()+"  "+circle.getCreationDate());
		}*/
	}
}
