/*
 * SNMPAuthenticationDialog.java
 *
 * Created on 3 August 2012, 00:49
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

import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import nz.co.abrahams.asithappens.core.DAOFactory;
import nz.co.abrahams.asithappens.core.DBException;
import nz.co.abrahams.asithappens.snmputil.*;
import nz.co.abrahams.asithappens.storage.Device;
import nz.co.abrahams.asithappens.storage.DeviceDAO;
import nz.co.abrahams.asithappens.uiutil.DeviceSelectorModel;
import nz.co.abrahams.asithappens.uiutil.ErrorHandler;

/**
 *
 * @author mark
 */
public class SNMPAuthenticationDialog extends javax.swing.JDialog {

    private Device originalDevice;
    private String deviceName;
    private SNMPAccessType snmpAccessType;
    private USMAuthProtocol[] authList;
    private USMPrivProtocol[] privList;

    /**
     * Creates new form EditDeviceDialog
     */
    public SNMPAuthenticationDialog(java.awt.Frame parent, boolean modal, String deviceName, SNMPAccessType snmpAccessType) {
        super(parent, modal);
        this.deviceName = deviceName;
        this.snmpAccessType = snmpAccessType;
        initComponents();
        setTitle("SNMP authentication - " + accessDescription());
        nameLabel.setText("Device: " + deviceName + " (" + accessDescription() + ")");
        version1Button.setSelected(true);

        initialiseCombos();
        typeCombo.setSelectedIndex(0);
        authProtocolCombo.setSelectedIndex(0);
        privProtocolCombo.setSelectedIndex(0);
        selectUSMLevel();
        loadDevice();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        versionGroup = new javax.swing.ButtonGroup();
        version1Button = new javax.swing.JRadioButton();
        version3Button = new javax.swing.JRadioButton();
        communityLabel = new javax.swing.JLabel();
        communityField = new javax.swing.JTextField();
        userLabel = new javax.swing.JLabel();
        userTypeLabel = new javax.swing.JLabel();
        authProtocolLabel = new javax.swing.JLabel();
        authLabel = new javax.swing.JLabel();
        privProtocolLabel = new javax.swing.JLabel();
        privLabel = new javax.swing.JLabel();
        userField = new javax.swing.JTextField();
        typeCombo = new JComboBox(nz.co.abrahams.asithappens.snmputil.USMLevel.values());
        authProtocolCombo = new JComboBox(nz.co.abrahams.asithappens.snmputil.USMAuthProtocol.values());
        authField = new javax.swing.JTextField();
        privProtocolCombo = new JComboBox(nz.co.abrahams.asithappens.snmputil.USMPrivProtocol.values());
        privField = new javax.swing.JTextField();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        nameLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("SNMP authentication");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        versionGroup.add(version1Button);
        version1Button.setText("SNMP v1 / v2c");
        version1Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                version1ButtonActionPerformed(evt);
            }
        });
        getContentPane().add(version1Button, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        versionGroup.add(version3Button);
        version3Button.setText("SNMP v3");
        version3Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                version3ButtonActionPerformed(evt);
            }
        });
        getContentPane().add(version3Button, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, -1, -1));

        communityLabel.setText("Community");
        getContentPane().add(communityLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 80, -1, -1));

        communityField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                communityFieldActionPerformed(evt);
            }
        });
        getContentPane().add(communityField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 80, 200, -1));

        userLabel.setText("User");
        getContentPane().add(userLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 170, -1, -1));

        userTypeLabel.setText("Type");
        getContentPane().add(userTypeLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 210, -1, -1));

        authProtocolLabel.setText("Auth protocol");
        getContentPane().add(authProtocolLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 250, -1, -1));

        authLabel.setText("Auth key");
        getContentPane().add(authLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 290, -1, -1));

        privProtocolLabel.setText("Priv protocol");
        getContentPane().add(privProtocolLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 330, -1, -1));

        privLabel.setText("Priv key");
        getContentPane().add(privLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 370, -1, -1));

        userField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userFieldActionPerformed(evt);
            }
        });
        getContentPane().add(userField, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 170, 200, -1));

        typeCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                typeComboActionPerformed(evt);
            }
        });
        getContentPane().add(typeCombo, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 210, 200, 20));

        getContentPane().add(authProtocolCombo, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 250, 200, 20));

        authField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                authFieldActionPerformed(evt);
            }
        });
        getContentPane().add(authField, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 290, 200, -1));

        getContentPane().add(privProtocolCombo, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 330, 200, 20));

        privField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                privFieldActionPerformed(evt);
            }
        });
        getContentPane().add(privField, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 370, 200, -1));

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        getContentPane().add(okButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 410, 90, 30));

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        getContentPane().add(cancelButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 410, 90, 30));

        nameLabel.setText("Device: ");
        getContentPane().add(nameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        validateSaveDispose();
    }//GEN-LAST:event_okButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void communityFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_communityFieldActionPerformed
        validateSaveDispose();
    }//GEN-LAST:event_communityFieldActionPerformed

    private void version1ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_version1ButtonActionPerformed
        enableSNMPv1();
    }//GEN-LAST:event_version1ButtonActionPerformed

    private void version3ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_version3ButtonActionPerformed
        enableSNMPv3();
    }//GEN-LAST:event_version3ButtonActionPerformed

    private void typeComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_typeComboActionPerformed
        selectUSMLevel();
    }//GEN-LAST:event_typeComboActionPerformed

    private void userFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userFieldActionPerformed
        validateSaveDispose();
    }//GEN-LAST:event_userFieldActionPerformed

    private void authFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_authFieldActionPerformed
        validateSaveDispose();
    }//GEN-LAST:event_authFieldActionPerformed

    private void privFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_privFieldActionPerformed
        validateSaveDispose();
    }//GEN-LAST:event_privFieldActionPerformed

    private String accessDescription() {
        return (snmpAccessType == SNMPAccessType.ReadOnly) ? "read-only" : "read-write";
    }
    
    private void initialiseCombos() {
        USMAuthProtocol[] authListFull;
        USMPrivProtocol[] privListFull;
        
        authListFull = USMAuthProtocol.values();
        authList = new USMAuthProtocol[authListFull.length - 1];
        System.arraycopy(authListFull, 1, authList, 0, authListFull.length - 1);
        privListFull = USMPrivProtocol.values();
        privList = new USMPrivProtocol[privListFull.length - 1];
        System.arraycopy(privListFull, 1, privList, 0, privListFull.length - 1);
    }

    private void enableSNMPv1() {
        userField.setEnabled(false);
        typeCombo.setEnabled(false);
        authProtocolCombo.setEnabled(false);
        authField.setEnabled(false);
        privProtocolCombo.setEnabled(false);
        privField.setEnabled(false);
        communityField.setEnabled(true);
        communityField.requestFocusInWindow();
    }

    private void enableSNMPv3() {
        int authIndex;
        int privIndex;
        
        communityField.setEnabled(false);
        userField.setEnabled(true);
        typeCombo.setEnabled(true);
        authProtocolCombo.setEnabled(true);
        authField.setEnabled(true);
        privProtocolCombo.setEnabled(true);
        privField.setEnabled(true);
        authIndex = authProtocolCombo.getSelectedIndex();
        privIndex = privProtocolCombo.getSelectedIndex();
        selectUSMLevel();
        authProtocolCombo.setSelectedIndex(authIndex);
        privProtocolCombo.setSelectedIndex(privIndex);
        userField.requestFocusInWindow();
    }

    private void selectUSMLevel() {
        if (typeCombo.getSelectedItem() == USMLevel.noAuthNoPriv) {
            selectNoAuthNoPriv();
        } else if (typeCombo.getSelectedItem() == USMLevel.AuthNoPriv) {
            selectAuthNoPriv();
        } else if (typeCombo.getSelectedItem() == USMLevel.AuthPriv) {
            selectAuthPriv();
        }        
    }
    
    private void selectNoAuthNoPriv() {
        authProtocolCombo.removeAllItems();
        authProtocolCombo.addItem(USMAuthProtocol.NoAuth);
        authProtocolCombo.setEnabled(false);
        authField.setEnabled(false);
        privProtocolCombo.removeAllItems();
        privProtocolCombo.addItem(USMPrivProtocol.NoPriv);
        privProtocolCombo.setEnabled(false);
        privField.setEnabled(false);
    }

    private void selectAuthNoPriv() {
        authProtocolCombo.removeAllItems();
        populateCombo(authProtocolCombo, authList);
        authProtocolCombo.setEnabled(true);
        authField.setEnabled(true);
        privProtocolCombo.removeAllItems();
        privProtocolCombo.addItem(USMPrivProtocol.NoPriv);
        privProtocolCombo.setEnabled(false);
        privField.setEnabled(false);
    }

    private void selectAuthPriv() {
        authProtocolCombo.removeAllItems();
        populateCombo(authProtocolCombo, authList);
        authProtocolCombo.setEnabled(true);
        authField.setEnabled(true);
        privProtocolCombo.removeAllItems();
        populateCombo(privProtocolCombo, privList);
        privProtocolCombo.setEnabled(true);
        privField.setEnabled(true);
    }

    private void populateCombo(JComboBox combo, Object[] items) {
        for (int i = 0; i < items.length; i++) {
            combo.addItem(items[i]);
        }
    }

    private void loadDevice() {
        try {
            USMUser user;
            String community;
            DeviceSelectorModel deviceSelectorModel;

            deviceSelectorModel = new DeviceSelectorModel(deviceName, snmpAccessType);
            if (!deviceSelectorModel.deviceExists()) {
                originalDevice = new Device(deviceName);
                version1Button.doClick();
                return;
            } else {
                originalDevice = deviceSelectorModel.loadDevice();
            }
            
            if (snmpAccessType == SNMPAccessType.ReadOnly) {
                if (originalDevice.getSNMPVersionRead() == SNMPVersion.v3) {
                    version3Button.doClick();
                } else {
                    version1Button.doClick();
                }
                community = originalDevice.getCommunityRead();
                user = originalDevice.getUsmUserRead();
            } else {
                if (originalDevice.getSNMPVersionWrite() == SNMPVersion.v3) {
                    version3Button.doClick();
                } else {
                    version1Button.doClick();
                }
                community = originalDevice.getCommunityWrite();
                user = originalDevice.getUsmUserWrite();
            }
            communityField.setText(community);
            if (user != null) {
                userField.setText(user.getUserName());
                typeCombo.setSelectedIndex(user.getUserLevel().getIndex() - 1);
                selectUSMLevel();
                authProtocolCombo.setSelectedIndex(user.getUserAuthProtocol().getIndex() - 2);
                authField.setText(user.getUserAuthKey());
                privProtocolCombo.setSelectedIndex(user.getUserPrivProtocol().getIndex() - 2);
                privField.setText(user.getUserPrivKey());
            }
        } catch (DBException ex) {
            Logger.getLogger(SNMPAuthenticationDialog.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnknownHostException ex) {
            Logger.getLogger(SNMPAuthenticationDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
    private void loadDefaults() {
        communityField.setText("");
        userField.setText("");
        typeCombo.setSelectedIndex(0);
        authProtocolCombo.setSelectedIndex(0);
        authField.setText("");
        privProtocolCombo.setSelectedIndex(0);
        privField.setText("");
    }
    */

    private void saveDevice() {
        DeviceDAO deviceDAO;
        SNMPVersion snmpVersion;
        String community;
        USMUser usmUser;
        Device updatedDevice;

        try {
            if (areCredentialsValid() == false)
                return;
            
            deviceDAO = DAOFactory.getDeviceDAO();
            if (deviceDAO.retrieveDeviceExists(deviceName) == false) {
                deviceDAO.create(new Device(deviceName));
            }
            if (version1Button.isSelected()) {
                snmpVersion = SNMPVersion.v1;
            } else {
                snmpVersion = SNMPVersion.v3;
            }
            community = communityField.getText();
            usmUser = new USMUser(userField.getText(), USMLevel.getLevel(typeCombo.getSelectedIndex() + 1),
                    USMAuthProtocol.getAuthProtocol(authProtocolCombo.getSelectedIndex() + 2), authField.getText(),
                    USMPrivProtocol.getPrivProtocol(privProtocolCombo.getSelectedIndex() + 2), privField.getText());
            if (snmpAccessType == SNMPAccessType.ReadWrite) {
                updatedDevice = new Device(deviceName, originalDevice.getSNMPVersionRead(), snmpVersion, originalDevice.getCommunityRead(), community,
                        originalDevice.getUsmUserRead(), usmUser,
                        null, null);
                //originalDevice.getAddress().toString(), originalDevice.getEthernetAddress().toString());
            } else {
                updatedDevice = new Device(deviceName, snmpVersion, originalDevice.getSNMPVersionWrite(), community, originalDevice.getCommunityWrite(),
                        usmUser, originalDevice.getUsmUserWrite(),
                        null, null);
                //originalDevice.getAddress().toString(), originalDevice.getEthernetAddress().toString());                
            }
            deviceDAO.updateDevice(updatedDevice);
            deviceDAO.closeConnection();
            originalDevice = updatedDevice;
        } catch (DBException ex) {
            Logger.getLogger(SNMPAuthenticationDialog.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnknownHostException ex) {
            Logger.getLogger(SNMPAuthenticationDialog.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    private boolean areCredentialsValid() {
        if (version1Button.isSelected()) {
            if (communityField.getText().isEmpty()) {
                ErrorHandler.modalError(this, "Please enter a community string", "Community string is empty");
                return false;
            } else {
                return true;
            }
        } else {
            if (userField.getText().isEmpty()) {
                ErrorHandler.modalError(this, "Please enter a user name", "User name is empty");
                return false;                
            } else if ((typeCombo.getSelectedItem() == USMLevel.AuthNoPriv ||
                    typeCombo.getSelectedItem() == USMLevel.AuthPriv) &&
                    authField.getText().length() < USMUser.MINIMUM_KEY_LENGTH) {
                ErrorHandler.modalError(this, "Please enter an authentication key of at least " + USMUser.MINIMUM_KEY_LENGTH + " characters",
                        "Authentication key too short");
                return false;                                
            } else if (typeCombo.getSelectedItem() == USMLevel.AuthPriv &&
                    privField.getText().length() < USMUser.MINIMUM_KEY_LENGTH) {
                ErrorHandler.modalError(this, "Please enter a privacy key of at least " + USMUser.MINIMUM_KEY_LENGTH + " characters",
                        "Privacy key too short");
                return false;                                
            } else {
                return true;
            }
        }
    }
    
    private void validateSaveDispose() {
        if (areCredentialsValid() == false)
            return;
        saveDevice();
        dispose();
    }   
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField authField;
    private javax.swing.JLabel authLabel;
    private javax.swing.JComboBox authProtocolCombo;
    private javax.swing.JLabel authProtocolLabel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JTextField communityField;
    private javax.swing.JLabel communityLabel;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JButton okButton;
    private javax.swing.JTextField privField;
    private javax.swing.JLabel privLabel;
    private javax.swing.JComboBox privProtocolCombo;
    private javax.swing.JLabel privProtocolLabel;
    private javax.swing.JComboBox typeCombo;
    private javax.swing.JTextField userField;
    private javax.swing.JLabel userLabel;
    private javax.swing.JLabel userTypeLabel;
    private javax.swing.JRadioButton version1Button;
    private javax.swing.JRadioButton version3Button;
    private javax.swing.ButtonGroup versionGroup;
    // End of variables declaration//GEN-END:variables
}
