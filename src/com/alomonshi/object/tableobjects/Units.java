package com.alomonshi.object.tableobjects;

import com.alomonshi.restwebservices.adaptors.LocalDateTimeAdaptor;
import com.alomonshi.restwebservices.views.JsonViews;
import com.fasterxml.jackson.annotation.JsonView;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;
import java.util.List;

@XmlRootElement(namespace = " ")
public class Units {
	private int ID;
	private int companyID;
	private int clientID;
	private String unitName;
	private int unitDuration;// Unit duration has been considered in minute
	private Boolean isActive;
	private List<Services> services;
	private String pictureURL;
	private List<Comments> unitComments;
	private String remark;
	private LocalDateTime createDate;
	private LocalDateTime updateDate;
	private LocalDateTime removeDate;
	private boolean onlineReserve;


	@JsonView(JsonViews.NormalViews.class)
	public int getID() {
		return ID;
	}

	@JsonView(JsonViews.HiddenViews.class)
	public int getCompanyID() {
		return companyID;
	}

	@JsonView(JsonViews.AdminViews.class)
	public int getClientID() {
		return clientID;
	}

	@JsonView(JsonViews.ClientViews.class)
	public String getUnitName() {
		return unitName;
	}

	@JsonView(JsonViews.AdminViews.class)
	public int getUnitDuration() {
		return unitDuration;
	}

	@XmlAttribute(name = "services")
	@JsonView(JsonViews.ClientViews.class)
	public List<Services> getServices() {
		return services;
	}

	@JsonView(JsonViews.AdminViews.class)
	public String getPictureURL() {
		return pictureURL;
	}

	@JsonView(JsonViews.ManagerViews.class)
	public Boolean isActive() {
		return isActive;
	}

	@JsonView(JsonViews.ClientViews.class)
	@XmlAttribute(name = "comments")
	public List<Comments> getUnitComments() {
		return unitComments;
	}

	@JsonView(JsonViews.AdminViews.class)
	public String getRemark() {
		return remark;
	}

	@XmlJavaTypeAdapter(LocalDateTimeAdaptor.class)
	@JsonView(JsonViews.AdminViews.class)
	public LocalDateTime getCreateDate() {
		return createDate;
	}

    @XmlJavaTypeAdapter(LocalDateTimeAdaptor.class)
	@JsonView(JsonViews.AdminViews.class)
	public LocalDateTime getUpdateDate() {
		return updateDate;
	}

    @XmlJavaTypeAdapter(LocalDateTimeAdaptor.class)
	@JsonView(JsonViews.ManagerViews.class)
	public LocalDateTime getRemoveDate() {
		return removeDate;
	}

	@JsonView(JsonViews.SubAdminViews.class)
	public boolean isOnlineReserve() {
		return onlineReserve;
	}

	public void setID(int id) {
		ID = id;
	}

	public void setCompanyID(int companyID) {
		this.companyID = companyID;
	}

	public void setClientID(int clientID) {
		this.clientID = clientID;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public void setUnitDuration(int unitDuration) {
		this.unitDuration = unitDuration;
	}

	public void setServices(List<Services> services) {
		this.services = services;
	}

	public void setPictureURL(String unitPictures) {
		this.pictureURL = unitPictures;
	}

	public void setActive(Boolean active) {
		isActive = active;
	}

	public void setUnitComments(List<Comments> unitComments) {
		this.unitComments = unitComments;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}

	public void setUpdateDate(LocalDateTime updateDate) {
		this.updateDate = updateDate;
	}

	public void setRemoveDate(LocalDateTime removeDate) {
		this.removeDate = removeDate;
	}

	public void setOnlineReserve(boolean onlineReserve) {
		this.onlineReserve = onlineReserve;
	}
}
