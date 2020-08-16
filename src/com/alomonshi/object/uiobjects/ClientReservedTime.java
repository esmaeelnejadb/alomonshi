package com.alomonshi.object.uiobjects;

import com.alomonshi.object.tableobjects.Services;
import com.alomonshi.restwebservices.adaptors.LocalDateTimeAdaptor;
import com.alomonshi.restwebservices.adaptors.LocalTimeAdaptor;
import com.alomonshi.restwebservices.adaptors.PersianDateIDAdaptor;
import com.alomonshi.restwebservices.views.JsonViews;
import com.fasterxml.jackson.annotation.JsonView;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class ClientReservedTime {

    private int reserveTimeID;
    private Integer dayID;
    private LocalTime startTime;
    private int duration;
    private int cost;
    private int companyID;
    private String companyName;
    private int unitID;
    private String unitName;
    private List<Services> services;
    private String reserveCodeID;
    private int commentID;
    private String comment;
    private float commentRate;
    private boolean commentable;
    private boolean cancelable;
    private LocalDateTime GregorianDateTime;
    private String companyCover;

    @JsonView({JsonViews.ClientViews.class})
    public int getReserveTimeID() {
        return reserveTimeID;
    }

    @XmlJavaTypeAdapter(PersianDateIDAdaptor.class)
    @JsonView({JsonViews.ClientViews.class})
    public Integer getDayID() {
        return dayID;
    }

    @XmlJavaTypeAdapter(LocalTimeAdaptor.class)
    @JsonView({JsonViews.ClientViews.class})
    public LocalTime getStartTime() {
        return startTime;
    }

    @JsonView({JsonViews.ClientViews.class})
    public int getDuration() {
        return duration;
    }

    @JsonView({JsonViews.ClientViews.class})
    public int getCost() {
        return cost;
    }

    @JsonView({JsonViews.ClientViews.class})
    public int getCompanyID() {
        return companyID;
    }

    @JsonView({JsonViews.ClientViews.class})
    public String getCompanyName() {
        return companyName;
    }

    @JsonView({JsonViews.ClientViews.class})
    public int getUnitID() {
        return unitID;
    }

    @JsonView({JsonViews.ClientViews.class})
    public String getUnitName() {
        return unitName;
    }

    @JsonView({JsonViews.ClientViews.class})
    public List<Services> getServices() {
        return services;
    }

    @JsonView({JsonViews.ClientViews.class})
    public String getReserveCodeID() {
        return reserveCodeID;
    }


    @JsonView({JsonViews.ClientViews.class})
    public int getCommentID() {
        return commentID;
    }

    @JsonView({JsonViews.ClientViews.class})
    public String getComment() {
        return comment;
    }

    @JsonView({JsonViews.ClientViews.class})
    public float getCommentRate() {
        return commentRate;
    }

    @JsonView({JsonViews.ClientViews.class})
    public boolean isCommentable() {
        return commentable;
    }

    @JsonView({JsonViews.ClientViews.class})
    @XmlJavaTypeAdapter(LocalDateTimeAdaptor.class)
    public LocalDateTime getGregorianDateTime() {
        return GregorianDateTime;
    }

    @JsonView({JsonViews.ClientViews.class})
    public String getCompanyCover() {
        return companyCover;
    }

    public boolean isCancelable() {
        return cancelable;
    }

    public void setReserveTimeID(int reserveTimeID) {
        this.reserveTimeID = reserveTimeID;
    }

    public void setDayID(Integer dayID) {
        this.dayID = dayID;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setCompanyID(int companyID) {
        this.companyID = companyID;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setUnitID(int unitID) {
        this.unitID = unitID;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public void setServices(List<Services> services) {
        this.services = services;
    }

    public void setReserveCodeID(String reserveCodeID) {
        this.reserveCodeID = reserveCodeID;
    }

    public void setCommentID(int commentID) {
        this.commentID = commentID;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setCommentRate(float commentRate) {
        this.commentRate = commentRate;
    }

    public void setCommentable(boolean commentable) {
        this.commentable = commentable;
    }

    public void setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
    }

    public void setGregorianDateTime(LocalDateTime gregorianDateTime) {
        GregorianDateTime = gregorianDateTime;
    }

    public void setCompanyCover(String companyCover) {
        this.companyCover = companyCover;
    }
}
