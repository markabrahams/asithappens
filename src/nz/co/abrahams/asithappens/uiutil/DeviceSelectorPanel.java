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

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.*;
import nz.co.abrahams.asithappens.core.DBException;
import nz.co.abrahams.asithappens.mainui.SNMPAuthenticationDialog;
import nz.co.abrahams.asithappens.storage.Device;

/**
 *
 * @author mark
 */
public class DeviceSelectorPanel extends JPanel {

    private JLabel deviceLabel;

    private JTextField nameField;

    private JButton authButton;
    
    private boolean useWriteAuth;
    
    private Device device;

    public DeviceSelectorPanel(boolean useWriteAuth) {
        initComponents();
        this.useWriteAuth = useWriteAuth;
    }

    private void initComponents() {
        GridBagConstraints constraints;

        setLayout(new GridBagLayout());

        deviceLabel = new JLabel("Device");
        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(0,0,10,0);
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
        constraints.insets = new Insets(0,10,10,0);
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
        constraints.insets = new Insets(0,10,0,0);
        add(authButton, constraints);
    }
    
    public DeviceSelectorModel getModel() {
        return new DeviceSelectorModel(nameField.getText(), useWriteAuth);
    }
    
    public void setUseWriteAuth(boolean newUseWriteAuth) {
        useWriteAuth = newUseWriteAuth;
    }

    private void editDialog() {
        SNMPAuthenticationDialog dialog;

        if ( nameField.getText().isEmpty() ) {
            ErrorHandler.modalError(this, "Please enter a device name", "Empty device name");
            return;
        }

        dialog = new SNMPAuthenticationDialog((JFrame)(SwingUtilities.getWindowAncestor(this)), true, nameField.getText(), useWriteAuth);
        dialog.setVisible(true);
        
    }
    
    /*
    public String retrieveCommunity(String deviceName) throws DBException {
        Device temporaryDevice;
        
        temporaryDevice = new Device(deviceName);
        if ( useWriteAuth )
            return temporaryDevice.retrieveWriteCommunity();
        else
            return temporaryDevice.retrieveReadCommunity();
    }
    */
}
