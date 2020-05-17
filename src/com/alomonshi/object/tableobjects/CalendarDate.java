package com.alomonshi.object.tableobjects;

public class CalendarDate {
    private int ID;
    private String date;
    private int year;
    private int month;
    private int dayOfMonth;
    private String monthName;
    private String dayName;
    private int dayOfWeek;

    public int getID() {
        return ID;
    }

    public String getDate() {
        return date;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public String getMonthName() {
        return monthName;
    }

    public String getDayName() {
        return dayName;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

}