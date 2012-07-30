/*
 * SessionsTableModel.java
 *
 * Created on 13 November 2005, 23:34
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

import javax.swing.table.DefaultTableModel;
import java.util.Vector;

/**
 * Table model for sessions table in database panel.  This exists solely to
 * prohibit the editing of any columns other than the "title" column.
 *
 * @author mark
 */
public class SessionsTableModel extends DefaultTableModel {
    
    /** Creates a new SessionsTableModel. */
    public SessionsTableModel(Object[][] data, Object[] columnNames) {
        super(data, columnNames);
    }
    
    /**
     * Allows editing of only the first column, which is the title.
     *
     * @param row     row number of cell
     * @param column  column number of cell
     * @return        true if the cell is in the first column
     */
    public boolean isCellEditable(int row, int column) {
        return column == 0;
    }
}
