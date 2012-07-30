/*
 * TimeSeriesXAxisPanel.java
 *
 * Created on 5 November 2003, 09:29
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

import nz.co.abrahams.asithappens.storage.SummaryData;
import nz.co.abrahams.asithappens.core.Configuration;
import java.awt.*;
import java.util.*;
import java.text.*;
import org.apache.log4j.Logger;

/**
 * Displays an x-axis panel complete with date-time markings.  If the markings
 * are "absolute", then clock date-times are shown.  If the markings are
 * "relative", then markings are shown offset from the right-hand edge of the
 * TimeSeriesXAxisPanel.
 * <p>
 * The dimensions for the graph will determine the x-axis markings.  These are
 * set by the setSummaryData method, which therefore needs to be called
 * before any markings will show on the axis.
 *
 * @author mark
 */
public class TimeSeriesXAxisPanel extends javax.swing.JPanel {
    
    protected static final int MAJOR_MARK_HEIGHT = 5;
    
    protected static final int MINOR_MARK_HEIGHT = 2;
    
    protected static final int GAP_TO_LABEL = 2;
    
    protected static final Font FONT_MINUTES = new Font("Dialog", Font.PLAIN, 12);
    
    protected static final Font FONT_SECONDS = new Font("Dialog", Font.PLAIN, 10);
    
    
    /** Logging provider */
    private static Logger logger = Logger.getLogger(TimeSeriesXAxisPanel.class);
    
    /** Graph context */
    protected TimeSeriesContext context;
    
    /**
     * Creates a new TimeSeriesXAxisPanel.
     *
     * @param context the graph context for the y-axis panel
     */
    public TimeSeriesXAxisPanel(TimeSeriesContext context) {
        this.context = context;
        //timeZone = TimeZone.getDefault();
    }
    
    /**
     * Paints the x-axis panel.  This draws:
     * <ul>
     * <li> the x-axis line
     * <li> the x-axis markings
     * <li> the date/time labels for the x-axis markings
     * </ul>
     * <p>
     * The calculations are based on whether the markings are "absolute" or
     * "relative" as specified by the TimeSeriesContext for the TimeSeriesXAxisPanel.
     * For "absolute" markings, times are shown without seconds if they always
     * fall on minute boundaries.  Additionally, instead of displaying a time
     * for "midnight" markings, the date of the new day is shown.
     *
     * @param g the Graphics object to draw on
     */
    public void paintComponent(Graphics g) {
        int panelWidth, panelHeight;
        int i;
        int xMajor;
        int xMinor;
        String unitLabel;
        GregorianCalendar calendar;
        SimpleDateFormat timeFormat, dateFormat;
        Font labelFont;
        long longTime;
        SummaryData summary;
        double pixelInterval;
        
        //panelWidth = context.getPanelWidth();
        panelWidth = getWidth();
        panelHeight = getHeight();
        if ( panelWidth != context.getPanelWidth() ) {
            logger.info("Creating new data summary: x-axis panel width (" + panelWidth + ") does not equal graph panel width (" + context.getPanelWidth() + ")");
            context.setPanelWidth(panelWidth);
            context.generateSummaryData();
            revalidate();
        }
        g.drawLine(0, 0, panelWidth - 1, 0);
        
        summary = context.getSummaryData();
        if ( summary == null )
            return;
        
        if ( context.getXAxisUnits() == 's' ) {
            timeFormat = TimeSeriesContext.TIME_FORMAT_SECONDS;
            //labelFont = FONT_SECONDS;
            g.setFont(FONT_SECONDS);
        }
        else {
            timeFormat = TimeSeriesContext.TIME_FORMAT_MINUTES;
            //labelFont = FONT_MINUTES;
            g.setFont(FONT_MINUTES);
        }
        
        //dateFormat = TimeSeriesContext.DATE_FORMAT;
        calendar = new GregorianCalendar();
        
        pixelInterval = context.findTimeInMillis(context.getXAxisInterval(), context.getXAxisUnits()) / summary.getPixelTime();
        if ( context.getXAxisAbsoluteTimes() == true ) {
            i = 0;
            xMajor = 0;
            while ( xMajor < panelWidth ) {
                longTime = (long)(summary.getOriginTime(panelWidth) - context.getXAxisOriginOffset() + (i * TimeSeriesContext.findTimeInMillis(context.getXAxisInterval(), context.getXAxisUnits())));
                calendar.setTimeInMillis(longTime);
                unitLabel = timeFormat.format(calendar.getTime());
                if ( unitLabel.equals("0:00") )
                    unitLabel = TimeSeriesContext.DATE_FORMAT.format(calendar.getTime());
                xMajor = (int)(pixelInterval * i - context.getXAxisOriginOffset() / summary.getPixelTime());
                g.drawLine(xMajor, 0, xMajor, Configuration.getPropertyInt("graph.axis.x.marking.major.height"));
                g.drawString(unitLabel, xMajor - ( g.getFontMetrics().stringWidth(unitLabel) / 2), Configuration.getPropertyInt("graph.axis.x.marking.label.offset") + g.getFontMetrics().getHeight());
                for ( int line = 1; line < context.getXAxisMinorMarks(); line++ ) {
                    xMinor = xMajor + (int)(line * pixelInterval / context.getXAxisMinorMarks());
                    g.drawLine(xMinor, 0, xMinor, Configuration.getPropertyInt("graph.axis.x.marking.minor.height"));
                }
                i++;
            }
        } else {
            i = 0;
            xMajor = panelWidth - 1;
            while ( xMajor >= 0 ) {
                unitLabel = Integer.toString(i * context.getXAxisInterval()) + " " + context.getXAxisUnits();
                xMajor = panelWidth - 1 - (int)( pixelInterval * i );
                g.drawLine(xMajor, 0, xMajor, Configuration.getPropertyInt("graph.axis.x.marking.major.height"));
                if ( xMajor > g.getFontMetrics().stringWidth(unitLabel) / 2)
                    g.drawString(unitLabel, xMajor - ( g.getFontMetrics().stringWidth(unitLabel) / 2), Configuration.getPropertyInt("graph.axis.x.marking.label.offset") + g.getFontMetrics().getHeight());
                for ( int line = 1; line < context.getXAxisMinorMarks(); line++ ) {
                    xMinor = xMajor + (int)(line * pixelInterval / context.getXAxisMinorMarks());
                    g.drawLine(xMinor, 0, xMinor, Configuration.getPropertyInt("graph.axis.x.marking.minor.height"));
                }
                i++;
            }
        }
    }
    
}
