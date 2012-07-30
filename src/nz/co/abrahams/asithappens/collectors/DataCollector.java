/*
 * DataCollector.java
 *
 * Created on 11 November 2003, 11:55
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

import nz.co.abrahams.asithappens.core.DataType;
import nz.co.abrahams.asithappens.storage.DataHeadings;
import nz.co.abrahams.asithappens.storage.Device;
import org.apache.log4j.Logger;

/**
 * Provides a superclass for all "collector" classes.  A collector must provide
 * only two abstract methods:
 * <ol>
 * <li>getNextValues - to collect a set of data values
 * <li>releaseCollector - called to release any resources when the collector
 *     is stopped
 * </ol>
 * <p>
 * The collector contains a poll interval, but does not initiate the poll
 * events.  These are called externally, by a caller that is expected to keep
 * the convention of polling roughly every polling interval.
 * <p>
 * If the database handle is set, data will be written to the database as
 * collected.  If the database handle is "null" then data is collected but not
 * stored in the database.
 *
 * @author  mark
 */
public abstract class DataCollector {
    
    /** Logging provider */
    private static Logger logger = Logger.getLogger(DataCollector.class);
    
    /** Number of data sets to collect for */
    //protected int sets;
    
    /** Collection target device */
    protected Device device;
    
    /** The desired time interval in milliseconds between successive polling instances */
    protected long pollInterval;
    
    /** Data storage object */
    //protected DataSets data;
    
    /** Type of data collected */
    protected DataType dataType;

    /** Number of sets currently being collected */
    protected int setCount;
    
    /**
     * Creates a new DataCollector.  Must be called by subclass constructors.
     *
     * @param device       the device to poll against
     * @param pollInterval the polling interval
     */
    public DataCollector(Device device, long pollInterval, DataType dataType) {
        this.device = device;
        this.pollInterval = pollInterval;
        this.dataType = dataType;
        setCount = dataType.initialSetCount();
    }
    
    /**
     * Initializes the data collector.  This is should be called in the
     * constructor of any concrete subclasses.
     */
    //protected abstract void initCollector() throws UnknownHostException;
    
    /**
     * Collects a set of data points as per the collector implementation.
     *
     * @return the set of data points
     */
    public abstract DataCollectorResponse getNextValues(DataHeadings headings);
    
    /**
     * Releases any resources the data collector may be holding.  This is called
     * when the collector is no longer required.
     */
    public abstract void releaseCollector();
        
    /**
     * Collects a single data value for each data set.
     * <p>
     * This method undertakes the following tasks as a part of collecting:
     * <ul>
     * <li> stores the time of the collection event
     * <li> invokes getNextValues() to add a value to the end of each data set
     * <li> adds the time and values to the database if database storing is
     *      enabled
     * <li> removes the oldest data values if the size of the data sets exceeds
     *      the maximum limit
     * </ul>
     */
    
    /*
    public void collect() {
        
        getNextValues();
        
        // Database recording
        if ( dba != null ) {
            for ( int set = 0 ; set < data.length ; set++ )
                dba.addValues(sessionID, set, data[set].lastElement().getTime(),
                              data[set].lastElement().getValue() );
        }
        
        // remove data values that will not be displayed
        for ( int set = 0 ; set < data.length ; set++ ) {
            if ( data[set].size() >= MAXIMUM_DATA_POINTS )
                data[set].remove(0);
        }
    }
     */

    /**
     * Returns the target device
     *
     * @return the target device
     */
    public Device getDevice() {
        return device;
    }
    
    /**
     * Returns the desired polling interval
     *
     * @return the polling interval
     */
    public long getPollInterval() {
        return pollInterval;
    }
    
    /**
     * Returns the data type for data being collected
     *
     * @return the data type
     */
    public DataType getDataType() {
        return dataType;
    }
    
    public Class getDAOClass() throws ClassNotFoundException {
        logger.info("Class name: " + getClass().getSimpleName());
        return Class.forName(getClass().getSimpleName());
    }
    
}
