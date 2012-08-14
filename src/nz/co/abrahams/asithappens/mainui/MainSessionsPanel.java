/*
 * MainSessionsPanel.java
 *
 * Created on 17 Sep 2006, 00:01
 *
 * AsItHappens - real-time network monitor
 * Copyright (C) 2006  Mark Abrahams
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 c*c
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

package nz.co.abrahams.asithappens.mainui;

import nz.co.abrahams.asithappens.storage.DataSetsDAO;
import nz.co.abrahams.asithappens.storage.DataSetDAO;
import nz.co.abrahams.asithappens.storage.DataSet;
import nz.co.abrahams.asithappens.storage.DataSets;
import nz.co.abrahams.asithappens.storage.DataPoint;
import nz.co.abrahams.asithappens.core.DAOFactory;
import nz.co.abrahams.asithappens.core.DBCreateDialog;
import nz.co.abrahams.asithappens.core.DBCreate;
import nz.co.abrahams.asithappens.core.DBException;
import nz.co.abrahams.asithappens.cartgraph.DataGraphDAO;
import nz.co.abrahams.asithappens.cartgraph.TimeSeriesContext;
import nz.co.abrahams.asithappens.cartgraph.DataGraph;
import nz.co.abrahams.asithappens.uiutil.ErrorHandler;
import nz.co.abrahams.asithappens.uiutil.ProgressBar;
import java.awt.Component;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.text.*;
import java.util.*;
import java.io.*;
import org.apache.log4j.Logger;

/**
 * The graphical pane for viewing graphs based on stored information in the database.
 *
 * @author  mark
 */
public class MainSessionsPanel extends javax.swing.JPanel implements Runnable {
    
    /** Logging provider */
    private static Logger logger = Logger.getLogger(MainSessionsPanel.class);
    
    /** Width of the "title" column */
    protected static final int TITLE_COLUMN_WIDTH = 50;
    
    /** Time interval between updates of selected session time */
    protected static final int TIME_REFRESH_INTERVAL = 1000;
    
    /** Headings for the table of stored database sessions */
    protected static final String[] TABLE_HEADINGS = { "Title", "Description" };
    
    /** Aggregation policies */
    public static final String[] AGGREGATION_COMBO_ITEMS = { "Average", "Maximum" };
    
    /** Time updater thread */
    private Thread timeUpdaterThread;
    
    /** List of session ID's from the database */
    private int[] sessions;
    
    /** List of collecting states for the sessions array */
    private boolean[] isCollecting;
    
    /** GUI list of sessions */
    private JList sessionsList;
    
    /** GUI table showing stored sessions */
    private JTable sessionsTable;
    
    /** Creates new MainDatabasePanel form */
    public MainSessionsPanel() throws DBException {
        DBCreateDialog createDialog;
        DBCreate create;
        initComponents();
        
        displaySessions();
        startTimeUpdater();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        sessionsLabel = new javax.swing.JLabel();
        sessionsPane = new javax.swing.JScrollPane();
        dateLabel = new javax.swing.JLabel();
        TimeLabel = new javax.swing.JLabel();
        fromLabel = new javax.swing.JLabel();
        toLabel = new javax.swing.JLabel();
        fromDateField = new javax.swing.JTextField();
        fromTimeField = new javax.swing.JTextField();
        toDateField = new javax.swing.JTextField();
        toTimeField = new javax.swing.JTextField();
        sessionsButtonsPanel = new javax.swing.JPanel();
        deleteSessionButton = new javax.swing.JButton();
        aggregationInterpolationPanel = new javax.swing.JPanel();
        aggregationLabel = new javax.swing.JLabel();
        aggregationCombo = new JComboBox(MainSessionsPanel.AGGREGATION_COMBO_ITEMS);
        interpolationLabel = new javax.swing.JLabel();
        interpolationCombo = new JComboBox(TimeSeriesContext.INTERPOLATION);
        actionsPanel = new javax.swing.JPanel();
        graphButton = new javax.swing.JButton();
        exportButton = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        sessionsLabel.setText("Sessions");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(sessionsLabel, gridBagConstraints);

        sessionsPane.setMinimumSize(new java.awt.Dimension(300, 100));
        sessionsPane.setPreferredSize(new java.awt.Dimension(300, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(sessionsPane, gridBagConstraints);

        dateLabel.setText("Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(dateLabel, gridBagConstraints);

        TimeLabel.setText("Time");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(TimeLabel, gridBagConstraints);

        fromLabel.setText("From");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        add(fromLabel, gridBagConstraints);

        toLabel.setText("To");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        add(toLabel, gridBagConstraints);

        fromDateField.setMinimumSize(new java.awt.Dimension(50, 20));
        fromDateField.setPreferredSize(new java.awt.Dimension(50, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 20);
        add(fromDateField, gridBagConstraints);

        fromTimeField.setMinimumSize(new java.awt.Dimension(70, 20));
        fromTimeField.setPreferredSize(new java.awt.Dimension(70, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 20);
        add(fromTimeField, gridBagConstraints);

        toDateField.setMinimumSize(new java.awt.Dimension(50, 20));
        toDateField.setPreferredSize(new java.awt.Dimension(50, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 20);
        add(toDateField, gridBagConstraints);

        toTimeField.setMinimumSize(new java.awt.Dimension(70, 20));
        toTimeField.setPreferredSize(new java.awt.Dimension(70, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 20);
        add(toTimeField, gridBagConstraints);

        sessionsButtonsPanel.setLayout(new java.awt.GridLayout(1, 0));

        deleteSessionButton.setText("Delete Session");
        deleteSessionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteSessionButtonActionPerformed(evt);
            }
        });
        sessionsButtonsPanel.add(deleteSessionButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
        add(sessionsButtonsPanel, gridBagConstraints);

        aggregationInterpolationPanel.setLayout(new java.awt.GridLayout(1, 0, 10, 0));

        aggregationLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        aggregationLabel.setText("Aggregation");
        aggregationInterpolationPanel.add(aggregationLabel);
        aggregationInterpolationPanel.add(aggregationCombo);

        interpolationLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        interpolationLabel.setText("Interpolation");
        aggregationInterpolationPanel.add(interpolationLabel);
        aggregationInterpolationPanel.add(interpolationCombo);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 5, 0);
        add(aggregationInterpolationPanel, gridBagConstraints);

        actionsPanel.setLayout(new java.awt.GridLayout(1, 0));

        graphButton.setText("Display Graph");
        graphButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                graphButtonActionPerformed(evt);
            }
        });
        actionsPanel.add(graphButton);

        exportButton.setText("Export Data");
        exportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportButtonActionPerformed(evt);
            }
        });
        actionsPanel.add(exportButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(actionsPanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    
    private void exportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportButtonActionPerformed
        JFileChooser chooser;
        JOptionPane dialog;
        int returnStatus;
        File savedFile;
        FileWriter out;
        DataSetsDAO dataSetsDAO;
        DataSetDAO dataSetDAO;
        int sessionID;
        String[] headings;
        DataSet dataSet;
        DataPoint point;
        
        savedFile = null;
        dialog = new JOptionPane();
        
        sessionID = getSessionID();
        if ( sessionID == -1 )
            return;
        
        if ( ((JTable)(sessionsPane.getViewport().getView())).getSelectedRowCount() > 1 ) {
            dialog.showMessageDialog(this, "Please select a single session for data export", "Multiple sessions selected", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            //.testConnection();
            dataSetsDAO = DAOFactory.getDataSetsDAO();
            dataSetDAO = DAOFactory.getDataSetDAO();
            chooser = new JFileChooser();
            returnStatus = chooser.showSaveDialog(this);
            if ( returnStatus == JFileChooser.APPROVE_OPTION ) {
                savedFile = chooser.getSelectedFile();
                if ( ! savedFile.exists() || ( savedFile.exists() && dialog.showConfirmDialog(null,
                        "File already exists - are you sure you want to overwrite it?",
                        "Confirm file overwrite", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION ) ) {
                    out = new FileWriter(savedFile);
                    
                    out.write("Title: " + dataSetsDAO.retrieveSessionTitle(sessionID) + "\n");
                    out.write("Description: " + DataSets.retrieveDescription(sessionID) + "\n\n");
                    
                    headings = DAOFactory.getDataHeadingsDAO().retrieve(sessionID);
                    
                    for ( int set = 0 ; set < headings.length ; set++ ) {
                        out.write("Set " + set + ": " + headings[set] + "\n");
                    }
                    out.write("\n");
                    
                    out.write("Set,Time,Value\n");
                    for ( int set = 0; set < headings.length ; set++ ) {
                        dataSet = dataSetDAO.retrieve(sessionID, set);
                        //out.write("Set " + set + ": " + headings[set] + "\n");
                        for ( int i = 0 ; i < dataSet.size() ; i++ ) {
                            point = dataSet.elementAt(i);
                            out.write(set + "," + point.getTime() + "," + point.getValue() + "\n");
                        }
                        //out.write("\n");
                    }
                    
                    out.close();
                }
            }
            //} catch (ParseException e) {
            //    ErrorHandler.modalError(this, "Unable to interpret given dates and times", "Can't display graph", e);
        } catch (DBException e) {
            ErrorHandler.modalError(this, "Please check database connectivity", "Cannot retrieve data from database", e);
        } catch (IOException e) {
            ErrorHandler.modalError(this, "Unable to write file \"" + savedFile.getName() + "\"", "Cannot write file", e);
        }
    }//GEN-LAST:event_exportButtonActionPerformed
    
    /** Displays a graph based on stored information. */
    private void graphButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_graphButtonActionPerformed

        JOptionPane errorDialog;
        DataSetsLoadTask task;
        ProgressBar progressBar;
        DataSetsDAO dataSetsDAO;
        int sessionID;
        TimeSeriesContext.Aggregation aggregation;
        TimeSeriesContext.Interpolation interpolation;
        SimpleDateFormat dateFormat;
        SimpleDateFormat timeFormat;
        long sessionStartTime;
        long sessionFinishTime;
        long fromDate;
        long toDate;
        String graphTitle;
        
        sessionID = getSessionID();
        if ( sessionID == -1 )
            return;
        
        if ( ((JTable)(sessionsPane.getViewport().getView())).getSelectedRowCount() > 1 ) {
            errorDialog = new JOptionPane();
            errorDialog.showMessageDialog(this, "Please select a single session for graph display", "Multiple sessions selected", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            dataSetsDAO = DAOFactory.getDataSetsDAO();
            
            aggregation = TimeSeriesContext.Aggregation.values()[aggregationCombo.getSelectedIndex()];
            interpolation = TimeSeriesContext.Interpolation.values()[interpolationCombo.getSelectedIndex()];
            //dataTypeID = dba.getDataTypeID(sessionID);
            
            dateFormat = new SimpleDateFormat("dd/MM/yyyy H:mm:ss");
            fromDate = dateFormat.parse(fromDateField.getText() + " " + fromTimeField.getText()).getTime();
            toDate = dateFormat.parse(toDateField.getText() + " " + toTimeField.getText()).getTime();
            sessionStartTime = dataSetsDAO.retrieveSessionStartTime(sessionID);
            sessionFinishTime = dataSetsDAO.retrieveSessionFinishTime(sessionID);
            logger.debug("Creating session from " + fromDate + " to " + toDate);
            
            if ( fromDate < sessionStartTime - 1000 ) {
                errorDialog = new JOptionPane();
                errorDialog.showMessageDialog(this, "The \'From\' date/time must be within the session interval", "Can't display graph", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if ( toDate > sessionFinishTime ) {
                errorDialog = new JOptionPane();
                errorDialog.showMessageDialog(this, "The \'To\' date/time must be within the session interval", "Can't display graph", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if ( fromDate >= toDate ) {
                errorDialog = new JOptionPane();
                errorDialog.showMessageDialog(this, "The \'From\' date/time must be less than the \'To\' date/time", "Can't display graph", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Old - large blocking call to fetch data that freezes GUI
            //dataSets = new DataSets(sessionID, fromDate, toDate, aggregation);
            
            // Instead create an asynchronous thread to fetch data while freeing up GUI
            task = new DataSetsLoadTask(this, sessionID, fromDate, toDate, aggregation, interpolation);
            graphTitle = ((String)(((JTable)(sessionsPane.getViewport().getView())).getModel().getValueAt(getSelectedRow(), 0)));
            progressBar = new ProgressBar("Retrieving data", "Graph title: " + graphTitle, task);
            
        } catch (ParseException e) {
            ErrorHandler.modalError(this, "Unable to interpret given dates and times", "Can't display graph", e);
        } catch (DBException e) {
            ErrorHandler.modalError(this, "Cannot retrieve data from database", "Please check database connectivity", e);
        } catch (Exception e) {
            ErrorHandler.modalError(this, e.toString(),
                    "Error creating collector", e);
        }
    }//GEN-LAST:event_graphButtonActionPerformed
    
    /** Deletes the selected session in the table. */
    private void deleteSessionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteSessionButtonActionPerformed
        int[] selections;
        int sessionID;
        boolean currentlyCollecting;
        JOptionPane dialog;
        DataSetsDAO dataSetsDAO;
        
        dialog = new JOptionPane();
        selections = ((JTable)(sessionsPane.getViewport().getView())).getSelectedRows();
        if ( selections.length == 0 ) {
            dialog.showMessageDialog(this, "Please select a session", "No session selected", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            dataSetsDAO = DAOFactory.getDataSetsDAO();
            
            currentlyCollecting = false;
            for ( int i = 0 ; i < selections.length ; i++ ) {
                if ( dataSetsDAO.retrieveSessionCollectingState(sessions[selections[i]]) == true ) {
                    currentlyCollecting = true;
                }
            }
            
            if ( currentlyCollecting ) {
                dialog.showMessageDialog(this, "At least one session is still currently collecting data", "Session still active", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if ( dialog.showConfirmDialog(null, "Are you sure you want to delete all selected session?", "Confirm session deletion", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                for ( int i = 0 ; i < selections.length ; i++ ) {
                    DAOFactory.getDataSetsDAO().deleteSession(sessions[selections[i]]);
                }
                displaySessions();
            }
            
        } catch (DBException e) {
            ErrorHandler.modalError(this, "Cannot retrieve data from database", "Please check database connectivity", e);
        }
        
    }//GEN-LAST:event_deleteSessionButtonActionPerformed
    
    /**
     * Resets the dates shown for the selected session.  The end date will
     *  constantly be changing if the session is still collecting.
     */
    /** Provides the capability to update session information from the database. */
    /** Begin the data collection.  This occurs in a separate thread. */
    private void startTimeUpdater() {
        
        //logger.debug("Starting time updater thread");
        timeUpdaterThread = new Thread(this, "AIH-sessions-time-updater");
        timeUpdaterThread.setDaemon(true);
        timeUpdaterThread.start();
    }
    
    public void run() {
        int selection;
        int sessionID;
        DataSetsDAO dataSetsDAO;
        
        try {
            dataSetsDAO = DAOFactory.getDataSetsDAO();
            while ( true ) {
                try {
                    Thread.sleep(TIME_REFRESH_INTERVAL);
                } catch (InterruptedException e) {
                }
                
                for ( int i = 0 ; i < sessions.length ; i++ ) {
                    if ( isCollecting[i] ) {
                        ((JTable)(sessionsPane.getViewport().getView())).setValueAt(DataSets.retrieveDescription(sessions[i]), i, 1);
                    }
                }
                
                /*
                if ( ((JTable)(sessionsPane.getViewport().getView())).getSelectedRowCount() == 1 ) {
                    selection = ((JTable)(sessionsPane.getViewport().getView())).getSelectedRow();
                    sessionID = sessions[selection];
                    if ( dataSetsDAO.retrieveSessionExists(sessionID) ) {
                        setDates(sessionID);
                    }
                }
                */
            }
        } catch ( DBException e ) {
            logger.error("Sessions time updater thread failed");
            e.printStackTrace();
        }
    }
    
    /** Reads the sessions from the database into the table. */
    public void displaySessions() {
        DataSetsDAO dataSetsDAO;
        String[][] tableData;
        
        try {
            dataSetsDAO = DAOFactory.getDataSetsDAO();
            sessions = dataSetsDAO.retrieveSessionIDs();
            tableData = new String[sessions.length][TABLE_HEADINGS.length];
            isCollecting = new boolean[sessions.length];
            for ( int i = 0 ; i < sessions.length ; i++ ) {
                tableData[i][0] = dataSetsDAO.retrieveSessionTitle(sessions[i]);
                tableData[i][1] = DataSets.retrieveDescription(sessions[i]);
                isCollecting[i] = dataSetsDAO.retrieveSessionCollectingState(sessions[i]);
            }
            
            /*
            sessionsList = new javax.swing.JList(descriptions);
            sessionsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            sessionsList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
                public void valueChanged(javax.swing.event.ListSelectionEvent event) {
                    //setDates(sessions[((JList)(event.getSource())).getSelectedIndex()]);
                    setDates(getSessionID());
                }
            });
             */
            
            sessionsTable = new javax.swing.JTable(new SessionsTableModel(tableData, TABLE_HEADINGS));
            //sessionsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            sessionsTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            sessionsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    if ( ((JTable)(sessionsPane.getViewport().getView())).getSelectedRowCount() == 1 )
                        setDates(getSessionID());
                    else
                        clearDates();
                }
            });
            sessionsTable.getModel().addTableModelListener(new TableModelListener() {
                public void tableChanged(TableModelEvent e) {
                    if ( e.getColumn() == 0 )
                        updateTitle(e.getFirstRow(), e.getColumn(), ((TableModel)e.getSource()).getValueAt(e.getFirstRow(), e.getColumn()).toString());
                }
            });
            sessionsTable.getColumnModel().getColumn(0).setPreferredWidth(TITLE_COLUMN_WIDTH);
            sessionsPane.setViewportView((java.awt.Component)sessionsTable);
        } catch (DBException e) {
            ErrorHandler.modalError(this, "Cannot retrieve data from database", "Please check database connectivity", e);
        }
    }
    
    /**
     * Updates the title of a stored session in the database.
     *
     * @param row     table row
     * @param column  table column
     * b     * @param title   new title for session
     */
    protected void updateTitle(int row, int column, String title) {
        try {
            DAOFactory.getDataSetsDAO().updateSessionTitle(getSessionID(), title);
        } catch (DBException e) {
            ErrorHandler.modalError(this, "Cannot change data in database", "Update of session title in database unsuccessful", e);
        }
    }
    
    /**
     * Sets the date fields from the given session.
     *
     * @param sessionID  unique database index for the session
     */
    private void setDates(int sessionID) {
        long startTime, finishTime;
        GregorianCalendar startCalendar, finishCalendar;
        SimpleDateFormat dateFormat, timeFormat;
        DataSetsDAO dataSetsDAO;
        
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        timeFormat = new SimpleDateFormat("H:mm:ss");
        startCalendar = new GregorianCalendar();
        finishCalendar = new GregorianCalendar();
        
        try {
            dataSetsDAO = DAOFactory.getDataSetsDAO();
            startCalendar.setTimeInMillis(dataSetsDAO.retrieveSessionStartTime(sessionID));
            finishCalendar.setTimeInMillis(dataSetsDAO.retrieveSessionFinishTime(sessionID));
            fromDateField.setText(dateFormat.format(startCalendar.getTime()));
            fromTimeField.setText(timeFormat.format(startCalendar.getTime()));
            toDateField.setText(dateFormat.format(finishCalendar.getTime()));
            if ( ! toTimeField.isFocusOwner() )
                toTimeField.setText(timeFormat.format(finishCalendar.getTime()));
            dataSetsDAO.closeConnection();
        } catch (DBException e) {
            ErrorHandler.modalError(this, "Cannot retrieve data from database", "Update of session title in database unsuccessful", e);
        }
    }
    
    /**
     * Clears the date fields.
     */
    private void clearDates() {
        fromDateField.setText("");
        fromTimeField.setText("");
        toDateField.setText("");
        toTimeField.setText("");
    }
    
    
    /** @return selected row index */
    protected int getSelectedRow() {
        return ((JTable)(sessionsPane.getViewport().getView())).getSelectedRow();
    }
    
    /** @return selected session ID */
    private int getSessionID() {
        int selection, sessionID;
        JOptionPane dialog;
        
        try {
            selection = ((JTable)(sessionsPane.getViewport().getView())).getSelectedRow();
            if ( selection == -1 ) {
                dialog = new JOptionPane();
                dialog.showMessageDialog(this, "Please select a session", "No session selected", JOptionPane.ERROR_MESSAGE);
                return -1;
            }
            
            sessionID = sessions[selection];
            if ( ! DAOFactory.getDataSetsDAO().retrieveSessionExists(sessionID) ) {
                dialog = new JOptionPane();
                dialog.showMessageDialog(this, "Please another session", "Can\'t find session in database", JOptionPane.ERROR_MESSAGE);
                displaySessions();
                return -1;
            }
            
            return sessionID;
        } catch (DBException e) {
            dialog = new JOptionPane();
            dialog.showMessageDialog(this, "Please ensure database is accessible", "Can\'t connect to database", JOptionPane.ERROR_MESSAGE);
            return -1;
        }
    }
    
    public static void createGraph(Component parent, DataSets dataSets, int dataTypeID, TimeSeriesContext.Aggregation aggregation, TimeSeriesContext.Interpolation interpolation) {
        int graphID;
        TimeSeriesContext context;
        DataGraph graphFrame;
        
        try {
            context = new TimeSeriesContext(dataSets);
            context.setXAxisScaling(TimeSeriesContext.XAxisScaling.AbsoluteBoundaries);
            context.setAggregation(aggregation);
            context.setInterpolation(interpolation);
            graphFrame = new DataGraph(context);
        } catch (Exception e) {
            ErrorHandler.modalError(parent, e.toString(),
                    "Error creating graph from stored session", e);
        }
    }
    
    public static void createGraphWithContext(Component parent, int sessionID, DataSets dataSets, TimeSeriesContext.Aggregation aggregation, TimeSeriesContext.Interpolation interpolation) {
        DataGraphDAO graphDAO;
        int graphID;
        TimeSeriesContext context;
        DataGraph graphFrame;
        
        try {
            
            graphDAO = DAOFactory.getDataGraphDAO();
            graphID = graphDAO.retrieveGraphIDForSession(sessionID);

            graphFrame = graphDAO.retrieveGraph(graphID, dataSets);

            graphDAO.closeConnection();
            graphFrame.getContext().setAggregation(aggregation);
            graphFrame.getContext().setInterpolation(interpolation);
            graphFrame.getContext().setXAxisScaling(TimeSeriesContext.XAxisScaling.AbsoluteBoundaries);
        } catch (Exception e) {
            ErrorHandler.modalError(parent, e.toString(),
                    "Error creating graph from stored session", e);
        }
    }
    
    /**
     * Resets the dates shown for the selected session.  The end date will
     *  constantly be changing if the session is still collecting.
     */
    /*
    private void resetDatesButtonActionPerformed(java.awt.event.ActionEvent evt) {
        int sessionID;
     
        sessionID = getSessionID();
        if ( sessionID == -1 )
            return;
     
        setDates(sessionID);
    }
     */
    /** Provides the capability to update session information from the database. */
    /*
    private void refreshSessionsButtonActionPerformed(java.awt.event.ActionEvent evt) {
        displaySessions();
    }
     */
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel TimeLabel;
    private javax.swing.JPanel actionsPanel;
    private javax.swing.JComboBox aggregationCombo;
    private javax.swing.JPanel aggregationInterpolationPanel;
    private javax.swing.JLabel aggregationLabel;
    private javax.swing.JLabel dateLabel;
    private javax.swing.JButton deleteSessionButton;
    private javax.swing.JButton exportButton;
    private javax.swing.JTextField fromDateField;
    private javax.swing.JLabel fromLabel;
    private javax.swing.JTextField fromTimeField;
    private javax.swing.JButton graphButton;
    private javax.swing.JComboBox interpolationCombo;
    private javax.swing.JLabel interpolationLabel;
    private javax.swing.JPanel sessionsButtonsPanel;
    private javax.swing.JLabel sessionsLabel;
    private javax.swing.JScrollPane sessionsPane;
    private javax.swing.JTextField toDateField;
    private javax.swing.JLabel toLabel;
    private javax.swing.JTextField toTimeField;
    // End of variables declaration//GEN-END:variables
    
}
