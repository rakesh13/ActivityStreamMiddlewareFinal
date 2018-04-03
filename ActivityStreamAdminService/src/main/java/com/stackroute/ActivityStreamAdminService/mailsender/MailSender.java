package com.stackroute.ActivityStreamAdminService.mailsender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
 
@Component("activityStreamMailSender")
public class MailSender {

	@Autowired
	JavaMailSender javaMailSender;
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public boolean sendMail(String from, String to, String subject, String body) {
		
		try
		{
		SimpleMailMessage mail = new SimpleMailMessage();
 
		mail.setFrom(from);
		mail.setTo(to);
		mail.setSubject(subject);
		mail.setText(body);
		
		logger.info("Sending...");
		
		javaMailSender.send(mail);
		logger.info("Done!");
		return true;
		}
		catch(Exception ex)
		{
			return false;
		}
		
	}
}
