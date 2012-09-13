/*
 * ResponseCollectorDefinition.java
 *
 * Created on 1 September 2012, 10:28
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

import java.net.UnknownHostException;
import nz.co.abrahams.asithappens.collectors.CollectorDefinition;
import nz.co.abrahams.asithappens.collectors.DataCollector;
import nz.co.abrahams.asithappens.core.Configuration;
import nz.co.abrahams.asithappens.core.DataType;
import nz.co.abrahams.asithappens.snmputil.SNMPException;
import nz.co.abrahams.asithappens.storage.Device;

public class ResponseCollectorDefinition extends CollectorDefinition {

    /** Units */
    public static final String UNITS = "ms";
    
    /** Initial headings */
    public static final String[] INITIAL_HEADINGS = {"Response"};

    /**
     * Creates a new ResponseCollectorDefinition.
     *
     * @param device the name or IP address of the target device
     * @param pollInterval the polling interval in milliseconds
     */
    public ResponseCollectorDefinition(String title, Device device, long pollInterval, boolean storing) {
        super(title, device, pollInterval, DataType.RESPONSE, UNITS, storing);
    }
    
    public String[] getInitialHeadings() {
        return INITIAL_HEADINGS;
    }
    
    public DataCollector spawnCollector() throws UnknownHostException, SNMPException {
        if ( Configuration.getProperty("collector.response.class").equals("windows")) {
            return new ResponseWindowsCollector(this);
        } else {
            return new ResponseCollector(this);            
        }
    }
    

}
