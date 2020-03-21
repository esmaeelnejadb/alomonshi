package com.alomonshi.object.tableobjects;

import com.alomonshi.restwebservices.views.JsonViews;
import com.fasterxml.jackson.annotation.JsonView;

import java.time.LocalDateTime;

public class CompanyPicture {

    private int ID;
    private int companyID;
    private boolean isActive;
    private String pictureURL;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private LocalDateTime removeDate;

    @JsonView(JsonViews.AdminViews.class)
    public int getID() {
        return ID;
    }

    @JsonView(JsonViews.AdminViews.class)
    public int getCompanyID() {
        return companyID;
    }

    @JsonView(JsonViews.ManagerViews.class)
    public boolean isActive() {
        return isActive;
    }

    @JsonView(JsonViews.NormalViews.class)
    public String getPictureURL() {
        return pictureURL;
    }

    @JsonView(JsonViews.AdminViews.class)
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    @JsonView(JsonViews.AdminViews.class)
    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    @JsonView(JsonViews.AdminViews.class)
    public LocalDateTime getRemoveDate() {
        return removeDate;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setCompanyID(int companyID) {
        this.companyID = companyID;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
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
