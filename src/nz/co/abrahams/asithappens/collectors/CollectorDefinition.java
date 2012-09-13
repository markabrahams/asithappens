/*
 * CollectorDefinition.java
 *
 * Created on 1 September 2012, 08:15
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
package nz.co.abrahams.asithappens.collectors;

import java.net.UnknownHostException;
import nz.co.abrahams.asithappens.core.DataType;
import nz.co.abrahams.asithappens.snmputil.SNMPException;
import nz.co.abrahams.asithappens.storage.DataAttributes;
import nz.co.abrahams.asithappens.storage.Device;

/**
 *
 * @author mark
 */
public abstract class CollectorDefinition implements DataAttributes {
    
    /** Title given to collector by user */
    protected String title;
    
    /** Flag to indicate whether the collector will store data in the database */
    protected boolean storing;
    
    /** Device the collector collects from */
    protected Device device;
    
    /** Interval between collection events */
    protected long pollInterval;
    
    /** Type of data the collector collects */
    protected DataType dataType;
    
    /** Units of value axis */
    protected String units;
    
    public CollectorDefinition(String title, Device device, long pollInterval,
            DataType dataType, String units, boolean storing) {
        this.title = title;
        this.device = device;
        this.pollInterval = pollInterval;
        this.dataType = dataType;
        this.units = units;
        this.storing = storing;
    }
    
    public String getTitle() {
        if (title == null) {
            return this.getDescription();
        } else {
            return title;
        }        
    }
    
    public void setTitle(String newTitle) {
        title = newTitle;
    }
    
    public DataType getDataType() {
        return dataType;
    }
    
    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }
    
    public abstract String[] getInitialHeadings();
    
    public abstract DataCollector spawnCollector() throws UnknownHostException, SNMPException;
    
    public String getDescription() {
        return dataType.description + " for " + device.getName();
    }
    
    public Device getDevice() {
        return device;
    }
    
    public long getPollInterval() {
        return pollInterval;
    }
    
    public void setPollInterval(long pollInterval) {
        this.pollInterval = pollInterval;
    }
    
    public String getUnits() {
        return units;
    }
    
    public void setUnits(String newUnits) {
        units = newUnits;
    }
    
    public boolean getStoring() {
        return storing;
    }
    
    public void setStoring(boolean storing) {
        this.storing = storing;
    }
    
}
