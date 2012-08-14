/*
 * MainDevicesPanel.java
 *
 * Created on 19 January 2005, 23:41
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
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import nz.co.abrahams.asithappens.core.DAOFactory;
import nz.co.abrahams.asithappens.core.DBException;
import nz.co.abrahams.asithappens.storage.DeviceDAO;
import nz.co.abrahams.asithappens.uiutil.ErrorHandler;

/**
 *
 * @author mark
 */
public class MainDevicesPanel extends JPanel {

    private JScrollPane devicePane;

    private JList deviceList;

    public MainDevicesPanel() {
        initComponents();
        displayDevices();
    }

    private void initComponents() {
        JPanel buttonPanel;
        JButton editButton;
        JButton addButton;
        JButton deleteButton;

        setLayout(new BorderLayout());

        devicePane = new JScrollPane();
        add(devicePane, BorderLayout.CENTER);

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        addButton = new JButton("Add");
        editButton = new JButton("Edit");
        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                editDialog();
            }
        });
        deleteButton = new JButton("Delete");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        add(buttonPanel, BorderLayout.SOUTH);

    }

    private void displayDevices() {
        DeviceDAO deviceDAO;
        String[] devices;

        try {
            deviceDAO = DAOFactory.getDeviceDAO();
            devices = deviceDAO.retrieveDevices();
            deviceList = new JList(devices);
            devicePane.setViewportView(deviceList);
        }
        catch (DBException e) {
            ErrorHandler.modalError(this, "Cannot retrieve devices from database",
                    "Please check database connectivity", e);
        }
    }

    private void editDialog() {
        EditDeviceDialog dialog;

        if ( deviceList == null ) {
            ErrorHandler.modalError(this, "Cannot find device list", "Cannot find device list");
            return;
        }
        if ( deviceList.getSelectedIndex() == -1 ) {
            ErrorHandler.modalError(this, "Please select at least one device", "No device selected");
            return;
        }
        dialog = new EditDeviceDialog((JFrame)(SwingUtilities.getWindowAncestor(this)), true, (String)(deviceList.getSelectedValue()));
        dialog.setVisible(true);
    }
}
