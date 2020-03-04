package com.alomonshi.object.tableobjects;

import com.alomonshi.restwebservices.adaptors.LocalDateTimeAdaptor;
import com.alomonshi.restwebservices.views.JsonViews;
import com.fasterxml.jackson.annotation.JsonView;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;
import java.util.List;

@XmlRootElement(namespace = " ")
public class Comments {	
	private int ID;
	private int clientID;
	private int reserveTimeID;
	private String comment;
	private String replyComment;
	private LocalDateTime commentDate;
	private LocalDateTime replyDate;
	private float serviceRate;
	private boolean isActive;
	private String clientName;
	private List<String> serviceNames;

	@JsonView(JsonViews.ClientViews.class)
	public int getID() {
		return ID;
	}

	@JsonView(JsonViews.ClientViews.class)
	public int getClientID() {
		return clientID;
	}

	@JsonView(JsonViews.ClientViews.class)
	public int getReserveTimeID() {
		return reserveTimeID;
	}

	@JsonView(JsonViews.ClientViews.class)
	public String getComment() {
		return comment;
	}

	@JsonView(JsonViews.SubAdminViews.class)
	public String getReplyComment() {
		return replyComment;
	}

	@JsonView(JsonViews.ClientViews.class)
	@XmlJavaTypeAdapter(LocalDateTimeAdaptor.class)
	public LocalDateTime getCommentDate() {
		return commentDate;
	}

	@JsonView(JsonViews.SubAdminViews.class)
	@XmlJavaTypeAdapter(LocalDateTimeAdaptor.class)
	public LocalDateTime getReplyDate() {
		return replyDate;
	}

	@JsonView(JsonViews.ClientViews.class)
	public float getServiceRate() {
		return serviceRate;
	}

	@JsonView(JsonViews.ClientViews.class)
	public String getClientName() {
		return clientName;
	}

	@JsonView(JsonViews.ClientViews.class)
	public List<String> getServiceNames() {
		return serviceNames;
	}

	@JsonView(JsonViews.ManagerViews.class)
	public boolean isActive() {
		return isActive;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public void setClientID(int clientID) {
		this.clientID = clientID;
	}

	public void setReserveTimeID(int reserveTimeID) {
		this.reserveTimeID = reserveTimeID;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setReplyComment(String replyComment) {
		this.replyComment = replyComment;
	}

	public void setCommentDate(LocalDateTime commentDate) {
		this.commentDate = commentDate;
	}

	public void setReplyDate(LocalDateTime replyDate) {
		this.replyDate = replyDate;
	}

	public void setServiceRate(float serviceRate) {
		this.serviceRate = serviceRate;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public void setServiceNames(List<String> serviceNames) {
		this.serviceNames = serviceNames;
	}
}
