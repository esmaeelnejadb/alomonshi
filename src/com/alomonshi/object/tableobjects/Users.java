package com.alomonshi.object.tableobjects;
import com.alomonshi.object.enums.UserLevels;
import com.alomonshi.restwebservices.adaptors.LocalDateTimeAdaptor;
import com.alomonshi.restwebservices.views.JsonViews;
import com.fasterxml.jackson.annotation.JsonView;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;

public class Users{

	private int clientID;
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

	@JsonView(JsonViews.NormalViews.class)
	public int getClientID() {
		return clientID;
	}

	@JsonView(JsonViews.NormalViews.class)
	public String getUsername() {
		return username;
	}

	@JsonView(JsonViews.NormalViews.class)
	public String getName() {
		return name;
	}

	@JsonView(JsonViews.ManagerViews.class)
	public String getPassword() {
		return password;
	}

	@JsonView(JsonViews.NormalViews.class)
	public String getEmail() {
		return email;
	}

	@JsonView(JsonViews.ManagerViews.class)
	public int getVerificationCode() {
		return userVerificationCode;
	}

	@JsonView(JsonViews.ClientViews.class)
	public String getPhoneNo() {
		return phoneNo;
	}

	@JsonView(JsonViews.ManagerViews.class)
	public UserLevels getUserLevel() {
		return userLevel;
	}

	@JsonView(JsonViews.ManagerViews.class)
	public String getToken() {
		return token;
	}

	@JsonView(JsonViews.ManagerViews.class)
	public boolean isActive() {
		return isActive;
	}

	@XmlJavaTypeAdapter(LocalDateTimeAdaptor.class)
	@JsonView(JsonViews.ManagerViews.class)
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

	public void setClientID(int clientID) {
		this.clientID = clientID;
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