/*
 * TimeSeriesContext.java
 *
 * Created on 31 October 2005, 21:48
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

package nz.co.abrahams.asithappens.cartgraph;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.Vector;
import nz.co.abrahams.asithappens.core.Configuration;
import nz.co.abrahams.asithappens.storage.DataSets;
import nz.co.abrahams.asithappens.storage.SummaryData;
import nz.co.abrahams.asithappens.uiutil.SetDisplay;
import org.apache.log4j.Logger;

/**
 * Describes how a DataSets instance is transformed into a graphical
 * representation.  This contains a DataSets instance, which only describes
 * the data itself, not its representation.  A TimeSeriesContext consists of
 * various variables that describe the graphical transform of the
 * DataSets instance and methods to achieve the transformation.
 * <p>
 * In addition to the data values and time values of collection, each instance
 * also stores the aggregation and interpolation policy settings for producing
 * summary data from the data sets.  See the SummaryData for further explanation
 * of these.
 * <p>
 * An instance also stores policy for x-axis scaling.  This can be set to one
 * of two values:
 * <ol>
 * <li> One point per value - where graphical displays should aim to have each
 *      graph point represent a single value of a data set.  This may result
 *      in showing only the most recent portion of the data, or alternatively
 *      showing all the data may leave a portion of x-axis of the graph without
 *      values.
 * <li> Absolute boundaries - where graphical displays should aim to display
 *      the entire data set.
 * </ol>
 *
 * @author mark
 */
public class TimeSeriesContext {
    
    /** X-axis scaling policies */
    public enum XAxisScaling { ConstantPixelWidth, AbsoluteBoundaries };
    
    /** Interpolation policies */
    public enum Interpolation { None, Flat, Sloping };
    
    /** Interpolation policies */
    public static final String[] INTERPOLATION = { "None", "Flat", "Sloping" };
    
    /** Aggregation policies */
    public enum Aggregation { Average, Maximum, Summed };
    
    public static final Character[] VALUE_UNITS = { new Character(' '), new Character('K'), new Character('M'), new Character('G') };
    
    public static final int MIN_GRAPH_VALUE = 10;
    
    /** The minimum width in pixels between x-axis value markings */
    private static final int X_AXIS_MINIMUM_PIXEL_GAP = 50;
    
    /** The minimum height in pixels between y-axis value markings */
    public static final int Y_AXIS_MINIMUM_PIXEL_GAP = 40;
    
    
    public static final float[] MAJOR_DASH_ARRAY = { 2, 3 };
    
    public static final float[] MINOR_DASH_ARRAY = { 1, 5 };
    
    public static final SimpleDateFormat TIME_FORMAT_MILLISECONDS = new SimpleDateFormat("H:mm:ss.SSS");
    
    public static final SimpleDateFormat TIME_FORMAT_SECONDS = new SimpleDateFormat("H:mm:ss");
    
    public static final SimpleDateFormat TIME_FORMAT_MINUTES = new SimpleDateFormat("H:mm");
    
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM dd");
    
    /** Logging provider */
    private static Logger logger = Logger.getLogger(TimeSeriesContext.class);
    
    /** Full sets of data */
    protected DataSets data;
    
    /** Semaphore for synchronizing summary access */
    protected Object summarySemaphore;
    
    /** Summary of data suitable for graphing */
    protected SummaryData summary;
    
    /** Flag to indicate smooth transition to new automatic graph top values */
    public boolean gradualGraphTopChange;
    
    /** The y-axis value of the target top of graph for gradual changing */
    public double targetGraphTop;
    
    /** The y-axis value of the automatically calculated top of the graph */
    public double autoGraphTop;
    
    /** The minimum top y-axis value */
    public double minDataValue;
    
    /** x-axis markings time interval */
    protected int xAxisInterval;
    
    /** x-axis minor marks per interval */
    protected int xAxisMinorMarks;
    
    /** x-axis markings units */
    protected char xAxisUnits;
    
    /** x-axis time offset from origin for first mark */
    protected double xAxisOriginOffset;
    
    /** y-axis markings value interval */
    protected long yAxisInterval;
    
    /** y-axis minor marks per interval */
    protected int yAxisMinorMarks;
    
    /** The local time-zone */
    protected TimeZone timeZone;
    
    /** Width of graphing panel */
    protected int panelWidth;
    
    /** Height of graphing panel */
    protected int panelHeight;
    
    /** Legend panel width */
    protected int legendPanelWidth;
    
    /** Threshold percentage of graph height before graph is downsized */
    protected int yRescaleMinimum;
    
    /** Threshold percentage of graph height before graph is upsized */
    protected int yRescaleMaximum;
    
    /** Percentage of largest graph value for rescaling */
    protected int yRescalePercentage;
    
    /** Strategy for calculating x-axis time scaling */
    protected XAxisScaling xAxisScaling;
    
    
    protected TimeSeriesOptions options;
    
    // BEGIN User-changeable variables
    
    /** Aggregation policy */
    protected Aggregation aggregation;
    
    /** Interpolation policy */
    protected Interpolation interpolation;
    
    /** The graphical display information for each data set */
    //public Vector<SetDisplay> setDisplays;
    
    /** The user-defined value (scaled by the units) fixed top of the graph */
    public double fixedGraphTop;
    
    /** The user-defined units of the fixed top of the graph */
    public char fixedGraphTopUnits;
    
    /** Graph type - grounded or stacked */
    public SetDisplay.Positioning setsPositioning;
    
     /*
      * Switch that mandates x-axis time markings; true for clock-based times,
      * false for time elapsed relative to right edge of graph
      */
    
    /** Show absolute times on x-axis */
    protected boolean xAxisAbsoluteTimes;
    
    /** Format y-axis units */
    protected boolean yAxisFormatUnits;
    
    /** Draw horizontal grid lines */
    protected boolean horizontalGridLines;
    
    /** Draw horizontal minor markings */
    protected boolean horizontalMinorLines;
    
    /** Draw vertical grid lines */
    protected boolean verticalGridLines;
    
    /** Draw vertical minor markings */
    protected boolean verticalMinorLines;
    
    /** Horizontal grid lines are in front of graph */
    protected boolean linesInFront;
    
    /** Sticky window setting */
    protected boolean stickyWindow;
    
    /** Label display setting */
    protected boolean showLabels;
    
    /** Trim display setting */
    protected boolean showTrim;
    
    /** Database storing */
    protected boolean storing;
    
    /** Legend location */
    protected boolean bottomLeftLegend;
    
    // END User-changeable variables
    
    
    /**
     * Creates a new TimeSeriesContext.
     *
     * @param data              the actual data to represent
     */
    public TimeSeriesContext(DataSets data) {
        this.data = data;
        options = new TimeSeriesOptions();
        xAxisScaling = data.getDataType().xAxisScaling;
        options.setAggregation(data.getDataType().aggregation);
        options.setInterpolation(data.getDataType().interpolation);
        options.setSetsPositioning(data.getDataType().positioningDefault);
        options.setYAxisFormattedUnits(data.getDataType().formatUnits);
        options.setBottomLeftLegend(data.getDataType().bottomLeftLegend);
        
        for ( int i = 0 ; i < data.getNumberOfSets() ; i++ ) {
            options.addSetDisplay(data.getDataType().setDisplayDefaults[i % data.getDataType().setDisplayDefaults.length]);
        }
        
        // defaults
        
        /*
        xAxisAbsoluteTimes = true;
        horizontalGridLines = true;
        horizontalMinorLines = true;
        verticalGridLines = true;
        verticalMinorLines = true;
        linesInFront = true;
        stickyWindow = false;
        showLabels = true;
        showTrim = true;
        fixedGraphTop = 0;
        fixedGraphTopUnits = ' ';
        */
        
        gradualGraphTopChange = false;
        targetGraphTop = MIN_GRAPH_VALUE;
        autoGraphTop = MIN_GRAPH_VALUE;
        legendPanelWidth = 0;
        timeZone = TimeZone.getDefault();
        summarySemaphore = new Object();
        yRescaleMinimum = Configuration.getPropertyInt("graph.axis.y.rescale.threshold.minimum");
        yRescaleMaximum = Configuration.getPropertyInt("graph.axis.y.rescale.threshold.maximum");
        yRescalePercentage = Configuration.getPropertyInt("graph.axis.y.rescale.percentage");
        
    }
    
    /**
     * Assumes data sets have been externally synchronized.
     *
     * @return  a summary of the data according to this graph context
     */
    public SummaryData generateSummaryData() {
        double pixelTime;
        double firstTime, lastTime;
        int pixels;
        boolean interpolateBeforeData;
        
        //panelWidth = getWidth();
        //panelHeight = getHeight();
        
        synchronized(data) {
            synchronized(summarySemaphore) {
                
                //logger.debug("Generating summary from data start");
                if ( xAxisScaling == XAxisScaling.ConstantPixelWidth ) {
                    pixels = Math.min(data.getNumberOfDataPoints(), panelWidth);
                    if ( pixels == 0 ) {
                        logger.debug("Points: " + data.getNumberOfDataPoints() + " panelWidth: " + panelWidth);
                        return null;
                    }
                    
            /* original tried and true boundary determination logic
            if ( pixels == 1 )
                pixelTime = data.getPollInterval();
            else {
                pixelTime = ( data.findLastTime() - data.findNPollsAgoTime(pixels) ) / (pixels - 1);
                //pixelTime = data.getPollInterval();
            }
             
            firstTime = data.findNPollsAgoTime(pixels) - pixelTime / 2;
            lastTime = data.findLastTime() + ( pixelTime / 2 );
             */
                    pixelTime = data.getPollInterval();
                    //lastTime = data.findLastTime() + ( pixelTime / 2 );
                    //firstTime = lastTime - ( pixels * pixelTime );
                    
                    // Alternative calculation for better stability
                    lastTime = ((long)( data.findLastTime() / pixelTime )) * pixelTime + pixelTime;
                    firstTime = Math.max( ((long)( data.findFirstTime() / pixelTime )) * pixelTime, lastTime - ( panelWidth * pixelTime ) );
                    pixels = (int)(( lastTime - firstTime ) / pixelTime);
                    
                    interpolateBeforeData = false;
                } else {
                    pixels = panelWidth;
                    firstTime = data.getStartTime();
                    lastTime = data.getFinishTime();
                    interpolateBeforeData = true;
                }
                //logger.debug("Generating summary from data: pixels: " + pixels + " firstTime: " + firstTime + " lastTime: " + lastTime);
                summary = data.summarize(pixels, firstTime, lastTime, options.getAggregation(), options.getInterpolation(), interpolateBeforeData);
                //logger.debug(summary.toString());
                //logger.debug(summary.dumpData());
                
            }
        }
        if ( options.getSetsPositioning() == SetDisplay.Positioning.Stacked )
            adjustGraphTop(Math.max(summary.findMaximumSummedValue(), MIN_GRAPH_VALUE));
        else
            adjustGraphTop(Math.max(summary.findMaximumValue(), MIN_GRAPH_VALUE));
        
        
        calculateXAxisMarkInterval();
        calculateYAxisMarkInterval();
        //System.out.println("maxvalue: " + summary.findMaximumValue());
        return summary;
    }
    
    /**
     * Adjusts the automatically scaled graph top if necessary.
     *
     * @param maxValue  the maximum value shown on the graph
     * @return          true if the graph top was adjusted
     */
    private boolean adjustGraphTop(double maxValue) {
        
        //if ( maxValue > autoGraphTop * Configuration.getPropertyInt("graph.axis.y.rescale.threshold.maximum") / 100 ) {
        if ( maxValue > targetGraphTop * yRescaleMaximum / 100 ) {
            // System.out.print("Growing y axis from " + graphTop);
            targetGraphTop = Math.max(targetGraphTop, maxValue);
            //autoGraphTop = autoGraphTop * ( 100 + Configuration.getPropertyInt("graph.axis.y.rescale.percentage") ) / 100;
            //targetGraphTop = targetGraphTop * ( 100 + yRescalePercentage ) / 100;
            targetGraphTop = maxValue * 100 / yRescalePercentage;
            // System.out.println(" to " + graphTop);
            if ( ! gradualGraphTopChange )
                autoGraphTop = targetGraphTop;
            return true;
            //} else if ( maxValue < autoGraphTop * Configuration.getPropertyInt("graph.axis.y.rescale.threshold.minimum") / 100 ) {
        } else if ( maxValue < targetGraphTop * yRescaleMinimum / 100 ) {
            // System.out.print("Shrinking y axis from " + graphTop));
            //autoGraphTop = Math.max(minDataValue, maxValue * ( 100 + Configuration.getPropertyInt("graph.axis.y.rescale.percentage") ) / 100);
            //targetGraphTop = Math.max(minDataValue, maxValue * ( 100 + yRescalePercentage ) / 100);
            targetGraphTop = Math.max(minDataValue, maxValue * 100 / yRescalePercentage);
            // System.out.println(" to " + graphTop);
            if ( ! gradualGraphTopChange )
                autoGraphTop = targetGraphTop;
            return true;
        }
        return false;
    }
    
    /**
     * Calculates and sets the interval between major and minor y-axis marks.
     * The values calculated are: yAxisInterval and yAxisMinorMarks.
     */
    public void calculateYAxisMarkInterval() {
        int maximumPoints;
        double minimumInterval;
        int exponent;
        double first;
        long desiredInterval;
        int fontHeight;
        int i, y;
        long pixelInterval;
        String unitLabel;
        
        maximumPoints = panelHeight / Configuration.getPropertyInt("graph.axis.y.marking.major.gap.minimum");
        if ( maximumPoints == 0 )
            minimumInterval = getGraphTop();
        else
            minimumInterval = getGraphTop() / maximumPoints;
        
        exponent = (int)(Math.log(minimumInterval) / Math.log(10));
        first = minimumInterval / ( Math.pow(10, exponent) );
        desiredInterval = -1;
        
        // calculate desired interval
        if ( first < 1 ) {
            logger.info("1st SF less than 1: " + first);
            logger.debug("maxPoints: " + maximumPoints + " minInt: " + minimumInterval + " exponent: " + exponent);
            desiredInterval = 1;
            yAxisMinorMarks = 5;
            //return;
        } else if ( first >= 1 && first < 2 ) {
            desiredInterval = 2 * (long)Math.pow(10, exponent);
            yAxisMinorMarks = 4;
        } else if ( first >= 2 && first < 5 ) {
            desiredInterval = 5 * (long)Math.pow(10, exponent);
            yAxisMinorMarks = 5;
        } else if ( first >= 5 && first <= 10 ) {
            exponent += 1;
            desiredInterval = (long)Math.pow(10, exponent);
            yAxisMinorMarks = 5;
        } else if ( first > 10 ) {
            logger.warn("1st SF greater than 10: " + first);
            //return -1;
        }
        yAxisInterval = desiredInterval;
    }
    
    /**
     * Sets the x-axis attributes to fit the given data.  The attributes that
     * are calculated are: xAxisInterval, xAxisUnits, xAxisMinorMarks, and
     * xAxisOriginOffset.
     */
    public void calculateXAxisMarkInterval() {
        double minimumTime;
        char units;
        int desiredTime;
        
        
        if ( summary == null )
            return;
        
        minimumTime = Configuration.getPropertyInt("graph.axis.x.marking.major.gap.minimum") * ( summary.getPixelTime() / 1000 );
        
        /*
        logger.debug("summary: " + summary.toString());
        logger.debug("pixelTime: " + summary.getPixelTime());
        logger.debug("minimumTime: " + minimumTime);
         */
        
        units = 's';
        desiredTime = findNearestTime(minimumTime, units);
        if ( desiredTime == 0 ) {
            minimumTime /= 60;
            units = 'm';
            desiredTime = findNearestTime(minimumTime, units);
            if ( desiredTime == 0 ) {
                minimumTime /= 60;
                units = 'h';
                desiredTime = findNearestTime(minimumTime, units);
                if ( desiredTime == 0 ) {
                    minimumTime /= 24;
                    units = 'd';
                    desiredTime = findNearestTime(minimumTime, units);
                    if ( desiredTime == 0 ) {
                        desiredTime = 7;
                        logger.warn("XAxisPanel: Warning: interval is maximum (7 days)");
                    }
                }
            }
        }
        //pixelInterval = findTimeInMillis(desiredTime, units) / summary.getPixelTime();
        xAxisInterval = desiredTime;
        xAxisUnits = units;
        xAxisOriginOffset = ( summary.getOriginTime(panelWidth) + timeZone.getOffset((long)summary.getOriginTime(panelWidth)) ) % findTimeInMillis(desiredTime, units);
        //System.out.println("minimumTime: " + minimumTime + " desiredTime: " + desiredTime + " units: " + units + " pixelInterval: " + pixelInterval + " timePixelOffset: " + timePixelOffset);
    }
    
    /**
     * Finds a "nice" unit-increment value for x-axis markings.  The value
     * returned will be the lowest of the predefined set of values for a unit
     * that exceeds the given minimum.
     *
     * @param minimum the minimum value
     * @param units   units to check for
     * @return        the nearest "nice" value; zero if none is found (because
     *                the minimum is too big for the given units)
     */
    public int findNearestTime(double minimum, char units) {
        if ( units == 's' || units == 'm' ) {
            if ( minimum <= 1 ) {
                xAxisMinorMarks = 5;
                return 1;
            } else if ( minimum <= 2 ) {
                xAxisMinorMarks = 4;
                return 2;
            } else if ( minimum <= 5 ) {
                xAxisMinorMarks = 5;
                return 5;
            } else if ( minimum <= 10 ) {
                xAxisMinorMarks = 5;
                return 10;
            } else if ( minimum <= 30 ) {
                xAxisMinorMarks = 6;
                return 30;
            }
            return 0;
        } else if ( units == 'h' ) {
            if ( minimum <= 1 ) {
                xAxisMinorMarks = 5;
                return 1;
            } else if ( minimum <= 2 ) {
                xAxisMinorMarks = 4;
                return 2;
            } else if ( minimum <= 3 ) {
                xAxisMinorMarks = 6;
                return 3;
            } else if ( minimum <= 6 ) {
                xAxisMinorMarks = 6;
                return 6;
            } else if ( minimum <= 12 ) {
                xAxisMinorMarks = 6;
                return 12;
            }
            return 0;
        } else if ( units == 'd' ) {
            if ( minimum <= 1 ) {
                xAxisMinorMarks = 4;
                return 1;
            } else if ( minimum <= 2 ) {
                xAxisMinorMarks = 4;
                return 2;
            } else if ( minimum <= 3 ) {
                xAxisMinorMarks = 6;
                return 3;
            } else if ( minimum <= 7 ) {
                xAxisMinorMarks = 7;
                return 7;
            }
            return 0;
        }
        return 0;
    }
    
    /**
     * Finds the time in milliseconds for a given time unit.
     *
     * @param time  time value
     * @param units unit for given time value
     * @return      the number of milliseconds represented
     */
    public static int findTimeInMillis(int time, char units) {
        if ( units == 's' )
            return time * 1000;
        else if ( units == 'm' )
            return time * 1000 * 60;
        else if ( units == 'h' )
            return time * 1000 * 60 * 60;
        else if ( units == 'd' )
            return time * 1000 * 60 * 60 * 24;
        else {
            //logger.error("XAxisPanel: Unknown units " + units);
            return 0;
        }
    }
    
    /**
     * Returns a value adjusted according to the given units.
     *
     * @param value  the value in the given unit
     * @param units  the unit used as a multiplier
     * @return       the unit value
     */
    public static double findUnitValue(double value, char units) {
        switch (units) {
            case ' ':
                return value;
            case 'K':
                return value * 1000;
            case 'M':
                return value * 1000000;
            case 'G':
                return value * 1000000000;
            default:
                return 0;
        }
        
    }
    
    /** @return the data sets for the context */
    public DataSets getData() {
        return data;
    }
    
    /** @return the graph options for the context */
    public TimeSeriesOptions getOptions() {
        return options;
    }
    
    /** @param newOptions  the new graph options for the context */
    public void setOptions(TimeSeriesOptions newOptions) {
        options = newOptions;
    }
    
    /** @return the current summary of the data sets */
    public SummaryData getSummaryData() {
        return summary;
    }
    
    /** @return the summary data semaphore for synchronization */
    public Object getSummarySemaphore() {
        return summarySemaphore;
    }
    
    /** @return the width of the graph drawing panel */
    public int getPanelWidth() {
        return panelWidth;
    }
    
    /** @return the height of the graph drawing panel */
    public int getPanelHeight() {
        return panelHeight;
    }
    
    /** @return the width of the legend panel */
    public int getLegendPanelWidth() {
        return legendPanelWidth;
    }
    
    /** @param value  the new width of the legend panel */
    public void setLegendPanelWidth(int value) {
        legendPanelWidth = value;
    }
    
    /** @return x-axis scaling strategy */
    public XAxisScaling getXAxisScaling() {
        return xAxisScaling;
    }
    
    public void setXAxisScaling(XAxisScaling scaling) {
        xAxisScaling = scaling;
    }
    
    /** @return interval between major x-axis marks */
    public int getXAxisInterval() {
        return xAxisInterval;
    }
    
    /** @return interval between minor x-axis marks */
    public int getXAxisMinorMarks() {
        return xAxisMinorMarks;
    }
    
    /** @return the time units for the x axis */
    public char getXAxisUnits() {
        return xAxisUnits;
    }
    
    /** @return the offset from the x-axis origin to the first major mark */
    public double getXAxisOriginOffset() {
        return xAxisOriginOffset;
    }
    
    /** @return interval between major x-axis marks */
    public long getYAxisInterval() {
        return yAxisInterval;
    }
    
    /** @return interval between minor x-axis marks */
    public int getYAxisMinorMarks() {
        return yAxisMinorMarks;
    }
    
    /** @param value  the new width of the graph drawing panel */
    public void setPanelWidth(int value) {
        panelWidth = value;
    }
    
    /** @param value  the new height of the graph drawing panel */
    public void setPanelHeight(int value) {
        panelHeight = value;
    }
    
    public int getYRescaleMinimum() {
        return yRescaleMinimum;
    }
    
    public void setYRescaleMinimum(int value) {
        yRescaleMinimum = value;
    }
    
    public int getYRescaleMaximum() {
        return yRescaleMaximum;
    }
    
    public void setYRescaleMaximum(int value) {
        yRescaleMaximum = value;
    }
    
    public int getYRescalePercentage() {
        return yRescalePercentage;
    }
    
    public void setYRescalePercentage(int value) {
        yRescalePercentage = value;
    }
    
    public void setGradualGraphTopChange(boolean value) {
        gradualGraphTopChange = value;
    }
    
    /** @return the currently selected graph top - automatic or manually fixed */
    public double getGraphTop() {
        /*
        if ( fixedGraphTop != 0 )
            return findUnitValue(fixedGraphTop, fixedGraphTopUnits);
        else
            return autoGraphTop;
         */
        if ( options.getUseFixedGraphTop() )
            return findUnitValue(options.getFixedGraphTop(), options.getFixedGraphTopUnits());
        else
            return autoGraphTop;
        
    }
    
    public double getTargetGraphTop() {
        return targetGraphTop;
    }
    
    public void setTargetGraphTop(double value) {
        targetGraphTop = value;
    }
    
    /** @return automatic graph top */
    public double getAutoGraphTop() {
        return autoGraphTop;
    }
    
    /** @param value  the new automatic initial graph top */
    public void setAutoGraphTop(double value) {
        autoGraphTop = value;
    }
    
    
    
    /** @param value  switch to toggle the use of the fix graph top */
    public void setUseFixedGraphTop(boolean value) {
        options.setUseFixedGraphTop(value);
    }
    
    public boolean getUseFixedGraphTop() {
        //return fixedGraphTop != 0;
        return options.getUseFixedGraphTop();
    }
    
    /** @return fixed graph top */
    public double getFixedGraphTop() {
        return options.getFixedGraphTop();
    }
    
    /** @param value  the new manually fixed graph top */
    public void setFixedGraphTop(double value) {
        options.setFixedGraphTop(value);
    }
    
    /** @return fixed graph top units */
    public char getFixedGraphTopUnits() {
        return options.getFixedGraphTopUnits();
    }
    
    /** @param value  the new manually fixed graph top units */
    public void setFixedGraphTopUnits(char value) {
        options.setFixedGraphTopUnits(value);
    }
    
    /** @return the graph type for the context */
    public SetDisplay.Positioning getSetsPositioning() {
        return options.getSetsPositioning();
    }
    
    /** @param value  the new graph type for the context */
    public void setSetsPositioning(SetDisplay.Positioning positioning) {
        options.setSetsPositioning(positioning);
    }
    
    /** @return the aggregation policy for the context */
    public Aggregation getAggregation() {
        return options.getAggregation();
    }
    
    /** @param value  the new aggregation policy for the context */
    public void setAggregation(Aggregation value) {
        options.setAggregation(value);
    }
    
    /** @return the interpolation policy for the context */
    public Interpolation getInterpolation() {
        return options.getInterpolation();
    }
    
    /** @param value  the new interpolation policy for the context */
    public void setInterpolation(Interpolation value) {
        options.setInterpolation(value);
    }
    
    public Vector<SetDisplay> getSetDisplays() {
        return options.getSetDisplays();
    }
    
    public SetDisplay getSetDisplay(int set) {
        /*
        extendSetDisplays(set);
        return setDisplays.elementAt(set);
         */
        return options.getSetDisplay(set);
    }
    
    /**
     * @param set  the index of the data set
     * @return     the color for the indexed data set
     */
    public Color getSetDisplayColor(int set) {
        /*
        return setDisplays.elementAt(set).color;
         */
        extendSetDisplays(set);
        return options.getSetDisplayColor(set);
    }
    
    /**
     * @param set  the index of the data set
     * @return     the line type for the indexed data set
     */
    public SetDisplay.Style getSetDisplayStyle(int set) {
        /*
        return setDisplays.elementAt(set).style;
         */
        extendSetDisplays(set);
        return options.getSetDisplayStyle(set);
    }
    
    public void extendSetDisplays(int set) {
        while ( options.getSetDisplays().size() - 1 < set ) {
            addSetDisplay(set);
        }
    }
    
    public void addSetDisplay(int set) {
        //addSetDisplay(SetDisplay.create(data.getDataType().setDisplayDefaults[setDisplays.size() % data.getDataType().setDisplayDefaults.length]));
        options.addSetDisplay(SetDisplay.create(data.getDataType().setDisplayDefaults[options.getSetDisplays().size() % data.getDataType().setDisplayDefaults.length]));
    }

    /*
    public void addSetDisplay(SetDisplay newDisplay) {
        //setDisplays.add(newDisplay);
        options.addSetDisplay(newDisplay);
    }
    */

    public void setSetDisplays(Vector<SetDisplay> newDisplays) {
        //setDisplays = newDisplays;
        options.setSetDisplays(newDisplays);
    }
    
    public void setSetDisplayColor(int set, Color color) {
        //setDisplays.elementAt(set).color = color;
        options.setSetDisplayColor(set, color);
    }
    
    public void setSetDisplayStyle(int set, SetDisplay.Style style) {
        //setDisplays.elementAt(set).style = style;
        options.setSetDisplayStyle(set, style);
    }
    
    /** return true if absolute x-axis markings are used, false for relative markings */
    public boolean getXAxisAbsoluteTimes() {
        return options.getXAxisAbsoluteTimes();
    }
    
    /** @param flag  setting for absolute x-axis markings */
    public void setXAxisAbsoluteTimes(boolean flag) {
        options.setXAxisAbsoluteTimes(flag);
    }
    
    /** @return true if y-axis text markings will be formatted by appropriate units */
    public boolean getYAxisFormattedUnits() {
        return options.getYAxisFormattedUnits();
    }
    
    /** @return true if graph window is "always on top" */
    public boolean getStickyWindow() {
        return options.getStickyWindow();
    }
    
    /** @param flag  setting for "always on top" feature */
    public void setStickyWindow(boolean flag) {
        options.setStickyWindow(flag);
    }
    
    /** @return true if the graph is showing text labels */
    public boolean getShowLabels() {
        return options.getShowLabels();
    }
    
    /** @param flag  setting for the display of text labels */
    public void setShowLabels(boolean flag) {
        options.setShowLabels(flag);
    }
    
    /** @return true if the graph is showing trim */
    public boolean getShowTrim() {
        return options.getShowTrim();
    }
    
    /** @param flag  setting for the display of graph trim */
    public void setShowTrim(boolean flag) {
        options.setShowTrim(flag);
    }
    
    /** @return true if the legend is at the bottom-left of the graph, false if to the right */
    public boolean bottomLeftLegend() {
        return options.getBottomLeftLegend();
    }
    
    /** @return true if horizontal grid lines are showing */
    public boolean horizontalGridLines() {
        return options.getHorizontalGridLines();
    }
    
    /** @param flag  setting to show horizontal grid lines */
    public void setHorizontalGridLines(boolean flag) {
        options.setHorizontalGridLines(flag);
    }
    
    /** @return true if horizontal minor grid lines are showing */
    public boolean horizontalMinorLines() {
        return options.getHorizontalMinorLines();
    }
    
    /** @param flag  setting to show horizontal minor grid lines */
    public void setHorizontalMinorLines(boolean flag) {
        options.setHorizontalMinorLines(flag);
    }
    
    /** @return true if vertical grid lines are showing */
    public boolean verticalGridLines() {
        return options.getVerticalGridLines();
    }
    
    /** @param flag  setting to show vertical grid lines */
    public void setVerticalGridLines(boolean flag) {
        options.setVerticalGridLines(flag);
    }
    
    /** @return true if vertical minor grid lines are showing */
    public boolean verticalMinorLines() {
        return options.getVerticalMinorLines();
    }
    
    /** @param flag  setting to show vertical minor grid lines */
    public void setVerticalMinorLines(boolean flag) {
        options.setVerticalMinorLines(flag);
    }
    
    /** @return true if grid lines are drawn in from of the graph */
    public boolean linesInFront() {
        return options.getLinesInFront();
    }
    
    /** @param flag  setting to draw grid lines in front of graph */
    public void setLinesInFront(boolean flag) {
        options.setLinesInFront(flag);
    }
    
    /**
     * Transforms x coordinate from pixel domain to time domain.
     *
     * @param x  pixel offset from origin
     * @return   time in seconds since Jan 1, 1970
     */
    public long getTimeFromXPixel(int x) {
        if ( summary != null && getPanelWidth() > 0 )
            return (long)( ( summary.getLastTime() - summary.getOriginTime(getPanelWidth()) ) /
                    getPanelWidth() * x + summary.getOriginTime(getPanelWidth()) );
        else
            return -1;
    }
    
    /**
     * Transforms y coordinate from pixel domain to value domain.
     *
     * @param y  pixel offset from origin
     * @return   the y-axis value
     */
    public double getValueFromYPixel(int y) {
        if ( getPanelHeight() > 0 )
            return getGraphTop() / getPanelHeight() * ( getPanelHeight() - 1 - y );
        else
            return -1;
    }
    
    /**
     * Transforms x coordinate from time domain to pixel domain.
     *
     * @param time  time in seconds since Jan 1, 1970
     * @return      pixel offset from origin
     */
    public int getXPixelFromTime(long time) {
        if ( summary != null )
            return (int)(( time - summary.getOriginTime(getPanelWidth()) ) * getPanelWidth() /
                    ( summary.getLastTime() - summary.getOriginTime(getPanelWidth()) ) );
        else
            return -1;
    }
    
    /**
     * Transforms y coordinate from value domain to pixel domain.
     *
     * @param value  the y-axis value
     * @return       pixel offset from origin
     */
    public int getYPixelFromValue(double value) {
        if ( getGraphTop() != 0 )
            return (int)( getPanelHeight() - 1 - (value * getPanelHeight() / getGraphTop()) );
        else
            return -1;
    }
    
    public double getXTimePerPixel() {
        return ( summary.getLastTime() - summary.getOriginTime(getPanelWidth()) ) / getPanelWidth();
    }
    
    public double getYValuesPerPixel() {
        return ( getGraphTop() / getPanelHeight() );
    }
    
    /** Returns first displayed time */
    public double getFirstDisplayedTime() {
        if ( summary != null )
            return summary.getFirstTime();
        else
            return -1;
    }
    
    /** Returns last displayed time */
    public double getLastDisplayedTime() {
        if ( summary != null )
            return summary.getLastTime();
        else
            return -1;
    }
    
    public double getTimeGraphOrigin() {
        if ( summary != null )
            return summary.getOriginTime(getPanelWidth());
        else
            return -1;
    }
    
    public double getTimeGraphWidth() {
        if ( summary != null )
            return summary.getLastTime() - summary.getOriginTime(getPanelWidth());
        else
            return -1;
    }
    
    public String toString() {
        StringBuffer buffer;
        
        buffer = new StringBuffer();
        buffer.append("xAxisScaling = " + xAxisScaling + "\n");
        buffer.append("aggregation = " + aggregation + "\n");
        buffer.append("interpolation = " + interpolation + "\n");
        buffer.append("setsPositioning = " + setsPositioning + "\n");
        buffer.append("yAxisFormatUnits = " + yAxisFormatUnits + "\n");
        buffer.append("bottomLeftLegend = " + bottomLeftLegend + "\n");
        buffer.append("autoGraphTop = " + autoGraphTop + "\n");
        buffer.append("fixedGraphTop = " + fixedGraphTop + "\n");
        buffer.append("fixedGraphTopUnits = " + fixedGraphTopUnits + "\n");
        buffer.append("legendPanelWidth = " + legendPanelWidth + "\n");
        buffer.append("xAxisAbsoluteTimes = " + xAxisAbsoluteTimes + "\n");
        buffer.append("horizontalGridLines = " + horizontalGridLines + "\n");
        buffer.append("horizontalMinorLines = " + horizontalMinorLines + "\n");
        buffer.append("verticalGridLines = " + verticalGridLines + "\n");
        buffer.append("verticalMinorLines = " + verticalMinorLines + "\n");
        buffer.append("linesInFront = " + linesInFront + "\n");
        buffer.append("stickyWindow = " + stickyWindow + "\n");
        buffer.append("showLabels = " + showLabels + "\n");
        buffer.append("showTrim = " + showTrim + "\n");
        buffer.append("timeZone = " + timeZone + "\n");
        
        return buffer.toString();
    }
}
