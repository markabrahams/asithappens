/*
 * TimeSeriesYAxisPanel.java
 *
 * Created on 5 November 2003, 09:30
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

import nz.co.abrahams.asithappens.core.Configuration;
import nz.co.abrahams.asithappens.*;
import org.apache.log4j.Logger;
import java.awt.*;
import java.text.*;

/**
 * A y-axis panel that displays value markings.  This should be positioned to
 * the immediate left of a panel displaying data (e.g. a GraphPanel), and
 * should always have the same understanding of the "top-of-graph" value as the
 * data panel so that the y-axis scaling matches the displayed data.  The
 * bottom of the y-axis panel (i.e. the origin of the graph) is assumed to have
 * value zero.
 * <p>
 * The y-axis markings are calculated according to the "top-of-graph" value.
 * The marking labels can be "formatted", in which case they are shortened
 * with a one-letter qualifier.  For example, 50000000 becomes 50 M when
 * formatted.  Letters used are:
 * <ul>
 * <li>K - kilo (10^3)
 * <li>M - mega (10^6)
 * <li>G - giga (10^9)
 * </ul>
 *
 * @author mark
 */
public class TimeSeriesYAxisPanel extends javax.swing.JPanel {
    
    protected static final int MAJOR_MARK_WIDTH = 5;
    protected static final int MINOR_MARK_WIDTH = 2;
    protected static final int GAP_TO_LABEL = 5;
    
    /** Logging provider */
    protected Logger logger;
    
    /** Graph context */
    protected TimeSeriesContext context;
    
    /**
     * Creates a new TimeSeriesYAxisPanel.
     *
     * @param context the graph context for the y-axis panel
     */
    public TimeSeriesYAxisPanel(TimeSeriesContext context) {
        logger = Logger.getLogger(this.getClass().getName());
        this.context = context;
    }
    
    /**
     * Paints the y-axis panel.  This draws:
     * <ul>
     * <li> the y-axis line
     * <li> the y-axis markings
     * <li> the value labels for the y-axis markings (subject to formatting)
     * </ul>
     *
     * @param g the Graphics object to draw on
     */
    public void paintComponent(Graphics g) {
        int panelWidth, panelHeight;
        long desiredInterval;
        int fontHeight;
        int i, y;
        int majorIndex;
        int yMajor;
        int yMinor;
        double pixelInterval;
        String unitLabel;
        
        panelWidth = getWidth();
        panelHeight = getHeight();
        //panelHeight = context.getPanelHeight();
        if ( panelHeight != context.getPanelHeight() ) {
            logger.warn("y-axis panel height (" + panelHeight + ") does not equal graph panel height (" + context.getPanelHeight() + ")");
        }
        g.drawLine(panelWidth - 1, 0, panelWidth - 1, panelHeight - 1);
        
        desiredInterval = context.getYAxisInterval();
        pixelInterval = (desiredInterval * panelHeight) / context.getGraphTop();
        if ( pixelInterval < Configuration.getPropertyInt("graph.axis.y.marking.major.gap.minimum") ) {
            logger.warn("pixelInterval (" + pixelInterval + ") is less than minimum y-axis major marking gap ("
                    + Configuration.getPropertyInt("graph.axis.y.marking.major.gap.minimum") + ")," + " panelHeight=" + panelHeight +
                    ", graphTop=" + context.getGraphTop() + ", desInt=" + desiredInterval);
            return;
        }
        
        majorIndex = 0;
        yMajor = panelHeight - 1;
        while ( yMajor >= 0 ) {
            if ( context.getYAxisFormattedUnits() )
                unitLabel = formatLabel(majorIndex * desiredInterval);
            else
                unitLabel = Long.toString(majorIndex * desiredInterval);
            g.drawString(unitLabel, panelWidth - Configuration.getPropertyInt("graph.axis.y.marking.major.width") - Configuration.getPropertyInt("graph.axis.y.marking.label.offset") - g.getFontMetrics().stringWidth(unitLabel), yMajor);
            g.drawLine(panelWidth - 1 - Configuration.getPropertyInt("graph.axis.y.marking.major.width"), yMajor, panelWidth - 1, yMajor);
            
            for ( int line = 1; line < context.getYAxisMinorMarks(); line++ ) {
                yMinor = yMajor - (int)(line * pixelInterval / context.getYAxisMinorMarks());
                g.drawLine(panelWidth - 1 - Configuration.getPropertyInt("graph.axis.y.marking.minor.width"), yMinor, panelWidth - 1, yMinor);
            }
            
            majorIndex++;
            yMajor = panelHeight - 1 - (int)(majorIndex * pixelInterval);
        }
    }
    
    /**
     * Returns a formatted label to appear beside a graph mark.
     *
     * @param number the number to be formatted
     * @return       the formatted label
     */
    private String formatLabel(long number) {
        double exponent;
        char unit;
        double formatNumber;
        DecimalFormat df;
        
        df = new DecimalFormat("###.##");
        formatNumber = number;
        unit = ' ';
        
        // can't use 10 because of rounding of imprecise results
        exponent = StrictMath.log(number) / StrictMath.log(9.9999999);
        if ( exponent >= 3 && exponent < 6 ) {
            unit = 'K';
            formatNumber = (double)number / 1000;
        } else if ( exponent >= 6 && exponent < 9 ) {
            unit = 'M';
            formatNumber = (double)number / 1000000;
        } else if ( exponent >= 9 ) {
            unit = 'G';
            formatNumber = (double)number / 1000000000;
        }
        // System.out.println("unit: " + unit + " Exponenet: " + exponent + " formatNumber: " + formatNumber);
        if ( unit == ' ' )
            return df.format(formatNumber);
        else
            return df.format(formatNumber) + " " + unit;
    }
    
}
