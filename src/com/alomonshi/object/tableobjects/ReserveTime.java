package com.alomonshi.object.tableobjects;

import com.alomonshi.object.views.JsonViews;
import org.codehaus.jackson.map.annotate.JsonView;

import java.sql.Time;
import java.util.List;

/**
 * @author Behzad
 * Define reserve time object
 */
public class ReserveTime {

	private int ID;
	@JsonView(JsonViews.HiddenViews.class)
	private int unitID;
	@JsonView(JsonViews.HiddenViews.class)
	private int middayID;
	@JsonView(JsonViews.HiddenViews.class)
	private int dateID;
	@JsonView(JsonViews.NormalViews.class)
	private Time startTime;
	@JsonView(JsonViews.HiddenViews.class)
	private Time duration;
	@JsonView(JsonViews.AdminViews.class)
	private int status; // 1: not reserved ; 2: reserved; 3 : canceled; 4: hold; 5 : deleted;
	@JsonView(JsonViews.ClientViews.class)
	private int clientID;
	@JsonView(JsonViews.ClientViews.class)
	private List<Integer> serviceIDs;
	@JsonView(JsonViews.ClientViews.class)
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
