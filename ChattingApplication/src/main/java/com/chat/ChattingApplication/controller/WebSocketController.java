package com.chat.ChattingApplication.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.print.attribute.standard.Destination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
@CrossOrigin("http://localhost:4200")
public class WebSocketController {

	private final SimpMessagingTemplate template;
	
	@Autowired
	public WebSocketController(SimpMessagingTemplate template)
	{
		this.template=template;
	}
	
	@MessageMapping("/send/message")
	public void onReceivedMessage(String message)
	{
		this.template.convertAndSend("/chat",new SimpleDateFormat("HH:mm:ss").format(new Date())+" - "+message);
	}
	
	
}
