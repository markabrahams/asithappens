/*
 * PortsSelectorTableModel.java
 *
 * Created on 28 May 2008, 23:23
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

package nz.co.abrahams.asithappens.uiutil;

import java.util.Vector;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author mark
 */
public class PortsSelectorTableModel extends DefaultTableModel {
    
    /** Creates a new instance of PortsSelectorTableModel */
    public PortsSelectorTableModel(Vector data, Vector headings) {
        super(data, headings);
    }
    
    /** @return false ensuring that cells cannot be edited */
    @Override public boolean isCellEditable(int row, int column) {
        return false;
    }    
}
