package com.stackroute.message.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.stackroute.ActivityStreamBackend.dao.UserMessageDao;
import com.stackroute.ActivityStreamBackend.model.UserMessage;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;



@RestController
@RequestMapping("/api/usermessage")
@CrossOrigin("http://localhost:4200")
public class UserMessageController {

	@Autowired
	UserMessageDao userMessageDao;
	
	@RequestMapping(value="/sendMessage",method=RequestMethod.POST)
	public ResponseEntity sendMessage(@RequestBody UserMessage userMessage)
	{
		boolean result=userMessageDao.sendMessage(userMessage);
		if(result)
		{
			return new ResponseEntity("Message Successfully Sent",HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity("Error Sending Message",HttpStatus.OK);
		}
		
	}
	
	@RequestMapping(value="/deleteMessage/{messageId}",method=RequestMethod.GET)
	public ResponseEntity deleteMessage(@PathVariable("messageId") int messageId)
	{
		boolean result=userMessageDao.deleteMessage(messageId);
		if(result)
		{
			return new ResponseEntity("Message Deleted Successfully",HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity("Error Deleting Message",HttpStatus.OK);
		}
	}
	
	@RequestMapping(value="/getAllMessages",method=RequestMethod.POST)
	public List getAllMessageOfUser(@RequestBody UserMessage userMessage)
	{
		return userMessageDao.getMyMessages(userMessage.getSenderEmailId());
	}
	
	@GetMapping("/getAllMessagesByCircleName/{circleName}")
	public List<UserMessage> getAllMessagesByCircleName(@PathVariable("circleName") String circleName)
	{
		List<UserMessage> messageList=userMessageDao.getAllMessageByCircleName(circleName);
		for(UserMessage message : messageList)
		{   Link link1= linkTo(UserMessageController.class).slash(message.getMessageText()).withSelfRel();
			Link link=linkTo(UserMessageController.class).slash(message.getCircleName()).withSelfRel();
			message.add(link);
			message.add(link1);
		}
		return messageList;
	}
	
	@GetMapping("/getAllMessageByUsers/{receiverEmailId}")
	public List<UserMessage> getAllMessageByUsers(@PathVariable("receiverEmailId") String receiverEmailId)
	{
		List<UserMessage> messageList=userMessageDao.getAllMessageByUsers("rakesh@gmail.com",receiverEmailId);
		for(UserMessage message : messageList)
		{   Link link1= linkTo(UserMessageController.class).slash(message.getMessageText()).withSelfRel();
			Link link=linkTo(UserMessageController.class).slash(message.getCircleName()).withSelfRel();
			message.add(link);
			message.add(link1);
		}
		return messageList;
	}

}
