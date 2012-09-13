/*
 * AsItHappens.java
 *
 * Created on 14 May 2005, 13:06
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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import nz.co.abrahams.asithappens.core.*;
import nz.co.abrahams.asithappens.storage.Layout;
import nz.co.abrahams.asithappens.uiutil.ErrorHandler;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * The main application JFrame.
 *
 * @author mark
 */
public class AsItHappens extends JFrame {

    /** Logging provider */
    private static Logger logger = Logger.getLogger(AsItHappens.class);

    /** Main application panel */
    private MainTabbedPanel mainPanel;

    /** Application status bar */
    private JLabel statusLabel;

    /** Creates a new instance of the AsItHappens application. */
    public AsItHappens() throws FileNotFoundException, IOException, DBException {
        PropertyConfigurator.configure(Configuration.DEFAULT_LOG4J_PROPERTIES_FILE);
        Configuration.initialize();
        initDatabase();
        initComponents();
    }

    /** Creates the database if it does not already exist. */
    private void initDatabase() throws DBException {
        DBCreateDialog createDialog;
        //DatabaseCreate create;

        DBUtil.setConnectionURL(DBUtil.getConnectionURL(true));
        if (!DBUtil.databaseExists()) {
            if (Configuration.getProperty("database.type").equals("mysql")) {
                createDialog = new DBCreateDialog(((JFrame) (this.getParent())), true);
                createDialog.setVisible(true);
            } else if (Configuration.getProperty("database.type").equals("h2")) {
                DBCreate.create(null, null);
            }
        }

    }

    /** Initializes and lays out the user interface. */
    private void initComponents() throws DBException {
        getContentPane().setLayout(new java.awt.BorderLayout());
        setTitle("AsItHappens");
        setPreferredSize(new Dimension(Configuration.getPropertyInt("gui.main.frame.width"), Configuration.getPropertyInt("gui.main.frame.height")));
        setMinimumSize(new Dimension(Configuration.getPropertyInt("gui.main.frame.width"), Configuration.getPropertyInt("gui.main.frame.height")));
        setIconImage(Toolkit.getDefaultToolkit().getImage(Configuration.FRAME_ICON));
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        mainPanel = new MainTabbedPanel();
        statusLabel = new javax.swing.JLabel();

        getContentPane().add(mainPanel);
        getContentPane().add(statusLabel, BorderLayout.SOUTH);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override public void windowClosing(java.awt.event.WindowEvent event) {
                exitForm();
            }
        });
        pack();
    }

    /** Closes the main application frame. */
    private void exitForm() {
        int decision;
        
        if (!Layout.currentIsEmpty()) {
            decision = JOptionPane.showConfirmDialog(this, "Do you want to close all open graphs?",
                    "Graphs open", JOptionPane.YES_NO_CANCEL_OPTION);
            if (decision == JOptionPane.YES_OPTION) {
                while (!Layout.currentIsEmpty()) {
                    Layout.getCurrentGraphs().get(0).closeGraph();
                }
            } else if (decision == JOptionPane.CANCEL_OPTION) {
                return;
            }
        }
        dispose();
    }

    /** Runs the application. */
    public static void main(String args[]) {
        AsItHappens application;
        
        try {
            application = new AsItHappens();
            application.setVisible(true);
        } catch (FileNotFoundException e) {
            logger.fatal("Problem locating configuration files");
            ErrorHandler.nonModalError("Ensure configuration files are present in \"config\" directory",
                    "Can\'t start AsItHappens: Problem locating configuration files", e);
        } catch (IOException e) {
            logger.fatal("Problem opening configuration files");
            ErrorHandler.nonModalError("Ensure configuration files are accessible in \"config\" directory",
                    "Can\'t start AsItHappens: Problem opening configuration files", e);
        } catch (DBException e) {
            logger.fatal("Problem connecting to database");
            ErrorHandler.nonModalError("Database locked by another running application - check that AsItHappens is not already running",
                    "Can\'t start AsItHappens: Problem connecting to database", e);
        }

    }
}
