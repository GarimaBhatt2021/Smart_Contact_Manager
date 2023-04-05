package com.contactmanager.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.contactmanager.dao.ContactRepo;
import com.contactmanager.dao.UserRepo;
import com.contactmanager.entities.Contact;
import com.contactmanager.entities.User;
import com.contactmanager.helper.Message;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private ContactRepo contactRepo;
	
	
	
	//                for providing common variables to use in every method
	
	@ModelAttribute
	public void addCommonData(Model model,Principal principal)
	{
		String userName =principal.getName();
		System.out.println("USERNAME =  "+userName);
		
		//getting user using username(email)
		
		User user = userRepo.getUserByUserName(userName);
		System.out.println("USER =  "+user);
		
		model.addAttribute("user", user);
	}
	

	@RequestMapping("/index")
	public String dashboard(Model model ,Principal principal)
	{	
		model.addAttribute("title", "User Dashboard");
		return "normal/user_dashboard";
	}
	
	
	@GetMapping("/add_contacts")
	public String addContactForm(Model model,Principal principal)
	{
		model.addAttribute("title", "Add Contacts");
		model.addAttribute("contact", new Contact());
		
		return "normal/add_contact";
	}
	
 
	@PostMapping("/process_contact")
	public String processContact(
				@ModelAttribute Contact contact,
				@RequestParam("ProfileImage")MultipartFile multipartFile ,
				Principal principal,
				HttpSession session)
	{
		try {
		
		String name = principal.getName();
		User user= this.userRepo.getUserByUserName(name);
		
		//				Processing and uploading file
		
		if(multipartFile.isEmpty())
		{
			//show custom error message
			System.out.println("File is Empty ,please recheck it.");
			contact.setC_imageURL("default.png");
		}
		else
		{
			//store the file in some folder and update the contact name 
			contact.setC_imageURL(multipartFile.getOriginalFilename());
			
			File file =new ClassPathResource("static/img").getFile();
			Path path = Paths.get(file.getAbsolutePath()+File.separator+multipartFile.getOriginalFilename());
			
			Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			
			System.out.println("Successfully uploaded.");
		}
		
		
		contact.setUser(user);
		
		user.getContacts().add(contact);
		this.userRepo.save(user);
		
		System.out.println("DATA : "+contact);
		System.out.println("Added to DataBase");
		
		//       Success message
		
		session.setAttribute("message", new Message("Your contact has been added .", "success"));
		
		}
		
		catch (Exception e) {
			System.out.println("Error"+e.getMessage());
			e.printStackTrace();
			
		//       Error Message
			
		session.setAttribute("message", new Message("Something went wrong !", "alert"));
			
		}
		 
		return "normal/add_contact";
	}
	
	
	@GetMapping("/view-contacts/{page}")
	public String showContacts( @PathVariable("page") Integer page,Model model,Principal principal)
	{
		
		//                can be written as this we need to show contacts as per userId saves
		
//		public String showContacts(Model model,Principal principal)
//		{
//			model.addAttribute("title", "Show Contacts");
//			String userName=principal.getName();
//			User user = this.userRepo.getUserByUserName(userName);
//			List<contact> Contacts = user.getContacts();
//			return "normal/show_contacts";
//		}
		
		String username = principal.getName();
		User user = this.userRepo.getUserByUserName(username);
		
		//                         currentPage - page
		//                         Contacts per page - 5
		Pageable pageable = PageRequest.of(page,5);
		
		Page<Contact> contacts = this.contactRepo.findContactsByUser(user.getId(),pageable);
		
		model.addAttribute("contacts", contacts);
		model.addAttribute("title", "Show Contacts");
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", contacts.getTotalPages());																												
		
		return "normal/show_contacts";
	}
	

	//                  for showing particular contact
	@RequestMapping("/{c_id}/contact")
	public String contactDetails(@PathVariable("c_id") Integer c_id,Model model,Principal principal)
	{	
		System.out.println("Contact ID :" +c_id);
		
		Optional<Contact> optional = this.contactRepo.findById(c_id);
		Contact contact = optional.get();
		
		String userName = principal.getName();
		User user = this.userRepo.getUserByUserName(userName);
		
		if(user.getId()==contact.getUser().getId())
		{
		model.addAttribute("contact", contact);
		model.addAttribute("title", "Contact-Details");
		}
		return "normal/contact_detail";
	}

	
	//                  for deleting single contact
	@GetMapping("/delete/{c_id}")
	public String deleteContact(@PathVariable("c_id") Integer c_Id, Principal principal, HttpSession session) {
	        Contact contact = this.contactRepo.findById(c_Id).get();
	        String userName = principal.getName();
	        
	        User user = this.userRepo.getUserByUserName(userName);
	        if(user.getId()==contact.getUser().getId()) {
	            contact.setUser(null);

	            try {
	                File saveFile = new ClassPathResource("static/img").getFile();
	                Path path = Paths.get(saveFile.getAbsolutePath() +
	                        File.separator+contact.getC_imageURL());
	                Files.delete(path);

	                user.getContacts().remove(contact);
	                this.userRepo.save(user);

	                session.setAttribute("message", new Message("Contact Deleted Successfully...", "success"));
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	        return "redirect:/user/view-contacts/0";
	    }
	
	//                  for opening update-form
	@PostMapping("/update-contact/{c_id}")
	public String updateForm(@PathVariable("c_id") Integer c_id ,Model m)
	{
		
		m.addAttribute("title", "Update Contact");
		
		Contact contact=this.contactRepo.findById(c_id).get();
		m.addAttribute("contact", contact);
		
		return "normal/update_form";
	}


	@PostMapping("/process_update")
	public String updateHandler(@ModelAttribute Contact contact,
			@RequestParam("ProfileImage") MultipartFile file,
			Model m ,
			HttpSession session,
			Principal principal)
	{
		try {
			Contact oldContact = this.contactRepo.findById(contact.getC_id()).get();
			
			if(!file.isEmpty())
			{
		//    DELETE OLD PHOTO
				File deletFile = new ClassPathResource("static/img").getFile();
				File file1 = new File(deletFile, oldContact.getC_imageURL());
				file1.delete();
				
		//    UPDATE NEW PHOTO
				File file2=new ClassPathResource("static/img").getFile();
				Path path= Paths.get(file2.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				
				contact.setC_imageURL(file.getOriginalFilename());
			}
			else {
				contact.setC_imageURL(oldContact.getC_imageURL());
			}
			User user=this.userRepo.getUserByUserName(principal.getName());
			contact.setUser(user);
			this.contactRepo.save(contact);
			
			session.setAttribute("message", new Message("Contact is successfully Updated...", "success"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		System.out.println("CONTACT : "+contact.getC_name());
		System.out.println("USER ID : "+contact.getC_id());
		return"redirect:/user/"+contact.getC_id()+"/contact";
	}


	@GetMapping("/profile")
	public String profileForm(Model model)
	{
		model.addAttribute("title","Profile Page");
		return "normal/profile";
	}
}
