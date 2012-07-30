/*
 * MainBandwidthPanel.java
 *
 * Created on 4 May 2005, 21:31
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

package nz.co.abrahams.asithappens.bandwidth;

import nz.co.abrahams.asithappens.core.DataType;
import nz.co.abrahams.asithappens.storage.DataSets;
import nz.co.abrahams.asithappens.storage.Device;
import nz.co.abrahams.asithappens.snmputil.SNMPException;
import nz.co.abrahams.asithappens.core.DBException;
import nz.co.abrahams.asithappens.cartgraph.TimeSeriesContext;
import nz.co.abrahams.asithappens.uiutil.ErrorHandler;
import nz.co.abrahams.asithappens.uiutil.PortsSelectorPanel;
import nz.co.abrahams.asithappens.cartgraph.DataGraph;
import java.net.*;
import javax.swing.*;
import org.apache.log4j.Logger;

/**
 * The graphical pane for creating bandwidth graphs.
 *
 * @author  mark
 */
public class MainBandwidthPanel extends javax.swing.JPanel {
    
    /** Ports selector interface */
    private PortsSelectorPanel portsSelectorPanel;

    /** Creates new form MainBandwidthPanel */
    public MainBandwidthPanel() {
        initComponents();
        initComponentsFinish();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pollLabel = new javax.swing.JLabel();
        pollField = new javax.swing.JTextField();
        pollUnitsLabel = new javax.swing.JLabel();
        prefer64BitCheckBox = new javax.swing.JCheckBox();
        storeDataCheckBox = new javax.swing.JCheckBox();
        bandwidthButton = new javax.swing.JButton();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pollLabel.setText("Poll Interval");
        add(pollLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 10, 80, 20));

        pollField.setText("2000");
        add(pollField, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 10, 80, -1));

        pollUnitsLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        pollUnitsLabel.setText("ms");
        add(pollUnitsLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 10, 20, 20));

        prefer64BitCheckBox.setText("Prefer 64-bit counters");
        add(prefer64BitCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 40, -1, -1));

        storeDataCheckBox.setText("Store collected data");
        add(storeDataCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 290, 180, -1));

        bandwidthButton.setText("Bandwidth Graph");
        bandwidthButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bandwidthButtonActionPerformed(evt);
            }
        });
        add(bandwidthButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 330, 620, 30));
    }// </editor-fold>//GEN-END:initComponents
    
    public void initComponentsFinish() {
        portsSelectorPanel = new PortsSelectorPanel(false, ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        add(portsSelectorPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));
    }
    
    /** Creates a new bandwidth graph. */
    private void bandwidthButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bandwidthButtonActionPerformed
        Device device;
        int[] ifIndices;
        String[] ifDescriptions;
        String portString;
        BandwidthSNMP snmp;
        BandwidthCollector collector;
        DataSets data;
        TimeSeriesContext context;
        DataGraph graphFrame;
        
        if ( ! portsSelectorPanel.getModel().hasEnumerated() ) {
            ErrorHandler.modalError(this, "Please enumerate ports on a device and select a port",
                    "No port selected");
            return;
        } else if ( portsSelectorPanel.rowsSelected().length == 0 ) {
            ErrorHandler.modalError(this, "Please select a port", "No port selected");
            return;
        }
        
        try {
            device = portsSelectorPanel.getModel().getDevice();
            ifIndices = portsSelectorPanel.getIfIndices();
            ifDescriptions = portsSelectorPanel.getIfDescriptions();
            portString = portsSelectorPanel.getPortsString();

            snmp = new BandwidthSNMP(device);
            collector = new BandwidthCollector(snmp, Integer.parseInt(pollField.getText()), ifIndices, ifDescriptions, prefer64BitCheckBox.isSelected());
            data = new DataSets(DataType.BANDWIDTH, collector, device, Integer.parseInt(pollField.getText()), portString, DataSets.DIRECTION_BOTH, null, storeDataCheckBox.isSelected());
            context = new TimeSeriesContext(data);
            graphFrame = new DataGraph(context);
        } catch (DBException e) {
            ErrorHandler.modalError(null, "Please ensure that database is running and accessible",
                    "Error opening database connection", e);
        } catch (UnknownHostException e) {
            ErrorHandler.modalError(null, "Please ensure that device name \"" + portsSelectorPanel.getModel().getDevice().getName() + "\" is valid",
                    "Unknown host " + portsSelectorPanel.getModel().getDevice().getName());
        } catch (SNMPException e) {
            ErrorHandler.modalError(null, "Please ensure that device name and community string are correct",
                    "Cannot access SNMP service on device " + portsSelectorPanel.getModel().getDevice().getName(), e);
        }
        
    }//GEN-LAST:event_bandwidthButtonActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bandwidthButton;
    private javax.swing.JTextField pollField;
    private javax.swing.JLabel pollLabel;
    private javax.swing.JLabel pollUnitsLabel;
    private javax.swing.JCheckBox prefer64BitCheckBox;
    private javax.swing.JCheckBox storeDataCheckBox;
    // End of variables declaration//GEN-END:variables
    
}