/*
 * CustomOIDCollectorDefinition.java
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
package nz.co.abrahams.asithappens.oid;

import java.net.UnknownHostException;
import java.util.Vector;
import nz.co.abrahams.asithappens.collectors.CollectorDefinition;
import nz.co.abrahams.asithappens.core.DataType;
import nz.co.abrahams.asithappens.snmputil.SNMPException;
import nz.co.abrahams.asithappens.storage.Device;

public class CustomOIDCollectorDefinition extends CollectorDefinition {

    /** Units */
    public static final String UNITS = "?";
    
    /** Initial headings */
    //public static final String[] INITIAL_HEADINGS = {};
    
    /** OIDs to collect from */
    Vector<CustomOID> oids;
    
    /** Initial headings */
    //protected String[] initialHeadings;

    /**
     * Creates a new CustomOIDCollectorDefinition.
     *
     * @param device the name or IP address of the target device
     * @param pollInterval the polling interval in milliseconds
     */
    public CustomOIDCollectorDefinition(String title, Device device, long pollInterval, boolean storing,
            Vector<CustomOID> oids, String units) {
        super(title, device, pollInterval, DataType.OID, units, storing);
        this.oids = oids;
    }
    
    public String[] getInitialHeadings() {
        String[] headings;
        
        headings = new String[oids.size()];
        for (int i = 0; i < oids.size(); i++) {
            headings[i] = oids.elementAt(i).label;
        }
        return headings;
    }
    
    public Vector<CustomOID> getOIDs() {
        return oids;
    }

    public CustomOIDCollector spawnCollector() throws UnknownHostException, SNMPException {
        CustomOIDSNMP snmp;
        
        snmp = new CustomOIDSNMP(device);
        return new CustomOIDCollector(this, snmp);
    }
}
