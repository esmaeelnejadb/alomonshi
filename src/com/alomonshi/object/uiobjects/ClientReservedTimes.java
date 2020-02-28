package com.alomonshi.object.uiobjects;

import com.alomonshi.object.tableobjects.Services;
import com.alomonshi.restwebservices.adaptors.LocalTimeAdaptor;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalTime;
import java.util.List;

public class ClientReservedTimes {

    private int reserveTimeID;
    private int dayID;
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

    public int getReserveTimeID() {
        return reserveTimeID;
    }

    public void setReserveTimeID(int reserveTimeID) {
        this.reserveTimeID = reserveTimeID;
    }

    public int getDayID() {
        return dayID;
    }

    public void setDayID(int dayID) {
        this.dayID = dayID;
    }

    @XmlJavaTypeAdapter(LocalTimeAdaptor.class)
    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getCompanyID() {
        return companyID;
    }

    public void setCompanyID(int companyID) {
        this.companyID = companyID;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getUnitID() {
        return unitID;
    }

    public void setUnitID(int unitID) {
        this.unitID = unitID;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public List<Services> getServices() {
        return services;
    }

    public void setServices(List<Services> services) {
        this.services = services;
    }

    public String getReserveCodeID() {
        return reserveCodeID;
    }

    public void setReserveCodeID(String reserveCodeID) {
        this.reserveCodeID = reserveCodeID;
    }

    public int getCommentID() {
        return commentID;
    }

    public void setCommentID(int commentID) {
        this.commentID = commentID;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public float getCommentRate() {
        return commentRate;
    }

    public void setCommentRate(float commentRate) {
        this.commentRate = commentRate;
    }

    public boolean isCommentable() {
        return commentable;
    }

    public void setCommentable(boolean commentable) {
        this.commentable = commentable;
    }

    public boolean isCancelable() {
        return cancelable;
    }

    public void setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
    }
}
