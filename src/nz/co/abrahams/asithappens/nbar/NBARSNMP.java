/*
 * NBARSNMP.java
 *
 * Created on 09 January 2010, 13:12
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

package nz.co.abrahams.asithappens.nbar;

import nz.co.abrahams.asithappens.core.Configuration;
import nz.co.abrahams.asithappens.snmputil.SNMPAccess;
import nz.co.abrahams.asithappens.snmputil.SNMPException;
import nz.co.abrahams.asithappens.snmputil.SNMPInterface;
import nz.co.abrahams.asithappens.storage.Device;
import nz.co.abrahams.asithappens.storage.DataSets;
import org.apache.log4j.Logger;
import java.net.UnknownHostException;

/**
 *
 * @author mark
 */
public class NBARSNMP extends SNMPInterface {

    /** Logging provider */
    private static Logger logger = Logger.getLogger(NBARSNMP.class);

    /* SNMP interface */
    //private SNMPAccess snmpAccess;

    /**
     * Creates the SNMP interface for bandwidth collection.
     *
     * @param device
     * @throws UnknownHostException
     * @throws SNMPException
     */
    public NBARSNMP(Device device) throws UnknownHostException, SNMPException {
        super(device);
        snmpAccess = device.createSNMPWriteInterface();
    }

    /**
     * Creates an NBAR Top-N table, which can subsequently be polled for statistics.
     *
     * @param port        ifIndex of desired port for monitoring
     * @param direction   direction of information flow for monitoring
     * @param tableSize   Top-N table size
     * @param sampleTime  time between statistic samples
     * @return            MIB index of the new NBAR Top-N table
     */
    public int setNBARTopNConfigTable(int port, int direction, int tableSize, int sampleTime) throws SNMPException {
        int result;
        int tableIndex;

        logger.debug("Creating Top-N table");
        tableIndex = 1;
        try {
            while ( tableIndex <= Configuration.getPropertyInt("collector.nbar.table.index.maximum") ) {
                result = snmpAccess.getMIBValueInteger(SNMPAccess.OID_cnpdTopNConfigIfIndex + "." + tableIndex);
                tableIndex++;
            }
        }
        // Exception thrown when value doesn't exist indicating a free table index
        catch (Exception e) {

        }
        result = snmpAccess.setMIBValueInteger(SNMPAccess.OID_cnpdTopNConfigIfIndex + "." + tableIndex, port);
        result = snmpAccess.setMIBValueInteger(SNMPAccess.OID_cnpdTopNConfigStatsSelect + "." + tableIndex, direction);
        result = snmpAccess.setMIBValueInteger(SNMPAccess.OID_cnpdTopNConfigRequestedSize + "." + tableIndex, tableSize);
        //snmpAccess.setMIBValueGauge32(SNMPAccess.OID_cnpdTopNConfigSampleTime + "." + tableIndex, (long)sampleTime);
        snmpAccess.setMIBValueGauge32(SNMPAccess.OID_cnpdTopNConfigSampleTime + "." + tableIndex, 10);
        result = snmpAccess.setMIBValueInteger(SNMPAccess.OID_cnpdTopNConfigStatus + "." + tableIndex, SNMPAccess.ROWSTATUS_createAndGo);

        logger.info("Created Top-N table: TableIndex=" + tableIndex + ", ifIndex=" + port + ", direction=" + DataSets.DIRECTIONS[direction]
                + ", TableSize=" + tableSize + ", SampleTime=" + sampleTime);
        return tableIndex;
    }

    /**
     * Sets a MIB value to trigger an NBAR Top-N table statistics update.
     *
     * @param tableIndex  index of NBAR Top-N table
     * @return            the result of the SNMP set command
     */
    public int setNBARTopNNewInterval(int tableIndex) throws SNMPException {
        return snmpAccess.setMIBValueInteger(SNMPAccess.OID_cnpdTopNConfigStatus + "." + tableIndex, SNMPAccess.ROWSTATUS_active);
    }

    /**
     * Retrieve the names of the current protocols in the given NBAR Top-N table.
     *
     * @param tableIndex  index of NBAR Top-N table
     * @param tableSize   number of desired table entries to retrieve
     * @return            the list of NBAR protocol names
     */
    public String[] getNBARTopNProtocols(int tableIndex, int tableSize) throws SNMPException {
        String[] protocols;
        int i;

        protocols = new String[tableSize];
        i = 0;
        try {
            for ( i = 0; i < tableSize; i++ ) {
                protocols[i] = snmpAccess.getMIBValueString(SNMPAccess.OID_cnpdTopNStatsProtocolName + "." + tableIndex + "." + (i + 1));
            }
            if (logger.isDebugEnabled()) {
                StringBuffer buffer = new StringBuffer("Protocols:");
                for ( int j = 0; j < tableSize; j++ ) {
                    buffer.append(" " + protocols[j]);
                }
                logger.debug(buffer.toString());
            }

            return protocols;
        } catch (Exception e) {
            String[] partialProtocols;

            partialProtocols = new String[i];
            System.arraycopy(protocols, 0, partialProtocols, 0, i);
            return partialProtocols;
        }
    }

    /**
     * Retrieve the rates for the current protocols in the given NBAR Top-N table.
     *
     * @param tableIndex  index of NBAR Top-N table
     * @param tableSize   number of desired table entries to retrieve
     * @return            the list of NBAR protocol rates
     */
    public long[] getNBARTopNRates(int tableIndex, int tableSize) throws SNMPException {
        long[] rates;
        int i;

        rates = new long[tableSize];
        i = 0;
        try {
            for ( i = 0; i < tableSize; i++ ) {
                rates[i] = snmpAccess.getMIBValueCounter32(SNMPAccess.OID_cnpdTopNStatsRate + "." + tableIndex + "." + (i + 1));
            }
            if (logger.isDebugEnabled()) {
                StringBuffer buffer = new StringBuffer("Rates:");
                for ( int j = 0; j < tableSize; j++ ) {
                    buffer.append(" " + rates[j]);
                }
                logger.debug(buffer.toString());
            }
            return rates;
        } catch (Exception e) {
            long[] partialRates;

            partialRates = new long[i];
            System.arraycopy(rates, 0, partialRates, 0, i);
            return partialRates;
        }

    }

    /**
     * Destroys the given NBAR Top-N table.
     *
     * @param tableIndex  index of NBAR Top-N table
     * @return            the result of the SNMP set command to destroy the table
     */
    public int setNBARTopNDestroy(int tableIndex) throws SNMPException {
        logger.debug("Destroying NBAR Top-N table " + tableIndex);
        return snmpAccess.setMIBValueInteger(SNMPAccess.OID_cnpdTopNConfigStatus + "." + tableIndex, SNMPAccess.ROWSTATUS_destroy);
    }


}
