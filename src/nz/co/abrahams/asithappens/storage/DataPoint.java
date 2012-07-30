/*
 * DataPoint.java
 *
 * Created on 25 April 2005, 13:36
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

package nz.co.abrahams.asithappens.storage;

import java.util.GregorianCalendar;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;
import java.util.Vector;

/**
 * Stores a value of observed data along with the time of the observation.
 * <p>
 * A DataPoint may have an undefined value, which represents the fact that
 * an observation was attempted at a certain time, but the observation failed
 * to complete successfully.
 *
 * @author mark
 */
public class DataPoint implements Comparable<DataPoint> {
    
    /** Time data point value was taken */
    protected long time;
    
    /** Data point value */
    protected double value;
    
    /** Data point status */
    protected boolean defined;
    
    /** Date format */
    static protected SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss.SSS");

    /** Date/Time format */
    static protected SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
    
    /* Calendar for time representation */
    static protected GregorianCalendar calendar = new GregorianCalendar();
    
    /**
     * Creates a new DataPoint with given time and value.
     *
     * @param time  time at which data was collected
     * @param value data value
     */
    public DataPoint(long time, double value) {
        this.time = time;
        if ( Double.isNaN(value) )
            this.defined = false;
        else {
            this.value = value;
            this.defined = true;
        }
    }
    
    /**
     * Creates a new DataPoint at given time with undefined value.
     *
     * @param time  time at which data was collected
     */
    public DataPoint(long time) {
        this.time = time;
        //this.value = 0;
        this.defined = false;
    }
    
    /** @return time at which data was collected */
    public long getTime() {
        return time;
    }
    
    /** @return value of data collected */
    public double getValue() {
        return value;
    }
    
    /** @return vector containing time and value respectively */
    public Vector getVector() {
        Vector vector;
        
        vector = new Vector();
        //vector.add(new Long(time));
        vector.add(new String(formatTime(time)));
        if ( defined )
            vector.add(new Double(value));
        else
            vector.add(new Double(Double.NaN));
        
        return vector;
    }
    
    /** @return true if the value of the data point is defined */
    public boolean isDefined() {
        return defined;
    }
    
    /**
     * Set the value of the data point
     *
     * @param newValue the new value for the data point
     */
    public void setValue(double newValue) {
        value = newValue;
    }
    
    /**
     * Compare the time to another data point.
     *
     * @param otherPoint  another data point
     * @return            difference between time of this point and other point
     */
    public int compareTo(DataPoint otherPoint) {
        return (int)(time - otherPoint.time);
    }
    
    /**
     * Formats the time of the data point.
     *
     * @return a string containing the formatted time
     */
    public static String formatTime(long time) {
        calendar.setTimeInMillis(time);
        return timeFormat.format(calendar.getTime());
    }

    /**
     * Formats the date and time of the data point.
     *
     * @return a string containing the formatted date/time
     */
    public static String formatDateTime(long time) {
        calendar.setTimeInMillis(time);
        return dateTimeFormat.format(calendar.getTime());
    }    
    
    /** @return a string representation of the data point */
    public String toString() {
        //GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(time);
        
        if ( defined )
            return timeFormat.format(calendar.getTime()) + " " + value;
        else
            return timeFormat.format(calendar.getTime()) + " NaN";
    }
}
