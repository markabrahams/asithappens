/*
 * SummaryStatisticsFrame.java
 *
 * Created on 12 August 2006, 23:45
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

import nz.co.abrahams.asithappens.storage.SummaryStatistics;
import nz.co.abrahams.asithappens.storage.DataSets;
import nz.co.abrahams.asithappens.storage.DataPoint;
import nz.co.abrahams.asithappens.core.Configuration;
import nz.co.abrahams.asithappens.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;

/**
 * A JFrame that displays summary statistics information for sets of data.
 *
 * @author mark
 */
public class SummaryStatisticsFrame extends JFrame {
    
    /** Format for statistics */
    protected static final DecimalFormat decimalFormat = new DecimalFormat("#,###");
    
    /** Table headings */
    protected static final String[] HEADINGS = { "Set", "Name", "Samples", "First sample", "Last sample", "Minimum", "Maximum", "Average" };
    
    /** Source data */
    protected DataSets data;
    
    /** Scrollable data pane */
    protected JScrollPane dataPane;

    /** Scrollable data panel */
    protected JPanel dataPanel;

    /** Summary table */
    protected JTable table;

    /** Text of table column headings */
    protected Vector headings;

    /** Text of table data */
    protected Vector tableData;

    /** Number of sets */
    protected int sets;

    /** Beginning time of summary period */
    protected long firstTime;

    /** End time of summary period */
    protected long lastTime;
    
    /** Creates a new SummaryStatisticsFrame */
    public SummaryStatisticsFrame(DataSets data, double firstTime, double lastTime) {
        this.data = data;
        this.firstTime = (long)firstTime;
        this.lastTime = (long)lastTime;
        sets = data.getNumberOfSets();
        headings = new Vector();
        for (int i = 0; i < HEADINGS.length; i++ ) {
            headings.add(HEADINGS[i]);
        }
        tableData = new Vector();
        for (int set = 0; set < sets; set++ ) {
            SummaryStatistics statistics;
            Vector row;
            
            statistics = data.getSummaryStatistics(set);
            row = new Vector();
            row.add(Integer.toString(set));
            row.add(data.getHeadings().getHeadings()[set]);
            row.add(Integer.toString(statistics.samples));
            if ( statistics.firstTime != -1 )
                row.add(DataPoint.formatDateTime(statistics.firstTime));
            else
                row.add("-");
            if ( statistics.lastTime != -1 )
                row.add(DataPoint.formatDateTime(statistics.lastTime));
            else
                row.add("-");
            row.add(decimalFormat.format(statistics.minimum));
            row.add(decimalFormat.format(statistics.maximum));
            row.add(decimalFormat.format(statistics.average));
            tableData.add(row);
        }
        initComponents();
    }
    
    /**
     * Lays out the GUI components of the table.
     */
    protected void initComponents() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(Configuration.FRAME_ICON));
        
        dataPane = new JScrollPane();
        table = new JTable(new DataTableModel(tableData, headings));
        
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                closeFrame(event);
            }
        });
        
        dataPane.setViewportView(table);
        add(dataPane);
        
        setTitle("Summary statistics (" + DataPoint.formatDateTime(firstTime) + " to " +
                DataPoint.formatDateTime(lastTime) + "): " + data.getTitle());
        
        pack();
    }
    
    /** Closes the table frame. */
    private void closeFrame(WindowEvent event) {
        dispose();
    }
    
}
