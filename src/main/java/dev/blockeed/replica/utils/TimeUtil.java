package dev.blockeed.replica.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {

    public static String convertMillisToTimeFormat(long millis) {
        // Calculate minutes, seconds, and milliseconds
        long minutes = (millis / 1000) / 60;
        long seconds = (millis / 1000) % 60;
        long milliseconds = millis % 1000;

        // Format the time to mm:ss.milliseconds
        return String.format("%02d:%02d.%03d", minutes, seconds, milliseconds);
    }

    public static String getFormattedDate() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return dateFormat.format(date);
    }

    public static String getFormattedTime(int i) {
        int minutes = i / 60; // calculates the number of minutes
        int seconds = i % 60; // calculates the remaining seconds

        String formattedTime = String.format("%d:%02d", minutes, seconds);
        return formattedTime;
    }

}
