package com.alomonshi.object.tableobjects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(namespace = " ")
public class Company {
	private int ID;
	private int companyCatID;
	private String companyName;
	private String companyAddress;
	private String companyPhoneNo1;
	private String companyPhoneNo2;
	private String companyPhoneNo3;
	private float LocationLon;
	private float LocationLat;
	private String locality;
	private int cityID;
	private int districtID;
	private String website;
	private List<Units> units;
	private float rate;
	private String logoURL;
	private boolean isActive;
	private String commercialCode;
	
	
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

	public String getCompanyPhoneNo1() {
		return companyPhoneNo1;
	}

	public String getCompanyPhoneNo2() {
		return companyPhoneNo2;
	}

	public String getCompanyPhoneNo3() {
		return companyPhoneNo3;
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

	public String getPicURL() {
		return logoURL;
	}

	@JsonIgnore
	public String getCommercialCode() {
		return commercialCode;
	}

	public Company setID(int iD) {
		ID = iD;
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
	public Company setCompanyPhoneNo1(String companyPhoneNo1) {
		this.companyPhoneNo1 = companyPhoneNo1;
		return this;
	}
	public Company setCompanyPhoneNo2(String companyPhoneNo2) {
		this.companyPhoneNo2 = companyPhoneNo2;
		return this;
	}
	public Company setCompanyPhoneNo3(String companyPhoneNo3) {
		this.companyPhoneNo3 = companyPhoneNo3;
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
		this.logoURL = url;
		return this;
	}

	public String getLogoURL() {
		return logoURL;
	}

	public Company setLogoURL(String logoURL) {
		this.logoURL = logoURL;
		return this;
	}

	@JsonIgnore
	public boolean isActive() {
		return isActive;
	}

	public Company setActive(boolean active) {
		isActive = active;
		return this;
	}

	public void setCommercialCode(String commercialCode) {
		this.commercialCode = commercialCode;
	}
}
