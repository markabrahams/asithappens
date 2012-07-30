/*
 * NetFlowMatchCriteria.java
 *
 * Created on 24 November 2005, 03:37
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

import nz.co.abrahams.asithappens.snmputil.SNMPException;
import nz.co.abrahams.asithappens.core.InvalidInputException;
import nz.co.abrahams.asithappens.core.DBException;
import java.net.*;

/**
 * Contains criteria that packets must meet to be included in a flow stored on
 * the target device.
 *
 * @author mark
 */
public class NetFlowMatchCriteria {
    
    public enum NetFlowMatchCriteriaState { OK, SRCADDRESS_INVALID, DSTADDRESS_INVALID, NHADDRESS_INVALID }
    
    public NetFlowMatchCriteriaState state;
    
    public int srcAddressType;
    public InetAddress srcAddress;
    public int srcAddressMask;
    public int dstAddressType;
    public InetAddress dstAddress;
    public int dstAddressMask;
    public int srcPortLo;
    public int srcPortHi;
    public int dstPortLo;
    public int dstPortHi;
    public int srcAS;
    public int dstAS;
    public int tosByte;
    public int protocol;
    
    public int nhAddressType;
    public InetAddress nhAddress;
    public int nhAddressMask;
    public int inputIf;
    public int outputIf;
    public String sampler;
    public String classMap;
    public int minPackets;
    public int maxPackets;
    public int minBytes;
    public int maxBytes;
    
    /** Creates a new NetFlowMatchCriteria. */
    public NetFlowMatchCriteria() {
        setDefaults();
    }
    
    /** Sets all match criteria to default values. */
    public void setDefaults() {
        state = NetFlowMatchCriteriaState.OK;
        srcAddressType = 0;
        srcAddress = null;
        srcAddressMask = 0;
        dstAddressType = 0;
        dstAddress = null;
        dstAddressMask = 0;
        srcPortLo = -1;
        srcPortHi = -1;
        dstPortLo = -1;
        dstPortHi = -1;
        srcAS = -1;
        dstAS = -1;
        protocol = -1;
        tosByte = -1;
        
        nhAddressType = 0;
        nhAddress = null;
        nhAddressMask = 0;
        inputIf = 0;
        outputIf = 0;
        sampler = "";
        classMap = "";
        minPackets = 0;
        maxPackets = 0;
        minBytes = 0;
        maxBytes = 0;
    }
    
    /** @return Source Address Type */
    public int getSrcAddressType() {
        return srcAddressType;
    }
    
    /** @return source address as a string */
    public String getSrcAddress() {
        return srcAddress.getHostAddress();
    }
    
    /** @return source address as a byte array */
    public byte[] getSrcAddressBytes() {
        return srcAddress.getAddress();
    }
    
    /** @param text source IP address */
    public void setSrcAddress(String text) throws InvalidInputException {
        try {
            srcAddress = InetAddress.getByName(text);
        } catch (UnknownHostException e) {
            throw new InvalidInputException("Invalid source address", e);
        }
    }

    /** @return destination address as a string */
    public String getDstAddress() {
        return dstAddress.getHostAddress();
    }
    
    /** @return destination address as a byte array */
    public byte[] getDstAddressBytes() {
        return dstAddress.getAddress();
    }
    
    /** @param text destination IP address */
    public void setDstAddress(String text) throws InvalidInputException {
        try {
            dstAddress = InetAddress.getByName(text);
        } catch (UnknownHostException e) {
            throw new InvalidInputException("Invalid destination address", e);
        }
    }

    /** @return next-hop address as a string */
    public String getNhAddress() {
        return nhAddress.getHostAddress();
    }
    
    /** @return next-hop address as a byte array */
    public byte[] getNhAddressBytes() {
        return nhAddress.getAddress();
    }
    
    /** @param text next-hop IP address */
    public void setNhAddress(String text) throws InvalidInputException {
        try {
            nhAddress = InetAddress.getByName(text);
        } catch (UnknownHostException e) {
            throw new InvalidInputException("Invalid next-hop address", e);
        }
    }
    
    /**
     * Copies match criteria from another NetFlowMatchCriteria object.
     *
     * @param source  the NetFlowMatchCriteria object to copy from
     */
    public void copyCriteria(NetFlowMatchCriteria source) {
        srcAddressType = source.srcAddressType;
        srcAddress = source.srcAddress;
        srcAddressMask = source.srcAddressMask;
        dstAddressType = source.dstAddressType;
        dstAddress = source.dstAddress;
        dstAddressMask = source.dstAddressMask;
        srcPortLo = source.srcPortLo;
        srcPortHi = source.srcPortHi;
        dstPortLo = source.dstPortLo;
        dstPortHi = source.dstPortHi;
        srcAS = source.srcAS;
        dstAS = source.dstAS;
        protocol = source.protocol;
        tosByte = source.tosByte;
        
        nhAddressType = source.nhAddressType;
        nhAddress = source.nhAddress;
        nhAddressMask = source.nhAddressMask;
        inputIf = source.inputIf;
        outputIf = source.outputIf;
        sampler = source.sampler;
        classMap = source.classMap;
        minPackets = source.minPackets;
        maxPackets = source.maxPackets;
        minBytes = source.minBytes;
        maxBytes = source.maxBytes;
    }

    /*
    public byte[] getAddress(String address) {
    }
    
    public NetFlowMatchCriteriaState validate() {
        if ( ! validateAddress(srcAddress) )
            return NetFlowMatchCriteriaState.SRCADDRESS_INVALID;
        if ( ! validateAddress(dstAddress) )
            return NetFlowMatchCriteriaState.DSTADDRESS_INVALID;
        if ( ! validateAddress(nhAddress) )
            return NetFlowMatchCriteriaState.NHADDRESS_INVALID;
        return NetFlowMatchCriteriaState.OK;
        
    }
    
    private boolean validateAddress(String address) {
        try {
            InetAddress.getByName(srcAddress);
            return true;
        } catch ( UnknownHostException e ) {
            return false;
        }
        
    }
     */
}
