/*
 * SummaryStatisticsPanel.java
 *
 * Created on 18 January 2010, 22:35
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
import org.apache.log4j.Logger;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.util.Vector;
import java.text.DecimalFormat;

/**
 * A JFrame that displays summary statistics information for sets of data.
 *
 * @author mark
 */
public class SummaryStatisticsPanel extends JPanel {

    /** Logging provider */
    private static Logger logger = Logger.getLogger(SummaryStatisticsPanel.class);

    /** Format for statistics */
    protected static final DecimalFormat decimalFormat = new DecimalFormat("#,###");

    private enum Columns {
        SET("Set", 50),
        NAME("Name", 100),
        SAMPLES("Samples", 70),
        FIRST("Fist sample", 170),
        LAST("Last sample", 170),
        MINIMUM("Minimum", 100),
        MAXIMUM("Maximum", 100),
        AVERAGE("Average", 100);

        String heading;

        int width;

        Columns(String heading, int width) {
            this.heading = heading;
            this.width = width;
        }
    }

    /** Table headings */
    //protected static final String[] HEADINGS = {"Set", "Name", "Samples", "First sample", "Last sample", "Minimum", "Maximum", "Average"};
    /** Table default column widths */
    //private static final int[] COLUMN_WIDTHS = {50, 100, 70, 170, 170, 100, 100, 100};
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
    /** Statistics updater thread */
    private SummaryStatisticsUpdaterThread updaterThread;

    /** Creates a new SummaryStatisticsFrame */
    public SummaryStatisticsPanel(DataSets data, double firstTime, double lastTime) {
        this.data = data;
        this.firstTime = (long) firstTime;
        this.lastTime = (long) lastTime;
        sets = data.getNumberOfSets();
        headings = new Vector();
        for (int i = 0; i < Columns.values().length ; i++) {
            headings.add(Columns.values()[i].heading);
        }

        /*
        tableData = new Vector();
        for (int set = 0; set < sets; set++) {
            SummaryStatistics statistics;
            Vector row;

            statistics = data.getSummaryStatistics(set);
            row = new Vector();
            row.add(Integer.toString(set));
            row.add(data.getHeadings().getHeadings()[set]);
            row.add(Integer.toString(statistics.samples));
            if (statistics.firstTime != -1) {
                row.add(DataPoint.formatDateTime(statistics.firstTime));
            } else {
                row.add("-");
            }
            if (statistics.lastTime != -1) {
                row.add(DataPoint.formatDateTime(statistics.lastTime));
            } else {
                row.add("-");
            }
            row.add(decimalFormat.format(statistics.minimum));
            row.add(decimalFormat.format(statistics.maximum));
            row.add(decimalFormat.format(statistics.average));
            tableData.add(row);
        }
         */
        createTableData();
        initComponents();
        updaterThread = new SummaryStatisticsUpdaterThread(this);
        updaterThread.start();
    }

    /**
     * Lays out the GUI components of the table.
     */
    protected void initComponents() {
        GridBagConstraints gridBagConstraints;

        setLayout(new GridBagLayout());

        dataPane = new JScrollPane();
        table = new JTable(new DataTableModel(tableData, headings));
        table.setAutoCreateRowSorter(true);
        for (int i = 0; i < Columns.values().length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(Columns.values()[i].width);
        }
        dataPane.setViewportView(table);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(dataPane, gridBagConstraints);

    }

    public DataSets getData() {
        return data;
    }

    public void createTableData() {
        tableData = new Vector();
        for (int set = 0; set < sets; set++) {
            addTableRow(set);
        }

    }

    public void updateTable() {
        SummaryStatistics statistics;

        // Add any extra rows for new data sets
        for (int set = table.getModel().getRowCount(); set < data.getDataSetCount(); set++) {
            addTableRow(set);
        }
        // Update existing rows
        for (int set = 0 ; set < data.getDataSetCount() ; set++) {
            statistics = data.getSummaryStatistics(set);
            table.getModel().setValueAt(statistics.samples, set, Columns.SAMPLES.ordinal());
            table.getModel().setValueAt(TextFormatter.formatDateTime(statistics.firstTime), set, Columns.FIRST.ordinal());
            table.getModel().setValueAt(TextFormatter.formatDateTime(statistics.lastTime), set, Columns.LAST.ordinal());
            table.getModel().setValueAt(TextFormatter.formatValue(statistics.minimum), set, Columns.MINIMUM.ordinal());
            table.getModel().setValueAt(TextFormatter.formatValue(statistics.maximum), set, Columns.MAXIMUM.ordinal());
            table.getModel().setValueAt(TextFormatter.formatValue(statistics.average), set, Columns.AVERAGE.ordinal());
        }
    }

    private void addTableRow(int set) {
        SummaryStatistics statistics;
        Vector row;

        statistics = data.getSummaryStatistics(set);
        row = new Vector();
        row.add(Integer.toString(set));
        row.add(data.getHeadings().getHeadings()[set]);
        row.add(Integer.toString(statistics.samples));
        if (statistics.firstTime != -1) {
            row.add(DataPoint.formatDateTime(statistics.firstTime));
        } else {
            row.add("-");
        }
        if (statistics.lastTime != -1) {
            row.add(DataPoint.formatDateTime(statistics.lastTime));
        } else {
            row.add("-");
        }
        row.add(TextFormatter.formatValue(statistics.minimum));
        row.add(TextFormatter.formatValue(statistics.maximum));
        row.add(TextFormatter.formatValue(statistics.average));
        tableData.add(row);
    }

    public void stopUpdating() {
        updaterThread.stopUpdating();
    }
}
