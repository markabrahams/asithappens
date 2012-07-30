/*
 * TimeSeriesDataPanel.java
 *
 * Created on 10 November 2003, 16:42
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
import nz.co.abrahams.asithappens.storage.DataLabel;
import nz.co.abrahams.asithappens.uiutil.SetDisplay;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import org.apache.log4j.Logger;

/**
 * Panel containing the time-value co-ordinate system with the graph information
 * drawn with this system.  It does not contain the axes or legend - just the
 * bar graph, with one bar per pixel.
 *
 * @author  mark
 */
public class TimeSeriesDataPanel extends javax.swing.JPanel {
    
    public static final int LABEL_PADDING = 4;
    
    /** Logging provider */
    protected static Logger logger = Logger.getLogger(TimeSeriesDataPanel.class);
    
    /** Graph context for the grapn panel */
    protected TimeSeriesContext context;

    /** Mouse handler for panel */
    protected TimeSeriesDataMouseAdapter mouseHandler;
    
    /** Line stroke for the major grid lines */
    protected BasicStroke majorStroke;
    
    /** Line stroke for the minor grid lines */
    protected BasicStroke minorStroke;
    
    /** Creates a new instance of TimeSeriesDataPanel */
    public TimeSeriesDataPanel(TimeSeriesContext context) {
        super();
        
        this.context = context;
        mouseHandler = new TimeSeriesDataMouseAdapter(this);
        majorStroke = new BasicStroke(1.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10, TimeSeriesContext.MAJOR_DASH_ARRAY, 2);
        minorStroke = new BasicStroke(1.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10, TimeSeriesContext.MINOR_DASH_ARRAY, 2);

        /*
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if ( evt.getClickCount() == 2 && evt.getButton() == MouseEvent.BUTTON1 ) {
                    labelEntry(evt.getX(), evt.getY());
                }
            }
        });
        */
        addMouseListener(mouseHandler);

    }
    
    /**
     * Draws the graph.
     *
     * @param g  the graphics object to draw on
     */
    public void paintComponent(Graphics g) {
        int set, i, point;
        int oldx, oldy, x, y;
        int xOffset;
        int panelWidth, panelHeight;
        //int pixelInterval;
        double graphTop;
        double value, valueSum;
        boolean graphBreak;
        SummaryData summary;
        DataLabel label;
        int labelWidth, labelHeight, labelPadding;
        
        panelWidth = getWidth();
        panelHeight = getHeight();
        if ( panelWidth != context.getPanelWidth() || panelHeight != context.getPanelHeight() ) {
            logger.debug("Adjusting panel width to " + panelWidth + " and panel height to " + panelHeight);
            context.setPanelWidth(panelWidth);
            context.setPanelHeight(panelHeight);
            context.generateSummaryData();
            revalidate();
        }
        //logger.debug("Painting TimeSeriesDataPanel: panelWidth=" + panelWidth + " panelHeight=" + panelHeight);
        
        summary = context.getSummaryData();
        if ( summary == null )
            return;
        
        graphTop = context.getGraphTop();
        xOffset = panelWidth - summary.getNumberOfPoints();
        g.clearRect(0, 0, panelWidth - 1, panelHeight - 1);
        
        // Draw grid markings if behind
        if ( context.horizontalGridLines() && ! context.linesInFront() ) {
            drawHorizontalGridLines(g, panelWidth, panelHeight);
        }
        if ( context.verticalGridLines() && ! context.linesInFront() ) {
            drawVerticalGridLines(g, summary, panelWidth, panelHeight);
        }
        
        if ( context.getSetsPositioning() == SetDisplay.Positioning.Grounded ) {
            
            for ( set = 0 ; set < summary.getNumberOfSets() ; set++ ) {
                if ( context.getSetDisplayStyle(set) == SetDisplay.Style.Fill ) {
                    g.setColor(context.getSetDisplayColor(set));
                    for ( point = 0 ; point < summary.getNumberOfPoints() ; point++ ) {
                        x = xOffset + point;
                        value = summary.getValue(set, point);
                        if ( ! Double.isNaN(value) ) {
                            y = (int)(panelHeight - 1 - panelHeight * value / graphTop);
                            g.drawLine(x, panelHeight - 1, x, y);
                        }
                    }
                }
            }
            
            for ( set = 0 ; set < summary.getNumberOfSets() ; set++ ) {
                if ( context.getSetDisplayStyle(set) == SetDisplay.Style.Line ) {
                    oldx = xOffset;
                    oldy = (int)(panelHeight - 1 - panelHeight * summary.getValue(set, 0) / graphTop);
                    graphBreak = false;
                    g.setColor(context.getSetDisplayColor(set));
                    for ( point = 0 ; point < summary.getNumberOfPoints() ; point++ ) {
                        x = xOffset + point;
                        value = summary.getValue(set, point);
                        if ( ! Double.isNaN(value) ) {
                            y = (int)(panelHeight - 1 - panelHeight * value / graphTop);
                            if ( ! graphBreak )
                                g.drawLine(oldx, oldy, x, y);
                            else {
                                g.drawLine(x, y, x, y);
                                graphBreak = false;
                            }
                            oldx = x;
                            oldy = y;
                        } else
                            graphBreak = true;
                    }
                }
            }
        } else if ( context.getSetsPositioning() == SetDisplay.Positioning.Stacked ) {
            
            for ( point = 0 ; point < summary.getNumberOfPoints() ; point++ ) {
                valueSum = 0;
                for ( set = 0 ; set < summary.getNumberOfSets() ; set++ ) {
                    g.setColor(context.getSetDisplayColor(set));
                    x = xOffset + point;
                    value = summary.getValue(set, point);
                    if ( ! Double.isNaN(value) ) {
                        oldy = (int)(panelHeight - 1 - valueSum * panelHeight / graphTop);
                        valueSum += value;
                        y = (int)(panelHeight - 1 - valueSum * panelHeight / graphTop);
                        if ( value > 0 )
                            g.drawLine(x, oldy, x, y);
                        
                    }
                }
            }
        }
        
        if ( context.horizontalGridLines() && context.linesInFront() ) {
            drawHorizontalGridLines(g, panelWidth, panelHeight);
        }
        if ( context.verticalGridLines() && context.linesInFront() ) {
            drawVerticalGridLines(g, summary, panelWidth, panelHeight);
        }
        
        // Draw labels
        if ( context.getShowLabels() ) {
            g.setColor(Color.BLACK);
            for ( i = 0; i < context.getData().getLabels().getLabelCount() ; i++ ) {
                label = context.getData().getLabels().getLabel(i);
                x = context.getXPixelFromTime(label.getTime());
                y = context.getYPixelFromValue(label.getValue());
                if ( label.getTime() > summary.getOriginTime(panelWidth) && label.getTime() < summary.getLastTime() ) {
                    if ( label.getValue() < context.getGraphTop() ) {
                        labelWidth = g.getFontMetrics().stringWidth(label.getLabel());
                        //labelHeight = g.getFontMetrics().getAscent() + g.getFontMetrics().getDescent();
                        labelHeight = g.getFontMetrics().getAscent();
                        //labelPadding = g.getFontMetrics().getLeading();
                        //labelPadding = 4;
                        g.clearRect(x - labelWidth / 2 - LABEL_PADDING, y - labelHeight - LABEL_PADDING, labelWidth + LABEL_PADDING * 2, labelHeight + LABEL_PADDING * 2 );
                        g.drawRect(x - labelWidth / 2 - LABEL_PADDING, y - labelHeight - LABEL_PADDING, labelWidth + LABEL_PADDING * 2, labelHeight + LABEL_PADDING * 2 );
                        g.drawString(label.getLabel(), x - labelWidth / 2, y);
                    }
                }
            }
        }
    }
    
    /**
     * Draws the horizontal grid lines on the graph panel.
     *
     * @param g            the graphics object to draw on
     * @param panelWidth   the width of the graph panel
     * @param panelHeight  the height of the graph panel
     */
    protected void drawHorizontalGridLines(Graphics g, int panelWidth, int panelHeight) {
        Graphics2D g2d;
        Stroke defaultStroke;
        int majorIndex;
        int yMajor;
        int yMinor;
        double pixelInterval;
        
        g2d = (Graphics2D)g;
        g.setColor(Color.LIGHT_GRAY);
        defaultStroke = g2d.getStroke();
        
        pixelInterval = (context.getYAxisInterval() * panelHeight) / context.getGraphTop();
        majorIndex = 0;
        yMajor = panelHeight - 1;
        while ( yMajor >= 0 ) {
            g2d.setStroke(majorStroke);
            if ( yMajor != panelHeight - 1 )
                g.drawLine(0, yMajor, panelWidth - 1, yMajor);
            if ( context.horizontalMinorLines() ) {
                g2d.setStroke(minorStroke);
                for ( int line = 1; line < context.getYAxisMinorMarks(); line++ ) {
                    yMinor = yMajor - (int)(line * pixelInterval / context.getYAxisMinorMarks());
                    g.drawLine(0, yMinor, panelWidth - 1, yMinor);
                }
            }
            //yMajor -= pixelInterval;
            majorIndex++;
            yMajor = panelHeight - 1 - (int)(majorIndex * pixelInterval);
        }
        g2d.setStroke(defaultStroke);
    }
    
    /**
     * Draws the vertical grid lines on the graph panel.
     *
     * @param g            the graphics object to draw on
     * @param summary      the summarized data to graph
     * @param panelWidth   the width of the graph panel
     * @param panelHeight  the height of the graph panel
     */
    protected void drawVerticalGridLines(Graphics g, SummaryData summary, int panelWidth, int panelHeight) {
        Graphics2D g2d;
        Stroke defaultStroke;
        int i;
        int xMajor;
        int xMinor;
        long longTime;
        double pixelInterval;
        
        g2d = (Graphics2D)g;
        g.setColor(Color.LIGHT_GRAY);
        defaultStroke = g2d.getStroke();
        g2d.setStroke(majorStroke);
        
        pixelInterval = context.findTimeInMillis(context.getXAxisInterval(), context.getXAxisUnits()) / summary.getPixelTime();
        if ( context.getXAxisAbsoluteTimes() == true ) {
            i = 0;
            xMajor = 0;
            while ( xMajor < panelWidth ) {
                longTime = (long)(summary.getOriginTime(panelWidth) - context.getXAxisOriginOffset() + (i * TimeSeriesContext.findTimeInMillis(context.getXAxisInterval(), context.getXAxisUnits())));
                xMajor = (int)(pixelInterval * i - context.getXAxisOriginOffset() / summary.getPixelTime());
                g2d.setStroke(majorStroke);
                g.drawLine(xMajor, 0, xMajor, panelHeight - 1);
                if ( context.verticalMinorLines() ) {
                    g2d.setStroke(minorStroke);
                    for ( int line = 1; line < context.getXAxisMinorMarks(); line++ ) {
                        xMinor = xMajor + (int)(line * pixelInterval / context.getXAxisMinorMarks());
                        g.drawLine(xMinor, 0, xMinor, panelHeight - 1);
                    }
                }
                i++;
            }
        } else {
            i = 0;
            xMajor = panelWidth - 1;
            while ( xMajor >= 0 ) {
                xMajor = panelWidth - 1 - (int)( pixelInterval * i );
                g2d.setStroke(majorStroke);
                g.drawLine(xMajor, 0, xMajor, panelHeight - 1);
                if ( context.verticalMinorLines() ) {
                    g2d.setStroke(minorStroke);
                    for ( int line = 1; line < context.getXAxisMinorMarks(); line++ ) {
                        xMinor = xMajor + (int)(line * pixelInterval / context.getXAxisMinorMarks());
                        g.drawLine(xMinor, 0, xMinor, panelHeight - 1);
                    }
                }
                i++;
            }
        }
        g2d.setStroke(defaultStroke);
    }
    
    /**
     * Creates a new label at the specified location.  This method will prompt
     * the user for the name of the label.
     *
     * @param x  the x pixel co-ordinate in the graph panel
     * @param y  the y pixel co-ordinate in the graph panel
     */
    public void labelEntry(int x, int y) {
        JOptionPane pane;
        String labelString;
        DataLabel label;
        long labelTime;
        double labelValue;
        
        labelTime = context.getTimeFromXPixel(x);
        labelValue = context.getValueFromYPixel(y);
        pane = new JOptionPane("Enter label", JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
        labelString = pane.showInputDialog("Enter label");
        if ( labelString != null && labelString.length() > 0 ) {
            label = new DataLabel(labelTime, labelValue, labelString);
            context.getData().getLabels().addLabel(label, false);
        }
        repaint();
    }
}