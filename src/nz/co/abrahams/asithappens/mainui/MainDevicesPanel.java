/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nz.co.abrahams.asithappens.mainui;

import nz.co.abrahams.asithappens.core.DAOFactory;
import nz.co.abrahams.asithappens.core.DBException;
import nz.co.abrahams.asithappens.uiutil.ErrorHandler;
import nz.co.abrahams.asithappens.storage.DeviceDAO;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

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

    public void displayDevices() {
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
        dialog = new EditDeviceDialog((JFrame)(SwingUtilities.getWindowAncestor(this)), true);
        dialog.setVisible(true);
    }
}
