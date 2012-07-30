/*
 * DataTableModel.java
 *
 * Created on 13 November 2005, 23:14
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
 * Table model that prohibits cell editing.
 *
 * @author mark
 */
public class DataTableModel extends DefaultTableModel {
    
    /** Creates a new DataTableModel */
    public DataTableModel(Vector data, Vector columnNames) {
        super(data, columnNames);
    }
    
    /** @return false ensuring that cells cannot be edited */
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
