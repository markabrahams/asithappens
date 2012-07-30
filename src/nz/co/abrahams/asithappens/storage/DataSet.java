/*
 * DataSet.java
 *
 * Created on 1 February 2006, 19:32
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

import java.util.*;
import org.apache.log4j.Logger;
import nz.co.abrahams.asithappens.cartgraph.TimeSeriesContext.Aggregation;
import nz.co.abrahams.asithappens.cartgraph.TimeSeriesContext.Interpolation;

/**
 * A DataSet is a Vector of DataPoints used to represent a single set of data.
 *
 * @author mark
 */
public class DataSet extends Vector<DataPoint> {
    
    /* Logging provider */
    private static Logger logger = Logger.getLogger(DataSet.class);

    /* Global statistics */
    private SummaryStatistics globalStatistics;

    /**
     * Creates a new DataSet.
     */
    public DataSet() {
        super();
        globalStatistics = new SummaryStatistics();
    }

    public void addDataPoint(DataPoint point) {
        add(point);
        globalStatistics.addDataPoint(point);
    }

    /**
     * Aggregates a set of data points according to the given policy over some
     * time interval.
     *
     * @param aggregation  the aggregation policy to use
     * @param startTime    the start of the aggregation interval
     * @param endTime      the end of the aggregation interval
     * @return             a single data point representing the aggregation over the interval
     */
    public DataPoint aggregate(Aggregation aggregation, long startTime, long endTime) {
        
        int numValues;
        double pointValue;
        long averageTime;
        Iterator<DataPoint> iterator;
        DataPoint datum;
        
        iterator = iterator();
        datum = null;
        if ( iterator.hasNext() )
            datum = iterator.next();
        pointValue = 0;
        numValues = 0;
        averageTime = 0;
        //System.out.println("startInterval: " + (long)startInterval + " endInterval: " + (long)endInterval);
        
        try {
            //while ( element < getNumberOfDataPoints() && ((DataPoint)(data[set].elementAt(element))).time < startInterval ) {
            while ( datum != null && datum.getTime() < startTime ) {
                //System.out.println("Time ignored: " + findTime(element));
                datum = iterator.next();
                //element++;
            }
            //while ( element < getNumberOfDataPoints() && ((DataPoint)(data[set].elementAt(element))).time < endInterval ) {
            while ( datum != null && datum.getTime() < endTime ) {
                if ( datum.isDefined() == true ) {
                    numValues++;
                    if ( aggregation == Aggregation.Maximum )
                        pointValue = Math.max(pointValue, datum.getValue());
                    else if ( aggregation == Aggregation.Average )
                        pointValue += datum.getValue();
                    else if ( aggregation == Aggregation.Summed )
                        pointValue += datum.getValue();
                    averageTime += datum.getTime();
                    // System.out.println("Time chosen: " + findTime(element));
                }
                datum = iterator.next();
                //element++;
            }
        }
        // Thrown by iterator.next() if at end of data
        catch (NoSuchElementException e) {
        } finally {
            if ( aggregation == Aggregation.Average && numValues > 0 )
                pointValue /= numValues;
            else if ( aggregation == Aggregation.Summed && numValues > 0 )
                pointValue = pointValue * 1000 / (endTime - startTime);
            
            if ( averageTime == 0 )
                averageTime = startTime + ( ( endTime - startTime ) / 2 );
            else
                averageTime /= numValues;
            
            if ( numValues == 0 )
                return new DataPoint(averageTime);
            else
                return new DataPoint(averageTime, pointValue);
        }
        //System.out.println("startInterval: " + (long)startInterval + " endInterval: " + (long)endInterval);
        //System.out.println("pointValue: " + pointValue);
        
    }
    
    /**
     * Aggregates the entire set of data points according to the given policy.
     *
     * @param aggregation  the aggregation policy to use
     * @return             a single data point representing the aggregation over the interval
     */
    public DataPoint aggregate(Aggregation aggregation) {
        if ( isEmpty() )
            return null;
        else
            return aggregate(aggregation, firstElement().getTime(), lastElement().getTime() + 1);
    }
    
    /**
     * Aggregates a set of data points according to the given policy over some
     * time interval.
     *
     * @param startTime    the start of the aggregation interval
     * @param endTime      the end of the aggregation interval
     * @return             a single data point representing the aggregation over the interval
     */
    public SummaryStatistics generateSummaryStatistics(long startTime, long endTime) {
        
        int numValues;
        double minValue;
        double maxValue;
        double averageValue;
        long firstTime;
        long lastTime;
        Iterator<DataPoint> iterator;
        DataPoint datum;
        SummaryStatistics statistics;
        
        minValue = Double.MAX_VALUE;
        maxValue = Double.MIN_VALUE;
        averageValue = 0;
        numValues = 0;
        lastTime = -1;
        firstTime = -1;
        iterator = iterator();
        datum = null;
        if ( iterator.hasNext() )
            datum = iterator.next();
        //System.out.println("startInterval: " + (long)startInterval + " endInterval: " + (long)endInterval);
        
        try {
            while ( datum != null && datum.getTime() < startTime ) {
                //System.out.println("Time ignored: " + findTime(element));
                datum = iterator.next();
            }
            while ( datum != null && datum.getTime() <= endTime ) {
                if ( datum.isDefined() == true ) {
                    numValues++;
                    minValue = Math.min(minValue, datum.getValue());
                    maxValue = Math.max(maxValue, datum.getValue());
                    averageValue += datum.getValue();
                    lastTime = datum.getTime();
                    if ( numValues == 1 ) {
                        firstTime = datum.getTime();
                    }
                }
                datum = iterator.next();
            }
        }
        // Thrown by iterator.next() if at end of data
        catch (NoSuchElementException e) {
        }
        if ( numValues > 0 ) {
            averageValue /= numValues;
            return new SummaryStatistics(numValues, firstTime, lastTime, minValue, maxValue, averageValue);
        } else
            return new SummaryStatistics(0, -1, -1, 0, 0, 0);
    }
    
    public Vector getVector() {
        Vector vector;
        
        vector = new Vector();
        for (int i = 0; i < size(); i++ ) {
            //logger.debug("Element " + i + " at " + elementAt(i).getTime() + " has value " + elementAt(i).getValue());
            vector.add(elementAt(i).getVector());
        }
        return vector;
    }
    
    // O(n) search
    /*
    public DataSet pointsBetween(long startTime, long endTime) {
        Iterator<DataPoint> iterator;
        DataSet newSet;
        DataPoint point;
     
        newSet = new DataSet();
        iterator = iterator();
        while ( iterator.hasNext() ) {
            point = iterator.next();
            if ( point.getTime() >= startTime && point.getTime() < endTime) {
                newSet.add(point);
            }
        }
        return newSet;
    }
     */
    
    public DataSet pointsBetween(long startTime, long endTime) {
        DataSet newSet;
        //int startIndex;
        int nextIndex;
        
        newSet = new DataSet();
        nextIndex = elementIndexAtTime(startTime);
        //nextIndex = startIndex;
        while ( nextIndex < size() && elementAt(nextIndex).getTime() < endTime ) {
            newSet.add(elementAt(nextIndex));
            nextIndex++;
        }
        return newSet;
    }
    
    
    public IntegerPair pointIndiciesBetween(long startTime, long endTime) {
        int startIndex;
        int endIndex;
        int nextIndex;
        
        endIndex = -1;
        startIndex = elementIndexAtTime(startTime);
        nextIndex = startIndex;
        while ( nextIndex < size() && elementAt(nextIndex).getTime() < endTime ) {
            endIndex = nextIndex;
            nextIndex++;
        }
        
        return new IntegerPair(startIndex, endIndex);
    }
    
    public IntegerPair pointIndiciesBetween(long startTime, long endTime, int startIndex) {
        int endIndex;
        int nextIndex;
        
        endIndex = -1;
        while ( startIndex < size() && elementAt(startIndex).getTime() < startTime ) {
            startIndex++;
        }
        nextIndex = startIndex;
        while ( nextIndex < size() && elementAt(nextIndex).getTime() < endTime ) {
            endIndex = nextIndex;
            nextIndex++;
        }
        
        return new IntegerPair(startIndex, endIndex);
    }
    
    
    public int elementIndexAtTime(long startTime) {
        int low;
        int high;
        int mid;
        
        low = 0;
        high = size();
        while ( low < high ) {
            mid = ( low + high ) / 2;
            if ( elementAt(mid).getTime() < startTime )
                low = mid + 1;
            else
                //can't be high = mid-1: here A[mid] >= value,
                //so high can't be < mid if A[mid] == value
                high = mid;
        }
        
        return low;
        /*
        if ( low < size() && elementAt(low).getTime() == startTime )
            return low; // found
        else
            return -1;
         */
    }
    
    public String toString() {
        StringBuffer buffer;
        
        buffer = new StringBuffer("( ");
        for ( int i = 0; i < size(); i++ ) {
            buffer.append("(" + elementAt(i) + ")");
            if ( i < size() - 1 )
                buffer.append(", ");
        }
        buffer.append(" )");
        return buffer.toString();
    }
    
}
