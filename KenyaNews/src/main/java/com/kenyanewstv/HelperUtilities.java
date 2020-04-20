package com.kenyanewstv;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HelperUtilities {

    private static final String TAG = "HelperUtilities";

    public static String getTimePeriodForDaysElapsed(int daysPassed) {
        String toReturn;
        if (daysPassed == 0) {
            toReturn = "Today";
        } else if (daysPassed == 1) {
            toReturn = "Yesterday";
        } else if (daysPassed < 7) {
            toReturn = daysPassed + " days ago";
        } else if (daysPassed < 14) {
            toReturn = "A week ago";
        } else if (daysPassed < 21) {
            toReturn = "2 weeks ago";
        } else if (daysPassed < 28) {
            toReturn = "3 weeks ago";
        } else if (daysPassed < 60) {
            toReturn = "A month ago";
        } else if (daysPassed < 365) {
            toReturn = daysPassed / 30 + " months ago";
        } else {
            toReturn = (int) Math.round(daysPassed / 365.0) + " years ago";
        }
        return toReturn;
    }

    public static String pluralizeItem(int itemCount, String descriptor) {
        String toReturn;
        String suffix = itemCount == 1 ? "" : "s";
        toReturn = itemCount + " " + descriptor + suffix;
        return toReturn;
    }

    public static String getShortDateFromDaysElapsed(int daysElapsed) {
        String toReturn;

        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, daysElapsed * -1);
        Date date = calendar.getTime();

        DateFormat inputFormat = new SimpleDateFormat("MMM dd, yyyy");
        toReturn = inputFormat.format(date);

        return toReturn;
    }

    public static String getShortDateFromLongDate(long longDate) {
        String toReturn;
        Date date = new Date();
        date.setTime(longDate);

        DateFormat inputFormat = new SimpleDateFormat("MMM dd, yyyy");
        toReturn = inputFormat.format(date);

        return toReturn;
    }
}
