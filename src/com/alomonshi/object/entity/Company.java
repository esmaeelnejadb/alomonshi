package com.alomonshi.object.entity;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(namespace = " ")
public class Company {
	private int ID;
	private int companyCatID;
	private String companyName;
	private String companyAddress;
	private String companyPhoneNo;
	private String username;
	private String password;
	private float LocationLon;
	private float LocationLat;
	private String locality;
	private int cityID;
	private int districtID;
	private String website;
	private List<Units> units;
	private float rate;
	private int status;
	private String pic_url;
	private boolean isActive;
	
	
	public int getCityID() {
		return cityID;
	}
	public Company setCityID(int cityID) {
		this.cityID = cityID;
		return this;
	}
	public int getDistrictID() {
		return districtID;
	}
	public Company setDistrictID(int districtID) {
		this.districtID = districtID;
		return this;
	}
	public String getLocality() {
		return locality;
	}
	public Company setLocality(String locality) {
		this.locality = locality;
		return this;		
	}	
	public int getID() {
		return ID;
	}
	public int getCompanyCatID() {
		return companyCatID;
	}
	public String getCompanyName() {
		return companyName;
	}
	public String getCompanyAddress() {
		return companyAddress;
	}
	public String getCompanyPhoneNo() {
		return companyPhoneNo;
	}
	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}
	public float getLocationLon() {
		return LocationLon;
	}
	public float getLocationLat() {
		return LocationLat;
	}
	public String getWebsite() {
		return website;
	}
	public float getRate() {
		return rate;
	}
	public int getStatus() {
		return status;
	}
	public String getPicURL() {
		return pic_url;
	}
	public Company setID(int iD) {
		ID = iD;
		return this;
	}
	public Company setStatus(int stat) {
		status = stat;
		return this;
	}
	public Company setCompanyCatID(int companyCatID) {
		this.companyCatID = companyCatID;
		return this;
	}
	public Company setCompanyName(String companyName) {
		this.companyName = companyName;
		return this;
	}
	public Company setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
		return this;
	}
	public Company setCompanyPhoneNo(String companyPhoneNo) {
		this.companyPhoneNo = companyPhoneNo;
		return this;
	}
	public Company setUsername(String username) {
		this.username = username;
		return this;
	}
	public Company setPassword(String password) {
		this.password = password;
		return this;
	}
	public Company setLocationLon(float locationLon) {
		LocationLon = locationLon;
		return this;
	}
	public Company setLocationLat(float locationLat) {
		LocationLat = locationLat;
		return this;
	}
	public List<Units> getUnits() {
		return units;
	}
	public Company setUnits(List<Units> units) {
		this.units = units;
		return this;
	}
	public Company setRate(float rate) {
		this.rate = rate;
		return this;
	}
	public Company setWebsite(String website) {
		this.website = website;
		return this;
	}
	public Company setPicURL(String url) {
		this.pic_url = url;
		return this;
	}

	public String getPic_url() {
		return pic_url;
	}

	public Company setPic_url(String pic_url) {
		this.pic_url = pic_url;
		return this;
	}

	public boolean isActive() {
		return isActive;
	}

	public Company setActive(boolean active) {
		isActive = active;
		return this;
	}
}
