package com.alomonshi.object.entity;


import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(namespace = " ")
public class Services {
	private int ID;
	private int unitID;
	private String serviceName;
	private String serviceTime;
	private int servicePrice;
	private boolean isActive;
	private List<String> pictureURLs;
	
	public boolean getIsActive() {
		return isActive;
	}
	public int getID() {
		return ID;
	}
	public int getUnitID() {
		return unitID;
	}
	public String getServiceName() {
		return serviceName;
	}
	public String getServiceTime() {
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
	public Services setServiceTime(String serviceTime) {
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

	public List<String> getPictureURLs() {
		return pictureURLs;
	}

	public void setPictureURLs(List<String> pictureURLs) {
		this.pictureURLs = pictureURLs;
	}
}
