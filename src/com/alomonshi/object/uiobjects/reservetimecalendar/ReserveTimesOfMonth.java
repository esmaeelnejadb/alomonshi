package com.alomonshi.object.uiobjects.reservetimecalendar;

import java.util.List;

public class ReserveTimesOfMonth {

    private List<ReserveTimeOfDate> reserveTimeOfDateList;
    private int monthNumber;
    private String monthName;
    private int year;

    public List<ReserveTimeOfDate> getReserveTimeOfDateList() {
        return reserveTimeOfDateList;
    }

    public int getMonthNumber() {
        return monthNumber;
    }

    public String getMonthName() {
        return monthName;
    }

    public int getYear() {
        return year;
    }

    public void setReserveTimeOfDateList(List<ReserveTimeOfDate> reserveTimeOfDateList) {
        this.reserveTimeOfDateList = reserveTimeOfDateList;
    }

    public void setMonthNumber(int monthNumber) {
        this.monthNumber = monthNumber;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
