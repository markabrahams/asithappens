/*
 * DataGraph.java
 *
 * Created on 28 May 2004, 20:42
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
package nz.co.abrahams.asithappens.cartgraph;

import nz.co.abrahams.asithappens.storage.Layout;
import nz.co.abrahams.asithappens.core.DAOFactory;
import nz.co.abrahams.asithappens.core.DAOCreationException;
import nz.co.abrahams.asithappens.core.DBException;
import nz.co.abrahams.asithappens.core.Configuration;
import nz.co.abrahams.asithappens.mainui.SummaryStatisticsFrame;
import nz.co.abrahams.asithappens.mainui.SummaryStatisticsPanel;
import nz.co.abrahams.asithappens.mainui.LabelTableDialog;
import nz.co.abrahams.asithappens.mainui.DataTableFrame;
import nz.co.abrahams.asithappens.mainui.DataTablePanel;
import nz.co.abrahams.asithappens.uiutil.ButtonTabComponent;
import nz.co.abrahams.asithappens.sdn.SDNContainerPanel;

import java.awt.BorderLayout;
import java.awt.*;
import java.awt.event.*;
import java.net.UnknownHostException;
import javax.swing.*;
import java.util.Vector;
import org.apache.log4j.Logger;

/**
 * A JFrame that graphically displays the information contained within a
 * DataSets object.  A DataGraph has a single TimeSeriesContext instance that
 * describes how to transform the DataSets into a graphical representation.
 * <p>
 * A DataGraph contains these visual components:
 * <ol>
 * <li>An x-axis containing time values
 * <li>A y-axis containing data values
 * <li>A graph panel containing the graphical representation of the data sets
 * <li>A legend for the data sets displayed
 * </ol>
 *
 * @author  mark
 */
public class DataGraph extends JFrame {

    /** Minimum size of the graph window */
    protected static final Dimension MINIMUM_SIZE = new Dimension(150, 150);
    /** Logging provider */
    protected static Logger logger = Logger.getLogger(DataGraph.class);
    /** Tabbed pane for display panels */
    protected JTabbedPane tabbedPane;
    /** Graph tab */
    protected TimeSeriesContainerPanel tsContainerPanel;
    /** Statistics tab */
    protected SummaryStatisticsPanel statsPanel;
    /** Data tab */
    protected DataTablePanel dataPanel;
    /** Graph ID */
    protected int graphID;
    /** Thread that performs redraws for collector updates */
    //protected Thread updaterThread;
    /** Graphing context including data and display logic */
    protected TimeSeriesContext context;
    /** Options chooser for this graph */
    protected TimeSeriesOptionsDialog options;

    public class CloseTabAction implements ActionListener {

        private DataGraph graph;

        public CloseTabAction(DataGraph graph) {
            this.graph = graph;
        }

        public void actionPerformed(ActionEvent event) {
            //logger.debug("Tab count: " + tabbedPane.getTabCount());
            if (tabbedPane.getTabCount() == 2) {
                tabbedPane.remove(tsContainerPanel);
                remove(tabbedPane);
                add(tsContainerPanel);
                tsContainerPanel.setFocusable(true);
                tsContainerPanel.requestFocusInWindow();
            }
        }
    }

    /** Creates a new DataGraph. */
    public DataGraph(TimeSeriesContext context) throws DBException, UnknownHostException {
        this(context, null);
    }

    /** Creates a new DataGraph. */
    public DataGraph(TimeSeriesContext context, Rectangle dimensions) throws DBException, UnknownHostException {
        try {
            this.context = context;
            //showTrim = true;

            initComponents();

            if (dimensions != null) {
                setBounds(dimensions);
            }
            setAlwaysOnTop(context.getStickyWindow());
            setMinimumSize(MINIMUM_SIZE);

            addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent evt) {
                    exitForm();
                }
            });

            //addMouseListener(new PopupListener(popupMenu));
            //addMouseListener(new PopupMouseAdapter(this));

            if (context.getData().isCollector() && context.getData().isStoring()) {
                store();
            }

            Layout.addGraphToCurrent(this);

            setVisible(true);
        } catch (DAOCreationException e) {
            throw new DBException("Error: DAOCreationException", e);
        }
        //summarizeRescaleRepaint();
    }

    /*
    public void setGraphID(int id) {
    graphID = id;
    }
     */
    /**
     * Lays out the GUI components of the graph.  This includes the layout
     * of the following four components:
     * <ul>
     * <li>XAxisPanel
     * <li>YAxisPanel
     * <li>MultiGraphPanel
     * <li>LegendPanel
     * </ul>
     */
    protected void initComponents() throws DBException, UnknownHostException {
        //GridBagConstraints gridBagConstraints;
        JMenuItem menuItem;
        PopupMouseAdapter popupListener;

        getContentPane().setLayout(new BorderLayout());
        setIconImage(Toolkit.getDefaultToolkit().getImage(Configuration.FRAME_ICON));

        tabbedPane = new JTabbedPane();
        tabbedPane.setFocusable(false);

        popupListener = new PopupMouseAdapter(this);
        tsContainerPanel = new SDNContainerPanel(context);
        tsContainerPanel.getGraphPanel().addMouseListener(new PopupMouseAdapter(this));
        tsContainerPanel.addMouseListener(new PopupMouseAdapter(this));
        tsContainerPanel.setFocusable(true);
        tsContainerPanel.requestFocusInWindow();
        tsContainerPanel.addKeyListener(new TimeSeriesKeyAdapter(this));
        add(tsContainerPanel);

        //tabbedPane.addTab("Graph", null, tsContainerPanel, "Graph");
        //add(tabbedPane);

        addWindowListener(new java.awt.event.WindowAdapter() {

            @Override
            public void windowClosing(java.awt.event.WindowEvent event) {
                exitForm();
            }
        });

        if (context.getData().getTitle() != null) {
            setTitle(context.getData().getTitle());
        } else {
            setTitle(context.getData().getDescription());
        }

        pack();
    }

    public TimeSeriesContext getContext() {
        return context;
    }

    /** Summarizes current data set, rescales x and y axes, and redraws the graph. */
    protected void summarizeRescaleRepaint() {
        tsContainerPanel.summarizeRescaleRepaint();
    }

    /** Displays the options chooser modal dialog. */
    protected void displayOptionsChooser() {
        options = new TimeSeriesOptionsDialog(this, context);
        options.setVisible(true);
    }

    /** Displays the data tables. */
    protected void displayDataTable() {
        dataPanel = new DataTablePanel(context.getData());
        addTabPanel("Data", dataPanel);
        /*
        tabbedPane.addTab("Data", null, dataPanel, "Data");
        tabbedPane.setTabComponentAt(tabbedPane.getTabCount() - 1, new ButtonTabComponent(tabbedPane));
        tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
         */

    }

    /** Displays the table of labels. */
    protected void displayLabelTable() {
        LabelTableDialog frame = new LabelTableDialog(this, context.getData());
        frame.setVisible(true);
    }

    /** Displays the table of summary statistics. */
    protected void displayStatisticsTable() {
        // Need a new panel
        if (statsPanel == null || !isStatisticsPanelVisible(statsPanel)) {
            statsPanel = new SummaryStatisticsPanel(context.getData(),
                    context.getFirstDisplayedTime(), context.getLastDisplayedTime());
            addTabPanel("Statistics", statsPanel);

        // Switch focus to existing statistics panel
        } else {
            tabbedPane.setSelectedComponent(statsPanel);
        }

    }

    private boolean isStatisticsPanelVisible(JPanel panel) {
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            if (tabbedPane.getComponentAt(i) == statsPanel) {
                return true;
            }
        }
        return false;
    }

    private void addTabPanel(String name, JPanel panel) {
        // Transition from no tabbed pane to tabbed pane if need be
        if (tabbedPane.getTabCount() == 0) {
            remove(tsContainerPanel);
            tabbedPane.addTab("Graph", null, tsContainerPanel, "Graph");
            add(tabbedPane);
        }
        tabbedPane.addTab(name, null, panel, name);
        tabbedPane.setTabComponentAt(tabbedPane.getTabCount() - 1, new ButtonTabComponent(tabbedPane));
        tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
        ((ButtonTabComponent)(tabbedPane.getTabComponentAt(tabbedPane.getTabCount() - 1))).addButtonActionListener((ActionListener)(new CloseTabAction(this)));
    }

    public int getGraphID() {
        return graphID;
    }

    /** Closes the graph frame. */
    public void closeGraph() {
        context.getData().stopCollecting();
        Layout.removeGraphFromCurrent(this);
        dispose();
    }

    /** Exits the form. */
    protected void exitForm() {
        closeGraph();
    }

    public void store() throws DBException, UnknownHostException, DAOCreationException {
        store(null);
    }

    public void store(String layout) throws DBException, UnknownHostException, DAOCreationException {
        DataGraphDAO graphDAO;

        graphDAO = DAOFactory.getDataGraphDAO();
        graphID = graphDAO.createGraph(null, this);
        graphDAO.closeConnection();
    }
}
