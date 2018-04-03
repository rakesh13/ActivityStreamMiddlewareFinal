package com.stackroute.ActivityStreamSchemaCreation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.spi.MetadataImplementor;
import org.hibernate.tool.hbm2ddl.SchemaExport;

@RestController
@RequestMapping("/api")
public class Controller {
	
	@RequestMapping(value="/create/{schemaName}",method=RequestMethod.GET)
	public ResponseEntity createSchema(@PathVariable("schemaName") String schemaName)
	{
		 Map<String, String> settings = new HashMap<>();
	        settings.put("connection.driver_class", "com.mysql.jdbc.Driver");
	        settings.put("dialect", "org.hibernate.dialect.MySQLDialect");
	        settings.put("hibernate.connection.url", "jdbc:mysql://localhost:3306/mydb?useSSL=false");
	        settings.put("hibernate.connection.username", "root");
	        settings.put("hibernate.connection.password", "");
	        settings.put("hibernate.hbm2ddl.auto", "update");
	        settings.put("show_sql", "true");
	 
	        MetadataSources metadata = new MetadataSources(
	                new StandardServiceRegistryBuilder()
	                        .applySettings(settings)
	                        .build());
	        //metadata.addAnnotatedClass(User.class);
        SchemaExport schemaExport = new SchemaExport(
                (MetadataImplementor) metadata.buildMetadata()
        );
        schemaExport.setHaltOnError(true);
        schemaExport.setFormat(true);
        schemaExport.setDelimiter(";");
        schemaExport.setOutputFile("db-schema.sql");
        schemaExport.execute(true, true, false, true);
        return new ResponseEntity("Schema Created Successfully",HttpStatus.OK);
	}

}
