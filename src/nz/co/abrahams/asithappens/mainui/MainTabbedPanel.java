/*
 * MainTabbedPanel.java
 *
 * Created on 4 May 2005, 01:02
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

package nz.co.abrahams.asithappens.mainui;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import nz.co.abrahams.asithappens.bandwidth.MainBandwidthPanel;
import nz.co.abrahams.asithappens.capture.MainCapturePanel;
import nz.co.abrahams.asithappens.core.DBException;
import nz.co.abrahams.asithappens.host.MainHostPanel;
import nz.co.abrahams.asithappens.nbar.MainNBARPanel;
import nz.co.abrahams.asithappens.netflow.MainNetFlowPanel;
import nz.co.abrahams.asithappens.oid.MainOIDPanel;
import nz.co.abrahams.asithappens.response.MainResponsePanel;

/**
 * The main application JPanel containing the tabbed pane components.
 *
 * @author mark
 */
public class MainTabbedPanel extends JPanel {
    
    JTabbedPane pane;
    //MainDevicesPanel devicePanel;
    //MainBandwidthPanel bandwidthPanel;
    //MainResponsePanel responsePanel;
    //MainHostPanel hostPanel;
    //MainNBARPanel nbarPanel;
    //MainNetFlowPanel netflowPanel;
    //MainOIDPanel oidPanel;
    CollectorsTabbedPanel collectorsPanel;
    MainCapturePanel capturePanel;
    MainSessionsPanel databasePanel;
    MainLayoutPanel layoutPanel;
    MainAboutPanel aboutPanel;
    
    /** Creates a new MainTabbedPanel. */
    public MainTabbedPanel() throws DBException {
        super(new java.awt.GridLayout(1, 1));
        initComponents();
    }
    
    /** Lays out GUI components. */
    private void initComponents() throws DBException {
        pane = new JTabbedPane();
        //devicePanel = new MainDevicesPanel();
        //bandwidthPanel = new MainBandwidthPanel();
        //responsePanel = new MainResponsePanel();
        //hostPanel = new MainHostPanel();
        //nbarPanel = new MainNBARPanel();
        //netflowPanel = new MainNetFlowPanel();
        //oidPanel = new MainOIDPanel();
        collectorsPanel = new CollectorsTabbedPanel();
        capturePanel = new MainCapturePanel();
        databasePanel = new MainSessionsPanel();
        layoutPanel = new MainLayoutPanel();
        aboutPanel = new MainAboutPanel();
        
        pane.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                paneStateChangedPerformed(evt);
            }
        });
        
        //pane.addTab("Device", null, devicePanel, "Device list");
        //pane.addTab("Bandwidth", null, bandwidthPanel, "Bandwidth SNMP collector");
        //pane.addTab("Response", null, responsePanel, "Ping response collector");
        //pane.addTab("Host", null, hostPanel, "Host resource collector");
        //pane.addTab("NBAR", null, nbarPanel, "NBAR Top-N collector");
        //pane.addTab("NetFlow", null, netflowPanel, "NetFlow Top-N collector");
        //pane.addTab("OID", null, oidPanel, "Custom OID collector");
        pane.addTab("Collectors", null, collectorsPanel, "Collectors");
        pane.addTab("Capture", null, capturePanel, "Packet capture analysis");
        pane.addTab("Sessions", null, databasePanel, "Stored session retrieval");
        pane.addTab("Layout", null, layoutPanel, "Save or load layouts");
        pane.addTab("About", null, aboutPanel, "About AsItHappens");
        add(pane);
    }
    
    /** Refreshes sessions when database panel is selected. */
    public void paneStateChangedPerformed(javax.swing.event.ChangeEvent evt) {
        if ( pane.getTitleAt(pane.getSelectedIndex()).equals("Sessions") ) {
            databasePanel.displaySessions();
            /*
            catch (DatabaseException e) {
                ErrorHandler.modalError(this, "Have not been able to connect to database", "No database connection");
                // Throwing an (any) Exception (crudely) stops the actual switch to the database pane
                // Tried throwing a DatabaseException, but it didn't with the implicit class
                throw new NullPointerException();
            }
             */
        }
    }
}
