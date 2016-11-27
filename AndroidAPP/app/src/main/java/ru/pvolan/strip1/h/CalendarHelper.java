package ru.pvolan.strip1.h;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class CalendarHelper
{

    public static String toNiceString(Calendar c){
        return format(c, "yyyy-MM-dd HH:mm:ss Z");
    }

    public static String format(Calendar c, String format, TimeZone timeZone){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        simpleDateFormat.setCalendar(c);
        simpleDateFormat.setTimeZone(timeZone);
        return simpleDateFormat.format(c.getTime());
    }

    public static String format(Calendar c, String format){
        return format(c, format, TimeZone.getDefault());
    }




    public static Calendar createFromUnix(long unixTimestamp){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(unixTimestamp*1000);
        return c;
    }


    public static long toUnix(Calendar c){
        return c.getTimeInMillis () / 1000;
    }

}
