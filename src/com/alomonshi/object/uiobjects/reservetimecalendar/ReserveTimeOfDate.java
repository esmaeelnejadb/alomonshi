package com.alomonshi.object.uiobjects.reservetimecalendar;

import com.alomonshi.object.tableobjects.CalendarDate;

public class ReserveTimeOfDate {

    private CalendarDate calendarDate;
    private boolean isActive;
    private int emptyReserveTimeNumbers;

    public CalendarDate getCalendarDate() {
        return calendarDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public int getEmptyReserveTimeNumbers() {
        return emptyReserveTimeNumbers;
    }

    public void setCalendarDate(CalendarDate calendarDate) {
        this.calendarDate = calendarDate;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setEmptyReserveTimeNumbers(int emptyReserveTimeNumbers) {
        this.emptyReserveTimeNumbers = emptyReserveTimeNumbers;
    }
}
