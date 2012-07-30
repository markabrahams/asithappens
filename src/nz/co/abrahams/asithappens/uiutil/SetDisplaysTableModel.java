/*
 * SetsDisplayTableModel.java
 *
 * Created on 6 June 2008, 18:24
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

import nz.co.abrahams.asithappens.cartgraph.TimeSeriesContext;
import nz.co.abrahams.asithappens.*;
import java.util.Vector;
import java.awt.Color;
import javax.swing.table.*;

/**
 *
 * @author mark
 */
public class SetDisplaysTableModel extends AbstractTableModel {
//public class SetsDisplayTableModel {
    
    public enum Columns { Label, Style, Color };
    
    protected TimeSeriesContext context;
    
    protected Vector<String> labels;
    
    protected Vector<SetDisplay> setDisplays;
    
    /** Creates a new instance of CustomOIDTable */
    public SetDisplaysTableModel(TimeSeriesContext context) {
        this.context = context;
        loadDataFromContext();
    }
    
    public void loadDataFromContext() {
        labels = new Vector();
        setDisplays = new Vector();
        for ( int set = 0 ; set < context.getSetDisplays().size() ; set++ ) {
            labels.add(new String(context.getData().getHeadings().getHeading(set)));
            setDisplays.add(SetDisplay.create(context.getSetDisplay(set)));
        }
    }
    
    public void saveDataToContext() {
        Vector<SetDisplay> saveDisplays;
        
        saveDisplays = setDisplays;
        context.setSetDisplays(saveDisplays);
        setDisplays = new Vector();
        for ( int set = 0 ; set < saveDisplays.size() ; set++ ) {
            setDisplays.add(SetDisplay.create(saveDisplays.elementAt(set)));
        }
        
    }
    
    
    public Object getValueAt(int row, int column) {
        if ( column == Columns.Label.ordinal() ) {
            return labels.elementAt(row);
        } else if ( column == Columns.Style.ordinal() ) {
            return setDisplays.elementAt(row).style;
        } else if ( column == Columns.Color.ordinal() ) {
            return setDisplays.elementAt(row).color;
        } else {
            return null;
        }
    }
    
    
    public void setValueAt(Object value, int row, int column) {
        
        if ( column == Columns.Style.ordinal() ) {
            setDisplays.elementAt(row).style = SetDisplay.Style.valueOf((String)value);
            //setDisplays.elementAt(row).type = (SetDisplay.Type)value;
        } else if ( column == Columns.Color.ordinal() ) {
            setDisplays.elementAt(row).color = (Color)value;
            //context.setSetDisplayColor(row, (Color)value);
            //setDisplays.elementAt(row).color = (Color)value;
        }
         
    }
    
    
    
    public String getColumnName(int column) {
        return Columns.values()[column].toString();
    }
    
    public int getRowCount() {
        return labels.size();
    }
    
    public int getColumnCount() {
        return Columns.values().length;
    }
    
    
    public boolean isCellEditable(int row, int column) {
        if ( column == Columns.Label.ordinal() )
            return false;
        else
            return true;
    }
    
    public Class getColumnClass(int column) {
        if ( column == Columns.Label.ordinal() ) {
            return String.class;
        } else if ( column == Columns.Style.ordinal() ) {
            return SetDisplay.Style.class;
        } else if ( column == Columns.Color.ordinal() ) {
            return Color.class;
        } else
            return null;
        
    }
    
    /*
    public Vector<SetDisplay> getSetDisplaysVector() {
        return setDisplays;
    }
    */
    
}
