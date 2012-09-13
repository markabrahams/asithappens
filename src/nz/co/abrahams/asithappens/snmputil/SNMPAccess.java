/*
 * SNMPAccess.java
 *
 * Created on 19 January 2005, 21:31
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
package nz.co.abrahams.asithappens.snmputil;

import java.io.*;
import java.net.*;
import java.util.*;
import org.snmp4j.*;
import org.snmp4j.smi.*;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.transport.*;
import org.snmp4j.security.USM;
import org.apache.log4j.Logger;
import org.snmp4j.mp.MPv3;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.security.SecurityModels;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.TableEvent;
import org.snmp4j.util.TableUtils;

/**
 * SNMP interface.
 *
 * This uses SNMP4J as the underlying SNMP access method, although this is
 * hidden from calling clients.
 *
 * @author mark
 */
public class SNMPAccess {

    public static final long SIGNED_32BIT_MINIMUM = Integer.MIN_VALUE;
    public static final long SIGNED_32BIT_MAXIMUM = Integer.MAX_VALUE;
    public static final long UNSIGNED_32BIT_MAXIMUM = 4294967295L;
    public static final long SIGNED_64BIT_MINIMUM = Long.MIN_VALUE;
    public static final long SIGNED_64BIT_MAXIMUM = Long.MAX_VALUE;
    public static final int VERSION = 0;
    public static final int SNMP_UDP_PORT = 161;
    public static final int DEFAULT_INITIAL_TIMEOUT = 2000;
    public static final int DEFAULT_INITIAL_RETRIES = 2;
    public static final int DEFAULT_TIMEOUT = 1000;
    public static final int DEFAULT_RETRIES = 0;
    public static final String OID_SYSNAME = "1.3.6.1.2.1.1.5.0";
    public static final String OID_IFNUMBER = "1.3.6.1.2.1.2.1.0";
    public static final String OID_IFINDEX = "1.3.6.1.2.1.2.2.1.1";
    public static final String OID_IFDESCR = "1.3.6.1.2.1.2.2.1.2";
    public static final String OID_IFALIAS = "1.3.6.1.2.1.31.1.1.1.18";
    public static final String OID_IFINOCTETS = "1.3.6.1.2.1.2.2.1.10";
    public static final String OID_IFOUTOCTETS = "1.3.6.1.2.1.2.2.1.16";
    public static final String OID_IFHCINOCTETS = "1.3.6.1.2.1.31.1.1.1.6";
    public static final String OID_IFHCOUTOCTETS = "1.3.6.1.2.1.31.1.1.1.10";
    public static final int ROWSTATUS_active = 1;
    public static final int ROWSTATUS_notInService = 2;
    public static final int ROWSTATUS_notReady = 3;
    public static final int ROWSTATUS_createAndGo = 4;
    public static final int ROWSTATUS_createAndWait = 5;
    public static final int ROWSTATUS_destroy = 6;
    public static final String OID_cnpdAllStatsProtocolName = "1.3.6.1.4.1.9.9.244.1.2.1.1.2";
    public static final String OID_cpndAllStatsInBytes = "1.3.6.1.4.1.9.9.244.1.2.1.1.5";
    public static final String OID_cnpdAllStatsOutBytes = "1.3.6.1.4.1.9.9.244.1.2.1.1.6";
    public static final String OID_cnpdTopNConfigTable = "1.3.6.1.4.1.9.9.244.1.3";
    public static final String OID_cnpdTopNConfigIfIndex = "1.3.6.1.4.1.9.9.244.1.3.1.1.2";
    public static final String OID_cnpdTopNConfigStatsSelect = "1.3.6.1.4.1.9.9.244.1.3.1.1.3";
    public static final String OID_cnpdTopNConfigSampleTime = "1.3.6.1.4.1.9.9.244.1.3.1.1.4";
    public static final String OID_cnpdTopNConfigRequestedSize = "1.3.6.1.4.1.9.9.244.1.3.1.1.5";
    public static final String OID_cnpdTopNConfigStatus = "1.3.6.1.4.1.9.9.244.1.3.1.1.8";
    public static final String OID_cnpdTopNStatsProtocolName = "1.3.6.1.4.1.9.9.244.1.4.1.1.2";
    public static final String OID_cnpdTopNStatsRate = "1.3.6.1.4.1.9.9.244.1.4.1.1.3";
    public static final String OID_cnfCINetflowEnable = "1.3.6.1.4.1.9.9.387.1.1.1.1.1";
    public static final String OID_cnfTopFlowsTopN = "1.3.6.1.4.1.9.9.387.1.7.2";
    public static final String OID_cnfTopFlowsSortBy = "1.3.6.1.4.1.9.9.387.1.7.6";
    public static final String OID_cnfTopFlowsCacheTimeout = "1.3.6.1.4.1.9.9.387.1.7.7";
    public static final String OID_cnfTopFlowsSrcAddress = "1.3.6.1.4.1.9.9.387.1.7.8.1.3";
    public static final String OID_cnfTopFlowsDstAddress = "1.3.6.1.4.1.9.9.387.1.7.8.1.6";
    public static final String OID_cnfTopFlowsSrcPort = "1.3.6.1.4.1.9.9.387.1.7.8.1.10";
    public static final String OID_cnfTopFlowsDstPort = "1.3.6.1.4.1.9.9.387.1.7.8.1.11";
    public static final String OID_cnfTopFlowsTOS = "1.3.6.1.4.1.9.9.387.1.7.8.1.18";
    public static final String OID_cnfTopFlowsProtocol = "1.3.6.1.4.1.9.9.387.1.7.8.1.19";
    public static final String OID_cnfTopFlowsBytes = "1.3.6.1.4.1.9.9.387.1.7.8.1.24";
    public static final String OID_cnfTopFlowsMatchSrcAddressType = "1.3.6.1.4.1.9.9.387.1.7.9";
    public static final String OID_cnfTopFlowsMatchSrcAddress = "1.3.6.1.4.1.9.9.387.1.7.10";
    public static final String OID_cnfTopFlowsMatchSrcAddressMask = "1.3.6.1.4.1.9.9.387.1.7.11";
    public static final String OID_cnfTopFlowsMatchDstAddressType = "1.3.6.1.4.1.9.9.387.1.7.12";
    public static final String OID_cnfTopFlowsMatchDstAddress = "1.3.6.1.4.1.9.9.387.1.7.13";
    public static final String OID_cnfTopFlowsMatchDstAddressMask = "1.3.6.1.4.1.9.9.387.1.7.14";
    public static final String OID_cnfTopFlowsMatchNhAddressType = "1.3.6.1.4.1.9.9.387.1.7.15";
    public static final String OID_cnfTopFlowsMatchNhAddress = "1.3.6.1.4.1.9.9.387.1.7.16";
    public static final String OID_cnfTopFlowsMatchNhAddressMask = "1.3.6.1.4.1.9.9.387.1.7.17";
    public static final String OID_cnfTopFlowsMatchSrcPortLo = "1.3.6.1.4.1.9.9.387.1.7.18";
    public static final String OID_cnfTopFlowsMatchSrcPortHi = "1.3.6.1.4.1.9.9.387.1.7.19";
    public static final String OID_cnfTopFlowsMatchDstPortLo = "1.3.6.1.4.1.9.9.387.1.7.20";
    public static final String OID_cnfTopFlowsMatchDstPortHi = "1.3.6.1.4.1.9.9.387.1.7.21";
    public static final String OID_cnfTopFlowsMatchSrcAS = "1.3.6.1.4.1.9.9.387.1.7.22";
    public static final String OID_cnfTopFlowsMatchDstAS = "1.3.6.1.4.1.9.9.387.1.7.23";
    public static final String OID_cnfTopFlowsMatchInputIf = "1.3.6.1.4.1.9.9.387.1.7.24";
    public static final String OID_cnfTopFlowsMatchOutputIf = "1.3.6.1.4.1.9.9.387.1.7.25";
    public static final String OID_cnfTopFlowsMatchTOSByte = "1.3.6.1.4.1.9.9.387.1.7.26";
    public static final String OID_cnfTopFlowsMatchProtocol = "1.3.6.1.4.1.9.9.387.1.7.27";
    public static final String OID_cnfTopFlowsMatchSampler = "1.3.6.1.4.1.9.9.387.1.7.28";
    public static final String OID_cnfTopFlowsMatchClass = "1.3.6.1.4.1.9.9.387.1.7.29";
    public static final String OID_cnfTopFlowsMatchMinPackets = "1.3.6.1.4.1.9.9.387.1.7.30";
    public static final String OID_cnfTopFlowsMatchMaxPackets = "1.3.6.1.4.1.9.9.387.1.7.31";
    public static final String OID_cnfTopFlowsMatchMinBytes = "1.3.6.1.4.1.9.9.387.1.7.32";
    public static final String OID_cnfTopFlowsMatchMaxBytes = "1.3.6.1.4.1.9.9.387.1.7.33";
    public static final String OID_cnfTopFlowsMatchDirection = "1.3.6.1.4.1.9.9.387.1.7.34";
    public static final String OID_hrDeviceIndex = "1.3.6.1.2.1.25.3.2.1.1";
    public static final String OID_hrDeviceType = "1.3.6.1.2.1.25.3.2.1.2";
    public static final String OID_hrDeviceDescr = "1.3.6.1.2.1.25.3.2.1.3";
    public static final String OID_hrProcessorLoad = "1.3.6.1.2.1.25.3.3.1.2";
    public static final String OID_hrStorageDescr = "1.3.6.1.2.1.25.2.3.1.3";
    public static final String OID_hrStorageAllocationUnits = "1.3.6.1.2.1.25.2.3.1.4";
    public static final String OID_hrStorageUsed = "1.3.6.1.2.1.25.2.3.1.6";
    public static final String OID_ssCpuRawUser = "1.3.6.1.4.1.2021.11.50";
    public static final String OID_ssCpuRawNice = "1.3.6.1.4.1.2021.11.51";
    public static final String OID_ssCpuRawSystem = "1.3.6.1.4.1.2021.11.52";
    public static final String OID_ssCpuRawIdle = "1.3.6.1.4.1.2021.11.53";
    public static final String OID_ssCpuRawWait = "1.3.6.1.4.1.2021.11.54";
    public static final String OID_ssCpuRawKernel = "1.3.6.1.4.1.2021.11.55";
    public static final String OID_ssCpuRawInterrupt = "1.3.6.1.4.1.2021.11.56";
    public static final String OID_memTotalSwap = "1.3.6.1.4.1.2021.4.3";
    public static final String OID_memAvailSwap = "1.3.6.1.4.1.2021.4.4";
    public static final String OID_memTotalReal = "1.3.6.1.4.1.2021.4.5";
    public static final String OID_memAvailReal = "1.3.6.1.4.1.2021.4.6";
    public static final String OID_oldCiscoCPUBusyPer = "1.3.6.1.4.1.9.2.1.56";
    public static final String OID_ciscoMemoryPoolName = "1.3.6.1.4.1.9.9.48.1.1.1.2";
    public static final String OID_ciscoMemoryPoolUsed = "1.3.6.1.4.1.9.9.48.1.1.1.5";
    public static final int HR_DEVICETYPE_processor = 3;
    public static final int NETFLOW_TOPN_SORT_BY_BYTES = 3;
    /**
     * Logging provider
     */
    private static Logger logger = Logger.getLogger(SNMPAccess.class);
    /**
     * IP address of target device
     */
    protected InetAddress address;
    /**
     * community string for target device
     */
    protected String community;
    /**
     * USM user for target device
     */
    protected USMUser user;
    /**
     * SNMP version supported by agent
     */
    protected int agentVersion;
    // BEGIN SNMP4J library variables
    /**
     * SNMP4J interface
     */
    protected static Snmp snmp;
    /**
     * SNMP4J target
     */
    protected Target target;
    /**
     * SNMP4J SNMPv3 user model
     */
    protected USM usm;
    /**
     * SNMP4J transport mapping
     */
    protected static TransportMapping transport;
    // END SNMP4J library variables

    /**
     * Creates a new SNMPAccess interface.
     *
     * @param address IP address of target
     * @param community SNMP community string for target
     */
    public SNMPAccess(InetAddress address, String community) throws SNMPException {
        this(address, community, DEFAULT_TIMEOUT);
    }

    /**
     * Creates a new SNMPAccess interface.
     *
     * @param address IP address of target
     * @param community SNMP community string for target
     * @param timeout SNMP timeout setting
     */
    public SNMPAccess(InetAddress address, String community, int timeout) throws SNMPException {

        this.address = address;
        this.community = community;

        try {
            //transport = new DefaultUdpTransportMapping();
            //snmp = new Snmp(transport);
            //snmp.listen();
            initSNMP4J();
            target = new CommunityTarget();
            ((CommunityTarget) target).setCommunity(new OctetString(community));
            target.setAddress(new UdpAddress(address, SNMP_UDP_PORT));
            setReliableCollection();

            //target.setVersion(SnmpConstants.version2c);
            determineAgentVersion();
        } catch (SocketException e) {
            logger.warn("Problem binding to SNMP socket");
            throw new SNMPException("Problem binding to SNMP socket", e);
        } catch (IOException e) {
            logger.warn("Problem initializing SNMP interface");
            throw new SNMPException("Problem initializing SNMP interface", e);
        }
    }

    public SNMPAccess(InetAddress address, USMUser user, int timeout) throws SNMPException {
        this.address = address;
        this.user = user;

        try {
            //transport = new DefaultUdpTransportMapping();
            //snmp = new Snmp(transport);
            //snmp.listen();
            initSNMP4J();
            if (usm == null) {
                usm = new USM(SecurityProtocols.getInstance(),
                        new OctetString(MPv3.createLocalEngineID()), 0);
                SecurityModels.getInstance().addSecurityModel(usm);
            }
            snmp.getUSM().addUser(new OctetString(user.getUserName()),
                    new org.snmp4j.security.UsmUser(
                    new OctetString(user.getUserName()),
                    user.getUserAuthProtocol().getSnmp4jID(),
                    new OctetString(user.getUserAuthKey()),
                    user.getUserPrivProtocol().getSnmp4jID(),
                    new OctetString(user.getUserPrivKey())));

            target = new UserTarget();
            ((UserTarget) target).setSecurityLevel(user.getUserLevel().getSnmp4jID());
            ((UserTarget) target).setSecurityName(new OctetString(user.getUserName()));
            target.setAddress(new UdpAddress(address, SNMP_UDP_PORT));
            target.setVersion(SnmpConstants.version3);
            setReliableCollection();
            agentVersion = 3;
        } catch (SocketException e) {
            logger.warn("Problem binding to SNMP socket");
            throw new SNMPException("Problem binding to SNMP socket", e);
        } catch (IOException e) {
            logger.warn("Problem initializing SNMP interface");
            throw new SNMPException("Problem initializing SNMP interface", e);
        }
    }

    public SNMPAccess(InetAddress address, USMUser user) throws SNMPException {
        this(address, user, DEFAULT_TIMEOUT);
    }

    private void initSNMP4J() throws IOException {
        if (transport == null) {
            transport = new DefaultUdpTransportMapping();
        }
        if (snmp == null) {
            snmp = new Snmp(transport);
        }
        if (!transport.isListening()) {
            snmp.listen();
        }
    }

    /**
     * Favour reliable collection over expedient collection.
     */
    public void setReliableCollection() {
        target.setTimeout(DEFAULT_INITIAL_TIMEOUT);
        target.setRetries(DEFAULT_INITIAL_RETRIES);
    }

    /**
     * Favour expedient collection over reliable collection.
     */
    public void setExpedientCollection() {
        target.setTimeout(DEFAULT_TIMEOUT);
        target.setRetries(DEFAULT_RETRIES);
    }

    /**
     * Determine if agent can use SNMPv2c or if it is SNMPv1 only.
     */
    private void determineAgentVersion() {
        String dummy;
        String addressString;

        addressString = address.getHostAddress();
        target.setVersion(SnmpConstants.version2c);
        try {
            logger.debug("Trying to contact " + addressString + " using SNMPv2c");
            dummy = getMIBValueString(OID_SYSNAME);
            logger.debug("Contacted " + addressString + " using SNMPv2c");
            agentVersion = 2;
            return;
        } catch (SNMPException e) {
            logger.debug("Failed to contact " + addressString + " using SNMPv2c");
        }
        target.setVersion(SnmpConstants.version1);
        try {
            logger.debug("Trying to contact " + addressString + " using SNMPv1");
            dummy = getMIBValueString(OID_SYSNAME);
            logger.debug("Contacted " + addressString + " using SNMPv1");
            agentVersion = 1;
            return;
        } catch (SNMPException e) {
            logger.debug("Failed to contact " + addressString + " using SNMPv1");
        }
        agentVersion = 0;
    }

    private PDU getNewPDU() {
        if (agentVersion == 3) {
            return new ScopedPDU();
        } else {
            return new PDU();
        }
    }

    /**
     * @param OIDString OID of variable to retrieve
     * @return SNMP4J variable containing retrieved value
     */
    protected Variable getMIBValue(String OIDString, SNMPType valueType) throws SNMPException, SNMPTypeException {
        PDU pdu;
        ResponseEvent response;
        Variable var;

        try {
            //pdu = new PDU();
            //pdu = new ScopedPDU();
            pdu = getNewPDU();
            if (OIDString == null) {
                logger.debug("OID string not set for SNMP GET of " + address.getHostAddress());
                throw new SNMPTypeException("OID string not set for SNMP GET of " + address.getHostAddress());
            }
            pdu.add(new VariableBinding(new OID(OIDString)));
            logger.debug("SNMP GET of " + address.getHostAddress() + " :: " + OIDString);
            response = snmp.get(pdu, target);
            if (response.getError() != null) {
                logger.error("Error while issuing SNMP GET for "
                        + address.getHostAddress() + " :: " + OIDString);
                throw new SNMPException("Error while issuing SNMP GET for "
                        + address.getHostAddress() + " :: " + OIDString, response.getError());
            }
            if (response.getResponse() == null) {
                logger.warn("SNMP GET timeout for " + address.getHostAddress() + " :: " + OIDString);
                throw (new SNMPException("SNMP GET timeout for " + address.getHostAddress() + " :: " + OIDString));
            }
            var = response.getResponse().get(0).getVariable();
            logger.debug("SNMP GET result: " + address.getHostAddress() + " :: " + OIDString + " = " + var);
            if (var instanceof org.snmp4j.smi.Null) {
                logger.warn("OID not found in MIB for SNMP GET for " + address.getHostAddress() + " :: " + OIDString);
                throw new SNMPException("OID not found in MIB for SNMP GET for " + address.getHostAddress() + " :: " + OIDString);
            }
            if (!valueType.sameType(var)) {
                logger.error("Variable type returned (" + var.getSyntaxString() + ") does not match requested type (" + valueType + ") SNMP GET of " + address.getHostAddress() + " :: " + OIDString);
                throw new SNMPTypeException("Variable type returned (" + var.getSyntaxString() + ") does not match requested type (" + valueType + ") SNMP GET of " + address.getHostAddress() + " :: " + OIDString);
            }
            return var;
        } catch (IOException e) {
            logger.error("IO error while issuing SNMP GET for " + address.getHostAddress() + " :: " + OIDString);
            throw new SNMPException("IO error while issuing SNMP GET for "
                    + address.getHostAddress() + " :: " + OIDString, e);
        }
    }

    /**
     * @param OIDString OID of variable to retrieve
     * @return retrieved value
     */
    public int getMIBValueInteger(String OIDString) throws SNMPException, SNMPTypeException {
        return ((Integer32) getMIBValue(OIDString, SNMPType.Integer32)).getValue();
    }

    /**
     * @param OIDString OID of variable to retrieve
     * @return retrieved value
     */
    public long getMIBValueUnsignedInteger(String OIDString) throws SNMPException, SNMPTypeException {
        return ((UnsignedInteger32) getMIBValue(OIDString, SNMPType.Unsigned32)).getValue();
    }

    /**
     * @param OIDString OID of variable to retrieve
     * @return retrieved value
     */
    public long getMIBValueCounter32(String OIDString) throws SNMPException, SNMPTypeException {
        return ((Counter32) getMIBValue(OIDString, SNMPType.Counter32)).getValue();
    }

    /**
     * @param OIDString OID of variable to retrieve
     * @return retrieved value
     */
    public long getMIBValueCounter64(String OIDString) throws SNMPException, SNMPTypeException {
        return ((Counter64) getMIBValue(OIDString, SNMPType.Counter64)).getValue();
    }

    /**
     * @param OIDString OID of variable to retrieve
     * @return retrieved value
     */
    public long getMIBValueGauge32(String OIDString) throws SNMPException, SNMPTypeException {
        return ((Gauge32) getMIBValue(OIDString, SNMPType.Gauge32)).getValue();
    }

    /**
     * @param OIDString OID of variable to retrieve
     * @return retrieved value
     */
    public String getMIBValueString(String OIDString) throws SNMPException, SNMPTypeException {
        return ((OctetString) getMIBValue(OIDString, SNMPType.OctetString)).toString();
    }

    /**
     * @param OIDString OID of variable to retrieve
     * @return retrieved value
     */
    public String getMIBValueOID(String OIDString) throws SNMPException, SNMPTypeException {
        return ((OID) getMIBValue(OIDString, SNMPType.OID)).toString();
    }

    /**
     * @param OIDString OID of variable to retrieve
     * @return retrieved value
     */
    public InetAddress getMIBValueIpAddress(String OIDString) throws SNMPException, SNMPTypeException, UnknownHostException {
        IpAddress address;

        address = new IpAddress();
        address.setAddress(((OctetString) getMIBValue(OIDString, SNMPType.OctetString)).getValue());
        return address.getInetAddress();
    }

    public LinkedList getNextMIBValue(String OIDString, SNMPType valueType, String scope) throws SNMPException, SNMPScopeException {
        PDU pdu;
        ResponseEvent response;
        String nextOID;
        Variable nextVariable;
        Object nextValue;
        LinkedList returnPair;

        try {
            //pdu = new PDU();
            pdu = getNewPDU();
            pdu.add(new VariableBinding(new OID(OIDString)));
            logger.debug("SNMP GETNEXT of " + address.getHostAddress() + " :: " + OIDString);
            response = snmp.getNext(pdu, target);

            if (response.getError() != null) {
                logger.error("Error while issuing SNMP GETNEXT for "
                        + address.getHostAddress() + " :: " + OIDString);
                throw new SNMPException("Error while issuing SNMP GETNEXT for "
                        + address.getHostAddress() + " :: " + OIDString, response.getError());
            }
            if (response.getResponse() == null) {
                logger.warn("SNMP GET timeout for " + address.getHostAddress() + " :: " + OIDString);
                throw (new SNMPException("SNMP GETNEXT timeout for " + address.getHostAddress() + " :: " + OIDString));
            }

            returnPair = new LinkedList();
            nextOID = response.getResponse().get(0).getOid().toString();

            // Scope check
            if (scope != null && !nextOID.matches(scope + ".*")) {
                logger.debug("OID " + nextOID + " not under scope " + scope + " in MIB for SNMP GETNEXT on " + address.getHostAddress() + " :: " + OIDString);
                throw new SNMPScopeException("OID " + nextOID + " not under scope " + scope + " in MIB on SNMP GETNEXT for " + address.getHostAddress() + " :: " + OIDString);
            }

            returnPair.add(nextOID);
            nextVariable = response.getResponse().get(0).getVariable();

            if (nextVariable instanceof org.snmp4j.smi.Null) {
                logger.warn("OID not found in MIB for SNMP GETNEXT on " + address.getHostAddress() + " :: " + OIDString);
                throw new SNMPException("OID not found in MIB for SNMP GETNEXT on " + address.getHostAddress() + " :: " + OIDString);
            }
            if (!valueType.sameType(nextVariable)) {
                logger.error("Variable type returned (" + nextVariable.getSyntaxString() + ") does not match requested type (" + valueType + ") SNMP GET on " + address.getHostAddress() + " :: " + OIDString);
                //throw new SNMPTypeException("Variable type returned (" + nextVariable.getSyntaxString() + ") does not match requested type (" + valueType + ") SNMP GET of " + address.getHostAddress() + " :: " + OIDString);
                throw new SNMPException("Variable type returned (" + nextVariable.getSyntaxString() + ") does not match requested type (" + valueType + ") SNMP GET on " + address.getHostAddress() + " :: " + OIDString);
            }

            nextValue = convertType(nextVariable);
            returnPair.add(nextValue);
            logger.debug("SNMP GETNEXT result: " + address.getHostAddress() + " :: " + nextOID + " = " + nextValue);
            return returnPair;
        } catch (IOException e) {
            logger.error("IO error while issuing SNMP GETNEXT for " + address.getHostAddress() + " :: " + OIDString);
            throw new SNMPException("IO error while issuing SNMP GETNEXT for "
                    + address.getHostAddress() + " :: " + OIDString);
        }
    }

    /**
     * @param OIDString OID of variable to retrieve
     * @return SNMP4J variable containing retrieved value
     */
    public LinkedList getNextMIBValue(String OIDString, SNMPType valueType) throws SNMPException {
        try {
            return getNextMIBValue(OIDString, valueType, null);
        } catch (SNMPScopeException e) {
            logger.error("Application error - SNMP scope exception where no scope was specified");
            return null;
        }
    }

    public static Object convertType(Variable variable) {
        if (variable instanceof Integer32) {
            return new Integer(((Integer32) variable).getValue());
        } else if (variable instanceof OID) {
            return ((OID) variable).toString();
        } else if (variable instanceof OctetString) {
            return variable.toString();
        } else {
            return variable;
        }
    }

    /**
     * Returns the next OID and value after the given OID using the SNMP GETNEXT
     * method.
     *
     * @param OIDString current OID
     * @return pair containing next OID and corresponding value
     */
    /*
     * public LinkedList getNextMIBValueInteger(String OIDString) throws
     * SNMPException { PDU pdu; ResponseEvent response; String nextOID; int
     * nextValue; LinkedList returnPair;
     *
     * try { pdu = new PDU(); pdu.add(new VariableBinding(new OID(OIDString)));
     * logger.debug("SNMP GETNEXT of " + address.getHostAddress() + " :: " +
     * OIDString); response = snmp.getNext(pdu, target);
     *
     * if ( response.getError() != null ) { logger.error("Error while issuing
     * SNMP GETNEXT for " + address.getHostAddress() + " :: " + OIDString);
     * throw new SNMPException("Error while issuing SNMP GETNEXT for " +
     * address.getHostAddress() + " :: " + OIDString, response.getError()); } if
     * ( response.getResponse() == null ) { logger.warn("SNMP GET timeout for "
     * + address.getHostAddress() + " :: " + OIDString); throw (new
     * SNMPException("SNMP GETNEXT timeout for " + address.getHostAddress() + "
     * :: " + OIDString)); }
     *
     * returnPair = new LinkedList(); nextOID =
     * response.getResponse().get(0).getOid().toString();
     * returnPair.add(nextOID); nextValue = new
     * Integer(((Integer32)response.getResponse().get(0).getVariable()).getValue());
     * returnPair.add(nextValue);
     * //returnPair.add(response.getResponse().get(0).getOid().toString());
     * //returnPair.add(new
     * Integer(((Integer32)response.getResponse().get(0).getVariable()).getValue()));
     * logger.debug("SNMP GETNEXT result: " + address.getHostAddress() + " :: "
     * + nextOID + " = " + nextValue); return returnPair; } catch (IOException
     * e) { logger.error("IO error while issuing SNMP GETNEXT for " +
     * address.getHostAddress() + " :: " + OIDString); throw new
     * SNMPException("IO error while issuing SNMP GETNEXT for " +
     * address.getHostAddress() + " :: " + OIDString); } }
     */
    /**
     * Sets the given OID in the MIB to the given value.
     *
     * @param OIDString OID of variable to set
     * @param value SNMP4J variable containing the value to set
     * @return value contained in the response PDU
     */
    public Variable setMIBValue(String OIDString, Variable value) throws SNMPException {
        PDU pdu;
        ResponseEvent response;
        Variable returnValue;

        try {
            //pdu = new PDU();
            pdu = getNewPDU();
            pdu.add(new VariableBinding(new OID(OIDString), value));
            logger.debug("SNMP SET of " + address.getHostAddress() + " :: " + OIDString + " to " + value);
            response = snmp.set(pdu, target);

            if (response.getError() != null) {
                logger.error("Error while issuing SNMP SET for "
                        + address.getHostAddress() + " :: " + OIDString);
                throw new SNMPException("Error while issuing SNMP GETNEXT for "
                        + address.getHostAddress() + " :: " + OIDString, response.getError());
            }
            if (response.getResponse() == null) {
                logger.warn("SNMP SET timeout for " + address.getHostAddress() + " :: " + OIDString);
                throw (new SNMPException("SNMP GET timeout for " + address.getHostAddress() + " :: " + OIDString));
            }

            returnValue = response.getResponse().get(0).getVariable();
            logger.debug("SNMP SET result: " + address.getHostAddress() + " :: " + OIDString + " = " + returnValue);
            return returnValue;
        } catch (IOException e) {
            logger.error("IO error while issuing SNMP GET for " + address.getHostAddress() + " :: " + OIDString);
            throw new SNMPException("IO error while issuing SNMP GET for "
                    + address.getHostAddress() + " :: " + OIDString, e);
        }
    }

    /**
     * @param OIDString OID of variable to set
     * @param value variable containing the value to set
     */
    public int setMIBValueInteger(String OIDString, int value) throws SNMPException {
        return ((Integer32) setMIBValue(OIDString, new Integer32(value))).getValue();
    }

    /**
     * @param OIDString OID of variable to set
     * @param value variable containing the value to set
     */
    public long setMIBValueGauge32(String OIDString, long value) throws SNMPException {
        return ((Gauge32) setMIBValue(OIDString, new Gauge32(value))).getValue();
    }

    /**
     * @param OIDString OID of variable to set
     * @param value variable containing the value to set
     */
    public byte[] setMIBValueHexString(String OIDString, byte[] value) throws SNMPException {
        return ((OctetString) setMIBValue(OIDString, new OctetString(value))).getValue();
    }

    /**
     * @param OIDString OID of variable to set
     * @param value variable containing the value to set
     */
    public String setMIBValueOctetString(String OIDString, String value) throws SNMPException {
        return ((OctetString) setMIBValue(OIDString, new OctetString(value))).toString();
    }

    /**
     * Retrieves a table via SNMP.
     *
     * @return the SNMP table
     */
    public List<SNMPTableRow> getTable(String columns[]) throws SNMPException, UnknownHostException {
        TableUtils tableUtils;
        List<TableEvent> events;
        Iterator<TableEvent> eventIterator;
        List<SNMPTableRow> rows;

        tableUtils = new TableUtils(snmp, new DefaultPDUFactory(PDU.GETBULK));
        events = tableUtils.getTable(target, convertOIDs(columns), null, null);
        eventIterator = events.iterator();
        rows = new LinkedList<SNMPTableRow>();

        while (eventIterator.hasNext()) {
            rows.add(new SNMPTableRow(eventIterator.next()));
        }
        return rows;
    }

    protected static OID[] convertOIDs(String[] strings) {
        OID[] oids;

        oids = new OID[strings.length];
        for (int i = 0; i < strings.length; i++) {
            oids[i] = new OID(strings[i]);
        }
        return oids;
    }
}