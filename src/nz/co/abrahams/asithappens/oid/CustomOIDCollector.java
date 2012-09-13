/*
 * CustomOIDCollector.java
 *
 * Created on 24 February 2006, 22:38
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

import java.util.Vector;
import nz.co.abrahams.asithappens.collectors.DataCollector;
import nz.co.abrahams.asithappens.collectors.DataCollectorResponse;
import nz.co.abrahams.asithappens.snmputil.SNMPException;
import nz.co.abrahams.asithappens.snmputil.SNMPType;
import nz.co.abrahams.asithappens.snmputil.SNMPTypeException;
import nz.co.abrahams.asithappens.storage.DataHeadings;
import nz.co.abrahams.asithappens.storage.DataPoint;
import org.apache.log4j.Logger;

/**
 * Processor load collector.
 *
 * @author  mark
 */
public class CustomOIDCollector implements DataCollector {

    /** Logging provider */
    private static Logger logger = Logger.getLogger(CustomOIDCollector.class);

    /** Collector definition */
    CustomOIDCollectorDefinition definition;
    
    /** SNMP interface */
    private CustomOIDSNMP snmp;

    /** Units for OID value */
    protected String units;

    /** OID list to collect */
    protected Vector<CustomOID> oids;

    /** Values from previous collection */
    protected long[] lastValues;

    /** The time that the previous successful counter reading was made */
    protected long[] lastCollectionTimes;

    /**
     * Creates a new CustomOIDCollector.
     *
     * @param device       the name or IP address of the target device
     * @param pollInterval the polling interval in milliseconds
     */
    public CustomOIDCollector(CustomOIDCollectorDefinition definition, CustomOIDSNMP snmp) throws SNMPException, SNMPTypeException {
        //super(snmp.getDevice(), pollInterval, DataType.OID);
        this.definition = definition;
        this.snmp = snmp;
        //this.units = units;
        oids = definition.getOIDs();
        lastCollectionTimes = new long[oids.size()];
        lastValues = new long[oids.size()];
        for (int i = 0; i < oids.size(); i++) {
            lastCollectionTimes[i] = System.currentTimeMillis();
            lastValues[i] = fetchOIDValue(oids.elementAt(i));
        }
        snmp.setExpedientCollection();
        logger.debug("Starting custom OID collector");
    }

    /**
     * Collects inbound and outbound bandwidth statistics for the collection
     * period.
     *
     * @return an array of two data points - one for inbound and outbound
     */
    public DataCollectorResponse getNextValues(DataHeadings headings) {
        long currentTime;
        long[] newValues;
        long[] newCollectionTimes;
        DataPoint[] returnData;
        //boolean changedValues;
        boolean[] clockedCounters;

        newValues = new long[oids.size()];
        newCollectionTimes = new long[oids.size()];
        returnData = new DataPoint[oids.size()];
        currentTime = System.currentTimeMillis();

        try {
            //changedValues = false;
            //clockedCounter = false;
            for (int i = 0; i < oids.size(); i++) {
                newCollectionTimes[i] = System.currentTimeMillis();
                newValues[i] = fetchOIDValue(oids.elementAt(i));
            }
            for (int i = 0; i < oids.size(); i++) {
                if ( oids.elementAt(i).type == SNMPType.Integer32 || oids.elementAt(i).type == SNMPType.Gauge32 ) {
                    returnData[i] = new DataPoint(newCollectionTimes[i], newValues[i]);
                } else if ( oids.elementAt(i).type == SNMPType.Counter32 || oids.elementAt(i).type == SNMPType.Counter64 ) {
                    long difference;
                    difference = newValues[i] - lastValues[i];
                    if ( difference < 0 ) {
                        if ( oids.elementAt(i).type == SNMPType.Counter32 ) {
                            difference += Integer.MAX_VALUE;
                        } else if ( oids.elementAt(i).type == SNMPType.Counter32 ) {
                            difference += Long.MAX_VALUE;
                        }
                    }
                    returnData[i] = new DataPoint(newCollectionTimes[i], (double)difference / ((double)(newCollectionTimes[i] - lastCollectionTimes[i]) / 1000) );
                }
                lastCollectionTimes[i] = newCollectionTimes[i];
                lastValues[i] = newValues[i];
            }
        } catch (SNMPTypeException e) {
            for (int i = 0; i < oids.size(); i++) {
                returnData[i] = new DataPoint(currentTime);
            }
            logger.warn("SNMP type problem fetching OID values");
        } catch (SNMPException e) {
            for (int i = 0; i < oids.size(); i++) {
                returnData[i] = new DataPoint(currentTime);
            }
            logger.warn("Timeout fetching OID values");
        }
        return new DataCollectorResponse(returnData,
                new String[0], oids.size());
    }

    protected long fetchOIDValue(CustomOID oid) throws SNMPException, SNMPTypeException {
        switch ( oid.type ) {
            case Integer32:
                return ((long) snmp.getCustomOIDInteger32(oid));
            case Gauge32:
                return ((long) snmp.getCustomOIDGauge32(oid));
            case Counter32:
                return ((long) snmp.getCustomOIDCounter32(oid));
            case Counter64:
                return ((long) snmp.getCustomOIDCounter64(oid));
        }
        return ((long) snmp.getCustomOIDInteger32(oid));
    }
    
    public CustomOIDCollectorDefinition getDefinition() {
        return definition;
    }    
    
    public String getValueUnits() {
        return units;
    }
    
    public Vector<CustomOID> getOIDs() {
        return oids;
    }

    /** Empty routine as there are no resources to release. */
    public void releaseCollector() {
    }
}
