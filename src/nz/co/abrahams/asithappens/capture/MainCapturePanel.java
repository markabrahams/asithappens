/*
 * MainCapturePanel.java
 *
 * Created on 4 May 2005, 02:18
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

package nz.co.abrahams.asithappens.capture;

import nz.co.abrahams.asithappens.storage.DataSets;
import nz.co.abrahams.asithappens.flow.FlowOptions;
import nz.co.abrahams.asithappens.core.Configuration;
import nz.co.abrahams.asithappens.cartgraph.TimeSeriesContext;
import nz.co.abrahams.asithappens.cartgraph.DataGraph;
import nz.co.abrahams.asithappens.uiutil.ErrorHandler;
import nz.co.abrahams.asithappens.uiutil.ProgressBar;
import java.io.*;
import java.awt.Component;
import javax.swing.*;
import java.text.*;
import java.util.*;

/**
 * The graphical pane for creating graphs based on packet captures.
 *
 * @author  mark
 */
public class MainCapturePanel extends javax.swing.JPanel {
    
    /** Creates new MainCapturePanel form. */
    public MainCapturePanel() {
        initComponents();
        if ( Configuration.getProperty("default.gui.capture.filename") != null ) {
            fileField.setText(Configuration.getProperty("default.gui.capture.filename"));
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fileLabel = new javax.swing.JLabel();
        fileField = new javax.swing.JTextField();
        fileButton = new javax.swing.JButton();
        fetchBoundaryTimesButton = new javax.swing.JButton();
        categorizeLabel = new javax.swing.JLabel();
        ipProtocolCheckBox = new javax.swing.JCheckBox();
        sourceAddressCheckBox = new javax.swing.JCheckBox();
        destinationAddressCheckBox = new javax.swing.JCheckBox();
        sourcePortCheckBox = new javax.swing.JCheckBox();
        destinationPortCheckBox = new javax.swing.JCheckBox();
        dateLabel = new javax.swing.JLabel();
        TimeLabel = new javax.swing.JLabel();
        fromLabel = new javax.swing.JLabel();
        fromDateField = new javax.swing.JTextField();
        fromTimeField = new javax.swing.JTextField();
        toLabel = new javax.swing.JLabel();
        toDateField = new javax.swing.JTextField();
        toTimeField = new javax.swing.JTextField();
        flowGraphButton = new javax.swing.JButton();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        fileLabel.setText("File");
        add(fileLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 40, 20));
        add(fileField, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, 290, -1));

        fileButton.setText("...");
        fileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileButtonActionPerformed(evt);
            }
        });
        add(fileButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 10, -1, 20));

        fetchBoundaryTimesButton.setText("Fetch Boundary Times");
        fetchBoundaryTimesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fetchBoundaryTimesButtonActionPerformed(evt);
            }
        });
        add(fetchBoundaryTimesButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 45, 380, -1));

        categorizeLabel.setText("Categorize by");
        add(categorizeLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, -1, -1));

        ipProtocolCheckBox.setSelected(true);
        ipProtocolCheckBox.setText("IP protocol");
        ipProtocolCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ipProtocolCheckBoxActionPerformed(evt);
            }
        });
        add(ipProtocolCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, -1, -1));

        sourceAddressCheckBox.setSelected(true);
        sourceAddressCheckBox.setText("Source IP address");
        add(sourceAddressCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, -1, -1));

        destinationAddressCheckBox.setSelected(true);
        destinationAddressCheckBox.setText("Destination IP address");
        add(destinationAddressCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, -1, -1));

        sourcePortCheckBox.setSelected(true);
        sourcePortCheckBox.setText("Source TCP/UDP port");
        add(sourcePortCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 180, -1, -1));

        destinationPortCheckBox.setSelected(true);
        destinationPortCheckBox.setText("Destination TCP/UDP port");
        add(destinationPortCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, -1, -1));

        dateLabel.setText("Date");
        add(dateLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 260, -1, -1));

        TimeLabel.setText("Time");
        add(TimeLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 260, -1, -1));

        fromLabel.setText("From");
        add(fromLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 280, -1, -1));

        fromDateField.setMinimumSize(new java.awt.Dimension(50, 20));
        fromDateField.setPreferredSize(new java.awt.Dimension(50, 20));
        add(fromDateField, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 280, 70, -1));

        fromTimeField.setMinimumSize(new java.awt.Dimension(70, 20));
        fromTimeField.setPreferredSize(new java.awt.Dimension(70, 20));
        add(fromTimeField, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 280, -1, -1));

        toLabel.setText("To");
        add(toLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 300, -1, -1));

        toDateField.setMinimumSize(new java.awt.Dimension(50, 20));
        toDateField.setPreferredSize(new java.awt.Dimension(50, 20));
        add(toDateField, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 300, 70, -1));

        toTimeField.setMinimumSize(new java.awt.Dimension(70, 20));
        toTimeField.setPreferredSize(new java.awt.Dimension(70, 20));
        add(toTimeField, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 300, -1, -1));

        flowGraphButton.setText("Flow Graph");
        flowGraphButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                flowGraphButtonActionPerformed(evt);
            }
        });
        add(flowGraphButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 350, 380, -1));
    }// </editor-fold>//GEN-END:initComponents
    
    /** Creates a new flow graph. */
    private void flowGraphButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_flowGraphButtonActionPerformed
        String fileName;
        FlowOptions options;
        PacketTraceLoadTask task;
        ProgressBar progressBar;
        FlowData data;
        TimeSeriesContext context;
        DataGraph graphFrame;
        SimpleDateFormat dateFormat;
        long fromDate;
        long toDate;
        
        try {
            fileName = fileField.getText();
            dateFormat = new SimpleDateFormat("dd/MM/yyyy H:mm:ss");
            fromDate = dateFormat.parse(fromDateField.getText() + " " + fromTimeField.getText()).getTime();
            toDate = dateFormat.parse(toDateField.getText() + " " + toTimeField.getText()).getTime();
            
            options = new FlowOptions(ipProtocolCheckBox.isSelected(), sourceAddressCheckBox.isSelected(),
                    destinationAddressCheckBox.isSelected(), false, sourcePortCheckBox.isSelected(), destinationPortCheckBox.isSelected());
            
            // create an asynchronous thread to fetch data while freeing up GUI
            task = new PacketTraceLoadTask(this, fileName, fromDate, toDate, options);
            progressBar = new ProgressBar("Reading capture file", "Graph title: Packet capture for " + fileName, task);
            
            // Can block event dispatch thread for a long time
            /*
            data = new FlowData(fileField.getText(), "Flow Graph", DataSets.DIRECTION_NONE, fromDate, toDate, options);
            context = new TimeSeriesContext(data, TimeSeriesContext.ABSOLUTE_BOUNDARIES,
                    TimeSeriesContext.SUMMED_AGGREGATION, TimeSeriesContext.NO_INTERPOLATION,
                    TimeSeriesContext.STACKED, null, TimeSeriesContext.FLOW_COLORS, true, false);
            graphFrame = new DataGraph(context);
             */
            //graphFrame.setVisible(true);
        } catch (Exception e) {
            ErrorHandler.modalError(this, e.toString(), "Problem creating graph", e);
        }
    }//GEN-LAST:event_flowGraphButtonActionPerformed
    
    /**
     * Enables/disables the options based on IP protocol when the "IP protocol"
     *  checkbox is selected/deselected.
     */
    private void ipProtocolCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ipProtocolCheckBoxActionPerformed
        if ( ipProtocolCheckBox.isSelected() == true ) {
            sourcePortCheckBox.setEnabled(true);
            destinationPortCheckBox.setEnabled(true);
        } else {
            sourcePortCheckBox.setEnabled(false);
            destinationPortCheckBox.setEnabled(false);
            sourcePortCheckBox.setSelected(false);
            destinationPortCheckBox.setSelected(false);        }
    }//GEN-LAST:event_ipProtocolCheckBoxActionPerformed
    
    /** Fetches statistics from a packet capture file. */
    private void fetchBoundaryTimesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fetchBoundaryTimesButtonActionPerformed
        String fileName;
        PacketTraceIterator iterator;
        long startTime, finishTime;
        GregorianCalendar startCalendar, finishCalendar;
        SimpleDateFormat dateFormat, timeFormat;
        
        fileName = fileField.getText();
        try {
            dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            timeFormat = new SimpleDateFormat("H:mm:ss");
            startCalendar = new GregorianCalendar();
            finishCalendar = new GregorianCalendar();
            iterator = new PacketTraceIterator(fileName);
            
            startCalendar.setTimeInMillis(iterator.getStartTime());
            finishCalendar.setTimeInMillis(iterator.getFinishTime());
            fromDateField.setText(dateFormat.format(startCalendar.getTime()));
            fromTimeField.setText(timeFormat.format(startCalendar.getTime()));
            toDateField.setText(dateFormat.format(finishCalendar.getTime()));
            toTimeField.setText(timeFormat.format(finishCalendar.getTime()));
        } catch (Exception e) {
            ErrorHandler.modalError(this, e.toString(), "Problem reading file " + fileName);
        }
    }//GEN-LAST:event_fetchBoundaryTimesButtonActionPerformed
    
    /** Produces a file chooser dialog to select a packet capture file. */
    private void fileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileButtonActionPerformed
        JFileChooser chooser;
        int returnStatus;
        
        chooser = new JFileChooser();
        returnStatus = chooser.showOpenDialog(this);
        if ( returnStatus == JFileChooser.APPROVE_OPTION) {
            fileField.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }//GEN-LAST:event_fileButtonActionPerformed
    
    public static void createGraph(Component parent, DataSets dataSets) {
        TimeSeriesContext context;
        DataGraph graphFrame;
        
        try {
            context = new TimeSeriesContext(dataSets);
            graphFrame = new DataGraph(context);
            //graphFrame.setVisible(true);
        } catch (Exception e) {
            ErrorHandler.modalError(parent, e.toString(), "Problem creating graph", e);
        }        
    }    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel TimeLabel;
    private javax.swing.JLabel categorizeLabel;
    private javax.swing.JLabel dateLabel;
    private javax.swing.JCheckBox destinationAddressCheckBox;
    private javax.swing.JCheckBox destinationPortCheckBox;
    private javax.swing.JButton fetchBoundaryTimesButton;
    private javax.swing.JButton fileButton;
    private javax.swing.JTextField fileField;
    private javax.swing.JLabel fileLabel;
    private javax.swing.JButton flowGraphButton;
    private javax.swing.JTextField fromDateField;
    private javax.swing.JLabel fromLabel;
    private javax.swing.JTextField fromTimeField;
    private javax.swing.JCheckBox ipProtocolCheckBox;
    private javax.swing.JCheckBox sourceAddressCheckBox;
    private javax.swing.JCheckBox sourcePortCheckBox;
    private javax.swing.JTextField toDateField;
    private javax.swing.JLabel toLabel;
    private javax.swing.JTextField toTimeField;
    // End of variables declaration//GEN-END:variables
    
}