/*
 * FlowOptions.java
 *
 * Created on 15 May 2005, 21:17
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

import nz.co.abrahams.asithappens.core.DBException;

/**
 * Represents the state - either "significant" or not - of a small number of fields
 * when grouping flow records.  This is used to mask out flow record attributes
 * that are not considered significant.
 * <p>
 * The list of fields that may or may not be signficant are:
 * <ul>
 * <li>IP protocol
 * <li>IP source address
 * <li>IP destination address
 * <li>IP TOS byte
 * <li>TCP/UDP source port
 * <li>TCP/UDP destination port
 * </ul>
 * <p>
 * If the state for any one of these options is set to "true", and the value of
 * the field in two separate flow records is different, then the flow records
 * must be categorised into different groups.  If an option is set to "false",
 * then two separate flows with differing field values can belong to the same
 * group.
 *
 * @author mark
 */
public class FlowOptions {
    
    /** IP protocol categorization */
    public boolean ipProtocol;
    /** IP source address categorization */
    public boolean ipSourceAddress;
    /** IP destination address categorization */
    public boolean ipDestinationAddress;
    /** TOS byte categorisation */
    public boolean tosByte;
    /** TCP/UDP source port categorization */
    public boolean tcpUdpSourcePort;
    /** TCP/UDP destination port categorization */
    public boolean tcpUdpDestinationPort;
    
    /** A FlowOptions instance that mandates segregation for all defined option fields */
    public static final FlowOptions all = new FlowOptions(true, true, true, true, true, true);
    
    /*
    static {
        all = new FlowOptions(true, true, true, true, true, true);
    }
    */
    
    /**
     * Creates a new FlowOptions instance.
     *
     * @param ipProtocol             IP protocol significance flag
     * @param ipSourceAddress        IP source address significance flag
     * @param ipDestinationAddress   IP destination address significance flag
     * @param tosByte                IP ToS byte significance flag
     * @param tcpUdpSourcePort       TCP/UDP source port significance flag
     * @param tcpUdpDestinationPort  TCP/UDP destination port significance flag
     */
    public FlowOptions(boolean ipProtocol, boolean ipSourceAddress, boolean ipDestinationAddress, boolean tosByte, boolean tcpUdpSourcePort, boolean tcpUdpDestinationPort) {
        this.ipProtocol = ipProtocol;
        this.ipSourceAddress = ipSourceAddress;
        this.ipDestinationAddress = ipDestinationAddress;
        this.tosByte = tosByte;
        this.tcpUdpSourcePort = tcpUdpSourcePort;
        this.tcpUdpDestinationPort = tcpUdpDestinationPort;
    }

    public String toString() {
        return "IP protocol: " + ipProtocol +
                "\nIP source address: " + ipSourceAddress +
                "\nIP destination address: " + ipDestinationAddress + 
                "\nTOS byte: " + tosByte +
                "\nTCP/UDP source port: " + tcpUdpSourcePort +
                "\nTCP/UDP destination port: " + tcpUdpDestinationPort + "\n";
    }
}
