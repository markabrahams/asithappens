/*
 * MainOIDPanel.java
 *
 * Created on 2 June 2008, 10:35
 */
package nz.co.abrahams.asithappens.oid;

import nz.co.abrahams.asithappens.core.DataType;
import nz.co.abrahams.asithappens.storage.DataSets;
import nz.co.abrahams.asithappens.storage.Device;
import nz.co.abrahams.asithappens.snmputil.SNMPException;
import nz.co.abrahams.asithappens.snmputil.SNMPType;
import nz.co.abrahams.asithappens.snmputil.SNMPTypeException;
import nz.co.abrahams.asithappens.core.DBException;
import nz.co.abrahams.asithappens.cartgraph.TimeSeriesContext;
import nz.co.abrahams.asithappens.cartgraph.DataGraph;
import nz.co.abrahams.asithappens.uiutil.ErrorHandler;
import java.util.Vector;
import java.net.UnknownHostException;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

/**
 *
 * @author  mark
 */
public class MainOIDPanel extends javax.swing.JPanel {

    /** SNMP type combo items */
    protected static final String[] oidTypes = {"Integer32", "Gauge32", "Counter32", "Counter64"};

    /** Set display types */
    protected static final String[] displayTypes = {"Fill", "Line"};

    /** The name of the current template being edited */
    protected String templateName;

    /** The OID editing table */
    protected JTable oidTable;

    /** Custom OID Template list */
    protected JList templatesList;

    /** Creates new form MainOIDPanel */
    public MainOIDPanel() {
        initComponents();
        initializeTable();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        deviceLabel = new javax.swing.JLabel();
        deviceField = new javax.swing.JTextField();
        communityLabel = new javax.swing.JLabel();
        communityField = new javax.swing.JTextField();
        graphButton = new javax.swing.JButton();
        unitsLabel = new javax.swing.JLabel();
        unitsField = new javax.swing.JTextField();
        storeDataCheckBox = new javax.swing.JCheckBox();
        editPane = new javax.swing.JScrollPane();
        removeButton = new javax.swing.JButton();
        addButton = new javax.swing.JButton();
        pollLabel = new javax.swing.JLabel();
        pollField = new javax.swing.JTextField();
        pollUnitsLabel = new javax.swing.JLabel();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        deviceLabel.setText("Device");
        add(deviceLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 50, 20));

        deviceField.setText("localhost");
        add(deviceField, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 10, 120, -1));

        communityLabel.setText("Community");
        add(communityLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 10, 110, -1));

        communityField.setText("public");
        add(communityField, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 10, 200, -1));

        graphButton.setText("Create graph");
        graphButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                graphButtonActionPerformed(evt);
            }
        });
        add(graphButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 350, 490, 30));

        unitsLabel.setText("Y axis units");
        add(unitsLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 50, -1, -1));
        add(unitsField, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 50, 50, 20));

        storeDataCheckBox.setText("Store collected data");
        add(storeDataCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 320, 180, 20));
        add(editPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 490, 170));

        removeButton.setText("Remove OID");
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeButtonActionPerformed(evt);
            }
        });
        add(removeButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 90, 240, 40));

        addButton.setText("Add OID");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });
        add(addButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 240, 40));

        pollLabel.setText("Poll Interval");
        add(pollLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, -1, -1));

        pollField.setText("2000");
        pollField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pollFieldActionPerformed(evt);
            }
        });
        add(pollField, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 50, 90, -1));

        pollUnitsLabel.setText("ms");
        add(pollUnitsLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 50, -1, -1));
    }// </editor-fold>//GEN-END:initComponents

    public void initializeTable() {
        JComboBox typeCombo;
        JComboBox setDisplayCombo;

        //try {
            /*
        for ( SetDisplay.Positioning positioning : SetDisplay.Positioning.values() )
        positioningCombo.addItem(positioning.toString());
         */
        /*
        if ( templateName == null ) {
        oidTable = new JTable(new CustomOIDTableModel());
        } else {
        load();
        }
         */
        oidTable = new JTable(new CustomOIDTableModel());
        oidTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        editPane.setViewportView((Component) oidTable);

        typeCombo = new JComboBox();
        //for ( int i = 0 ; i < oidTypes.length ; i++ )
        //    typeCombo.addItem(oidTypes[i]);
        for (SNMPType type : SNMPType.values()) {
            typeCombo.addItem(type.toString());
        }
        oidTable.getColumnModel().getColumn(CustomOIDTableModel.Columns.Type.ordinal()).setCellEditor(new DefaultCellEditor(typeCombo));
        oidTable.getColumnModel().getColumn(CustomOIDTableModel.Columns.Type.ordinal()).setCellRenderer(new DefaultTableCellRenderer() {

            public void setValue(Object value) {
                setText((value == null) ? "" : ((SNMPType) value).label);
            }
        });

        /*
        setDisplayCombo = new JComboBox();
        for ( SetDisplay.Style type : SetDisplay.Style.values() )
        setDisplayCombo.addItem(type.toString());
        oidTable.getColumnModel().getColumn(CustomOIDTableModel.Columns.Style.ordinal()).setCellEditor(new DefaultCellEditor(setDisplayCombo));
        oidTable.getColumnModel().getColumn(CustomOIDTableModel.Columns.Style.ordinal()).setCellRenderer(new DefaultTableCellRenderer() {
        public void setValue(Object value) {
        //setText( (value == null) ? "" : displayTypes[((Integer)value).intValue()] );
        setText( (value == null) ? "" : ((SetDisplay.Style)value).toString() );
        }
        });
         */

        //oidTable.getColumnModel().getColumn(CustomOIDTableModel.Columns.Color.ordinal()).setCellEditor(new ColorCellEditor());
        //oidTable.getColumnModel().getColumn(CustomOIDTableModel.Columns.Color.ordinal()).setCellRenderer(new ColorCellRenderer());

        oidTable.getColumnModel().getColumn(CustomOIDTableModel.Columns.Label.ordinal()).setPreferredWidth(140);
        oidTable.getColumnModel().getColumn(CustomOIDTableModel.Columns.OID.ordinal()).setPreferredWidth(220);
        oidTable.getColumnModel().getColumn(CustomOIDTableModel.Columns.Type.ordinal()).setPreferredWidth(120);
        oidTable.getColumnModel().getColumn(CustomOIDTableModel.Columns.Description.ordinal()).setPreferredWidth(220);
    //oidTable.getColumnModel().getColumn(CustomOIDTableModel.Columns.Style.ordinal()).setPreferredWidth(60);
    //oidTable.getColumnModel().getColumn(CustomOIDTableModel.Columns.Color.ordinal()).setPreferredWidth(60);
    //} catch ( DatabaseException e ) {
    //    ErrorHandler.modalError(null, "Please ensure that database is running and accessible",
    //            "Error accessing database", e);
    //}

    }

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        ((CustomOIDTableModel) oidTable.getModel()).addOID();
    }//GEN-LAST:event_addButtonActionPerformed

    private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeButtonActionPerformed
        if (oidTable.getSelectedRowCount() == 1) {
            ((CustomOIDTableModel) oidTable.getModel()).removeOID(oidTable.getSelectedRow());
        }
    }//GEN-LAST:event_removeButtonActionPerformed

    private void graphButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_graphButtonActionPerformed
        String deviceName;
        Device device;
        int ifIndex;
        String portString;
        Vector<CustomOID> oids;
        CustomOIDSNMP snmp;
        CustomOIDCollector collector;
        DataSets data;
        TimeSeriesContext context;
        DataGraph graphFrame;


        if ( oidTable.getRowCount() == 0 ) {
            ErrorHandler.modalError(this, "Please add at least one OID to the table",
                    "No OIDs added");
            return;
        }
        

        deviceName = deviceField.getText();
        oids = ((CustomOIDTableModel)oidTable.getModel()).getCustomOIDVector();
        try {
            device = new Device(deviceName, communityField.getText(), null, false);
            snmp = new CustomOIDSNMP(device);
            collector = new CustomOIDCollector(snmp, Integer.parseInt(pollField.getText()), unitsField.getText(), ((CustomOIDTableModel) oidTable.getModel()).getCustomOIDVector());
            data = new DataSets(DataType.OID, collector, device, Integer.parseInt(pollField.getText()), null, 0, null, storeDataCheckBox.isSelected());
            for (int i = 0; i < oids.size(); i++) {
                data.addSet(oids.elementAt(i).label);
            }
            context = new TimeSeriesContext(data);
            graphFrame = new DataGraph(context);
        } catch (DBException e) {
            ErrorHandler.modalError(null, "Please ensure that database is running and accessible",
                    "Error opening database connection", e);
        } catch (UnknownHostException e) {
            ErrorHandler.modalError(null, "Please ensure that device name \"" + deviceName + "\" is valid",
                    "Unknown host " + deviceName, e);
        } catch (SNMPTypeException e) {
            ErrorHandler.modalError(null, "Please ensure that OID values and types are correct",
                    "Cannot access OID on " + deviceName, e);
        } catch (SNMPException e) {
            ErrorHandler.modalError(null, "Please ensure that device name and community string are correct",
                    "Cannot access SNMP service on device " + deviceName, e);
        } 

    }//GEN-LAST:event_graphButtonActionPerformed

private void pollFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pollFieldActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_pollFieldActionPerformed
    
    /*
    protected void displayTemplates() {
        DatabaseAccess dba;
        
        try {
            dba = new DatabaseAccess();
            templatesList = new JList(dba.loadCustomOIDTemplates());
            templatesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            templatesPane.setViewportView((Component)templatesList);
        } catch ( DatabaseException e ) {
            ErrorHandler.modalError(null, "Please ensure that database is running and accessible",
                    "Error accessing database", e);
        }
    }
    */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JTextField communityField;
    private javax.swing.JLabel communityLabel;
    private javax.swing.JTextField deviceField;
    private javax.swing.JLabel deviceLabel;
    private javax.swing.JScrollPane editPane;
    private javax.swing.JButton graphButton;
    private javax.swing.JTextField pollField;
    private javax.swing.JLabel pollLabel;
    private javax.swing.JLabel pollUnitsLabel;
    private javax.swing.JButton removeButton;
    private javax.swing.JCheckBox storeDataCheckBox;
    private javax.swing.JTextField unitsField;
    private javax.swing.JLabel unitsLabel;
    // End of variables declaration//GEN-END:variables
    
}
