/*
 * PainterThread.java
 *
 * Created on 23 December 2007, 03:19
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

import nz.co.abrahams.asithappens.*;
import org.apache.log4j.Logger;

/**
 *
 * @author mark
 */
public class PainterThread extends Thread {
    
    protected Logger logger;    
    
    protected SDNContainerPanel graph;
    
    /** Creates a new instance of PainterThread */
    public PainterThread(SDNContainerPanel graph) {
        super("SDN-Painter");
        logger = Logger.getLogger(this.getClass().getName());
        this.graph = graph;
    }

    /**
     * Run method for graph updater thread.  Used for collector graphs when
     * any new data is collected so that this data is displayed immediately.
     */
    public void run() {
        while ( graph.getGame().isGameOn() ) {
            try {
                synchronized(graph.getGame().getPaintMonitor()) {
                    graph.getGame().getPaintMonitor().wait(5000);
                    graph.repaint();
                }
            } catch (InterruptedException e) {
                logger.error("Painter thread interrupted from wait");
            }
        }
        graph.endGame();
    }
    
}
