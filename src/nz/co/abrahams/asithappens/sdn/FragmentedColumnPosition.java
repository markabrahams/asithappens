/*
 * FragmentedColumnPosition.java
 *
 * Created on 28 December 2007, 17:53
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

package nz.co.abrahams.asithappens.sdn;

/**
 *
 * @author mark
 */
public class FragmentedColumnPosition {
    
    /** Number of set */
    public int setNumber;
    
    /** Number of fragment */
    public int fragmentNumber;
    
    /** Value offset into set */
    public double setOffset;
    
    /** Value offset into fragment */
    public double fragmentOffset;
    
    /** Screen panel x pixel co-ordinate */
    public double xPixel;
    
    /** Screen panel y pixel co-ordinate */
    public double yPixel;
    
    /** Time representing fragmented column */
    public long time;
    
    /** Value point on the graph panel (accounting for fragmentation) */
    public double panelValue;
    
    /** Value point in data */
    public double dataValue;
    
    /** Flag showing whether the point is on landscape or not */
    public boolean onLandscape;
    
    /** Creates a new instance of FragmentedColumnPosition */
    public FragmentedColumnPosition() {
        setNumber = -1;
        fragmentNumber = -1;
        setOffset = Double.NaN;
        fragmentOffset = Double.NaN;
        xPixel = Double.NaN;
        yPixel = Double.NaN;
        time = -1;
        panelValue = Double.NaN;
        dataValue = Double.NaN;
        onLandscape = false;
    }
    
    public void zeroNegativeOffsets() {
        setOffset = Math.max(setOffset, 0);
        fragmentOffset = Math.max(fragmentOffset, 0);
    }
    
    public int getFragmentNumber() {
        return fragmentNumber;
    }
    
    public void setFragmentNumber(int value) {
        fragmentNumber = value;
    }
    
    public double getFragmentOffset() {
        return fragmentOffset;
    }
    
    public void setFragmentOffset(double value) {
        fragmentOffset = value;
    }
    
    public double getDataValue() {
        return dataValue;
    }
    
    public void setDataValue(double value) {
        dataValue = value;
    }

    public double getPanelValue() {
        return panelValue;
    }
    
    public void setPanelValue(double value) {
        panelValue = value;
    }
    
    public int getSetNumber() {
        return setNumber;
    }
    
    public void setSetNumber(int value) {
        setNumber = value;
    }
    
    public double getSetOffset() {
        return setOffset;
    }
    
    public void setSetOffset(double value) {
        setOffset = value;
    }
    
    public void setOnLandscape(boolean value) {
        onLandscape = value;
    }
    
    public boolean isOnLandscape() {
        return onLandscape;
    }
    
    public boolean isFragmentDefined() {
        return fragmentNumber != -1;
    }
    
    public String toString() {
        StringBuffer buffer;
        
        buffer = new StringBuffer();
        buffer.append("setNumber=" + setNumber + ",");
        buffer.append("setOffset=" + setOffset + ",");
        buffer.append("fragmentNumber=" + fragmentNumber + ",");
        buffer.append("fragmentOffset=" + fragmentOffset + ",");
        buffer.append("time=" + time + ",");
        buffer.append("dataValue=" + dataValue + ",");
        buffer.append("panelValue=" + panelValue + ",");
        buffer.append("xPixel=" + xPixel + ",");
        buffer.append("yPixel=" + yPixel + ",");
        buffer.append("onLandscape=" + onLandscape);
        return buffer.toString();
    }
    
    
}
