/*
 * SNMPInterface.java
 *
 * Created on 06 January 2010, 23:48
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

import nz.co.abrahams.asithappens.storage.Device;
import java.net.UnknownHostException;

/**
 *
 * @author mark
 */
public abstract class SNMPInterface {

    /** SNMP device */
    protected Device device;

    /** Underlying SNMP access class */
    protected SNMPAccess snmpAccess;

    public SNMPInterface(Device device) throws UnknownHostException, SNMPException {
        snmpAccess = null;
        this.device = device;
    }

    public Device getDevice() {
        return device;
    }

    /** Prefer expedient SNMP collection with no retries on failure */
    public void setExpedientCollection() {
        if ( snmpAccess != null)
            snmpAccess.setExpedientCollection();
    }

    /** Prefer reliable SNMP collection with retries on failure */
    public void setReliableCollection() {
        if ( snmpAccess != null)
            snmpAccess.setReliableCollection();
    }

}
