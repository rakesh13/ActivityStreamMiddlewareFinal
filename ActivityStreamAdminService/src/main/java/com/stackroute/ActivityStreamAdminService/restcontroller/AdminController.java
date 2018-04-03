package com.stackroute.ActivityStreamAdminService.restcontroller;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.stackroute.ActivityStreamAdminService.mailsender.MailSender;
import com.stackroute.ActivityStreamBackend.dao.WorkspaceDao;
import com.stackroute.ActivityStreamBackend.dao.WorkspaceUsersDao;
import com.stackroute.ActivityStreamBackend.model.Workspace;
import com.stackroute.ActivityStreamBackend.model.WorkspaceUsers;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin("http://localhost:4200")
public class AdminController {

	@Autowired
    @Qualifier("activityStreamMailSender")
	public MailSender mailSender;
	
	@Autowired
	WorkspaceDao workspaceDao;
	
	@Autowired
	WorkspaceUsersDao workspaceUsersDao;
	
	
	private boolean sendMail(String emailId,String workspaceName)
	{
		System.out.println("Email Id = "+emailId);
		try
		{
			String from = "mail.from.activitystream@gmail.com";
			String to = emailId;
			String subject = "Activity Stream : New User Joining Confirmation";
			String body = "Hi.\nGreetings from ActivityStream. \n\nPlease click on the link to get joined: http://localhost:4200/subscribe?workspaceName="+workspaceName+"&userEmail="+emailId+"\n"
					+ "\n\nRegards,"
					+ "\nRakesh Pandit"
					+ "\nAdmin"
					+ "\nActivityStream";
			
			mailSender.sendMail(from, to, subject, body);
			return true;
		}
		catch(Exception ex)
		{
			return false;
		}
	}
	
	private String otp;
	@RequestMapping(value="/sendCode/{adminEmailId}",method=RequestMethod.GET)
	public ResponseEntity sendMailForCode(@PathVariable("adminEmailId") String adminEmailId)
	{
		try
		{
			otp=generateOTP();
			otp=otp.replaceAll("[^\\.0123456789]","");
			String from = "mail.from.activitystream@gmail.com";
			String to = adminEmailId+".com";
			String subject = "Activity Stream : EmailID Confirmation";
			String body = "Hi.\nGreetings from ActivityStream. \n\nPlease enter the code to get started.\n"
					+ "\nCode : "+otp
					+ "\n\nRegards,"
					+ "\nRakesh Pandit"
					+ "\nAdmin"
					+ "\nActivityStream";
			
			if(mailSender.sendMail(from, to, subject, body))
			{
			return new ResponseEntity("Mail Sent Successfully",HttpStatus.OK);
			}
			else
			{
				return new ResponseEntity("Error Sending mail. Invalid Email Address : "+adminEmailId,HttpStatus.UNAUTHORIZED);
				
			}
		}
		catch(Exception ex)
		{
			return new ResponseEntity("Exception. Invalid Email Address : ",HttpStatus.UNAUTHORIZED);
		}
	}
	
	private String generateOTP()
	{
		 String numbers = "0123456789";
		 Random rndm_method = new Random();
		 char[] otp = new char[5];
		 for (int i = 0; i < 5; i++)
	        {
	            otp[i] =numbers.charAt(rndm_method.nextInt(numbers.length()));
	        }
		 return otp.toString();
		
	}
	
	@RequestMapping(value="/validateCode/{code}",method=RequestMethod.GET)
	public ResponseEntity<String> validateCode(@PathVariable("code") String code)
	{
		try
		{
			if(code.equals(otp))
			{
				return new ResponseEntity<String>(code,HttpStatus.OK);
				//return true;
			}
			else
			{
				return new ResponseEntity("Invalid Code.",HttpStatus.NOT_FOUND);
				//return false;
			}
		}
		catch (Exception ex) {
			return new ResponseEntity("Exception in validation "+ex,HttpStatus.OK);
			//return false;
		}
	}
	@RequestMapping(value="/sendInvitation",method=RequestMethod.POST)
	public ResponseEntity<WorkspaceUsers> sendInvitation(@RequestBody WorkspaceUsers workspaceUsers)
	{
		if(sendMail(workspaceUsers.getUserEmailId().trim(),workspaceUsers.getCompanyName().trim()))
		{
			boolean result=workspaceUsersDao.sendInvitation(workspaceUsers.getCompanyName(), workspaceUsers.getUserEmailId());
			if(result)
			{
				return new ResponseEntity("User "+workspaceUsers.getUserEmailId()+" invited Successfully",HttpStatus.OK);
			}
			else
			{
				return new ResponseEntity("Adding User "+workspaceUsers.getUserEmailId()+" encountered database error. Please Check Logs for details..",HttpStatus.OK);
			}
		}
		else
		{
			return new ResponseEntity("Invalid Email address.. Please check emailId of the user.",HttpStatus.UNAUTHORIZED);
		}
	}
	@RequestMapping(value="/acceptedInvitation",method=RequestMethod.POST)
	public ResponseEntity<WorkspaceUsers> acceptedInvitation(@RequestBody WorkspaceUsers workspaceUsers)
	{
		
		boolean result=workspaceUsersDao.acceptInvitation(workspaceUsers.getCompanyName().trim(), workspaceUsers.getUserEmailId().trim());
		if(result)
		{
			return new ResponseEntity("User "+workspaceUsers.getUserEmailId()+" added Successfully",HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity("Adding User "+workspaceUsers.getUserEmailId()+" encountered database error. Please Check Logs for details..",HttpStatus.OK);
		}
	}
	
	@RequestMapping(value="/rejectInvitation",method=RequestMethod.POST)
	public ResponseEntity<WorkspaceUsers> rejectInvitation(@RequestBody WorkspaceUsers workspaceUsers)
	{
		boolean result=workspaceUsersDao.rejectInvitation(workspaceUsers.getCompanyName().trim(), workspaceUsers.getUserEmailId().trim());
		if(result)
		{
			return new ResponseEntity("User "+workspaceUsers.getUserEmailId()+" was unsubscribed Successfully",HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity("Unsubscribing User "+workspaceUsers.getUserEmailId()+" encountered database error. Please Check Logs for details..",HttpStatus.UNAUTHORIZED);
		}
	}
	
	@RequestMapping(value="/removeUser",method=RequestMethod.POST)
	public ResponseEntity<WorkspaceUsers> removeUserFromWorkspace(@RequestBody WorkspaceUsers workspaceUsers)
	{
		boolean result=workspaceUsersDao.removeUserFromWorkspace(workspaceUsers.getCompanyName(), workspaceUsers.getUserEmailId());
		if(result)
		{
			return new ResponseEntity("User "+workspaceUsers.getUserEmailId()+" removed Successfully",HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity("Deleting User "+workspaceUsers.getUserEmailId()+" encountered database error. Please Check Logs for details..",HttpStatus.UNAUTHORIZED);
		}
	}
	
	@RequestMapping(value="/createWorkspace",method=RequestMethod.POST)
	public ResponseEntity<WorkspaceUsers> createWorkspace(@RequestBody Workspace workspace)
	{
		
		boolean result=workspaceDao.createWorkspace(workspace);
		if(result)
		{
			return new ResponseEntity("Workspace "+workspace.getCompanyName()+" Created Successfully",HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity("Creating Workspace "+workspace.getCompanyName()+" encountered database error. Please Check Logs for details..",HttpStatus.UNAUTHORIZED);
		}
	}
}
