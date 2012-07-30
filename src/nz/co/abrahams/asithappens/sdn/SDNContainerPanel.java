/*
 * SDNContainerPanel.java
 *
 * Created on 22 December 2007, 10:33
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
 *
 */

package nz.co.abrahams.asithappens.sdn;

import nz.co.abrahams.asithappens.core.DBException;
import nz.co.abrahams.asithappens.cartgraph.TimeSeriesContext;
import nz.co.abrahams.asithappens.cartgraph.TimeSeriesContainerPanel;
import nz.co.abrahams.asithappens.cartgraph.TimeSeriesDataPanel;
import nz.co.abrahams.asithappens.cartgraph.PopupMouseAdapter;
import nz.co.abrahams.asithappens.*;
import org.apache.log4j.Logger;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.net.UnknownHostException;
import javax.swing.SwingUtilities;

/**
 *
 * @author mark
 */
public class SDNContainerPanel extends TimeSeriesContainerPanel {
    
    //protected static final Dimension minimumSize = new Dimension(150, 150);
    
    //protected static final String[] MENU_ITEMS = { "Graph options (o)", "Delete labels (l)", "Display data (d)", "Summary statistics (s)", "Defend Your Network (n)" };

    private static Logger logger = Logger.getLogger(SDNContainerPanel.class);

    protected GameContext gameContext;
    
    protected TimeSeriesContext graphContext;
    
    protected PainterThread painterThread;
    
    protected KeyHandler keyHandlerGame;
    
    protected KeyHandlerLauncher keyHandlerLauncher;
    
    public SDNContainerPanel(TimeSeriesContext graphContext) throws DBException, UnknownHostException {
        this(graphContext, null);
    }
    
    /** Creates a new instance of SDNContainerPanel */
    public SDNContainerPanel(TimeSeriesContext graphContext, Rectangle dimensions) throws DBException, UnknownHostException {
        super(graphContext, dimensions);
        //gameContext = new GameContext(graphContext);
        //keyHandlerGame = new KeyHandler(gameContext);
        gameContext = null;
        keyHandlerGame = null;
        //addKeyListener(new KeyHandler(game));
        this.graphContext = graphContext;
        keyHandlerLauncher = new KeyHandlerLauncher(this);
        addKeyListener(keyHandlerLauncher);
        //setMinimumSize(minimumSize);
        if ( updaterThread == null )
            startUpdaterThread();
    }
    
    public void startGame() {
        // Can't start game if one is already in progress
        if ( gameContext != null && gameContext.isGameOn() )
            return;
        
        // Can't play game with no graph data
        if ( graphContext.getSummaryData() == null )
            return;
        
        gameContext = new GameContext(graphContext);
        keyHandlerGame = new KeyHandler(gameContext);
        removeKeyListener(keyHandlerLauncher);
        remove(graphPanel);
        graphPanel = new SDNDataPanel(context, gameContext);
        add(graphPanel, graphPanelConstraints);
        //setSize(GameContext.DEFAULT_WINDOW_WIDTH, GameContext.DEFAULT_WINDOW_HEIGHT);
        SwingUtilities.windowForComponent(this).setSize(GameContext.DEFAULT_WINDOW_WIDTH, GameContext.DEFAULT_WINDOW_HEIGHT);
        validate();
        addKeyListener(keyHandlerGame);
        gameContext.startGame();
        painterThread = new PainterThread(this);
        painterThread.start();
        
        synchronized(graphContext.getData()) {
            summarizeRescaleRepaint();
        }
    }
    
    /**
     * Starts the updater thread.  Used for collector graphs when
     * any new data is collected so that this data is displayed immediately.
     */
    public void startUpdaterThread() {
        
        logger.debug("Attempting to start updater thread");
        if ( updaterThread == null ) {
            updaterThread = new Thread(this, "AIH-SDN-Updater");
            updaterThread.start();
        } else if ( updaterThread != null ) {
            logger.warn("Updater thread already started");
        }
    }
    
    /**
     * Run method for graph updater thread.  Used for collector graphs when
     * any new data is collected so that this data is displayed immediately.
     */
    public void run() {
        logger.debug("Updater thread started");
        while ( context.getData().isCollecting() ) {
            try {
                synchronized(context.getData()) {
                    context.getData().wait();
                    
                    // Was just summarizeRescaleRepaint();
                    // But apparently swing object changes should execute on the Swing event queue
                    javax.swing.SwingUtilities.invokeLater(new Runnable() {
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
    
    public GameContext getGame() {
        return gameContext;
    }
    
    /** Summarizes current data set, rescales x and y axes, and redraws the graph. */
    protected void summarizeRescaleRepaint() {
        if ( showTrim != context.getShowTrim() )
            repackGraph();
        context.setPanelWidth(graphPanel.getWidth());
        context.setPanelHeight(graphPanel.getHeight());
        //logger.debug("Setting panel width to " + graphPanel.getWidth() + " and panel height to " + graphPanel.getHeight());
        context.generateSummaryData();
        if ( gameContext != null && gameContext.isGameOn() && context.getSummaryData() != null ) {
            context.getData().addInterpolatedDataPoints(context.getSummaryData());
            gameContext.graphDataHasChanged();
        }
        repaint();
    }
    
    public void endGame() {
        
        gameContext = new GameContext(graphContext);
        keyHandlerGame = new KeyHandler(gameContext);
        removeKeyListener(keyHandlerGame);
        remove(graphPanel);
        graphPanel = new TimeSeriesDataPanel(context);
        add(graphPanel, graphPanelConstraints);
        validate();
        addKeyListener(keyHandlerLauncher);
        synchronized(graphContext.getData()) {
            summarizeRescaleRepaint();
        }
        logger.info("Game over");
    }

    /*
    public void closeGraph() {
        if ( gameContext != null)
            gameContext.endGame();
        super.closeGraph();
    }
     */
}
