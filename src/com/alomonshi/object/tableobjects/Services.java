package com.alomonshi.object.tableobjects;

import com.alomonshi.restwebservices.adaptors.LocalDateTimeAdaptor;
import com.alomonshi.restwebservices.views.JsonViews;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;
import java.util.List;

@XmlRootElement(namespace = " ")
public class Services {
	private int ID;
	private int unitID;
	private int clientID;
	private String serviceName;
	private int serviceDuration; // Service duration has been considered in minute
	private int servicePrice;
	private int discountID;
	private int discount;
	private boolean isActive;
	private List<ServicePicture> pictureURLs;
	private String remark;
	private LocalDateTime createDate;
	private LocalDateTime updateDate;
	private LocalDateTime removeDate;

	@JsonView(JsonViews.NormalViews.class)
	public int getID() {
		return ID;
	}

	@JsonView(JsonViews.SubAdminViews.class)
	@JsonIgnore
	public int getUnitID() {
		return unitID;
	}

	@JsonView(JsonViews.SubAdminViews.class)
	public int getClientID() {
		return clientID;
	}

	@JsonView(JsonViews.NormalViews.class)
	public String getServiceName() {
		return serviceName;
	}

	@JsonView(JsonViews.NormalViews.class)
	public int getServiceDuration() {
		return serviceDuration;
	}

	@JsonView(JsonViews.NormalViews.class)
	public int getServicePrice() {
		return servicePrice;
	}

	@JsonView(JsonViews.HiddenViews.class)
	public int getDiscountID() {
		return discountID;
	}

	@JsonView(JsonViews.NormalViews.class)
	public int getDiscount() {
		return discount;
	}

	@JsonView(JsonViews.NormalViews.class)
	@XmlAttribute(name = "servicePictures")
	public List<ServicePicture> getPictureURLs() {
		return pictureURLs;
	}

	@JsonView(JsonViews.NormalViews.class)
	public String getRemark() {
		return remark;
	}

	@JsonView(JsonViews.ManagerViews.class)
	public boolean isActive() {
		return isActive;
	}

	@JsonView(JsonViews.SubAdminViews.class)
	@XmlJavaTypeAdapter(LocalDateTimeAdaptor.class)
	public LocalDateTime getCreateDate() {
		return createDate;
	}

	@JsonView(JsonViews.SubAdminViews.class)
	@XmlJavaTypeAdapter(LocalDateTimeAdaptor.class)
	public LocalDateTime getUpdateDate() {
		return updateDate;
	}

	@JsonView(JsonViews.ManagerViews.class)
	@XmlJavaTypeAdapter(LocalDateTimeAdaptor.class)
	public LocalDateTime getRemoveDate() {
		return removeDate;
	}

	public Services setUnitID(int unitID) {
		this.unitID = unitID;
		return this;
	}

	public void setClientID(int clientID) {
		this.clientID = clientID;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public void setServiceDuration(int serviceDuration) {
		this.serviceDuration = serviceDuration;
	}

	public void setServicePrice(int servicePrice) {
		this.servicePrice = servicePrice;
	}

	public void setDiscountID(int discountID) {
		this.discountID = discountID;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
	}

	public void setID(int ID) {
		this.ID = ID;
	}

	public void setPictureURLs(List<ServicePicture> pictureURLs) {
		this.pictureURLs = pictureURLs;
	}

	public void setActive(boolean active) {
		isActive = active;
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
}
