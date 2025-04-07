package com.example.today_only.utilities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarUtil {

    public List<Date> getCurrentWeek() {
        List<Date> currentWeek = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

        for (int i = 0; i < 7; i++) {
            currentWeek.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_WEEK, 1);
        }

        return currentWeek;
    }


}
