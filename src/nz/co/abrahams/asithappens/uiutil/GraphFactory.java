/*
 * GraphFactory.java
 *
 * Created on 08 September 2012, 22:10
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
import javax.swing.JComboBox;
import nz.co.abrahams.asithappens.accounting.MACAccountingCollectorDefinition;
import nz.co.abrahams.asithappens.cartgraph.DataGraph;
import nz.co.abrahams.asithappens.cartgraph.TimeSeriesContext;
import nz.co.abrahams.asithappens.collectors.CollectorDefinition;
import nz.co.abrahams.asithappens.collectors.DataCollector;
import nz.co.abrahams.asithappens.collectors.InterfaceDirectionCollectorDefinition;
import nz.co.abrahams.asithappens.core.DBException;
import nz.co.abrahams.asithappens.snmputil.SNMPException;
import nz.co.abrahams.asithappens.storage.DataSets;
import nz.co.abrahams.asithappens.storage.Device;
import nz.co.abrahams.asithappens.storage.Direction;

/**
 *
 * @author mark
 */
public class GraphFactory {
    
    private GraphFactory() {
    }
    
    public static void create(CollectorDefinition definition) {
        DataCollector collector;
        DataSets data;
        TimeSeriesContext context;
        DataGraph graphFrame;
        
        try {
            collector = definition.spawnCollector();
            data = new DataSets(collector);
            context = new TimeSeriesContext(data);
            graphFrame = new DataGraph(context);
        } catch (DBException e) {
            ErrorHandler.modalError(null, "This is most likely a software bug - please report to the author",
                    "Error accessing database", e);
        } catch (UnknownHostException e) {
            ErrorHandler.modalError(null, "Please ensure that device name \"" + definition.getDevice().getName() + "\" is valid",
                    "Unknown host " + definition.getDevice().getName());
        } catch (SNMPException e) {
            ErrorHandler.modalError(null, "Please ensure that device name and community string are correct",
                    "Cannot access SNMP service on device " + definition.getDevice().getName(), e);
        }
    }
            
}
