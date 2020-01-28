package com.alomonshi.object.tableobjects;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace = " ")
public class Comments {	
	private int ID;
	private int clientID;
	private int unitID;
	private int resTimeID;
	private String comment;
	private String replyComment;
	private String commentDate;
	private float serviceRate;
	private boolean isActive;
		
	public int getID() {
		return ID;
	}
	public Comments setID(int iD) {
		ID = iD;
		return this;
	}
	public int getClientID() {
		return clientID;
	}
	public Comments setClientID(int clientID) {
		this.clientID = clientID;
		return this;
	}
	public int getUnitID() {
		return unitID;
	}
	public Comments setUnitID(int unitID) {
		this.unitID = unitID;
		return this;
	}
	public int getResTimeID() {
		return resTimeID;
	}
	public Comments setResTimeID(int resTimeID) {
		this.resTimeID = resTimeID;
		return this;
	}
	public String getComment() {
		return comment;
	}
	public Comments setComment(String comment) {
		this.comment = comment;
		return this;
	}
	public String getReplyComment() {
		return replyComment;
	}
	public Comments setReplyComment(String replyComment) {
		this.replyComment = replyComment;
		return this;
	}

	public String getCommentDate() {
		return commentDate;
	}

	public Comments setCommentDate(String commentDate) {
		this.commentDate = commentDate;
		return this;
	}

	public float getServiceRate() {
		return serviceRate;
	}
	public Comments setServiceRate(float serviceRate) {
		this.serviceRate = serviceRate;
		return this;
	}
	public boolean getIsACtive() {
		return isActive;
	}
	public Comments setIsACtive(boolean isACtive) {
		this.isActive = isACtive;
		return this;
	}	
}
