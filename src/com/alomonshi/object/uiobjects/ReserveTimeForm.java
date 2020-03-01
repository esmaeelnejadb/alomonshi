package com.alomonshi.object.uiobjects;

import com.alomonshi.restwebservices.adaptors.LocalTimeAdaptor;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalTime;

public class ReserveTimeForm {
    private int clientID;
    private int unitID;
    private int startDate;
    private int endDate;
    private LocalTime morningStartTime;
    private LocalTime morningEndTime;
    private LocalTime afternoonStartTime;
    private LocalTime afternoonEndTime;

    @NotNull
    public int getClientID() {
        return clientID;
    }

    @NotNull
    public int getUnitID() {
        return unitID;
    }

    public int getStartDate() {
        return startDate;
    }

    public int getEndDate() {
        return endDate;
    }

    @XmlJavaTypeAdapter(LocalTimeAdaptor.class)
    public LocalTime getMorningStartTime() {
        return morningStartTime;
    }

    @XmlJavaTypeAdapter(LocalTimeAdaptor.class)
    public LocalTime getMorningEndTime() {
        return morningEndTime;
    }

    @XmlJavaTypeAdapter(LocalTimeAdaptor.class)
    public LocalTime getAfternoonStartTime() {
        return afternoonStartTime;
    }

    @XmlJavaTypeAdapter(LocalTimeAdaptor.class)
    public LocalTime getAfternoonEndTime() {
        return afternoonEndTime;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public void setUnitID(int unitID) {
        this.unitID = unitID;
    }

    public void setStartDate(int startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(int endDate) {
        this.endDate = endDate;
    }

    public void setMorningStartTime(LocalTime morningStartTime) {
        this.morningStartTime = morningStartTime;
    }

    public void setMorningEndTime(LocalTime morningEndTime) {
        this.morningEndTime = morningEndTime;
    }

    public void setAfternoonStartTime(LocalTime afternoonStartTime) {
        this.afternoonStartTime = afternoonStartTime;
    }

    public void setAfternoonEndTime(LocalTime afternoonEndTime) {
        this.afternoonEndTime = afternoonEndTime;
    }
}