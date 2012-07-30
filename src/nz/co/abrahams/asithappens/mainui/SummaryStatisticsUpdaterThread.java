/*
 * SummaryStatisticsUpdaterThread.java
 *
 * Created on 20 January 2010, 01:48
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
import nz.co.abrahams.asithappens.mainui.SummaryStatisticsPanel;

import org.apache.log4j.Logger;

/**
 *
 * @author mark
 */
public class SummaryStatisticsUpdaterThread extends Thread {

    private static Logger logger = Logger.getLogger(SummaryStatisticsUpdaterThread.class);

    private SummaryStatisticsPanel panel;

    private boolean keepUpdating;

    public SummaryStatisticsUpdaterThread(SummaryStatisticsPanel panel) {
        this.panel = panel;
        keepUpdating = true;
    }

    @Override
    public void run() {
        while ( panel != null && keepUpdating && panel.getData().isCollecting() ) {
            try {
                synchronized(panel.getData()) {
                    panel.getData().wait();
                    java.awt.EventQueue.invokeLater(new Runnable() {
                        public void run() {
                            panel.updateTable();
                        }
                    });
                    //summarizeRescaleRepaint();
                }
            } catch (InterruptedException e) {
                logger.error("Statistics updater thread interrupted from wait");
            }
        }
    }

    public void stopUpdating() {
        keepUpdating = false;
    }
}
