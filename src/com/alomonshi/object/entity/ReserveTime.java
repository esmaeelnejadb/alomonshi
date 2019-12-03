package com.alomonshi.object.entity;

import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Time;
import java.util.List;

/**
 * @author Behzad
 * Define reserve time object
 */
@XmlRootElement(namespace = " ")
public class ReserveTime {
	
	private int ID;
	private int unitID;
	private int middayID;
	private int dateID;
	private Time startTime;
	private Time duration;
	private int status; // 1: not reserved ; 2: reserved; 3 : canceled; 4: hold; 5 : deleted;
	private int clientID;
	private List<Integer> serviceIDs;
	private String resCodeID;
	
	public int getID() {
		return ID;
	}
	public int getUnitID() {
		return unitID;
	}
	public int getMiddayID() {
		return middayID;
	}
	public int getDateID() {
		return dateID;
	}
	public Time getStartTime() {
		return startTime;
	}
	public Time getDuration() {
		return duration;
	}
	public int getStatus() {
		return status;
	}
	public int getClientID() {
		return clientID;
	}
	public List<Integer> getServiceIDs() {
		return serviceIDs;
	}
	public String getResCodeID() {
		return resCodeID;
	}
	public ReserveTime setUnitID(int unitID) {
		this.unitID = unitID;
		return this;
	}
	public ReserveTime setID(int iD) {
		ID = iD;
		return this;
	}
	public ReserveTime setMiddayID(int middayID) {
		this.middayID = middayID;
		return this;
	}
	public ReserveTime setDateID(int dateID) {
		this.dateID = dateID;
		return this;
	}
	public ReserveTime setStartTime(Time startTime) {
		this.startTime = startTime;
		return this;
	}
	public ReserveTime setDuration(Time duration) {
		this.duration = duration;
		return this;
	}
	public ReserveTime setStatus(int status) {
		this.status = status;
		return this;
	}
	public ReserveTime setClientID(int clientID) {
		this.clientID = clientID;
		return this;
	}
	public ReserveTime setServiceIDs(List<Integer> serviceIDs) {
		this.serviceIDs = serviceIDs;
		return this;
	}
	public ReserveTime setResCodeID(String resCodeID) {
		this.resCodeID = resCodeID;
		return this;
	}		
}
