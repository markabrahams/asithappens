/*
 * MainLayoutPanel.java
 *
 * Created on 8 October 2006, 21:57
 */

package nz.co.abrahams.asithappens.mainui;

import nz.co.abrahams.asithappens.storage.Layout;
import nz.co.abrahams.asithappens.snmputil.SNMPException;
import nz.co.abrahams.asithappens.core.DAOCreationException;
import nz.co.abrahams.asithappens.core.DBException;
import nz.co.abrahams.asithappens.*;
import nz.co.abrahams.asithappens.uiutil.ErrorHandler;
import javax.swing.*;
import java.net.UnknownHostException;

/**
 *
 * @author  mark
 */
public class MainLayoutPanel extends javax.swing.JPanel {
    
    /** Creates new form MainLayoutPanel */
    public MainLayoutPanel() {
        initComponents();
        displayLayoutsList();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        layoutsLabel = new javax.swing.JLabel();
        layoutsPane = new javax.swing.JScrollPane();
        saveButton = new javax.swing.JButton();
        saveField = new javax.swing.JTextField();
        overwriteButton = new javax.swing.JButton();
        restoreButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        layoutsLabel.setText("Layouts");
        add(layoutsLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));
        add(layoutsPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 180, 230));

        saveButton.setText("Save As");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });
        add(saveButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 30, 120, 30));
        add(saveField, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 30, 110, 30));

        overwriteButton.setText("Overwrite");
        overwriteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                overwriteButtonActionPerformed(evt);
            }
        });
        add(overwriteButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 70, 120, 30));

        restoreButton.setText("Restore");
        restoreButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                restoreButtonActionPerformed(evt);
            }
        });
        add(restoreButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 170, 120, 30));

        deleteButton.setText("Delete");
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });
        add(deleteButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 230, 120, 30));
    }// </editor-fold>//GEN-END:initComponents
    
    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        String layout;
        try {
            layout = ((JList)(layoutsPane.getViewport().getView())).getSelectedValue().toString();
            Layout.deleteLayout(layout);
            displayLayoutsList();
        } catch (DBException e) {
            ErrorHandler.modalError(null, "Please ensure that database is running and accessible",
                    "Error accessing database", e);
        }
    }//GEN-LAST:event_deleteButtonActionPerformed
    
    private void overwriteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_overwriteButtonActionPerformed
        String layout;
        
        try {
            layout = ((JList)(layoutsPane.getViewport().getView())).getSelectedValue().toString();
            Layout.deleteLayout(layout);
            Layout.saveLayout(layout);
            //displayLayoutsList();
        } catch (DBException e) {
            ErrorHandler.modalError(null, "Please ensure that database is running and accessible",
                    "Error accessing database", e);
        } catch (UnknownHostException e) {
            ErrorHandler.modalError(null, "Invalid device name",
                    "Invalid device name", e);
        } catch (DAOCreationException e) {
            ErrorHandler.modalError(null, "Some data access application component could not be found",
                    "Data access class not found", e);
        }
        
    }//GEN-LAST:event_overwriteButtonActionPerformed
    
    private void restoreButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_restoreButtonActionPerformed
        String layout;
        
        try {
            if ( ((JList)(layoutsPane.getViewport().getView())).getSelectedIndex() == -1 ) {
                ErrorHandler.modalError(this, "Please select a layout", "No layout selected");
                return;
            }
            layout = ((JList)(layoutsPane.getViewport().getView())).getSelectedValue().toString();
            Layout.loadLayout(layout);
        } catch (DBException e) {
            ErrorHandler.modalError(null, "Please ensure that database is running and accessible",
                    "Error opening database connection", e);
        } catch (UnknownHostException e) {
            ErrorHandler.modalError(null, "Invalid device name",
                    "Invalid device name", e);
        } catch (SNMPException e) {
            ErrorHandler.modalError(null, "Please ensure that device name and community string are correct",
                    "Cannot access SNMP service on device ", e);
        } catch (DAOCreationException e) {
            ErrorHandler.modalError(null, "Some data access application component could not be found",
                    "Data access class not found", e);
        }
        
    }//GEN-LAST:event_restoreButtonActionPerformed
    
    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        try {
            Layout.saveLayout(saveField.getText());
            displayLayoutsList();
        } catch (DBException e) {
            ErrorHandler.modalError(null, "Please ensure that database is running and accessible",
                    "Error accessing database", e);
        } catch (UnknownHostException e) {
            ErrorHandler.modalError(null, "Invalid device name",
                    "Invalid device name", e);
        } catch (DAOCreationException e) {
            ErrorHandler.modalError(null, "Some data access application component could not be found",
                    "Data access class not found", e);
        }
    }//GEN-LAST:event_saveButtonActionPerformed
    
    private void displayLayoutsList() {
        JList layoutsList;
        
        try {
            layoutsList = new javax.swing.JList(Layout.getLayoutsList());
            layoutsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            //layoutsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            layoutsPane.setViewportView((java.awt.Component)layoutsList);
        } catch (DBException e) {
            ErrorHandler.modalError(null, "Please ensure that database is running and accessible",
                    "Error accessing database", e);
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton deleteButton;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel layoutsLabel;
    private javax.swing.JScrollPane layoutsPane;
    private javax.swing.JButton overwriteButton;
    private javax.swing.JButton restoreButton;
    private javax.swing.JButton saveButton;
    private javax.swing.JTextField saveField;
    // End of variables declaration//GEN-END:variables
    
}
