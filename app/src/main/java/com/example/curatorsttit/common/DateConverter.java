package com.example.curatorsttit.common;

import androidx.room.TypeConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateConverter {
    private static final String TWITTER_RESPONSE_FORMAT="EEE MMM dd HH:mm:ss ZZZZZ yyyy"; // Thu Oct 26 07:31:08 +0000 2017
    private static final String MONTH_DAY_FORMAT = "MMM d"; // Oct 26
    public static final String DATE_TIME_MS_SQL = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String DATE_STANDART = "EEE MMM dd";
    public static final String DATE_MY = "dd.MM.yyyy";

    private String getFormattedDate(String rawDate) {
        SimpleDateFormat utcFormat = new SimpleDateFormat(TWITTER_RESPONSE_FORMAT, Locale.ROOT);
        SimpleDateFormat displayedFormat = new SimpleDateFormat(MONTH_DAY_FORMAT, Locale.getDefault());
        try {
            Date date = utcFormat.parse(rawDate);
            return displayedFormat.format(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    public static String getNyFormattedDate(String rawDate) {
        SimpleDateFormat utcFormat = new SimpleDateFormat(TWITTER_RESPONSE_FORMAT, Locale.ROOT);
        SimpleDateFormat displayedFormat = new SimpleDateFormat(DATE_MY, Locale.getDefault());
        try {
            Date date = utcFormat.parse(rawDate);
            return displayedFormat.format(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
