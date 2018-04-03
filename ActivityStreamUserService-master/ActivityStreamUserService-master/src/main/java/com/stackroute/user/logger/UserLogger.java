package com.stackroute.user.logger;



import java.util.Date;
import java.util.logging.FileHandler;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class UserLogger {

	private static final Logger logger=LoggerFactory.getLogger(UserLogger.class);
	 public UserLogger() {
		
	}
	
	@Before("execution(* com.stackroute.user.restcontroller.UserController.getUser())")
	public void beforeGetAllUsers()
	{
		logger.info("Request made to get all Users at "+new Date());
	}
}
