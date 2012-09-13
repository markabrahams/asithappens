/*
 * TimeSeriesOptions.java
 *
 * Created on 10 July 2008, 22:24
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
 *
 */

package nz.co.abrahams.asithappens.cartgraph;

import java.awt.Color;
import java.util.Vector;
import nz.co.abrahams.asithappens.cartgraph.TimeSeriesContext.Aggregation;
import nz.co.abrahams.asithappens.cartgraph.TimeSeriesContext.Interpolation;
import nz.co.abrahams.asithappens.uiutil.SetDisplay;

/**
 *
 * @author mark
 */
public class TimeSeriesOptions {
    
    /** Aggregation policy */
    protected Aggregation aggregation;
    
    /** Interpolation policy */
    protected Interpolation interpolation;
    
    /** True if graph top is fixed, false if automatic scaling of graph top */
    public boolean useFixedGraphTop;
    
    /** The user-defined value (scaled by the units) fixed top of the graph */
    public double fixedGraphTop;
    
    /** The user-defined units of the fixed top of the graph */
    public char fixedGraphTopUnits;
    
     /*
      * Switch that mandates x-axis time markings; true for clock-based times,
      * false for time elapsed relative to right edge of graph
      */
    
    /** Show absolute times on x-axis */
    protected boolean xAxisAbsoluteTimes;
    
    /** Format y-axis units */
    protected boolean yAxisFormattedUnits;
    
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
    
    /** Graph type - grounded or stacked */
    public SetDisplay.Positioning setsPositioning;
    
    /** The graphical display information for each data set */
    public Vector<SetDisplay> setDisplays;
    
    
    /** Creates a new instance of TimeSeriesOptions */
    public TimeSeriesOptions() {        
        // defaults
        aggregation = Aggregation.Average;
        interpolation = Interpolation.None;
        setsPositioning = SetDisplay.Positioning.Grounded;
        setDisplays = new Vector();
        useFixedGraphTop = false;
        fixedGraphTop = 0;
        fixedGraphTopUnits = ' ';
        xAxisAbsoluteTimes = true;
        yAxisFormattedUnits = true;
        horizontalGridLines = true;
        horizontalMinorLines = true;
        verticalGridLines = true;
        verticalMinorLines = true;
        linesInFront = true;
        stickyWindow = false;
        showLabels = true;
        showTrim = true;
        bottomLeftLegend = false;
        
    }
    
    /** @return the aggregation policy for the context */
    public Aggregation getAggregation() {
        return aggregation;
    }
    
    /** @param value  the new aggregation policy for the context */
    public void setAggregation(Aggregation value) {
        aggregation = value;
    }
    
    /** @return the interpolation policy for the context */
    public Interpolation getInterpolation() {
        return interpolation;
    }
    
    /** @param value  the new interpolation policy for the context */
    public void setInterpolation(Interpolation value) {
        interpolation = value;
    }
    
    /** @return the policy for positioning the sets */
    public SetDisplay.Positioning getSetsPositioning() {
        return setsPositioning;
    }
    
    /** @param value  the new set positioning policy */
    public void setSetsPositioning(SetDisplay.Positioning positioning) {
        setsPositioning = positioning;
    }
    
    public Vector<SetDisplay> getSetDisplays() {
        return setDisplays;
    }
    
    public SetDisplay getSetDisplay(int set) {
        extendSetDisplays(set);
        return setDisplays.elementAt(set);
    }
    
    /**
     * @param set  the index of the data set
     * @return     the color for the indexed data set
     */
    public Color getSetDisplayColor(int set) {
        extendSetDisplays(set);
        return setDisplays.elementAt(set).color;
        //return lineColor[set % lineColor.length];
    }
    
    /**
     * @param set  the index of the data set
     * @return     the line type for the indexed data set
     */
    public SetDisplay.Style getSetDisplayStyle(int set) {
        extendSetDisplays(set);
        return setDisplays.elementAt(set).style;
        //return lineType[set];
    }
    
    public void extendSetDisplays(int set) {
        while ( setDisplays.size() - 1 < set ) {
            addSetDisplay(set);
        }
    }
    
    public void addSetDisplay(int set) {
        addSetDisplay(SetDisplay.create(SetDisplay.DYNAMIC_DEFAULTS[setDisplays.size() % SetDisplay.DYNAMIC_DEFAULTS.length]));
    }
    
    public void addSetDisplay(SetDisplay newDisplay) {
        setDisplays.add(newDisplay);
    }
    
    public void setSetDisplays(Vector<SetDisplay> newDisplays) {
        setDisplays = newDisplays;
    }
    
    public void setSetDisplayColor(int set, Color color) {
        setDisplays.elementAt(set).color = color;
    }
    
    public void setSetDisplayStyle(int set, SetDisplay.Style style) {
        setDisplays.elementAt(set).style = style;
    }
    
    /** @return true if fixed graph top is used, false if not */
    public boolean getUseFixedGraphTop() {
        return useFixedGraphTop;
    }
    
    /** @param value  switch to toggle the use of the fix graph top */
    public void setUseFixedGraphTop(boolean value) {
        useFixedGraphTop = value;
    }
    
    /** @return fixed graph top */
    public double getFixedGraphTop() {
        return fixedGraphTop;
    }
    
    /** @param value  the new manually fixed graph top */
    public void setFixedGraphTop(double value) {
        fixedGraphTop = value;
    }
    
    /** @return fixed graph top units */
    public char getFixedGraphTopUnits() {
        return fixedGraphTopUnits;
    }
    
    /** @param value  the new manually fixed graph top units */
    public void setFixedGraphTopUnits(char value) {
        fixedGraphTopUnits = value;
    }
    
    /** @return x-axis time displya format */
    public boolean getXAxisAbsoluteTimes() {
        return xAxisAbsoluteTimes;
    }
    
    /** @param flag  switch that specifies display of absolute or relative x-axis times */
    public void setXAxisAbsoluteTimes(boolean flag) {
        xAxisAbsoluteTimes = flag;
    }
    
    /** return true if absolute x-axis markings are used, false for relative markings */
    public boolean getAbsoluteMarkings() {
        return xAxisAbsoluteTimes;
    }
    
    /** @param flag  setting for absolute x-axis markings */
    public void setAbsoluteMarkings(boolean flag) {
        xAxisAbsoluteTimes = flag;
    }
    
    /** @return true if y-axis text markings will be formatted by appropriate units */
    public boolean getYAxisFormattedUnits() {
        return yAxisFormattedUnits;
    }
    
    /** @param flag  specifies whether to format units or not */
    public void setYAxisFormattedUnits(boolean flag) {
        yAxisFormattedUnits = flag;
    }
    
    /** @return true if horizontal grid lines are showing */
    public boolean getHorizontalGridLines() {
        return horizontalGridLines;
    }
    
    /** @param flag  setting to show horizontal grid lines */
    public void setHorizontalGridLines(boolean flag) {
        horizontalGridLines = flag;
    }
    
    /** @return true if horizontal minor grid lines are showing */
    public boolean getHorizontalMinorLines() {
        return horizontalMinorLines;
    }
    
    /** @param flag  setting to show horizontal minor grid lines */
    public void setHorizontalMinorLines(boolean flag) {
        horizontalMinorLines = flag;
    }
    
    /** @return true if vertical grid lines are showing */
    public boolean getVerticalGridLines() {
        return verticalGridLines;
    }
    
    /** @param flag  setting to show vertical grid lines */
    public void setVerticalGridLines(boolean flag) {
        verticalGridLines = flag;
    }
    
    /** @return true if vertical minor grid lines are showing */
    public boolean getVerticalMinorLines() {
        return verticalMinorLines;
    }
    
    /** @param flag  setting to show vertical minor grid lines */
    public void setVerticalMinorLines(boolean flag) {
        verticalMinorLines = flag;
    }
    
    /** @return true if horizontal grid lines are drawn in from of the graph */
    public boolean getLinesInFront() {
        return linesInFront;
    }
    
    /** @param flag  setting to draw horizontal grid lines in front of graph */
    public void setLinesInFront(boolean flag) {
        linesInFront = flag;
    }
    
    /** @return true if graph window is "always on top" */
    public boolean getStickyWindow() {
        return stickyWindow;
    }
    
    /** @param flag  setting for "always on top" feature */
    public void setStickyWindow(boolean flag) {
        stickyWindow = flag;
    }
    
    /** @return true if the graph is showing text labels */
    public boolean getShowLabels() {
        return showLabels;
    }
    
    /** @param flag  setting for the display of text labels */
    public void setShowLabels(boolean flag) {
        showLabels = flag;
    }
    
    /** @return true if the graph is showing trim */
    public boolean getShowTrim() {
        return showTrim;
    }
    
    /** @param flag  setting for the display of graph trim */
    public void setShowTrim(boolean flag) {
        showTrim = flag;
    }
    
    /** @return true if the legend is at the bottom-left of the graph, false if to the right */
    public boolean getBottomLeftLegend() {
        return bottomLeftLegend;
    }
    
    /** @param flag  setting for the location of the legend */
    public void setBottomLeftLegend(boolean flag) {
        bottomLeftLegend = flag;
    }
    
    public String toString() {
        StringBuffer buffer;
        
        buffer = new StringBuffer();
        buffer.append("aggregation = " + aggregation + "\n");
        buffer.append("interpolation = " + interpolation + "\n");
        buffer.append("setsPositioning = " + setsPositioning + "\n");
        buffer.append("yAxisFormattedUnits = " + yAxisFormattedUnits + "\n");
        buffer.append("bottomLeftLegend = " + bottomLeftLegend + "\n");
        buffer.append("fixedGraphTop = " + fixedGraphTop + "\n");
        buffer.append("fixedGraphTopUnits = " + fixedGraphTopUnits + "\n");
        buffer.append("xAxisAbsoluteTimes = " + xAxisAbsoluteTimes + "\n");
        buffer.append("horizontalGridLines = " + horizontalGridLines + "\n");
        buffer.append("horizontalMinorLines = " + horizontalMinorLines + "\n");
        buffer.append("verticalGridLines = " + verticalGridLines + "\n");
        buffer.append("verticalMinorLines = " + verticalMinorLines + "\n");
        buffer.append("linesInFront = " + linesInFront + "\n");
        buffer.append("stickyWindow = " + stickyWindow + "\n");
        buffer.append("showLabels = " + showLabels + "\n");
        buffer.append("showTrim = " + showTrim + "\n");
        
        return buffer.toString();
    }
    
}
