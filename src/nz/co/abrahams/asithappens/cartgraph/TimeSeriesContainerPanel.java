/*
 * TimeSeriesContainerPanel.java
 *
 * Created on 16 January 2010, 08:45
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
import nz.co.abrahams.asithappens.storage.DataSets;
import nz.co.abrahams.asithappens.core.DAOFactory;
import nz.co.abrahams.asithappens.core.DAOCreationException;
import nz.co.abrahams.asithappens.core.DBException;
import nz.co.abrahams.asithappens.core.Configuration;
import nz.co.abrahams.asithappens.mainui.SummaryStatisticsFrame;
import nz.co.abrahams.asithappens.mainui.LabelTableDialog;
import nz.co.abrahams.asithappens.mainui.DataTableFrame;
import java.awt.*;
import java.awt.event.*;
import java.net.UnknownHostException;
import javax.swing.*;
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
public class TimeSeriesContainerPanel extends JPanel implements Runnable {
    
    /** Minimum size of the graph window */
    protected static final Dimension MINIMUM_SIZE = new Dimension(150, 150);
    
    /** Logging provider */
    protected static Logger logger = Logger.getLogger(TimeSeriesContainerPanel.class);
    
    /** Graph ID */
    //protected int graphID;
    
    /** Thread that performs redraws for collector updates */
    protected Thread updaterThread;
    
    /** Graphing context including data and display logic */
    protected TimeSeriesContext context;
    
    /** Switch for showing graph trim */
    protected boolean showTrim;
    
    /** The panel where graph data is displayed */
    protected TimeSeriesDataPanel graphPanel;

    /** The panel containing the x-axis */
    protected TimeSeriesXAxisPanel xAxisPanel;

    /** The panel containing the y-axis */
    protected TimeSeriesYAxisPanel yAxisPanel;

    /** The label for the x-axis */
    protected JLabel xAxisLabel;

    /** The label for the y-axis */
    protected JLabel yAxisLabel;

    /** The panel containing the legend */
    protected TimeSeriesLegendPanel legendPanel;
    
    /** Constraints for repacking x-axis label */
    protected GridBagConstraints xAxisLabelConstraints;

    /** Constraints for repacking y-axis label */
    protected GridBagConstraints yAxisLabelConstraints;

    /** Constraints for repacking legend panel */
    protected GridBagConstraints legendPanelConstraints;

    /** Constraints for repacking graph panel */
    protected GridBagConstraints graphPanelConstraints;
    
    
    /** Creates a new DataGraph. */
    /*
    public TimeSeriesContainerPanel(TimeSeriesContext context) {
        this(context, null);
    }
    */

    /** Creates a new DataGraph. */
    public TimeSeriesContainerPanel(TimeSeriesContext context, Rectangle dimensions) {
        //try {
        this.context = context;
        showTrim = true;
        
        initComponents();
        /*
        if ( dimensions != null )
            setBounds(dimensions);
        //setAlwaysOnTop(context.getStickyWindow());
        setMinimumSize(MINIMUM_SIZE);
         */

        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent evt) {
                summarizeRescaleRepaint();
            }
        });


        /*
        addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent evt) {
                if ( evt.getKeyChar() == 'o' || evt.getKeyChar() == 'O' )
                    displayOptionsChooser();
                else if ( evt.getKeyChar() == 'd' || evt.getKeyChar() == 'D' )
                    displayDataTable();
                else if ( evt.getKeyChar() == 'l' || evt.getKeyChar() == 'L' )
                    displayLabelTable();
                else if ( evt.getKeyChar() == 's' || evt.getKeyChar() == 'S' )
                    displayStatisticsTable();
                
            }
        });
        */


        //addMouseListener(new PopupMouseAdapter(this));

        /*
        if ( context.getData().isCollector() && context.getData().isStoring() )
            store();
        */

        //Layout.addGraphToCurrent(this);

        if ( context.getData().isCollector() ) {
            startUpdaterThread();
        }

        //setVisible(true);
        /*
        } catch (DAOCreationException e) {
            throw new DBException("Error: DAOCreationException", e);
        }
         */
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
     * <li>TimeSeriesXAxisPanel
     * <li>TimeSeriesYAxisPanel
     * <li>TimeSeriesDataPanel
     * <li>TimeSeriesLegendPanel
     * </ul>
     */
    protected void initComponents() {
        GridBagConstraints gridBagConstraints;
        JMenuItem menuItem;
        
        setLayout(new GridBagLayout());

        yAxisLabel = new JLabel();
        yAxisLabel.setText(context.getData().getValueUnits());
        yAxisLabelConstraints = new GridBagConstraints();
        yAxisLabelConstraints.gridx = 0;
        yAxisLabelConstraints.gridy = 0;
        add(yAxisLabel, yAxisLabelConstraints);
        
        yAxisPanel = new TimeSeriesYAxisPanel(context);
        yAxisPanel.setMinimumSize(new Dimension(50, 10));
        yAxisPanel.setPreferredSize(new Dimension(50, 10));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        add(yAxisPanel, gridBagConstraints);
        
        graphPanel = new TimeSeriesDataPanel(context);
        graphPanel.setMinimumSize(new Dimension(100, 100));
        graphPanel.setPreferredSize(new Dimension(200, 200));
        graphPanelConstraints = new GridBagConstraints();
        graphPanelConstraints.gridx = 2;
        graphPanelConstraints.gridy = 0;
        graphPanelConstraints.fill = GridBagConstraints.BOTH;
        graphPanelConstraints.weightx = 1.0;
        graphPanelConstraints.weighty = 1.0;
        add(graphPanel, graphPanelConstraints);
        
        xAxisPanel = new TimeSeriesXAxisPanel(context);
        xAxisPanel.setMinimumSize(new Dimension(10, 30));
        xAxisPanel.setPreferredSize(new Dimension(10, 30));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        add(xAxisPanel, gridBagConstraints);
        
        xAxisLabel = new JLabel();
        xAxisLabel.setText("time");
        xAxisLabelConstraints = new java.awt.GridBagConstraints();
        xAxisLabelConstraints.gridx = 2;
        xAxisLabelConstraints.gridy = 2;
        add(xAxisLabel, xAxisLabelConstraints);
        
        legendPanel = new TimeSeriesLegendPanel(context);
        if ( context.bottomLeftLegend() ) {
            legendPanelConstraints = new GridBagConstraints();
            legendPanelConstraints.gridx = 0;
            legendPanelConstraints.gridy = 1;
            legendPanelConstraints.gridwidth = 2;
            legendPanelConstraints.gridheight = 2;
            legendPanelConstraints.fill = GridBagConstraints.BOTH;
            add(legendPanel, legendPanelConstraints);
            
        } else {
            legendPanel.setMinimumSize(new java.awt.Dimension(20, 20));
            legendPanel.setPreferredSize(new java.awt.Dimension(20, 20));
            legendPanelConstraints = new GridBagConstraints();
            legendPanelConstraints.gridx = 3;
            legendPanelConstraints.gridy = 0;
            legendPanelConstraints.gridwidth = 1;
            legendPanelConstraints.gridheight = 2;
            legendPanelConstraints.fill = GridBagConstraints.BOTH;
            add(legendPanel, legendPanelConstraints);
        }
                
    }

    public TimeSeriesDataPanel getGraphPanel() {
        return graphPanel;
    }

    /**
     * Starts the updater thread.  Used for collector graphs when
     * any new data is collected so that this data is displayed immediately.
     */
    public void startUpdaterThread() {
        
        logger.debug("Attempting to start updater thread");
        if ( context.getData().isCollector() && updaterThread == null ) {
            updaterThread = new Thread(this, "AIH-Updater");
            updaterThread.start();
        } else if ( ! context.getData().isCollector() ) {
            logger.warn("This is not a collector");
        } else if ( updaterThread != null ) {
            logger.warn("Collector thread already started");
        }
    }
    
    /**
     * Run method for graph updater thread.  Used for collector graphs when
     * any new data is collected so that this data is displayed immediately.
     */
    public void run() {
        while ( context.getData().isCollecting() ) {
            try {
                synchronized(context.getData()) {
                    context.getData().wait();
                    java.awt.EventQueue.invokeLater(new Runnable() {
                        public void run() {
                            summarizeRescaleRepaint();
                        }
                    });
                    //summarizeRescaleRepaint();
                }
            } catch (InterruptedException e) {
                logger.error("Updater thread interrupted from wait");
            }
        }
    }
    
    public TimeSeriesContext getContext() {
        return context;
    }
    
    /** Summarizes current data set, rescales x and y axes, and redraws the graph. */
    protected void summarizeRescaleRepaint() {
        if ( showTrim != context.getShowTrim() )
            repackGraph();
        context.setPanelWidth(graphPanel.getWidth());
        context.setPanelHeight(graphPanel.getHeight());
        //logger.debug("Setting panel width to " + graphPanel.getWidth() + " and panel height to " + graphPanel.getHeight());
        context.generateSummaryData();
        repaint();
    }
    
    /** Displays the options chooser modal dialog. */
    /*
    protected void displayOptionsChooser() {
        options = new GraphOptionsDialog(this, context);
        options.setVisible(true);
    }
    */

    /** Displays the data tables. */
    /*
    protected void displayDataTable() {
        DataTableFrame frame = new DataTableFrame(context.getData());
        frame.setVisible(true);
    }
    */

    /** Displays the table of labels. */
    /*
    protected void displayLabelTable() {
        LabelTableDialog frame = new LabelTableDialog(this, context.getData());
        frame.setVisible(true);
    }
    */

    /** Displays the table of summary statistics. */
    /*
    protected void displayStatisticsTable() {
        SummaryStatisticsFrame frame = new SummaryStatisticsFrame(context.getData(),
                context.getFirstDisplayedTime(), context.getLastDisplayedTime());
        frame.setVisible(true);
    }
    */

    protected void repackGraph() {
        if ( ! context.getShowTrim() ) {
            showTrim = false;
            remove(xAxisLabel);
            remove(yAxisLabel);
            remove(legendPanel);
            validate();
        } else {
            showTrim = true;
            add(xAxisLabel, xAxisLabelConstraints);
            add(yAxisLabel, yAxisLabelConstraints);
            add(legendPanel, legendPanelConstraints);
            validate();
        }
    }

    public boolean isDeletable() {
        return false;
    }

    /*
    public int getGraphID() {
        return graphID;
    }
    */

    /*
    public void store() throws DBException, UnknownHostException, DAOCreationException {
        store(null);
    }
    */

    /*
    public void store(String layout) throws DBException, UnknownHostException, DAOCreationException {
        DataGraphDAO graphDAO;
        
        graphDAO = DAOFactory.getDataGraphDAO();
        graphID = graphDAO.createGraph(null, this);
        graphDAO.closeConnection();
    }
    */

}
