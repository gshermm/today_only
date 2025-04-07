package com.example.today_only.utilities;

import com.example.today_only.entities.Deal;

import java.util.*;

public final class DealUtil {

    private List<Deal> allDeals;

    public List<Deal> getAllDeals() {
        if (allDeals == null) {
            loadAllDeals();
        }
        return allDeals;
    }

    public void loadAllDeals() {
        allDeals = getSampleDeals();
    }

    public List<Deal> showDealsForDay(int dayOfWeek) {
        List<Deal> dealsForDay = new ArrayList<>();
        for (Deal deal : getAllDeals()) {
            if (deal.isAvailableOnDay(dayOfWeek)) {
                dealsForDay.add(deal);
            }
        }
        return dealsForDay;
    }

//    public List<Deal> showDealsForDate(Date date) {
//        List<Deal> dealsForDate = new ArrayList<>();
//        for (Deal deal : getAllDeals()) {
//            if (deal.isAvailableOnDate(date)) {
//                dealsForDate.add(deal);
//            }
//        }
//        return dealsForDate;
//    }
//
//    public List<Deal> showDealsForWeek() {
//        List<Deal> dealsForWeek = new ArrayList<>();
//        Calendar cal = Calendar.getInstance();
//        int currentWeek = cal.get(Calendar.WEEK_OF_YEAR);
//        int currentYear = cal.get(Calendar.YEAR);
//
//        for (Deal deal : getAllDeals()) {
//            for (Date availableDate : deal.getAvailableDates()) {
//                cal.setTime(availableDate);
//                if (cal.get(Calendar.WEEK_OF_YEAR) == currentWeek &&
//                        cal.get(Calendar.YEAR) == currentYear) {
//                    dealsForWeek.add(deal);
//                    break;
//                }
//            }
//        }
//        return dealsForWeek;
//    }
//
//    public List<Deal> showDealsForMonth() {
//        List<Deal> dealsForMonth = new ArrayList<>();
//        Calendar cal = Calendar.getInstance();
//        int currentMonth = cal.get(Calendar.MONTH);
//        int currentYear = cal.get(Calendar.YEAR);
//
//        for (Deal deal : getAllDeals()) {
//            for (Date availableDate : deal.getAvailableDates()) {
//                cal.setTime(availableDate);
//                if (cal.get(Calendar.MONTH) == currentMonth &&
//                        cal.get(Calendar.YEAR) == currentYear) {
//                    dealsForMonth.add(deal);
//                    break;
//                }
//            }
//        }
//        return dealsForMonth;
//    }

    // Generates sample deals with realistic dates
    public List<Deal> getSampleDeals() {
        List<Deal> deals = new ArrayList<>();
//        Random random = new Random();
//
//        String[] titles = {"Discount Meal", "Happy Hour", "Special Offer", "Buy One Get One", "Limited Time Deal"};
//        String[] restaurants = {"Murphy's Pub", "BrewLab", "Joe's Bar", "The Diner", "Sushi Place"};
//        float[] ratings = {3.5f, 4.0f, 4.5f, 5.0f};
//        String[] times = {"2pm-4pm", "4pm-6pm", "6pm-8pm", "8pm-10pm"};
//        String[] locations = {"0.5 miles away", "1.2 miles away", "0.7 miles away", "2 miles away", "1 mile away"};
//
//        for (int i = 0; i < 10; i++) {
//            String title = titles[random.nextInt(titles.length)];
//            String restaurant = restaurants[random.nextInt(restaurants.length)];
//            float rating = ratings[random.nextInt(ratings.length)];
//            String time = times[random.nextInt(times.length)];
//            String location = locations[random.nextInt(locations.length)];
//
//            Set<Date> availableDates = new HashSet<>();
//            Calendar dateCal = Calendar.getInstance();
//
//            int numberOfAvailableDays = random.nextInt(15) + 1;
//            for (int j = 0; j < numberOfAvailableDays; j++) {
//                dateCal = Calendar.getInstance();
//                dateCal.add(Calendar.DAY_OF_MONTH, random.nextInt(30));
//                availableDates.add(dateCal.getTime());
//            }
//
//            deals.add(new Deal(title, restaurant, rating, time, location, new ArrayList<>(availableDates)));
//        }

        deals.add(new Deal(
                "$3.50 Mocha Mondays",
                "Espresso Royale",
                5.0f, "10am-10pm",
                "0.4 miles away",
                Arrays.asList(Calendar.MONDAY)
        ));
        deals.add(new Deal(
                "$3 Latte Wednesdays",
                "Espresso Royale",
                5.0f, "10am-10pm",
                "0.4 miles away",
                Arrays.asList(Calendar.WEDNESDAY)
        ));
        deals.add(new Deal(
                "$3 Chai Fridays",
                "Espresso Royale",
                5.0f, "10am-10pm",
                "0.4 miles away",
                Arrays.asList(Calendar.FRIDAY)
        ));
        deals.add(new Deal(
                "$10 for 5 tacos",
                "Juanito's",
                5.0f, "12pm-4pm",
                "0.4 miles away",
                Arrays.asList(Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY)
        ));
        deals.add(new Deal(
                "Half Price Burgers",
                "Murphy's Pub",
                5.0f, "5pm-9pm",
                "0.3 miles away",
                Arrays.asList(Calendar.MONDAY)
        ));
        deals.add(new Deal(
                "BOGO Happy Hour",
                "Latea Boba Tea",
                4.0f, "3pm-8pm",
                "0.35 miles away",
                Arrays.asList(Calendar.TUESDAY)
        ));
        deals.add(new Deal(
                "$0.70 Boneless Wings",
                "Wingstop",
                4.5f, "10am-2pm",
                "1.1 miles away",
                Arrays.asList(Calendar.MONDAY, Calendar.TUESDAY)
        ));
        deals.add(new Deal(
                "10% Student Discount",
                "Shawarma Joint",
                4.5f, "12pm-6pm",
                "0.4 miles away",
                Arrays.asList(Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY)
        ));
        deals.add(new Deal(
                "$3 Cheeseburger",
                "Brother's Bar and Grill",
                4.5f, "4pm-10pm",
                "0.9 miles away",
                Arrays.asList(Calendar.TUESDAY)
        ));
        deals.add(new Deal(
                "10% Student Discount",
                "Culver's",
                4.5f, "10pm-10pm",
                "0.8 miles away",
                Arrays.asList(Calendar.SATURDAY, Calendar.SUNDAY)
        ));
        deals.add(new Deal(
                "Two Entree Lunch Special",
                "EVO Cafe",
                5.0f, "11am-3pm",
                "0.2 miles away",
                Arrays.asList(Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY)
        ));
        return deals;
    }
}
