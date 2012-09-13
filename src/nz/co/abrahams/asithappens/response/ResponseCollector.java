/*
 * ResponseCollector.java
 *
 * Created on 19 December 2005, 20:13
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

package nz.co.abrahams.asithappens.response;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import nz.co.abrahams.asithappens.collectors.DataCollector;
import nz.co.abrahams.asithappens.collectors.DataCollectorResponse;
import nz.co.abrahams.asithappens.storage.DataHeadings;
import nz.co.abrahams.asithappens.storage.DataPoint;

/**
 * Collect response information from a device using the Java InetAddress.isReachable()
 * method.  This method prefers to use ICMP, but falls back to UDP echo if ICMP is
 * unavailable.
 *
 * @author mark
 */
public class ResponseCollector implements DataCollector {
    
    /** Collector definition */
    ResponseCollectorDefinition definition;
    /** time to wait for each poll response before giving up */
    private long timeout;
    /** address of target device */
    private InetAddress deviceAddress;
    /** name of device */
    private String deviceString;    
    
    /** Creates a new instance of ResponseCollector */
    public ResponseCollector(ResponseCollectorDefinition definition) throws UnknownHostException {
        //super(device, pollInterval, DataType.RESPONSE);
        this.definition = definition;
        deviceAddress = definition.getDevice().getResolvedAddress();
        timeout = definition.getPollInterval() / 2;
    }
    
    public DataCollectorResponse getNextValues(DataHeadings headings) {
        long startTime;
        long responseTime;
        DataPoint[] point;
     
        point = new DataPoint[1];
        startTime = System.currentTimeMillis();
     
        try {
            if ( deviceAddress.isReachable((int)timeout) ) {
                responseTime = System.currentTimeMillis() - startTime;
                //System.out.println("Response" + responseTime);
            }
            else {
                responseTime = timeout;
                //responseTime = System.currentTimeMillis() - startTime;
                //System.out.println("Timeout " + responseTime);
            }
            point[0] = new DataPoint(startTime, (double)responseTime);
     
            return new DataCollectorResponse(point, new String[0], definition.getInitialHeadings().length);
        }
        catch (IOException e) {
            point[0] = new DataPoint(startTime, timeout);
        }
        return new DataCollectorResponse(point, new String[0], definition.getInitialHeadings().length);
    }

    public ResponseCollectorDefinition getDefinition() {
        return definition;
    }    
    
    public void releaseCollector() {
    }
}
