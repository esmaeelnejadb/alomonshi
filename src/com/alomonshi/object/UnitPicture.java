package com.alomonshi.object;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Date;

@XmlRootElement(namespace = " ")
public class UnitPicture {
	private int ID;
	private int unitID;
	private int serviceID;
	private String caption;
	private String picURL;
	private Date date;
	private boolean isActive;
	
	public int getID() {
		return ID;
	}
	
	public UnitPicture setID(int ID) {
		this.ID = ID;
		return this;
	}
	
	public int getUnitID() {
		return unitID;
	}
	public UnitPicture setUnitID(int unitID) {
		this.unitID = unitID;
		return this;
	}
	public int getServiceID() {
		return serviceID;
	}
	public UnitPicture setServiceID(int serviceID) {
		this.serviceID = serviceID;
		return this;
	}
	public String getCaption() {
		return caption;
	}
	public UnitPicture setCaption(String caption) {
		this.caption = caption;
		return this;
	}
	public String getPicURL() {
		return picURL;
	}
	public UnitPicture setPicURL(String picURL) {
		this.picURL = picURL;
		return this;
	}
	public Date getDate() {
		return date;
	}
	public UnitPicture setDate(Date date) {
		this.date = date;
		return this;
	}
	public boolean getIsActive() {
		return isActive;
	}
	public UnitPicture setIsActive(boolean isActive) {
		this.isActive = isActive;
		return this;
	}
}
