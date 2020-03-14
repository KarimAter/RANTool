package Helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class ToolCalendar {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("ww_yyyy");

    public static String getWeekName(LocalDate localDate) {
        Calendar calendar = Calendar.getInstance();
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        calendar.setTime(date);
        return dateFormat.format(calendar.getTime());
    }

    public static String getPreviousWeek(String weekName) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(dateFormat.parse(weekName));
            calendar.add(Calendar.WEEK_OF_YEAR, -1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateFormat.format(calendar.getTime());
    }

    public static String getNextWeek(String weekName) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(dateFormat.parse(weekName));
            calendar.add(Calendar.WEEK_OF_YEAR, 1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateFormat.format(calendar.getTime());
    }
}
