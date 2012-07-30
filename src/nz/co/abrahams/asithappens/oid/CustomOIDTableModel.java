/*
 * CustomOIDTableModel.java
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

package nz.co.abrahams.asithappens.oid;

import nz.co.abrahams.asithappens.snmputil.SNMPType;
import nz.co.abrahams.asithappens.uiutil.SetDisplay;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author mark
 */
public class CustomOIDTableModel extends AbstractTableModel {
    
    //public static final SetDisplay.Style STYLE_DEFAULT = SetDisplay.Style.Line;
    
    public enum Columns { Label, OID, Type, Description };
    
    /*
    protected static final String[] columnNames = { "Label", "OID", "Type", "Description", "Style", "Color" };
    
    protected static final int COLUMN_COUNT = 6;
    
    protected static final int COLUMN_LABEL = 0;
    
    protected static final int COLUMN_OID = 1;
    
    protected static final int COLUMN_TYPE = 2;
    
    protected static final int COLUMN_DESCRIPTION = 3;
    
    protected static final int COLUMN_LINE_TYPE = 4;
    
    protected static final int COLUMN_LINE_COLOR = 5;
    */
    
    protected Vector<CustomOID> oids;
    
    //protected Vector<SetDisplay> setDisplays;
    
    /** Creates a new instance of CustomOIDTable */
    public CustomOIDTableModel() {
        oids = new Vector();
        //setDisplays = new Vector();
    }
    
    public Object getValueAt(int row, int column) {
        
        if ( column == Columns.Label.ordinal() ) {
            return oids.elementAt(row).label;
        }
        else if ( column == Columns.OID.ordinal() ) {
            return oids.elementAt(row).oid;
        }
        else if ( column == Columns.Type.ordinal() ) {
            return oids.elementAt(row).type;
        }
        else if ( column == Columns.Description.ordinal() ) {
            return oids.elementAt(row).description;
        }
        /*
        else if ( column == Columns.Style.ordinal() ) {
            return setDisplays.elementAt(row).style;
        }
        else if ( column == Columns.Color.ordinal() ) {
            return setDisplays.elementAt(row).color;
        }
         */
        else {
            return null;
        }
    }
    
    public void setValueAt(Object value, int row, int column) {
        if ( column == Columns.Label.ordinal() ) {
            oids.elementAt(row).label = (String)value;
        }
        else if ( column == Columns.OID.ordinal() ) {
            oids.elementAt(row).oid = (String)value;
        }
        else if ( column == Columns.Type.ordinal() ) {
            //oids.elementAt(row).type = SNMPType.getTypeFromLabel((String)value);
            oids.elementAt(row).type = SNMPType.valueOf((String)value);
        }
        else if ( column == Columns.Description.ordinal() ) {
            oids.elementAt(row).description = (String)value;
        }
        /*
        else if ( column == Columns.Style.ordinal() ) {
            //setDisplayTypes.setElementAt((GraphContext.SetDisplayType)value, row);
            setDisplays.elementAt(row).style = SetDisplay.Style.valueOf((String)value);
        }
        else if ( column == Columns.Color.ordinal() ) {
            setDisplays.elementAt(row).color = (Color)value;
        }
         */
    }
    
    public String getColumnName(int column) {
        return Columns.values()[column].toString();
    }
    
    public int getRowCount() {
        return oids.size();
    }
    
    public int getColumnCount() {
        return Columns.values().length;
    }
    
    public boolean isCellEditable(int row, int column) {
        return true;
    }
    
    public Class getColumnClass(int column) {
        if ( column == Columns.Label.ordinal() ) {
            return String.class;
        }
        else if ( column == Columns.OID.ordinal() ) {
            return String.class;
        }
        else if ( column == Columns.Type.ordinal()  ) {
            return Integer.class;
        }
        else if ( column == Columns.Description.ordinal() ) {
            return String.class;
        }
        /*
        else if ( column == Columns.Style.ordinal() ) {
            return SetDisplay.Style.class;
        }
        else if ( column == Columns.Color.ordinal() ) {
            return Color.class;
        }
         */
        else
            return null;
        
    }
    
    public void addOID() {
        oids.add(new CustomOID());
        //setDisplays.add(new SetDisplay(STYLE_DEFAULT, SetDisplay.DYNAMIC_DEFAULTS[setDisplays.size() % SetDisplay.DYNAMIC_DEFAULTS.length].color));
        fireTableRowsInserted(oids.size() - 1, oids.size() - 1);
    }
    
    public void addOID(CustomOID customOID, SetDisplay display) {
        oids.add(customOID);
        //setDisplays.add(display);
        fireTableRowsInserted(oids.size() - 1, oids.size() - 1);
    }
    
    public void removeOID(int row) {
        oids.remove(row);
        //setDisplays.remove(row);
        fireTableRowsDeleted(row, row);
    }
    
    public Vector<CustomOID> getCustomOIDVector() {
        return oids;
    }
    
    /*
    public Vector<SetDisplay> getSetDisplaysVector() {
        return setDisplays;
    }
    */
    
    /*
    public Vector<GraphContext.SetDisplayType> getDisplayTypeVector() {
        return setDisplayTypes;
    }
    
    public Vector<Color> getDisplayColorVector() {
        return setColors;
    }
    */
}
