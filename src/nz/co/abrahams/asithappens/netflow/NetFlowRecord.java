/*
 * NetFlowRecord.java
 *
 * Created on 21 November 2005, 19:51
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

package nz.co.abrahams.asithappens.netflow;

import nz.co.abrahams.asithappens.flow.FlowOptions;
import nz.co.abrahams.asithappens.*;
import java.net.*;

/**
 * Stores information for a single NetFlow record.
 *
 * @author mark
 */
public class NetFlowRecord {
    
    public int srcAddressType;
    public InetAddress srcAddress;
    public int srcAddressMask;
    public int dstAddressType;
    public InetAddress dstAddress;
    public int dstAddressMask;
    public int nhAddressType;
    public InetAddress nhAddress;
    public int srcPort;
    public int dstPort;
    public int srcAS;
    public int dstAS;
    public int inputIfIndex;
    public int outputIfIndex;
    public long firstSwitched;
    public long lastSwitched;
    public int tos;
    public int protocol;
    public int tcpFlags;
    public int samplerID;
    public int classID;
    public int flags;
    public long bytes;
    public long packets;
    
    /** Creates a new NetFlowRecord. */
    public NetFlowRecord() {
        srcAddressType = 1;
        srcAddress = null;
        srcAddressMask = 0;
        nhAddressType = 1;
        nhAddress = null;
        srcPort = 0;
        dstPort = 0;
        srcAS = 0;
        dstAS = 0;
        inputIfIndex = 0;
        outputIfIndex = 0;
        firstSwitched = 0;
        lastSwitched = 0;
        tos = 0;
        protocol = 0;
        tcpFlags = 0;
        samplerID = 0;
        classID = 0;
        flags = 0;
        bytes = 0;
        packets = 0;
    }
    
    /**
     * Creates a new NetFlowRecord with given attributes.
     *
     * @param srcAddress  source IP address
     * @param dstAddress  destination IP address
     * @param srcPort     source TCP/UDP port
     * @param dstPort     destination TCP/UDP port
     * @param tos         ToS byte
     * @param protocol    IP protocol
     */
    public NetFlowRecord(InetAddress srcAddress, InetAddress dstAddress, int srcPort, int dstPort, int tos, int protocol) {
        this();
        this.srcAddress = srcAddress;
        this.dstAddress = dstAddress;
        this.srcPort = srcPort;
        this.dstPort = dstPort;
        this.tos = tos;
        this.protocol = protocol;
    }
    
    /**
     * Checks that the given record matches this record.
     *
     * @param record  the record to compare against thie record
     */
    public boolean matches(NetFlowRecord record) {
        return ( srcAddress.equals(record.srcAddress) && dstAddress.equals(record.dstAddress) &&
                srcPort == record.srcPort && dstPort == record.dstPort && tos == record.tos &&
                protocol == record.protocol);
    }
    
    /**
     * Checks that the given record matches this record with the given options mask.
     *
     * @param other    the record to compare against thie record
     * @param options  flow options mask
     */
    public boolean matches(NetFlowRecord other, FlowOptions options) {
        return (
                ( ! options.ipSourceAddress || srcAddress.equals(other.srcAddress) ) &&
                ( ! options.ipDestinationAddress || dstAddress.equals(other.dstAddress ) ) &&
                ( ! options.ipProtocol || protocol == other.protocol ) &&
                ( ! options.ipProtocol || (! options.tcpUdpSourcePort || srcPort == other.srcPort) ) &&
                ( ! options.ipProtocol || (! options.tcpUdpDestinationPort || dstPort == other.dstPort) )
                );
    }
    
    /** @return a textual representation of the flow record */
    public String printable() {
        return getProtocolName(protocol) + "[" + tos + "]: " + srcAddress.getHostAddress() + "(" + srcPort + ") -> " +
                dstAddress.getHostAddress() + "(" + dstPort + ")";
    }
    
    /**
     * Returns a textual representation of the flow record according to the given
     * mask.
     *
     * @param options  flow options mask
     * @return         a textual representation of the flow record
     */
    public String printable(FlowOptions options) {
        StringBuffer buffer;
        
        buffer = new StringBuffer();
        if ( options.ipProtocol )
            buffer.append(getProtocolName(protocol));
        else
            buffer.append("IP");
        if ( options.tosByte )
            buffer.append("[" + tos + "]");
        buffer.append(": ");
        if ( options.ipSourceAddress )
            buffer.append(srcAddress.getHostAddress());
        else
            buffer.append("any");
        if ( options.tcpUdpSourcePort && ( protocol == 6 || protocol == 17 ) )
            buffer.append("(" + srcPort + ")");
        buffer.append(" -> ");
        if ( options.ipDestinationAddress )
            buffer.append(dstAddress.getHostAddress());
        else
            buffer.append("any");
        if ( options.tcpUdpDestinationPort && ( protocol == 6 || protocol == 17 ) )
            buffer.append("(" + dstPort + ")");
        return buffer.toString();
    }
    
    /**
     * @param protocol  the IP protocol number
     * @return          the name corresponding to the IP protocol if recognised,
     *                  if not the same IP protocol number that was given
     */
    private String getProtocolName(int protocol) {
        switch (protocol) {
            case 1:
                return "ICMP";
            case 2:
                return "IGMP";
            case 6:
                return "TCP";
            case 17:
                return "UDP";
            case 50:
                return "ESP";
            default:
                return Integer.toString(protocol);
        }
    }
    /*
    public boolean matches(InetAddress srcAddress, InetAddress dstAddress, int srcPort, int dstPort, int tos, int protocol) {
     
    }
     */
}
