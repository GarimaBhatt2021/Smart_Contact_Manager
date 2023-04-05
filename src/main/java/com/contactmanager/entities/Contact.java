package com.contactmanager.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "CONTACT")
public class Contact {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int c_id;
	
	@Column(unique = true)
	private String c_email;
	
	private String c_name;
	
	private String nickname;
	
	private int c_no;
	
	@Column(length = 10000000)
	private String c_about;
	
	private String work;
	
	private String c_imageURL;


	@ManyToOne(cascade = CascadeType.ALL)
//	@JsonIgnore
	private User user;
	
	

	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}
	

	public Contact() {
		super();
					 }


	public int getC_id() {
		return c_id;
	}


	public void setC_id(int c_id) {
		this.c_id = c_id;
	}


	public String getC_email() {
		return c_email;
	}


	public void setC_email(String c_email) {
		this.c_email = c_email;
	}


	public String getC_name() {
		return c_name;
	}


	public void setC_name(String c_name) {
		this.c_name = c_name;
	}


	public String getNickname() {
		return nickname;
	}


	public void setNickname(String nickname) {
		this.nickname = nickname;
	}


	public int getC_no() {
		return c_no;
	}


	public void setC_no(int c_no) {
		this.c_no = c_no;
	}


	public String getC_about() {
		return c_about;
	}


	public void setC_about(String c_about) {
		this.c_about = c_about;
	}


	public String getWork() {
		return work;
	}


	public void setWork(String work) {
		this.work = work;
	}


	public String getC_imageURL() {
		return c_imageURL;
	}


	public void setC_imageURL(String c_imageURL) {
		this.c_imageURL = c_imageURL;
	}



 
	//                             Creating problems in loading page 
	
//	@Override
//	public String toString() {
//		return "Contact [c_id=" + c_id + ", c_email=" + c_email + ", c_name=" + c_name + ", nickname=" + nickname
//				+ ", c_no=" + c_no + ", c_about=" + c_about + ", work=" + work + ", c_imageURL=" + c_imageURL
//				+ ", user=" + user + "]";
//	}
	
	
	
}
