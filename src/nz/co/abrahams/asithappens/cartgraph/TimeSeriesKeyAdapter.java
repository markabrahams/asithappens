/*
 * TimeSeriesDataPanel.java
 *
 * Created on 18 January 2010, 01:02
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

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 *
 * @author mark
 */
public class TimeSeriesKeyAdapter extends KeyAdapter {

    public DataGraph graph;

    public TimeSeriesKeyAdapter(DataGraph graph) {
        this.graph = graph;
    }

    public void keyTyped(KeyEvent evt) {
        if (evt.getKeyChar() == 'o' || evt.getKeyChar() == 'O') {
            graph.displayOptionsChooser();
        } else if (evt.getKeyChar() == 'd' || evt.getKeyChar() == 'D') {
            graph.displayDataTable();
        } else if (evt.getKeyChar() == 'l' || evt.getKeyChar() == 'L') {
            graph.displayLabelTable();
        } else if (evt.getKeyChar() == 's' || evt.getKeyChar() == 'S') {
            graph.displayStatisticsTable();
        }

    }
}
