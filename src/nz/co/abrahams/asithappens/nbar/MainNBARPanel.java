/*
 * MainNBARPanel.java
 *
 * Created on 19 October 2005, 02:38
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

package nz.co.abrahams.asithappens.nbar;

import nz.co.abrahams.asithappens.core.DataType;
import nz.co.abrahams.asithappens.storage.DataSets;
import nz.co.abrahams.asithappens.storage.Device;
import nz.co.abrahams.asithappens.snmputil.SNMPException;
import nz.co.abrahams.asithappens.core.DBException;
import nz.co.abrahams.asithappens.core.Configuration;
import nz.co.abrahams.asithappens.cartgraph.DataGraph;
import nz.co.abrahams.asithappens.cartgraph.TimeSeriesContext;
import nz.co.abrahams.asithappens.uiutil.ErrorHandler;
import nz.co.abrahams.asithappens.uiutil.PortsSelectorPanel;
import java.net.*;
import javax.swing.*;
import org.apache.log4j.Logger;

/**
 * The graphical pane for creating NBAR graphs.  NBAR (Network-based application
 * recognition) is a Cisco-proprietary technology that is implemented on various
 * Cisco router platforms.
 * <p>
 * This class relies on the NBAR protocol discovery MIB being available on the target
 * device.  The device's ports must be enumerated using an SNMP write string,
 * which is used to set up the necessary NBAR Top-N table in the MIB.
 *
 * @author  mark
 */
public class MainNBARPanel extends javax.swing.JPanel {
    
    /** Direction */
    public static final String[] DIRECTIONS = { "In", "Out", "Both" };
    
    /** Logging provider */
    protected static Logger logger = Logger.getLogger(MainNBARPanel.class);
    
    /** Ports selector interface */
    private PortsSelectorPanel portsSelectorPanel;

    /** The device for which ports are enumerated */
    //private Device device;
    
    /** Creates new MainNBARPanel form */
    public MainNBARPanel() {
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
        directionLabel = new javax.swing.JLabel();
        directionCombo = new JComboBox(MainNBARPanel.DIRECTIONS);
        storeDataCheckBox = new javax.swing.JCheckBox();
        nbarButton = new javax.swing.JButton();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pollLabel.setText("Poll Interval");
        add(pollLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 10, 80, 20));

        pollField.setText("2");
        add(pollField, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 10, 60, -1));

        pollUnitsLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        pollUnitsLabel.setText("s");
        add(pollUnitsLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 10, 20, 20));

        directionLabel.setText("Direction");
        add(directionLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 40, 90, 24));
        add(directionCombo, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 40, 80, -1));

        storeDataCheckBox.setText("Store collected data");
        add(storeDataCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 290, 180, -1));

        nbarButton.setText("NBAR Graph");
        nbarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nbarButtonActionPerformed(evt);
            }
        });
        add(nbarButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 330, 620, 30));
    }// </editor-fold>//GEN-END:initComponents
    
    public void initComponentsFinish() {
        portsSelectorPanel = new PortsSelectorPanel(true, ListSelectionModel.SINGLE_SELECTION);
        add(portsSelectorPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));
    }
    
    /** Creates a new NBAR graph. */
    private void nbarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nbarButtonActionPerformed
        Device device;
        int ifIndex;
        String portString;
        NBARSNMP snmp;
        NBARCollector collector;
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
        
        device = portsSelectorPanel.getModel().getDevice();
        try {
            /*
            ifIndex = device.getPortsIndex()[((JList)(portsPane.getViewport().getView())).getSelectedIndex()];
            portString = (String)((JList)portsPane.getViewport().getView()).getSelectedValue();
             */
            
            ifIndex = portsSelectorPanel.getIfIndex();
            portString = portsSelectorPanel.getIfDescription();
            snmp = new NBARSNMP(device);
            collector = new NBARCollector(snmp, Integer.parseInt(pollField.getText()) * 1000, ifIndex, portString, directionCombo.getSelectedIndex() + 1, Configuration.getPropertyInt("collector.nbar.table.size"));
            data = new DataSets(DataType.NBAR, collector, device, Integer.parseInt(pollField.getText()) * 1000, portString, directionCombo.getSelectedIndex() + 1, null, storeDataCheckBox.isSelected());
            context = new TimeSeriesContext(data);
            graphFrame = new DataGraph(context);
        } catch (DBException e) {
            ErrorHandler.modalError(null, "Please ensure that database is running and accessible",
                    "Error opening database connection", e);
        } catch (UnknownHostException e) {
            ErrorHandler.modalError(null, "Please ensure that device name \"" + device.getName() + "\" is valid",
                    "Unknown host " + device.getName(), e);
        } catch (SNMPException e) {
            ErrorHandler.modalError(null, "Please ensure that device name and community string are correct",
                    "Cannot access SNMP service on device " + device.getName(), e);
        }
    }//GEN-LAST:event_nbarButtonActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox directionCombo;
    private javax.swing.JLabel directionLabel;
    private javax.swing.JButton nbarButton;
    private javax.swing.JTextField pollField;
    private javax.swing.JLabel pollLabel;
    private javax.swing.JLabel pollUnitsLabel;
    private javax.swing.JCheckBox storeDataCheckBox;
    // End of variables declaration//GEN-END:variables
    
}