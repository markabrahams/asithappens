/*
 * SummaryStatisticsPanel.java
 *
 * Created on 20 January 2010, 19:57
 *
 * AsItHappens - real-time network monitor
 * Copyright (C) 2006  Mark Abrahams
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */
package nz.co.abrahams.asithappens.mainui;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.text.DecimalFormat;

/**
 *
 * @author mark
 */
public class TextFormatter {

    /** Date format */
    private static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss.SSS");
    /** Date/Time format */
    private static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
    /** Format for statistics */
    private static final DecimalFormat valueFormat = new DecimalFormat("#,###");
    /* Calendar for time representation */
    private static GregorianCalendar calendar = new GregorianCalendar();

    private TextFormatter() {
    }

    /**
     * Formats the time of the data point.
     *
     * @return a string containing the formatted time
     */
    public static String formatTime(long time) {
        if (time == -1) {
            return "-";
        } else {
            calendar.setTimeInMillis(time);
            return timeFormat.format(calendar.getTime());
        }
    }

    /**
     * Formats the date and time of the data point.
     *
     * @return a string containing the formatted date/time
     */
    public static String formatDateTime(long time) {
        if (time == -1) {
            return "-";
        } else {
            calendar.setTimeInMillis(time);
            return dateTimeFormat.format(calendar.getTime());
        }
    }

    public static String formatValue(double value) {
        return valueFormat.format(value);
    }
}
