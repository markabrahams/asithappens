/*
 * DeviceSelectorPanel.java
 *
 * Created on 19 June 2010, 18:00
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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.net.UnknownHostException;
import javax.swing.*;
import nz.co.abrahams.asithappens.mainui.SNMPAuthenticationDialog;
import nz.co.abrahams.asithappens.snmputil.SNMPAccessType;
import nz.co.abrahams.asithappens.storage.Device;

/**
 *
 * @author mark
 */
public class DeviceSelectorPanel extends JPanel {

    private JLabel deviceLabel;
    private JTextField nameField;
    private JButton authButton;
    private SNMPAccessType snmpAccessType;

    public DeviceSelectorPanel(SNMPAccessType snmpAccessType) {
        initComponents();
        this.snmpAccessType = snmpAccessType;
    }

    private void initComponents() {
        GridBagConstraints constraints;

        setLayout(new GridBagLayout());

        deviceLabel = new JLabel("Device");
        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(0, 0, 10, 0);
        add(deviceLabel, constraints);

        nameField = new JTextField();
        nameField.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                editDialog();
            }
        });
        nameField.addFocusListener(new FocusAdapter() {

            @Override
            public void focusLost(FocusEvent evt) {
                //nameFieldFocusLost(evt);
            }
        });
        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.insets = new Insets(0, 10, 10, 0);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.ipadx = 6;
        constraints.ipady = 6;
        constraints.weightx = 1;
        add(nameField, constraints);

        authButton = new JButton();
        authButton.setText("Auth");
        authButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                editDialog();
            }
        });
        constraints = new GridBagConstraints();
        constraints.gridx = 2;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.PAGE_START;
        constraints.insets = new Insets(0, 10, 0, 0);
        add(authButton, constraints);
    }

    public Device loadDevice() {
        DeviceSelectorModel model;

        if (nameField.getText().isEmpty()) {
            ErrorHandler.modalError(this, "Please enter a device name", "Device field is empty");
            return null;
        }

        try {
            model = getModel();
            if (!model.deviceExists()) {
                ErrorHandler.modalError(this, "Please enter authentication credentials for " + nameField.getText(),
                        "No SNMP authentication credentials");
                return null;
            }
            return model.loadDevice();
        } catch (UnknownHostException e) {
            ErrorHandler.modalError(null, "Please ensure that device name \"" + nameField.getText() + "\" is valid",
                    "Unknown host " + nameField.getText());
            return null;
        }
    }

    public DeviceSelectorModel getModel() {
        return new DeviceSelectorModel(nameField.getText(), snmpAccessType);
    }

    public void setUseWriteAuth(SNMPAccessType newType) {
        snmpAccessType = newType;
    }

    private void editDialog() {
        SNMPAuthenticationDialog dialog;

        if (nameField.getText().isEmpty()) {
            ErrorHandler.modalError(this, "Please enter a device name", "Empty device name");
            return;
        }

        dialog = new SNMPAuthenticationDialog((JFrame) (SwingUtilities.getWindowAncestor(this)), true, nameField.getText(), snmpAccessType);
        dialog.setVisible(true);

    }
}
