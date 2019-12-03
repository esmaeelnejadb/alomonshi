package com.alomonshi.object.entity;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(namespace = " ")
public class CompanyCategories {
	private int ID;
	private String categoryName;
	private int companySize;
	private List<Company> companies;
	private boolean isActive;
	
	public int getID() {
		return ID;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public List<Company> getCompanies() {
		return companies;
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

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean active) {
		isActive = active;
	}

	public int getCompanySize() {
		return companySize;
	}

	public void setCompanySize(int companySize) {
		this.companySize = companySize;
	}
}
