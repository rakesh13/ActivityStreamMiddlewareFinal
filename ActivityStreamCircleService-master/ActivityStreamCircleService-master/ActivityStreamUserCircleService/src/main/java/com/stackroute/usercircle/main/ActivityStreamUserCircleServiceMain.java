package com.stackroute.usercircle.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.vendor.HibernateJpaSessionFactoryBean;
import org.springframework.web.client.RestTemplate;

import com.stackroute.ActivityStreamBackend.model.User;
import com.stackroute.ActivityStreamBackend.model.UserCircle;


@SpringBootApplication(scanBasePackages={"com.stackroute"})
@EntityScan(basePackageClasses=UserCircle.class)
public class ActivityStreamUserCircleServiceMain {

	public static void main(String[] args) {
		SpringApplication.run(ActivityStreamUserCircleServiceMain.class, args);

	}
	/*
	@Bean
	public HibernateJpaSessionFactoryBean sessionFactory() {
	    return new HibernateJpaSessionFactoryBean();
	}
	@Bean
	public RestTemplate restTemplate() {
	    return new RestTemplate();
	}*/
}
