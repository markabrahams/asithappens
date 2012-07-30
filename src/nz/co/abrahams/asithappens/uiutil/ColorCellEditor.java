/*
 * ColorCellEditor.java
 *
 * Created on 7 June 2008, 18:42
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
import java.awt.event.*;

/**
 *
 * @author mark
 */
public class ColorCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
    
    public class ColorCellEditorDialogOKListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            color = colorChooser.getColor();
        }
    }
    
    Color color;
    JColorChooser colorChooser;
    JButton button;
    
    /** Creates a new instance of CustomOIDColorCellEditor */
    public ColorCellEditor() {
        button = new JButton();
        button.addActionListener(this);
        button.setBorderPainted(false);
        
        colorChooser = new JColorChooser();
    }
    
    public void actionPerformed(ActionEvent e) {
        JDialog dialog;
        
        dialog = JColorChooser.createDialog(button, "Pick a Color", true, colorChooser, new ColorCellEditorDialogOKListener(), null);
        
        button.setBackground(color);
        colorChooser.setColor(color);
        dialog.setVisible(true);
        
        fireEditingStopped();
    }
    
    public Object getCellEditorValue() {
        return color;
    }
    
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        color = (Color)value;
        return button;
    }
}
