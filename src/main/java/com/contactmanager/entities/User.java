package com.contactmanager.entities;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.boot.context.properties.bind.DefaultValue;

@Entity
@Table(name = "USER")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@NotBlank( message = "Name required to proceed..")
	@Size(min = 3,max = 20,message = "max 20 and min 3 characters are allowed .")
	private String username;
	
	@Column(unique = true)
	private String email;
	
	private String password;
	
	@Column(length = 1000)
	private String about;
	
	private String role;
	
	private String imageURL;
	
	private boolean enabled;

	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	List<Contact> contacts = new ArrayList<>();
	
    public User() {
		super();
				  }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public List<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}
	
	
	
//	@Override
//	public String toString() {
//		return " User [ id = " + id + " ,\n username = " + username + " ,\n email = " + email + " ,\n password = " + password + " ,\n about = "
//				+ about + " ,\n role = " + role + " ,\n imageURL = " + imageURL + " ,\n enabled = " + enabled + " ,\n contacts = "
//				+ contacts + " ]";
//	}

}
