package com.alomonshi.object.entity;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(namespace = " ")
public class Units {
	private int ID;
	private int companyID;
	private String unitName;
	private String unitStepTime;
	private Boolean isActive;
	private List<Services> services;
	private List<UnitPicture> unitPics;
	private List<Comments> unitComments;
	
	public int getID() {
		return ID;
	}
	public int getCompanyID() {
		return companyID;
	}
	public String getUnitName() {
		return unitName;
	}
	public String getUnitStepTime() {
		return unitStepTime;
	}
	public List<Services> getServices() {
		return services;
	}
	public List<UnitPicture> getUnitPics() {
		return unitPics;
	}
	public boolean getIsActive() {
		return isActive;
	}
	public Units setID(int iD) {
		ID = iD;
		return this;		
	}
	public Units setCompanyID(int companyID) {
		this.companyID = companyID;
		return this;
	}
	public Units setUnitName(String unitName) {
		this.unitName = unitName;
		return this;
	}
	public Units setUnitStepTime(String unitStepTime) {
		this.unitStepTime = unitStepTime;
		return this;
	}
	public Units setServices(List<Services> services) {
		this.services = services;
		return this;
	}
	public Units setUnitPics(List<UnitPicture> unitpics) {
		this.unitPics = unitpics;
		return this;
	}
	public Units setIsActive(boolean isActive) {
		this.isActive = isActive;
		return this;
	}

	public List<Comments> getUnitComments() {
		return unitComments;
	}

	public void setUnitComments(List<Comments> unitComments) {
		this.unitComments = unitComments;
	}
}
