package com.alomonshi.object.tableobjects;
import com.alomonshi.object.enums.UserLevels;

import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDateTime;

@XmlRootElement(namespace = " ")
public class Users{

	private int ID;
	private String name;
	private String username;
	private String password;
	private String email;
	private int userVerificationCode;
	private String phoneNo;
	private UserLevels userLevel;	// Indicates level of the user
	private String token;
	private LocalDateTime expirationDate;
	private boolean isActive;

	public String getUsername() {
		return username;
	}
	public String getName() {
		return name;
	}
	public String getPassword() {
		return password;
	}
	public String getEmail() {
		return email;
	}
	public int getID() {
		return ID;
	}
	public int getVerificationCode() {
		return userVerificationCode;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public UserLevels getUserLevel() {
		return userLevel;
	}
	public Users setName(String name) {
		this.name = name;
		return this;
	}
	public Users setUsername(String username) {
		this.username = username;
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

	public Users setID(int ID) {
		this.ID = ID;
		return this;
	}
	public Users setVerificationCode(int user_action) {
		this.userVerificationCode = user_action;
		return this;
	}
	public Users setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
		return this;
	}
	public Users setUserLevel(UserLevels userLevel) {
		this.userLevel = userLevel;
		return this;
	}

	public String getToken() {
		return token;
	}

	public Users setToken(String token) {
		this.token = token;
		return this;
	}

	public LocalDateTime getExpirationDate() {
		return expirationDate;
	}

	public Users setExpirationDate(LocalDateTime expirationDate) {
		this.expirationDate = expirationDate;
		return this;
	}

	public boolean isActive() {
		return isActive;
	}

	public Users setActive(boolean active) {
		isActive = active;
		return this;
	}
}
