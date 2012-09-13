/*
 * CaptureAttributes.java
 *
 * Created on 31 August 2012, 22:21
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
package nz.co.abrahams.asithappens.capture;

import nz.co.abrahams.asithappens.core.DataType;
import nz.co.abrahams.asithappens.storage.DataAttributesTitle;

/**
 *
 * @author mark
 */
public class CaptureAttributes extends DataAttributesTitle {
    
    public static final String[] INITIAL_HEADINGS = {};
    
    public static final String UNITS = "bps";
    
    protected String fileName;
    
    protected long startTime;
    
    protected long finishTime;
    
    public CaptureAttributes(String fileName) {
        super("Packet capture for " + fileName);
        this.fileName = fileName;
    }
    
    public DataType getDataType() {
        return DataType.CAPTURE;
    }
    
    // Cannot change data type
    public void setDataType(DataType dataType) {
    }
    
    public String[] getInitialHeadings() {
        return INITIAL_HEADINGS;
    }
    
    public String getUnits() {
        return UNITS;
    }
    
    // Cannot change units
    public void setUnits(String units) {        
    }
    
    public String getDescription() {
        return "Packet capture for " + fileName;
    }
    
}
