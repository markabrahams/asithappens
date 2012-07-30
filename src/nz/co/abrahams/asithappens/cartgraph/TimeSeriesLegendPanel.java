/*
 * TimeSeriesLegendPanel.java
 *
 * Created on 20 May 2005, 23:00
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
import java.awt.*;
import java.util.*;
import org.apache.log4j.Logger;

/**
 * A JPanel containing a graph legend.
 *
 * @author mark
 */
public class TimeSeriesLegendPanel extends javax.swing.JPanel {
    
    private Logger logger;
    
    /** The pixel width of each representative square of the legend */
    protected static final int RECTANGLE_WIDTH = 10;
    
    /** The pixel height of each representative square of the legend */
    protected static final int RECTANGLE_HEIGHT = 10;
    
    /** The pixel height of each representative square of the legend */
    protected static final int MINIMUM_GAP = 10;
    
    /** The pixel offset of each representative square from the left edge */
    protected static final int X_OFFSET = 10;
    
    /** The gap between a representative square and its associated text */
    protected static final int X_GAP = 10;
    
    /** The color for the rectangular border of each key */
    private static Color borderColor = Color.BLACK;
    
    /** The graph context with this legend */
    protected TimeSeriesContext context;
    
    /** Label font */
    private Font labelFont;
    
    /** Key rectangle width */
    protected int rectangleWidth;
    
    /** Key rectangle height */
    protected int rectangleHeight;
    
    /** Minimum vertical pixel gap between adjacent key rectangles */
    protected int minimumGap;
    
    /** Horizontal pixel offset from left of legend to key rectangle */
    protected int rectangleOffset;
    
    /** Horizontal pixel offset from right of key rectangle to start of text */
    protected int textOffset;
    
    /**
     * Creates a new TimeSeriesLegendPanel.
     *
     * @param context  the graph context for the panel
     */
    public TimeSeriesLegendPanel(TimeSeriesContext context) {
        logger = Logger.getLogger(this.getClass().getName());
        this.context = context;
        labelFont = new Font("Dialog", Font.PLAIN, 9);
        rectangleWidth = Configuration.getPropertyInt("graph.legend.rectangle.width");
        rectangleHeight = Configuration.getPropertyInt("graph.legend.rectangle.height");
        rectangleOffset = Configuration.getPropertyInt("graph.legend.rectangle.offset");
        textOffset = Configuration.getPropertyInt("graph.legend.text.offset");
        minimumGap = Configuration.getPropertyInt("graph.legend.height.gap.minimum");
    }
    
    /**
     * Draws the legend.
     *
     * @param g  the Graphics object to draw on
     */
    public void paintComponent(Graphics g) {
        int fontHeight;
        int maximumValues;
        int numValues;
        int maximumStringWidth;
        String[] labels;
        int minimumPanelWidth;
        int rectangleTop;
        int set;
        
        g.setFont(labelFont);
        fontHeight = g.getFontMetrics().getHeight();
        maximumStringWidth = 0;
        
        //maximumValues = ( getHeight() - minimumGap ) / (Configuration.getProperty("graph.legend.rectangle.height") + minimumGap );
        maximumValues = getHeight() / ( rectangleHeight + minimumGap );
        // must fix for FlowData
        //labels = data.getDescriptions(data.getNumberOfSets());
        labels = context.getData().getHeadings().getHeadings();
        numValues = Math.min(maximumValues, labels.length);
        
        for (int i = 0; i < numValues; i++) {
            if ( context.bottomLeftLegend() ) {
                rectangleTop = ( ( getHeight() - ( labels.length * rectangleHeight ) ) / (labels.length + 1) ) * (i + 1) + (rectangleHeight * i);
                set = i;
            } else {
                rectangleTop = minimumGap + (minimumGap + rectangleHeight) * i;
                if ( Configuration.getProperty("graph.legend.ordering").equals("volume") && context.getSummaryData() != null )
                    set = context.getSummaryData().getRankedSetIndex(i);
                else
                    set = i;
            }
            g.setColor(context.getSetDisplayColor(set));
            g.fillRect(rectangleOffset, rectangleTop, rectangleWidth, rectangleHeight);
            g.setColor(borderColor);
            g.drawRect(rectangleOffset, rectangleTop, rectangleWidth, rectangleHeight);
            g.drawString(labels[set], rectangleOffset + rectangleWidth + textOffset, rectangleTop + fontHeight);
            maximumStringWidth = Math.max(maximumStringWidth, g.getFontMetrics().stringWidth(labels[set]));
        }
        minimumPanelWidth = rectangleOffset + rectangleWidth + textOffset + maximumStringWidth;
        if ( context.getLegendPanelWidth() < minimumPanelWidth) {
            context.setLegendPanelWidth(minimumPanelWidth);
            setMinimumSize(new Dimension(minimumPanelWidth, 20));
            setPreferredSize(new Dimension(minimumPanelWidth, 20));
            revalidate();
            logger.debug("Setting panel width to " + minimumPanelWidth);
        }
    }
    
}
