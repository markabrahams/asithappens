/*
 * TimeSeriesDataMouseAdapter.java
 *
 * Created on 17 January 210, 15:37
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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 *
 * @author mark
 */
public class TimeSeriesDataMouseAdapter extends MouseAdapter {

    public TimeSeriesDataPanel graphPanel;

    public TimeSeriesDataMouseAdapter(TimeSeriesDataPanel graphPanel) {
        this.graphPanel = graphPanel;
    }

    public void mouseClicked(MouseEvent evt) {
        if (evt.getClickCount() == 2 && evt.getButton() == MouseEvent.BUTTON1) {
            graphPanel.labelEntry(evt.getX(), evt.getY());
        }
        /*
        else {
            graphPanel.removeMouseListener(this);
            graphPanel.dispatchEvent(evt);
            //graphPanel.addMouseListener(this);
        }
         */
    }
}
