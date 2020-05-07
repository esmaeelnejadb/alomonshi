package com.alomonshi.object.tableobjects;

import com.alomonshi.restwebservices.views.JsonViews;
import com.fasterxml.jackson.annotation.JsonView;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(namespace = " ")
public class CompanyCategories {
	private int ID;
	private String categoryName;
	private int companySize;
	private List<Company> companies;
	private boolean isActive;
	private String logoURL;

	@JsonView(JsonViews.NormalViews.class)
	public int getID() {
		return ID;
	}

	@JsonView(JsonViews.NormalViews.class)
	public String getCategoryName() {
		return categoryName;
	}

	@JsonView(JsonViews.ClientViews.class)
	public List<Company> getCompanies() {
		return companies;
	}

	@JsonView(JsonViews.NormalViews.class)
	public int getCompanySize() {
		return companySize;
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

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public void setCompanies(List<Company> companies) {
		this.companies = companies;
	}

	public void setActive(boolean active) {
		isActive = active;
	}

	public void setCompanySize(int companySize) {
		this.companySize = companySize;
	}

	public void setLogoURL(String logoURL) {
		this.logoURL = logoURL;
	}
}
