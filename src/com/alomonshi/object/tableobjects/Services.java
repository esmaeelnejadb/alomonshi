package com.alomonshi.object.tableobjects;


//import org.codehaus.jackson.annotate.JsonIgnore;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(namespace = " ")
public class Services {
	private int ID;
	private int unitID;
	private String serviceName;
	private int serviceTime;	// Service duration has been considered in minute
	private int servicePrice;
	private boolean isActive;
	private List<ServicePicture> pictureURLs;
	private String remark;

	public int getID() {
		return ID;
	}
    @JsonIgnore
	public int getUnitID() {
		return unitID;
	}
	public String getServiceName() {
		return serviceName;
	}
	public int getServiceTime() {
		return serviceTime;
	}
	public int getServicePrice() {
		return servicePrice;
	}
	public Services setUnitID(int unitID) {
		this.unitID = unitID;
		return this;
	}
	public Services setServiceName(String serviceName) {
		this.serviceName = serviceName;
		return this;
	}
	public Services setServiceTime(int serviceTime) {
		this.serviceTime = serviceTime;
		return this;
	}
	public Services setServicePrice(int servicePrice) {
		this.servicePrice = servicePrice;
		return this;
	}
	public Services setID(int ID) {
		this.ID = ID;
		return this;
	}
	public Services setIsActive(boolean isActive) {
		this.isActive = isActive;
		return this;
	}

	@XmlAttribute(name = "servicePictures")
	public List<ServicePicture> getPictureURLs() {
		return pictureURLs;
	}

	public void setPictureURLs(List<ServicePicture> pictureURLs) {
		this.pictureURLs = pictureURLs;
	}

    @JsonIgnore
	public boolean isActive() {
		return isActive;
	}

	public Services setActive(boolean active) {
		isActive = active;
		return this;
	}

	public String getRemark() {
		return remark;
	}

	public Services setRemark(String remark) {
		this.remark = remark;
		return this;
	}
}
