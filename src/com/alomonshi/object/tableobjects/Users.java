package com.alomonshi.object.tableobjects;
import com.alomonshi.object.enums.UserLevels;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

public class Users{

	private int id;
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

	public int getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}
	public String getName() {
		return name;
	}

	@JsonIgnore
	public String getPassword() {
		return password;
	}
	public String getEmail() {
		return email;
	}

	@JsonIgnore
	public int getVerificationCode() {
		return userVerificationCode;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	@JsonIgnore
	public UserLevels getUserLevel() {
		return userLevel;
	}

	@JsonIgnore
	public String getToken() {
		return token;
	}

	@JsonIgnore
	public boolean isActive() {
		return isActive;
	}

	@JsonIgnore
	public LocalDateTime getExpirationDate() {
		return expirationDate;
	}

	public void setName(String name) {
		this.name = name;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public void setId(int id) {
		this.id = id;
	}
	public void setVerificationCode(int user_action) {
		this.userVerificationCode = user_action;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public void setUserLevel(UserLevels userLevel) {
		this.userLevel = userLevel;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setExpirationDate(LocalDateTime expirationDate) {
		this.expirationDate = expirationDate;
	}

	public void setActive(boolean active) {
		isActive = active;
	}
}