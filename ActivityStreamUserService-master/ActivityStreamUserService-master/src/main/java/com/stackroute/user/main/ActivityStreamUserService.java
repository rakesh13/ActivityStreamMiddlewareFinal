package com.stackroute.user.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.vendor.HibernateJpaSessionFactoryBean;
import org.springframework.web.client.RestTemplate;

import com.stackroute.ActivityStreamBackend.model.User;




@SuppressWarnings("deprecation")
@SpringBootApplication(scanBasePackages={"com.stackroute"})
@EntityScan(basePackageClasses=User.class)
public class ActivityStreamUserService {

	public static void main(String[] args)
	{
		SpringApplication.run(ActivityStreamUserService.class, args);
	}
	
	@Bean
	public HibernateJpaSessionFactoryBean sessionFactory() {
	    return new HibernateJpaSessionFactoryBean();
	}
	@Bean
	public RestTemplate restTemplate() {
	    return new RestTemplate();
	}
	
}
