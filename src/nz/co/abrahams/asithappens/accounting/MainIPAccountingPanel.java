/*
 * MainIPAccountingPanel.java
 *
 * Created on 14 August 2012, 21:50
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
package nz.co.abrahams.asithappens.accounting;

import nz.co.abrahams.asithappens.bandwidth.*;
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
import nz.co.abrahams.asithappens.collectors.SNMPTableCollector;
import nz.co.abrahams.asithappens.collectors.SNMPTableInterface;
import nz.co.abrahams.asithappens.snmputil.SNMPType;
import nz.co.abrahams.asithappens.storage.Direction;
import nz.co.abrahams.asithappens.uiutil.DeviceSelectorPanel;
import org.apache.log4j.Logger;

/**
 * The graphical pane for creating Cisco accounting graphs.
 *
 * @author mark
 */
public class MainIPAccountingPanel extends javax.swing.JPanel {

    /**
     * Ports selector interface
     */
    private DeviceSelectorPanel deviceSelectorPanel;

    /**
     * Creates new form MainBandwidthPanel
     */
    public MainIPAccountingPanel() {
        initComponents();
        initComponentsFinish();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        databaseButtonGroup = new javax.swing.ButtonGroup();
        pollLabel = new javax.swing.JLabel();
        pollField = new javax.swing.JTextField();
        pollUnitsLabel = new javax.swing.JLabel();
        storeDataCheckBox = new javax.swing.JCheckBox();
        accountingButton = new javax.swing.JButton();
        databaseLabel = new javax.swing.JLabel();
        activeButton = new javax.swing.JRadioButton();
        checkpointButton = new javax.swing.JRadioButton();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pollLabel.setText("Poll Interval");
        add(pollLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 10, 90, 20));

        pollField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        pollField.setText("2000");
        add(pollField, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 10, 70, -1));

        pollUnitsLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        pollUnitsLabel.setText("ms");
        add(pollUnitsLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 10, 20, 20));

        storeDataCheckBox.setText("Store collected data");
        add(storeDataCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 60, 180, -1));

        accountingButton.setText("IP Accounting Graph");
        accountingButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                accountingButtonActionPerformed(evt);
            }
        });
        add(accountingButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 410, 30));

        databaseLabel.setText("IP Accounting database to poll");
        add(databaseLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, -1, -1));

        databaseButtonGroup.add(activeButton);
        activeButton.setText("Active database (read-only, less efficient)");
        activeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                activeButtonActionPerformed(evt);
            }
        });
        add(activeButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 90, 380, -1));

        databaseButtonGroup.add(checkpointButton);
        checkpointButton.setText("Checkpoint database (read-write, more efficient)");
        checkpointButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkpointButtonActionPerformed(evt);
            }
        });
        add(checkpointButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 120, 380, -1));
        checkpointButton.setSelected(true);
    }// </editor-fold>//GEN-END:initComponents

    private void initComponentsFinish() {
        deviceSelectorPanel = new DeviceSelectorPanel(true);
        add(deviceSelectorPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 410, -1));
        
    }

    /**
     * Creates a new IP Accounting graph.
     */
    private void accountingButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_accountingButtonActionPerformed
        Device device;
        IPAccountingCheckpointSNMP checkpointSNMP;
        IPAccountingActiveSNMP activeSNMP;
        IPAccountingCheckpointCollector checkpointCollector;
        SNMPTableCollector activeCollector;
        DataSets data;
        TimeSeriesContext context;
        DataGraph graphFrame;

        try {
            if (activeButton.isSelected()) {
                deviceSelectorPanel.setUseWriteAuth(false);
                device = deviceSelectorPanel.getModel().loadDevice();
                activeSNMP = new IPAccountingActiveSNMP(device);
                activeCollector = new SNMPTableCollector(DataType.ACCOUNTING, SNMPType.Integer32,
                        activeSNMP, Integer.parseInt(pollField.getText()));
                data = new DataSets(DataType.ACCOUNTING, activeCollector, device, Integer.parseInt(pollField.getText()), "", DataSets.DIRECTION_BOTH, null, storeDataCheckBox.isSelected());
            } else {
                deviceSelectorPanel.setUseWriteAuth(true);
                device = deviceSelectorPanel.getModel().loadDevice();
                checkpointSNMP = new IPAccountingCheckpointSNMP(device);
                checkpointCollector = new IPAccountingCheckpointCollector(checkpointSNMP, Integer.parseInt(pollField.getText()));
                data = new DataSets(DataType.ACCOUNTING, checkpointCollector, device, Integer.parseInt(pollField.getText()), "", DataSets.DIRECTION_BOTH, null, storeDataCheckBox.isSelected());
            }
            context = new TimeSeriesContext(data);
            graphFrame = new DataGraph(context);
        } catch (DBException e) {
            ErrorHandler.modalError(null, "Please ensure that database is running and accessible",
                    "Error opening database connection", e);
        } catch (UnknownHostException e) {
            ErrorHandler.modalError(null, "Please ensure that device name \"" + deviceSelectorPanel.getModel().getName() + "\" is valid",
                    "Unknown host " + deviceSelectorPanel.getModel().getName());
        } catch (SNMPException e) {
            ErrorHandler.modalError(null, "Please ensure that device name and community string are correct",
                    "Cannot access SNMP service on device " + deviceSelectorPanel.getModel().getName(), e);
        }

    }//GEN-LAST:event_accountingButtonActionPerformed

    private void activeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_activeButtonActionPerformed
        deviceSelectorPanel.setUseWriteAuth(false);
    }//GEN-LAST:event_activeButtonActionPerformed

    private void checkpointButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkpointButtonActionPerformed
        deviceSelectorPanel.setUseWriteAuth(true);
    }//GEN-LAST:event_checkpointButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton accountingButton;
    private javax.swing.JRadioButton activeButton;
    private javax.swing.JRadioButton checkpointButton;
    private javax.swing.ButtonGroup databaseButtonGroup;
    private javax.swing.JLabel databaseLabel;
    private javax.swing.JTextField pollField;
    private javax.swing.JLabel pollLabel;
    private javax.swing.JLabel pollUnitsLabel;
    private javax.swing.JCheckBox storeDataCheckBox;
    // End of variables declaration//GEN-END:variables
}
