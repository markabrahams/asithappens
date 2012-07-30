/*
 * SummaryData.java
 *
 * Created on 29 May 2004, 13:08
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

import org.apache.log4j.Logger;
import java.util.GregorianCalendar;
import nz.co.abrahams.asithappens.cartgraph.TimeSeriesContext.Aggregation;
import nz.co.abrahams.asithappens.cartgraph.TimeSeriesContext.Interpolation;
import nz.co.abrahams.asithappens.cartgraph.TimeSeriesContext;

/**
 * Contains data suitable for display on a graph.  The data is stored in a
 * two-dimensional array of "double" primitives.  The first dimension is the
 * index for the data sets contained; the second dimension is the index for the
 * data values.
 * <p>
 * Data is represented over a continuous time interval, between the first and
 * last date-time values given on object creation.  Each data value therefore
 * represents the value over a specific time interval within this range.  These
 * time-intervals are contiguous in the time-domain i.e. the ending point for
 * one interval will be the starting point for the next, and each interval is of
 * equal size.  Conceptually, you can think of the x-axis of a graph being
 * segmented into equal portions, with the number of portions being the number
 * of data values.  Each value represents the data for its portion of time,
 * which corresponds to a single point in the data set.  Such data values can
 * then be used as discrete points on a graph.  For example, each pixel might
 * represent one time-interval i.e. one point of data.
 * <p>
 * Since data values are gathered at specific times, a data summary would be
 * unnecessary if each time-interval contained exactly one data value.  However,
 * this is frequently not the case.  Sometimes there will be more than one data
 * value in a time interval, and sometimes there will be no time values in an
 * interval.  In the latter case, the value Double.NaN is used as the internal
 * representation for the lack of a value in the time interval corresponding to
 * a point.
 * <p>
 * When more than one value exists in a time interval, the values for the
 * interval must be grouped together to give a single value in the summary,
 * a practice called "aggregation".  Two aggregation policies are presently
 * defined:
 * <ul>
 * <li> "Maximum" - select the largest of the values
 * <li> "Average" - select the average of the values
 * </ul>
 * <p>
 * When there are no data values in a time interval, the values can be
 * inferred from the existing values in surrounding intervals.  This practice
 * is called "interpolation".  Three interpolation policies are presently
 * defined:
 * <ul>
 * <li> "None"
 * <li> "Flat" - where the value takes on that of the previous closest interval
 *               with a value in it.
 * <li> "Sloping" - where the value is graduated between the previous closest
 *                  interval with a value in it and the next closest interval
 *                  with a value in it.
 * </ul>
 *
 * @author  mark
 */
public class SummaryData {
    
    /** Logging provider */
    protected Logger logger;
    
    /** The number of data sets in this summary */
    private int sets;
    
    /** The number of data points per set in this summary */
    private int points;
    
    /** The per-pixel summary data values to show on the graph */
    private double[][] values;
    
    /** Indicators of whether a data point has been created through interpolation */
    private boolean interpolated[][];
    
    /** Totals for each set of data in this summary */
    private double[] totals;
    
    /** Ranking index ordering sets by largest to smallest volume */
    private int[] ranks;
    
    /** The number of milliseconds between the left and right edge of a pixel */
    private double pixelTime;
    
    /** The time at the origin of the graph */
    private double firstDate;
    
    /** The time at the right edge of the graph */
    private double lastDate;
    
    /** The aggregation policy employed on intervals containing more than one
     * value */
    private Aggregation aggregation;
    
    /** The interpolation policy employed on intervals containing no values */
    private Interpolation interpolation;
    
    /** Flag indicating initial interpolation before data starts */
    private boolean interpolateBeforeData;
    
    /**
     * Creates a new instance of SummaryData.
     *
     * @param sets          the number of data sets
     * @param points        the number of data points in the data sets
     * @param firstDate     the date on the left-hand edge of the data
     * @param lastDate      the date on the right-hand edge of the data
     * @param aggregation   the aggregation strategy
     * @param interpolation the interpolation strategy
     */
    public SummaryData(int sets, int points, double firstDate, double lastDate, Aggregation aggregation, Interpolation interpolation, boolean interpolateBeforeData) {
        logger = Logger.getLogger(this.getClass().getName());
        this.sets = sets;
        this.points = points;
        this.firstDate = firstDate;
        this.lastDate = lastDate;
        this.aggregation = aggregation;
        this.interpolation = interpolation;
        this.interpolateBeforeData = interpolateBeforeData;
        
        values = new double[sets][points];
        interpolated = new boolean[sets][points];
        pixelTime = ( lastDate - firstDate ) / points;
        logger.debug("Created SummaryData: " + toString());
    }
    
    /**
     * Returns a single data value from the data sets.
     *
     * @param set   the data set number
     * @param point the index of the value to retrieve
     * @return      the data value at the given location
     */
    public double getValue(int set, int point) {
        return values[set][point];
    }
    
    /**
     * Sets a single data value in the data sets.
     *
     * @param set   the data set number
     * @param point the index of the value to set
     * @param value the desired value
     */
    public void setValue(int set, int point, double value) {
        values[set][point] = value;
    }
    
    /**
     * Interpolates any missing values in the data sets.  Interpolation occurs
     * according to the policy given for the data.
     */
    public void interpolate() {
        int set, point, fill;
        int lastSeen;
        double lastSeenValue;
        
        if ( interpolation == Interpolation.None ) {
            for ( set = 0 ; set < values.length ; set++ ) {
                for ( point = 0 ; point < values[set].length ; point++ ) {
                    interpolated[set][point] = false;
                }
            }
            return;
        }
        
        for ( set = 0 ; set < values.length ; set++ ) {
            lastSeen = -1;
            lastSeenValue = 0;
            for ( point = 0 ; point < values[set].length ; point++ ) {
                if ( Double.isNaN(values[set][point]) ) {
                    interpolated[set][point] = true;
                    if ( point == values[set].length - 1 ) {
                        for ( fill = lastSeen + 1 ; fill <= point ; fill++ )
                            values[set][fill] = lastSeenValue;
                    }
                } else {
                    interpolated[set][point] = false;
                    for ( fill = lastSeen + 1 ; fill < point ; fill++ ) {
                        if ( interpolation == Interpolation.Sloping ) {
                            if ( lastSeen != -1 )
                                values[set][fill] = lastSeenValue + ( fill - lastSeen ) * ( values[set][point] - lastSeenValue ) / ( point - lastSeen );
                            else {
                                if ( interpolateBeforeData )
                                    values[set][fill] = values[set][point];
                            }
                        } else if ( interpolation == Interpolation.Flat ) {
                            if ( lastSeen != -1 || interpolateBeforeData )
                                values[set][fill] = values[set][point];
                        }
                    }
                    lastSeen = point;
                    lastSeenValue = values[set][point];
                }
            }
        }
    }
    
    /**
     * Finds the maximum value over all the data sets.
     *
     * @return the maximum data value
     */
    public double findMaximumValue() {
        double maximum;
        int set, point;
        
        maximum = 0;
        for ( set = 0 ; set < values.length ; set++ ) {
            for ( point = 0 ; point < values[set].length ; point++ ) {
                if ( ! Double.isNaN(values[set][point]) )
                    maximum = Math.max(maximum, values[set][point]);
            }
        }
        return maximum;
    }
    
    /**
     * Finds the maximum summed value over all the data sets.
     *
     * @return the maximum summed data value
     */
    public double findMaximumSummedValue() {
        double maximum;
        int point;
        
        maximum = 0;
        for ( point = 0 ; point < values[0].length ; point++ ) {
            maximum = Math.max(maximum, getSummedValue(point));
        }
        return maximum;
    }
    
    /**
     * Finds the sum of all data set values for a given x-axis point.
     *
     * @return the summed data value
     */
    public double getSummedValue(int point) {
        double total;
        
        try {
            total = 0;
            for ( int set = 0 ; set < values.length ; set++ ) {
                if ( ! Double.isNaN(values[set][point]) )
                    total += values[set][point];
            }
            return total;
        } catch (ArrayIndexOutOfBoundsException e) {
            return 0;
        }
    }
    
    /**
     * Returns the number of data sets in the data summary.
     *
     * @return the number of data sets
     */
    public int getNumberOfSets() {
        return sets;
    }
    
    /**
     * Returns the number of points in a single data set.  There should be the
     * same number of points in every data set.
     *
     * @return the number of points in a data set
     */
    public int getNumberOfPoints() {
        return points;
    }
    
    /**
     * Returns the time at the beginning of the summray data sets.
     *
     * @return the time at the start of the data in milliseconds since midnight,
     *         Jan 1, 1970
     */
    public double getFirstTime() {
        return firstDate;
    }
    
    /**
     * Returns the time at the end of the summary data sets.
     *
     * @return the time at the end of the data in milliseconds since midnight,
     *         Jan 1, 1970
     */
    public double getLastTime() {
        return lastDate;
    }
    
    public double getTimeAtPoint(int point) {
        return firstDate + pixelTime * point;
    }
    
    /**
     * Returns the time interval for a single data point
     *
     * @return the time interval in milliseconds
     */
    public double getPixelTime() {
        return pixelTime;
    }
    
    /**
     * Returns the time at the origin of a graph.  The width of the graph is
     * given in pixels, and the summary data is assumed to hold one data point
     * per pixel.  The resulting time will be that at the start of the data
     * if there are enough data points to entirely fill the x-axis of the graph.
     * Otherwise, the origin will be earlier than the time at which the data
     * starts.
     *
     * @param width the number of pixels wide the graph will be
     * @return      the time at the origin of the graph in milliseconds since
     *              midnight, Jan 1, 1970
     */
    public double getOriginTime(int width) {
        return lastDate - ( (lastDate - firstDate) * width / points );
    }
    
    /**
     * Sets total sum of all point values of each data set within the summary.
     * Assumes these have been calculated externally, and does no verification
     * of the totals.
     *
     * @param totals the data set total values
     */
    public void setTotals(double[] totals) {
        this.totals = totals;
    }
    
    /**
     * Generates an index of the data sets ranked by highest volume first.
     */
    public void generateRanks() {
        boolean[] sorted;
        double nextLargest;
        int nextIndex;
        
        ranks = new int[sets];
        sorted = new boolean[sets];
        
        for ( int set = 0; set < sets; set++ ) {
            sorted[set] = false;
        }
        
        for ( int dst = 0; dst < sets; dst++ ) {
            nextLargest = Double.NEGATIVE_INFINITY;
            nextIndex = -1;
            for ( int src = 0; src < sets; src++ ) {
                if ( ! sorted[src] && totals[src] > nextLargest ) {
                    nextLargest = totals[src];
                    nextIndex = src;
                }
            }
            ranks[dst] = nextIndex;
            if ( nextIndex == -1 ) {
                logger.debug(toString());
                logger.debug("Sorted: " + sorted.toString());
                logger.debug("Totals: " + totals.toString());
                return;
            }
            sorted[nextIndex] = true;
        }
    }
    
    /**
     * Get set index with the given total volume rank.
     *
     * @param rank rank of set to find index for
     * @return     set with given rank
     */
    public int getRankedSetIndex(int set) {
        return ranks[set];
    }
    
    public boolean isInterpolated(int set, int point) {
        return interpolated[set][point];
    }
    
    public String toString() {
        StringBuffer buffer;
        GregorianCalendar firstCalendar;
        GregorianCalendar lastCalendar;
        
        firstCalendar = new GregorianCalendar();
        lastCalendar = new GregorianCalendar();
        firstCalendar.setTimeInMillis((long)firstDate);
        lastCalendar.setTimeInMillis((long)lastDate);
        
        buffer = new StringBuffer();
        buffer.append("Sets=" + sets + ",");
        buffer.append("Points=" + points + ",");
        buffer.append("FirstDate=" + TimeSeriesContext.TIME_FORMAT_MILLISECONDS.format(firstCalendar.getTime()) + ",");
        buffer.append("LastDate=" + TimeSeriesContext.TIME_FORMAT_MILLISECONDS.format(lastCalendar.getTime()) + ",");
        buffer.append("PixelTime=" + pixelTime + ",");
        buffer.append("Interpolation=" + interpolation);
        return buffer.toString();
    }
    
    /** @return a text dump of the summary data */
    public String dumpData() {
        StringBuffer buffer;
        
        buffer = new StringBuffer();
        for ( int i = 0; i < points; i++ ) {
            buffer.append("(");
            for ( int set = 0; set < sets; set++ ) {
                if ( ! Double.isNaN(values[set][i]))
                    buffer.append(values[set][i]);
                else
                    buffer.append("NaN");
                if ( set != sets - 1 )
                    buffer.append(",");
            }
            buffer.append(")\n");
        }
        return buffer.toString();
    }
    
}