package com.example.today_only.entities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Deal {
    private String id;
    private String title;
    private String restaurant;
    private float rating;
    private String time;
    private String location;
    // private List<Date> availableDates;
    private List<Integer> availableDays;

    public Deal(String title, String restaurant, float rating, String time, String location, List<Integer> availableDays) {
        this.id = generateId(title, restaurant);
        this.title = title;
        this.restaurant = restaurant;
        this.rating = rating;
        this.time = time;
        this.location = location;
        // this.availableDates = availableDates;
        this.availableDays = availableDays;
    }

    // Getters and Setters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getRestaurant() { return restaurant; }
    public float getRating() { return rating; }
    public String getTime() { return time; }
    public String getLocation() { return location; }
    // public List<Date> getAvailableDates() { return availableDates; }
    public List<Integer> getAvailableDays() { return availableDays; }

    public void setTitle(String title) { this.title = title; }
    public void setRestaurant(String restaurant) { this.restaurant = restaurant; }
    public void setRating(float rating) { this.rating = rating; }
    public void setTime(String time) { this.time = time; }
    public void setLocation(String location) { this.location = location; }
    // public void setAvailableDates(List<Date> availableDates) { this.availableDates = availableDates; }
    public void setAvailableDays(List<Integer> availableDays) { this.availableDays = availableDays; }

    private String generateId(String title, String restaurant) {
        return title.toLowerCase().replaceAll("\\s+", "_") + "_" +
                restaurant.toLowerCase().replaceAll("\\s+", "_");
    }

    public boolean isAvailableOnDay(int dayOfWeek) {
        return availableDays.contains(dayOfWeek);
    }

    public String getAvailableDatesAsString() {
        StringBuilder builder = new StringBuilder();
        for (Integer day : availableDays) {
            if (builder.length() > 0) {
                builder.append(", ");
            }
            switch (day) {
                case Calendar.MONDAY: builder.append("Mon"); break;
                case Calendar.TUESDAY: builder.append("Tue"); break;
                case Calendar.WEDNESDAY: builder.append("Wed"); break;
                case Calendar.THURSDAY: builder.append("Thu"); break;
                case Calendar.FRIDAY: builder.append("Fri"); break;
                case Calendar.SATURDAY: builder.append("Sat"); break;
                case Calendar.SUNDAY: builder.append("Sun"); break;
            }
        }
        return builder.toString();
    }

//    public boolean isAvailableOnDate(Date date) {
//        Calendar cal1 = Calendar.getInstance();
//        Calendar cal2 = Calendar.getInstance();
//        cal2.setTime(date);
//
//        for (Date availableDate : availableDates) {
//            cal1.setTime(availableDate);
//            if (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
//                    cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    // Method to get available dates as a formatted string (e.g., "Mon, Wed, Fri")
//    public String getAvailableDatesAsString() {
//        SimpleDateFormat dayFormat = new SimpleDateFormat("EEE", Locale.getDefault());
//        StringBuilder builder = new StringBuilder();
//        Calendar cal = Calendar.getInstance();
//        for (Date availableDate : availableDates) {
//            cal.setTime(availableDate);
//            String day = dayFormat.format(cal.getTime());
//            if (!builder.toString().contains(day)) {
//                if (builder.length() > 0) {
//                    builder.append(", ");
//                }
//                builder.append(day);
//            }
//        }
//        return builder.toString();
//    }
}
