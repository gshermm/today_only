package com.example.today_only.entities;

import java.util.Calendar;

public class Day {
    private Calendar date;
    private boolean isToday;
    private boolean isSelected;

    public Day(Calendar date, boolean isToday) {
        this.date = date;
        this.isToday = isToday;
        this.isSelected = false;
    }

    public Calendar getDate() {
        return date;
    }

    public boolean isToday() {
        return isToday;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public void setToday(boolean today) {
        isToday = today;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
