package com.alomonshi.object.tableobjects;

import com.alomonshi.restwebservices.adaptors.LocalDateTimeAdaptor;

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
		
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public int getClientID() {
		return clientID;
	}
	public void setClientID(int clientID) {
		this.clientID = clientID;
	}

	public int getReserveTimeID() {
		return reserveTimeID;
	}
	public void setReserveTimeID(int reserveTimeID) {
		this.reserveTimeID = reserveTimeID;
	}

	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getReplyComment() {
		return replyComment;
	}
	public void setReplyComment(String replyComment) {
		this.replyComment = replyComment;
	}

	@XmlJavaTypeAdapter(LocalDateTimeAdaptor.class)
	public LocalDateTime getCommentDate() {
		return commentDate;
	}

	public void setCommentDate(LocalDateTime commentDate) {
		this.commentDate = commentDate;
	}

	@XmlJavaTypeAdapter(LocalDateTimeAdaptor.class)
	public LocalDateTime getReplyDate() {
		return replyDate;
	}

	public void setReplyDate(LocalDateTime replyDate) {
		this.replyDate = replyDate;
	}

	public float getServiceRate() {
		return serviceRate;
	}

	public void setServiceRate(float serviceRate) {
		this.serviceRate = serviceRate;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public List<String> getServiceNames() {
		return serviceNames;
	}

	public void setServiceNames(List<String> serviceNames) {
		this.serviceNames = serviceNames;
	}
}
