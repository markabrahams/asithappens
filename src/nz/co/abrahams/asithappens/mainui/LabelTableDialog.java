/*
 * LabelTableDialog.java
 *
 * Created on 17 December 2005, 10:39
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

import nz.co.abrahams.asithappens.storage.DataSets;
import nz.co.abrahams.asithappens.cartgraph.DataGraph;
import nz.co.abrahams.asithappens.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import org.apache.log4j.Logger;

/**
 * Produces a table of text labels for the given graph.  The labels can then be
 * manipulated through this dialog.
 *
 * @author mark
 */
public class LabelTableDialog extends JDialog {
    
    protected Logger logger;
    
    protected DataGraph graph;
    
    protected DataSets data;
    
    protected JScrollPane labelsPane;
    
    /**
     * Creates a new LabelTableDialog.
     *
     * @param graph  the graph for the labels
     * @param data   the data set containing the labels
     */
    public LabelTableDialog(DataGraph graph, DataSets data) {
        super(graph, true);
        logger = Logger.getLogger(this.getClass().getName());
        this.graph = graph;
        this.data = data;
        initComponents();
    }
    
    /** Lays out the GUI components. */
    protected void initComponents() {
        GridBagConstraints gridBagConstraints;
        JList labelsList;
        JLabel tableTitle;
        JButton deleteButton;
        
        getContentPane().setLayout(new GridBagLayout());
        
        tableTitle = new JLabel("Label list");
        labelsPane = new JScrollPane();
        labelsList = new JList(data.getLabels().getLabelStrings());
        labelsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        labelsPane.setViewportView((java.awt.Component)labelsList);
        deleteButton = new JButton("Delete label");
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });
        
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        getContentPane().add(tableTitle, gridBagConstraints);
        
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        labelsPane.setMinimumSize(new Dimension(200, 100));
        labelsPane.setPreferredSize(new Dimension(200, 100));
        getContentPane().add(labelsPane, gridBagConstraints);
        
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridheight = 1;
        //gridBagConstraints.fill = GridBagConstraints.BOTH;
        getContentPane().add(deleteButton, gridBagConstraints);
        
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                closeFrame(event);
            }
        });        
        
        //setMinimumSize(new Dimension(200, 200));
        //setPreferredSize(new Dimension(200, 200));        
        setTitle("Labels for " + data.getTitle());
        pack();
    }
    
    /** Deletes the selected label. */
    public void deleteButtonActionPerformed(ActionEvent evt) {
        int labelIndex;
        JList labelsList;
        
        labelIndex = getLabelIndex();
        if ( labelIndex == -1 )
            return;
        
        data.getLabels().deleteLabel(labelIndex);
        
        labelsList = new JList(data.getLabels().getLabelStrings());
        labelsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        labelsPane.setViewportView((java.awt.Component)labelsList);
        
        graph.repaint();
    }
    
    /** @return the indes of the selected label */
    protected int getLabelIndex() {
        int selection;
        JOptionPane dialog;
        
        selection = ((JList)(labelsPane.getViewport().getView())).getSelectedIndex();
        if ( selection == -1 ) {
            dialog = new JOptionPane();
            dialog.showMessageDialog(this, "Please select a label", "No label selected", JOptionPane.ERROR_MESSAGE);
            return -1;
        }
        
        return selection;
    }
    
    /** Disposes the window on close. */
    public void closeFrame(WindowEvent event) {
        dispose();
    }
}
