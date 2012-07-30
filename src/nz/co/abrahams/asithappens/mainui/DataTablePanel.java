/*
 * DataTablePanel.java
 *
 * Created on 19 January 2010, 05:00
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
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.*;
import java.text.Format;
import java.text.DecimalFormat;

/**
 * A JFrame that displays data set information in a collection of tables.
 *
 * @author mark
 */
public class DataTablePanel extends JPanel {
    
    public class FormatRenderer extends DefaultTableCellRenderer {
        
        private Format formatter;
        
        public FormatRenderer(Format formatter) {
            if ( formatter == null )
                throw new NullPointerException();
            this.formatter = formatter;
        }
        
        protected void setValue(Object object) {
            setText( object == null ? "" : formatter.format(object) );
        }
    }
    
    /** Value number format */
    protected static final DecimalFormat valueFormat = new DecimalFormat("#,###");
    
    /** Table headings */
    protected static final String[] HEADINGS = { "Time", "Value" };
    
    /** Source data */
    protected DataSets data;
    
    /** Scrollable data pane */
    protected JScrollPane dataPane;

    /** Scrollable data panel */
    protected JPanel dataPanel;

    /** Panes containing scrollable data */
    protected JScrollPane[] scrollPanes;

    /** Headings for each data set */
    protected JLabel[] headingNumberLabels;

    /** Headings for each data set */
    protected JLabel[] headingLabels;

    /** Data set tables */
    protected JTable[] tables;

    /** Text of table column headings */
    protected Vector headings;

    /** Number of sets */
    protected int sets;
    
    /** Creates a new DataTableFrame */
    public DataTablePanel(DataSets data) {
        this.data = data;
        sets = data.getNumberOfSets();
        headingNumberLabels = new JLabel[sets];
        headingLabels = new JLabel[sets];
        scrollPanes = new JScrollPane[sets];
        tables = new JTable[sets];
        headings = new Vector();
        for (int i = 0; i < HEADINGS.length; i++ ) {
            headings.add(HEADINGS[i]);
        }
        //createTables();
        initComponents();
    }
    
    /**
     * Lays out the GUI components of the table.
     */
    protected void initComponents() {
        GridBagConstraints gridBagConstraints;
        FormatRenderer renderer;

        setLayout(new GridBagLayout());
        dataPane = new JScrollPane();
        dataPanel = new JPanel();
        dataPanel.setLayout(new GridBagLayout());
        
        for (int set = 0; set < sets; set++) {
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = set;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.gridwidth = 1;
            gridBagConstraints.gridheight = 1;
            headingNumberLabels[set] = new JLabel("Set " + (set + 1));
            headingLabels[set] = new JLabel(data.getHeadings().getHeadings()[set]);
            
            dataPanel.add(headingNumberLabels[set], gridBagConstraints);
            
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = set;
            gridBagConstraints.gridy = 1;
            gridBagConstraints.gridwidth = 1;
            gridBagConstraints.gridheight = 1;
            headingLabels[set] = new JLabel(data.getHeadings().getHeadings()[set]);
            
            dataPanel.add(headingLabels[set], gridBagConstraints);
        }
        
        for (int set = 0; set < sets; set++) {
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = set;
            gridBagConstraints.gridy = 2;
            gridBagConstraints.gridwidth = 1;
            gridBagConstraints.gridheight = 1;
            gridBagConstraints.fill = GridBagConstraints.BOTH;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.weighty = 1.0;
            scrollPanes[set] = new JScrollPane();
            //scrollPanes[set].setMinimumSize(new Dimension(200, 200));
            scrollPanes[set].setPreferredSize(new Dimension(200, 200));
            dataPanel.add(scrollPanes[set], gridBagConstraints);
            tables[set] = new JTable(new DataTableModel(data.getDataSetVector(set), headings));
            renderer = new FormatRenderer(valueFormat);
            renderer.setHorizontalAlignment(SwingConstants.RIGHT);
            tables[set].getColumnModel().getColumn(1).setCellRenderer(renderer);
            //tables[set].getColumnModel().getColumn(0).setMaxWidth(150);
            //tables[set].getColumnModel().getColumn(1).setMaxWidth(150);
            scrollPanes[set].setViewportView((java.awt.Component)tables[set]);
        }
        
        dataPane.setViewportView(dataPanel);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(dataPane, gridBagConstraints);
        
    }
        
}
