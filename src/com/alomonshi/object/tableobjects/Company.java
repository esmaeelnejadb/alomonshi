package com.alomonshi.object.tableobjects;

import com.alomonshi.restwebservices.views.JsonViews;
import com.fasterxml.jackson.annotation.JsonView;

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
	private List<CompanyPicture> companyPictures;
	private float rate;
	private int discount;
	private String logoURL;
	private boolean isActive;
	private String commercialCode;

	@JsonView(JsonViews.NormalViews.class)
	public int getID() {
		return ID;
	}

	@JsonView(JsonViews.NormalViews.class)
	public int getCityID() {
		return cityID;
	}

	@JsonView(JsonViews.NormalViews.class)
	public int getDistrictID() {
		return districtID;
	}

	@JsonView(JsonViews.NormalViews.class)
	public String getLocality() {
		return locality;
	}

	@JsonView(JsonViews.NormalViews.class)
	public int getCompanyCatID() {
		return companyCatID;
	}

	@JsonView(JsonViews.NormalViews.class)
	public String getCompanyName() {
		return companyName;
	}

	@JsonView(JsonViews.NormalViews.class)
	public String getCompanyAddress() {
		return companyAddress;
	}

	@JsonView(JsonViews.NormalViews.class)
	public String getCompanyPhoneNo1() {
		return companyPhoneNo1;
	}

	@JsonView(JsonViews.NormalViews.class)
	public String getCompanyPhoneNo2() {
		return companyPhoneNo2;
	}

	@JsonView(JsonViews.NormalViews.class)
	public String getCompanyPhoneNo3() {
		return companyPhoneNo3;
	}

	@JsonView(JsonViews.NormalViews.class)
	public float getLocationLon() {
		return LocationLon;
	}

	@JsonView(JsonViews.NormalViews.class)
	public float getLocationLat() {
		return LocationLat;
	}

	@JsonView(JsonViews.NormalViews.class)
	public String getWebsite() {
		return website;
	}

	@JsonView(JsonViews.NormalViews.class)
	public float getRate() {
		return rate;
	}

	@JsonView(JsonViews.NormalViews.class)
	public int getDiscount() {
		return discount;
	}

	@JsonView(JsonViews.ManagerViews.class)
	public String getCommercialCode() {
		return commercialCode;
	}

	@JsonView(JsonViews.NormalViews.class)
	public List<Units> getUnits() {
		return units;
	}

	@JsonView(JsonViews.NormalViews.class)
	public List<CompanyPicture> getCompanyPictures() {
		return companyPictures;
	}

	@JsonView(JsonViews.NormalViews.class)
	public String getLogoURL() {
		return logoURL;
	}

	@JsonView(JsonViews.ManagerViews.class)
	public boolean isActive() {
		return isActive;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public void setDistrictID(int districtID) {
		this.districtID = districtID;
	}

	public void setCityID(int cityID) {
		this.cityID = cityID;
	}

	public void setCompanyCatID(int companyCatID) {
		this.companyCatID = companyCatID;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	public void setCompanyPhoneNo1(String companyPhoneNo1) {
		this.companyPhoneNo1 = companyPhoneNo1;
	}

	public void setCompanyPhoneNo2(String companyPhoneNo2) {
		this.companyPhoneNo2 = companyPhoneNo2;
	}

	public void setCompanyPhoneNo3(String companyPhoneNo3) {
		this.companyPhoneNo3 = companyPhoneNo3;
	}

	public void setLocationLon(float locationLon) {
		LocationLon = locationLon;
	}

	public void setLocationLat(float locationLat) {
		LocationLat = locationLat;
	}

	public void setUnits(List<Units> units) {
		this.units = units;
	}

	public void setCompanyPictures(List<CompanyPicture> companyPictures) {
		this.companyPictures = companyPictures;
	}

	public void setRate(float rate) {
		this.rate = rate;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public void setLogoURL(String logoURL) {
		this.logoURL = logoURL;
	}

	public void setActive(boolean active) {
		isActive = active;
	}

	public void setCommercialCode(String commercialCode) {
		this.commercialCode = commercialCode;
	}
}