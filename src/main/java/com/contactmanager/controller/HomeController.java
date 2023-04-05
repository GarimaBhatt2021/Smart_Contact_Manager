package com.contactmanager.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.contactmanager.dao.UserRepo;
import com.contactmanager.entities.User;
import com.contactmanager.helper.Message;

@Controller
public class HomeController {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepo userRepo;
	
	@RequestMapping("/")
	public String starting(Model model)
	{
		model.addAttribute("title", "Home - Smart Contact Manager");
		return "home";
	}

	
	@RequestMapping("/about")
	public String intro(Model model)
	{
		model.addAttribute("title", "About - Smart Contact Manager");
		return "about";
	}
	
	
	@RequestMapping("/sign-up")
	public String sign(Model model)
	{
		model.addAttribute("title", "Sign-Up - Smart Contact Manager");
		model.addAttribute("user", new User());
		return "sign-up";
	}
	
	
	@RequestMapping("/register")
	public String registerf(
			@Valid @ModelAttribute("user") User user,
			BindingResult bindres,
			@RequestParam(value="agreement",defaultValue = "false")boolean agreement,
			Model model,
			HttpSession session)
	{
		try {
			
			if(!agreement) 
			{
				System.out.println("You have to agree on terms and conditions first.");
				throw new Exception("You have to agree on terms and conditions first.");
			}
			
			if(bindres.hasErrors()) 
			{
				System.out.println("ERROR" + bindres.toString());
				model.addAttribute("user", user);
				return "sign-up";
			}
			
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageURL("thumbs_up.png");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
			
			System.out.println("Agreement : "+agreement);
			System.out.println("User : "+user);
			
			User result = this.userRepo.save(user);
			
			model.addAttribute("user", new User());
			
			session.setAttribute("message", new Message("Successfully Registered !", "alert-success"));
			return "sign-up";
			
		} 
		
		catch (Exception e) {
			e.printStackTrace();
			
			model.addAttribute("user", user);
			
			session.setAttribute("message", new Message("Something went wrong !" +e.getMessage(), "alert-danger"));
			return "sign-up";
		}
		
	}
	
	
	@GetMapping("/signin")
	public String customlogin(Model model)
	{
		model.addAttribute("title", "Log-In - Smart Contact Manager");
		return "login";
	}
	
	
}
