package com.stackroute.message.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.vendor.HibernateJpaSessionFactoryBean;
import org.springframework.web.client.RestTemplate;



@SpringBootApplication(scanBasePackages={"com.stackroute"})
@EntityScan(basePackages={"com.stackroute.message"})
public class ActivityStreamMessageService {

	public static void main(String[] args) {
		SpringApplication.run(ActivityStreamMessageService.class, args);

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
