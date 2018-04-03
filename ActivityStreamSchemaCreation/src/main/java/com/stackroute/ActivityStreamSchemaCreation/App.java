package com.stackroute.ActivityStreamSchemaCreation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

/**
 * Hello world!
 *
 */
@SpringBootApplication(scanBasePackages={"com.stackroute"})
public class App 
{
    public static void main( String[] args )
    {
    	SpringApplication.run(App.class, args);
    	
    }
}
