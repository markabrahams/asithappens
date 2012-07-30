/*
 * DataLabel.java
 *
 * Created on 12 December 2005, 21:53
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

/**
 * Stores a textual label corresponding to a particular time and value in a data set.
 *
 * @author mark
 */
public class DataLabel {
    
    /* The location of the label */
    protected DataPoint point;
    
    /* The text of the label */
    protected String label;
    
    /**
     * Creates a new DataLabel.
     *
     * @param time  the time that the label corresponds to
     * @param value the data value that the label corresponds to
     * @param label the text of the label
     */
    public DataLabel(long time, double value, String label) {
        point = new DataPoint(time, value);
        this.label = label;
    }
    
    /** @return the time that the label corresponds to */
    public long getTime() {
        return point.getTime();
    }
    
    /** @return the data value that the label corresponds to */
    public double getValue() {
        return point.getValue();
    }
    
    /** @return the text of the label */
    public String getLabel() {
        return label;
    }
}
