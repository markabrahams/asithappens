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
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

/**
 *
 * @author mark
 */
public class DeviceSelectorPanel extends JPanel {

    private JLabel deviceLabel;

    private JTextField nameField;

    private JButton selectorButton;

    public DeviceSelectorPanel() {
        initComponents();
    }

    private void initComponents() {
        GridBagConstraints constraints;

        setLayout(new GridBagLayout());

        deviceLabel = new JLabel("Device");
        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        add(deviceLabel, constraints);

        nameField = new JTextField();
        nameField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                //nameFieldActionPerformed(evt);
            }
        });
        nameField.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent evt) {
                //nameFieldFocusLost(evt);
            }
        });
        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;
        add(nameField, constraints);

        selectorButton = new JButton();
        selectorButton.setText("...");
        constraints = new GridBagConstraints();
        constraints.gridx = 2;
        constraints.gridy = 0;
        add(selectorButton, constraints);
    }


}
