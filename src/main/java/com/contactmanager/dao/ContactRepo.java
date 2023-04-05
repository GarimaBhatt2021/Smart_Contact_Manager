package com.contactmanager.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.contactmanager.entities.Contact;
import com.contactmanager.entities.User;

public interface ContactRepo extends JpaRepository<Contact, Integer>{
	

//  PAGINATION
//                         currentPage - page
//                         Contacts per page - 5
	
	@Query("from Contact as c where c.user.id =:userId")
	public Page<Contact> findContactsByUser(@Param("userId") int userId , Pageable pePageable);

	
//  SEARCH 
	
//	public List<Contact> findByC_NameContainingAndUser(String c_name, User user);
	
}
