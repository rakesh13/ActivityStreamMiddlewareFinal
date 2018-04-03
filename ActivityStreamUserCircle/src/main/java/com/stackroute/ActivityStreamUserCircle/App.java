package com.stackroute.ActivityStreamUserCircle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.vendor.HibernateJpaSessionFactoryBean;
import org.springframework.web.client.RestTemplate;

import com.stackroute.ActivityStreamBackend.model.UserCircle;


/**
 * Hello world!
 *
 */
@SpringBootApplication(scanBasePackages={"com.stackroute"})
@EntityScan(basePackageClasses=UserCircle.class)
public class App 
{
    public static void main( String[] args )
    {
    	SpringApplication.run(App.class, args);
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
