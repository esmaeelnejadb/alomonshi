package com.alomonshi.object.uiobjects;

import com.alomonshi.restwebservices.views.JsonViews;
import com.fasterxml.jackson.annotation.JsonView;

public class AdminReport {
    private int serviceID;
    private String serviceName;
    private String averageDayHourWorking;
    private String averageMonthHourWorking;
    private String totalHourWorking;
    private int averageDayIncome;
    private int averageMonthIncome;
    private int totalIncome;

    @JsonView(JsonViews.SubAdminViews.class)
    public int getServiceID() {
        return serviceID;
    }

    @JsonView(JsonViews.SubAdminViews.class)
    public String getServiceName() {
        return serviceName;
    }

    @JsonView(JsonViews.SubAdminViews.class)
    public String getAverageDayHourWorking() {
        return averageDayHourWorking;
    }

    @JsonView(JsonViews.SubAdminViews.class)
    public String getAverageMonthHourWorking() {
        return averageMonthHourWorking;
    }

    @JsonView(JsonViews.SubAdminViews.class)
    public String getTotalHourWorking() {
        return totalHourWorking;
    }

    @JsonView(JsonViews.SubAdminViews.class)
    public int getAverageDayIncome() {
        return averageDayIncome;
    }

    @JsonView(JsonViews.SubAdminViews.class)
    public int getAverageMonthIncome() {
        return averageMonthIncome;
    }

    @JsonView(JsonViews.SubAdminViews.class)
    public int getTotalIncome() {
        return totalIncome;
    }

    public void setServiceID(int serviceID) {
        this.serviceID = serviceID;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setAverageDayHourWorking(String averageDayHourWorking) {
        this.averageDayHourWorking = averageDayHourWorking;
    }

    public void setAverageMonthHourWorking(String averageMonthHourWorking) {
        this.averageMonthHourWorking = averageMonthHourWorking;
    }

    public void setTotalHourWorking(String totalHourWorking) {
        this.totalHourWorking = totalHourWorking;
    }

    public void setAverageDayIncome(int averageDayIncome) {
        this.averageDayIncome = averageDayIncome;
    }

    public void setAverageMonthIncome(int averageMonthIncome) {
        this.averageMonthIncome = averageMonthIncome;
    }

    public void setTotalIncome(int totalIncome) {
        this.totalIncome = totalIncome;
    }
}
