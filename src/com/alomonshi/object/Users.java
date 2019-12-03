package com.alomonshi.object;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace = " ")
public class Users{
	
	private String name;
	private String botUsername;
	private String username;
	private String password;
	private String email;
	private int companyID;
	private int user_id;
	private int ID;
	private int user_action;
	private String phoneNo;
	private int status;
	
	// Constructor
	public Users() 
	{ }
	
	
	public int getCompanyID() {
		return companyID;
	}
	public String getUsername() {
		return username;
	}
	public String getName() {
		return name;
	}
	public String getBotUsername() {
		return botUsername;
	}
	public String getPassword() {
		return password;
	}
	public String getEmail() {
		return email;
	}
	public int getUserID() {
		return user_id;
	}
	public int getID() {
		return ID;
	}
	public int getUserAction() {
		return user_action;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public int getStatus() {
		return status;
	}
	public Users setName(String name) {
		this.name = name;
		return this;
	}
	public Users setUsername(String username) {
		this.username = username;
		return this;		
	}
	public Users setBotUsername(String botUsername) {
		this.botUsername = botUsername;
		return this;
	}
	public Users setPassword(String password) {
		this.password = password;
		return this;
	}
	public Users setEmail(String email) {
		this.email = email;
		return this;
	}
	public Users setUserID(int chat_id) {
		this.user_id = chat_id;
		return this;
	}
	public Users setID(int ID) {
		this.ID = ID;
		return this;
	}
	public Users setUserAction(int user_action) {
		this.user_action = user_action;
		return this;
	}
	public Users setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
		return this;
	}
	public Users setStatus(int status) {
		this.status = status;
		return this;
	}
	public Users setCompanyID(int companyID) {
		this.companyID = companyID;
		return this;
	}
}
