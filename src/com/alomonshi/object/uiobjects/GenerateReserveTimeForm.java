package com.alomonshi.object.uiobjects;

import com.alomonshi.restwebservices.adaptors.LocalTimeAdaptor;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalTime;

public class GenerateReserveTimeForm {
    private int unitID;
    private int startDate;
    private int endDate;
    private LocalTime startTime1;
    private LocalTime endTime1;
    private LocalTime startTime2;
    private LocalTime endTime2;

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
    public LocalTime getStartTime1() {
        return startTime1;
    }

    @XmlJavaTypeAdapter(LocalTimeAdaptor.class)
    public LocalTime getEndTime1() {
        return endTime1;
    }

    @XmlJavaTypeAdapter(LocalTimeAdaptor.class)
    public LocalTime getStartTime2() {
        return startTime2;
    }

    @XmlJavaTypeAdapter(LocalTimeAdaptor.class)
    public LocalTime getEndTime2() {
        return endTime2;
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

    public void setStartTime1(LocalTime startTime1) {
        this.startTime1 = startTime1;
    }

    public void setEndTime1(LocalTime endTime1) {
        this.endTime1 = endTime1;
    }

    public void setStartTime2(LocalTime startTime2) {
        this.startTime2 = startTime2;
    }

    public void setEndTime2(LocalTime endTime2) {
        this.endTime2 = endTime2;
    }
}