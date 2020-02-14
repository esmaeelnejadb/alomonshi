package com.alomonshi.object.tableobjects;

import com.fasterxml.jackson.annotation.JsonIgnore;
//import org.codehaus.jackson.annotate.JsonIgnore;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(namespace = " ")
public class Units {
	private int ID;
	private int companyID;
	private String unitName;
	private int unitDuration;// Unit duration has been considered in minute
	private Boolean isActive;
	private List<Services> services;
	private String pictureURL;
	private List<Comments> unitComments;
	private String remark;

	public int getID() {
		return ID;
	}
	@JsonIgnore
	public int getCompanyID() {
		return companyID;
	}
	public String getUnitName() {
		return unitName;
	}
	@JsonIgnore
	public int getUnitDuration() {
		return unitDuration;
	}
	@XmlAttribute(name = "services")
	public List<Services> getServices() {
		return services;
	}
	public String getPictureURL() {
		return pictureURL;
	}
	@JsonIgnore
	public boolean getActive() {
		return isActive;
	}

	@XmlAttribute(name = "comments")
	public List<Comments> getUnitComments() {
		return unitComments;
	}

	public String getRemark() {
		return remark;
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
	public Units setUnitDuration(int unitDuration) {
		this.unitDuration = unitDuration;
		return this;
	}
	public Units setServices(List<Services> services) {
		this.services = services;
		return this;
	}
	public Units setPictureURL(String unitpics) {
		this.pictureURL = unitpics;
		return this;
	}
	public Units setActive(boolean isActive) {
		this.isActive = isActive;
		return this;
	}

	public void setUnitComments(List<Comments> unitComments) {
		this.unitComments = unitComments;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
