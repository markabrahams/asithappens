/*
 * ColorCellRenderer.java
 *
 * Created on 7 June 2008, 18:18
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

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

/**
 *
 * @author mark
 */
public class ColorCellRenderer implements TableCellRenderer {
    
    protected JLabel templateLabel;
    
    /** Creates a new instance of CustomOIDColorCellRenderer */
    public ColorCellRenderer() {
        templateLabel = new JLabel();
        templateLabel.setOpaque(true);
    }
    
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Color color;
        
        color = (Color)value;
        templateLabel.setBackground(color);
        return templateLabel;
    }
    
}
