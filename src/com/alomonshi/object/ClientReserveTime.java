package com.alomonshi.object;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ClientReserveTime {
	private int clientID;
	private int compID;
	private int unitID;
	private int ServiceID;
	private int dateID;
	private int middayID;
	private int reserveTimeID;
	private int status;
	private String reserveCodeID;
	public int getClientID() {
		return clientID;
	}
	public int getCompID() {
		return compID;
	}
	public int getUnitID() {
		return unitID;
	}
	public int getServiceID() {
		return ServiceID;
	}
	public int getDateID() {
		return dateID;
	}
	public int getMiddayID() {
		return middayID;
	}
	public int getReserveTimeID() {
		return reserveTimeID;
	}
	public int getStatus() {
		return status;
	}
	public String getReserveCodeID() {
		return reserveCodeID;
	}
	public void setClientID(int clientID) {
		this.clientID = clientID;
	}
	public void setCompID(int compID) {
		this.compID = compID;
	}
	public void setUnitID(int unitID) {
		this.unitID = unitID;
	}
	public void setServiceID(int serviceID) {
		ServiceID = serviceID;
	}
	public void setDateID(int dateID) {
		this.dateID = dateID;
	}
	public void setMiddayID(int middayID) {
		this.middayID = middayID;
	}
	public void setReserveTimeID(int reserveTimeID) {
		this.reserveTimeID = reserveTimeID;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public void setReserveCodeID(String reserveCodeID) {
		this.reserveCodeID = reserveCodeID;
	}

}
