/*
 * GraphPanelMouseAdapter.java
 *
 * Created on 17 January 2010, 15:37
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
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;

/**
 *
 * @author mark
 */
public class PopupMouseAdapter extends MouseAdapter implements ActionListener {

    /** Popup menu items */
    private enum MenuItems {

        GRAPH_OPTIONS("Graph options (o)"),
        DELETE_LABELS("Delete labels (l)"),
        DISPLAY_DATA("Display data (d)"),
        SUMMARY_STATISTICS("Summary statistics (s)");

        String text;

        MenuItems(String text) {
            this.text = text;
        }
    };

    private DataGraph graph;

    private JPopupMenu menu;

    public PopupMouseAdapter(DataGraph graph) {
        this.graph = graph;

        menu = new JPopupMenu();
        for (int i = 0; i < MenuItems.values().length; i++) {
            JMenuItem menuItem;
            menuItem = new JMenuItem(MenuItems.values()[i].text);
            menuItem.addActionListener(this);
            menu.add(menuItem);
        }

    }

    public void actionPerformed(ActionEvent evt) {
        String selected;

        selected = ((JMenuItem) (evt.getSource())).getText();

        if (selected.equals(MenuItems.GRAPH_OPTIONS.text)) {
            graph.displayOptionsChooser();
        }
        if (selected.equals(MenuItems.DELETE_LABELS.text)) {
            graph.displayLabelTable();
        }
        if (selected.equals(MenuItems.DISPLAY_DATA.text)) {
            graph.displayDataTable();
        }
        if (selected.equals(MenuItems.SUMMARY_STATISTICS.text)) {
            graph.displayStatisticsTable();
        }

    }

    public void mousePressed(MouseEvent evt) {
        maybeShowPopup(evt);
    }

    public void mouseReleased(MouseEvent evt) {
        maybeShowPopup(evt);
    }

    private void maybeShowPopup(MouseEvent evt) {
        if (evt.isPopupTrigger()) {
            menu.show(evt.getComponent(),
                    evt.getX(), evt.getY());
        }
    }
}
