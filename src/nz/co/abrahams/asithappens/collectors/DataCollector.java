/*
 * DataCollector.java
 *
 * Created on 1 September 2012, 08:25
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

import nz.co.abrahams.asithappens.storage.DataHeadings;

/**
 * Provides an interface for all "collector" classes.  A collector must provide
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
 *
 * @author  mark
 */
public interface DataCollector {
    
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
    public DataCollectorResponse getNextValues(DataHeadings headings);
    
    /**
     * Releases any resources the data collector may be holding.  This is called
     * when the collector is no longer required.
     */
    public void releaseCollector();
    
    /**
     * Returns the collector definition.
     *
     * @return the collector definition
     */
    public CollectorDefinition getDefinition();
        
}
