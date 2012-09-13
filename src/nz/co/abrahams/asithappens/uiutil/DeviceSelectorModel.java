/*
 * Created on 10 August 2012, 00:22
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
package nz.co.abrahams.asithappens.uiutil;

import java.net.UnknownHostException;
import nz.co.abrahams.asithappens.core.DAOFactory;
import nz.co.abrahams.asithappens.core.DBException;
import nz.co.abrahams.asithappens.snmputil.SNMPAccessType;
import nz.co.abrahams.asithappens.snmputil.SNMPException;
import nz.co.abrahams.asithappens.storage.Device;
import nz.co.abrahams.asithappens.storage.DeviceDAO;

/**
 *
 * @author mark
 */
public class DeviceSelectorModel {

    public String deviceName;
    
    public SNMPAccessType snmpAccessType;

    public DeviceSelectorModel(String deviceName, SNMPAccessType snmpAccessType) {
        this.deviceName = deviceName;
        this.snmpAccessType = snmpAccessType;
    }

    public String getName() {
        return deviceName;
    }

    public boolean deviceExists() throws DBException {
        DeviceDAO deviceDAO;
        boolean exists;

        deviceDAO = DAOFactory.getDeviceDAO();
        exists = deviceDAO.retrieveDeviceExists(deviceName);
        deviceDAO.closeConnection();

        return exists;
    }

    public Device loadDevice() throws DBException, UnknownHostException {

        DeviceDAO deviceDAO;
        Device device;

        deviceDAO = DAOFactory.getDeviceDAO();
        if (deviceDAO.retrieveDeviceExists(deviceName) == false) {
            device = new Device(deviceName);
        } else {
            device = deviceDAO.retrieveDevice(deviceName, snmpAccessType);
        }
        deviceDAO.closeConnection();
        return device;
    }
}
