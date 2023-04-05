package com.contactmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.contactmanager.dao.UserRepo;
import com.contactmanager.entities.User;

//@Controller
public class ContactController {

	@Autowired
	private UserRepo userRepo;
	
	@GetMapping("/show")
	@ResponseBody
	public String front()
	{
		User user = new User();
		
		user.setUsername("Ashley Armdilo");
		user.setEmail("ash123@gmail.com");
		
		userRepo.save(user);
		
		return " Working !";
	}
	
}
