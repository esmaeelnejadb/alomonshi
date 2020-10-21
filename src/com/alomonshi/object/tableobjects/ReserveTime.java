package com.alomonshi.object.tableobjects;

import com.alomonshi.object.enums.MiddayID;
import com.alomonshi.object.enums.ReserveTimeStatus;
import com.alomonshi.restwebservices.adaptors.LocalDateTimeAdaptor;
import com.alomonshi.restwebservices.adaptors.LocalTimeAdaptor;
import com.alomonshi.restwebservices.views.JsonViews;
import com.fasterxml.jackson.annotation.JsonView;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * @author Behzad
 * Define reserve time object
 */
@XmlRootElement
public class ReserveTime {

	private int ID;
	private int unitID;
	private MiddayID middayID;
	private int dateID;
	private LocalTime startTime;
	private int duration;
	private ReserveTimeStatus status; // 1: not reserved ; 2: reserved; 3 : canceled; 4: hold; 5 : deleted;
	private int clientID;
	private List<Integer> serviceIDs;
	private String resCodeID;
	private LocalDateTime reserveTimeGRDateTime;

	@JsonView(JsonViews.ClientViews.class)
	public int getID() {
		return ID;
	}

	@JsonView(JsonViews.ManagerViews.class)
	public int getUnitID() {
		return unitID;
	}

	@JsonView(JsonViews.SubAdminViews.class)
	public MiddayID getMiddayID() {
		return middayID;
	}

	@JsonView(JsonViews.SubAdminViews.class)
	public int getDateID() {
		return dateID;
	}

	@XmlElement
	@XmlJavaTypeAdapter(LocalTimeAdaptor.class)
	@JsonView(JsonViews.ClientViews.class)
	public LocalTime getStartTime() {
		return startTime;
	}

	@JsonView(JsonViews.ClientViews.class)
	public int getDuration() {
		return duration;
	}

	@JsonView(JsonViews.SubAdminViews.class)
	public ReserveTimeStatus getStatus() {
		return status;
	}

	@JsonView(JsonViews.AdminViews.class)
	public int getClientID() {
		return clientID;
	}

	@JsonView(JsonViews.AdminViews.class)
	public List<Integer> getServiceIDs() {
		return serviceIDs;
	}

	@JsonView(JsonViews.ClientViews.class)
	public String getResCodeID() {
		return resCodeID;
	}

	@XmlJavaTypeAdapter(LocalDateTimeAdaptor.class)
	@JsonView(JsonViews.ManagerViews.class)
	public LocalDateTime getReserveTimeGRDateTime() {
		return reserveTimeGRDateTime;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public void setUnitID(int unitID) {
		this.unitID = unitID;
	}

	public void setMiddayID(MiddayID middayID) {
		this.middayID = middayID;
	}

	public void setDateID(int dateID) {
		this.dateID = dateID;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public void setStatus(ReserveTimeStatus status) {
		this.status = status;
	}

	public void setClientID(int clientID) {
		this.clientID = clientID;
	}

	public void setServiceIDs(List<Integer> serviceIDs) {
		this.serviceIDs = serviceIDs;
	}

	public void setResCodeID(String resCodeID) {
		this.resCodeID = resCodeID;
	}

	public void setReserveTimeGRDateTime(LocalDateTime reserveTimeGRDateTime) {
		this.reserveTimeGRDateTime = reserveTimeGRDateTime;
	}
}
