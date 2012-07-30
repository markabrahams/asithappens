/*
 * Flow.java
 *
 * Created on 1 May 2005, 21:35
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

package nz.co.abrahams.asithappens.flow;

import nz.co.abrahams.asithappens.storage.Device;
import nz.co.abrahams.asithappens.*;
import java.net.*;
import java.sql.Timestamp;
import org.apache.log4j.Logger;

/**
 * Represents a captured flow, which corresponds to a captured packet or set of
 * packets sharing some similar characteristics.
 *
 * @author mark
 */
public class Flow {
    
    Logger logger;
    public Device sourceDevice;
    public Device destinationDevice;
    public int ethertype;
    public int protocol;
    public int sourcePort;
    public int destinationPort;
    public long timestamp;
    public int length;
    
    /**
     * Creates a new flow record for an IP-based flow.
     *
     * @param timestamp         the time at which the flow was captured
     * @param sourceDevice      the sending device
     * @param destinationDevice the receiving device
     * @param ethertype         the "ethertype" field in the the packet
     * @param protocol          the IP protocol
     * @param sourcePort        the source UDP/TCP port
     * @param destinationPort   the destination UDP/TCP port
     * @param length            the length of the packet
     */
    public Flow(long timestamp, Device sourceDevice, Device destinationDevice,
            int ethertype, int protocol, int sourcePort, int destinationPort, int length) {
        logger = Logger.getLogger(this.getClass().getName());
        this.timestamp = timestamp;
        this.sourceDevice = sourceDevice;
        this.destinationDevice = destinationDevice;
        this.ethertype = ethertype;
        this.protocol = protocol;
        this.sourcePort = sourcePort;
        this.destinationPort = destinationPort;
        this.length = length;
    }
    
    /**
     * Creates a new flow record for non-IP flows.
     *
     * @param timestamp         the time at which the flow was captured
     * @param sourceDevice      the sending device
     * @param destinationDevice the receiving device
     * @param ethertype         the "ethertype" field in the the packet
     * @param length            the length of the packet
     */
    public Flow(long timestamp, Device sourceDevice, Device destinationDevice, int ethertype, int length) {
        this(timestamp, sourceDevice, destinationDevice, ethertype, -1, -1, -1, length);
    }
    
    /**
     * Compares this flow against another flow.
     *
     * @param other  flow to compare against
     * @return       true if this flow matches the given flow
     */
    public boolean matches(Flow other) {
        try {
            return ( sourceDevice.getResolvedAddress().equals(other.sourceDevice.getResolvedAddress()) &&
                    destinationDevice.getResolvedAddress().equals(other.destinationDevice.getResolvedAddress()) &&
                    sourceDevice.getEthernetAddress().equals(other.sourceDevice.getEthernetAddress()) &&
                    destinationDevice.getEthernetAddress().equals(other.destinationDevice.getEthernetAddress()) &&
                    ethertype == other.ethertype && protocol == other.protocol &&
                    sourcePort == other.sourcePort && destinationPort == other.destinationPort );
        } catch (UnknownHostException e) {
            return false;
        }
    }
    
    /**
     * Compares this flow against another flow using a set of mask options.
     *
     * @param other  flow to compare against
     * @return       true if this flow matches the given flow
     */
    public boolean matches(Flow other, FlowOptions options) {
        try {
            return ( ( ethertype == other.ethertype ) &&
                    (! options.ipSourceAddress || sourceDevice.getResolvedAddress().equals(other.sourceDevice.getResolvedAddress())) &&
                    (! options.ipDestinationAddress || destinationDevice.getResolvedAddress().equals(other.destinationDevice.getResolvedAddress())) &&
                    (! options.ipProtocol || protocol == other.protocol) &&
                    (! options.ipProtocol || (! options.tcpUdpSourcePort || sourcePort == other.sourcePort)) &&
                    (! options.ipProtocol || (! options.tcpUdpDestinationPort || destinationPort == other.destinationPort)) );
        } catch (UnknownHostException e) {
            return false;
        }
    }
    
    /**
     * Returns a text description of the flow record with the given options mask
     *
     * @param options  flow mask options
     * @return         a printable representation of the flow record
     */
    public String printable(FlowOptions options) {
        String sourceDeviceString;
        String destinationDeviceString;
        String sourcePortString;
        String destinationPortString;
        
        try {
            if ( sourcePort == -1 || ! options.tcpUdpSourcePort )
                sourcePortString = "any";
            else
                sourcePortString = Integer.toString(sourcePort);
            
            if ( destinationPort == -1 || ! options.tcpUdpDestinationPort )
                destinationPortString = "any";
            else
                destinationPortString = Integer.toString(destinationPort);
            
            if ( ethertype == 0x0800 ) {
                if ( ! options.ipSourceAddress )
                    sourceDeviceString = "any";
                else
                    sourceDeviceString = sourceDevice.getResolvedAddress().getHostAddress();
                
                if ( ! options.ipDestinationAddress )
                    destinationDeviceString = "any";
                else
                    destinationDeviceString = destinationDevice.getResolvedAddress().getHostAddress();
                
                if ( ( options.tcpUdpSourcePort || options.tcpUdpDestinationPort )
                          && ( protocol == 6 || protocol == 17 ) ) {
                    if ( sourcePort == -1 || ! options.tcpUdpSourcePort )
                        sourcePortString = "any";
                    else
                        sourcePortString = Integer.toString(sourcePort);
                    
                    if ( destinationPort == -1 || ! options.tcpUdpDestinationPort )
                        destinationPortString = "any";
                    else
                        destinationPortString = Integer.toString(destinationPort);
                    
                    if ( protocol == 6 )
                        return "TCP: " + sourceDeviceString + "(" + sourcePortString +
                                ") -> " + destinationDeviceString + "(" + destinationPortString + ")";
                    else if ( protocol == 17 )
                        return "UDP: " + sourceDeviceString + "(" + sourcePortString +
                                ") -> " + destinationDeviceString + "(" + destinationPortString + ")";
                } else {
                    if ( options.ipProtocol )
                        return "IP(" + protocol + "): " + sourceDeviceString + " -> " + destinationDeviceString;
                    else
                        return "IP: " + sourceDeviceString + " -> " + destinationDeviceString;
                }
            } else {
                return "Ethernet(0x" + Integer.toHexString(ethertype) + "): " + sourceDevice.getEthernetAddress() + " -> " +
                        destinationDevice.getEthernetAddress();
            }
        } catch ( UnknownHostException e ) {
            logger.error("UnknownHostException");
        }
        return null;
    }
    
    /**
     * Returns a text description of the flow record
     *
     * @return         a printable representation of the flow record
     */
    public String printable() {
        return printable(FlowOptions.all);
    }
    
    public String toString() {
        try {
            if ( sourceDevice.getResolvedAddress() == null || destinationDevice.getResolvedAddress() == null )
                return "src=" + destinationDevice.getResolvedAddress() + " dst=" + destinationDevice.getEthernetAddress() +
                        " ethertype=" + ethertype;
            else
                return "src=" + sourceDevice.getResolvedAddress() + " dst=" + destinationDevice.getResolvedAddress() +
                        " protocol=" + protocol + " srcPort=" + sourcePort + " dstPort=" + destinationPort;
            
        } catch (UnknownHostException e) {
            logger.debug("Unexpected UnknownHostException");
            e.printStackTrace();
            return null;
        }
    }
}
