package com.contactmanager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.contactmanager.dao.UserRepo;
import com.contactmanager.entities.User;

public class UserDetailsServiceImpli implements UserDetailsService {

	@Autowired
	private UserRepo userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		//               fetching user from database to show 
		User user = userRepo.getUserByUserName(username);
		
		if(user == null)
		{
			throw new UsernameNotFoundException("Couldn't found User Details.");
		}
		
		CustomerUserDetail customerUserDetail= new CustomerUserDetail(user);
		return customerUserDetail;
	}

}
