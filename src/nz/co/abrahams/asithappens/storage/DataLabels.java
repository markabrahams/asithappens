/*
 * DataLabels.java
 *
 * Created on 14 December 2005, 19:20
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

package nz.co.abrahams.asithappens.storage;

import nz.co.abrahams.asithappens.core.DAOFactory;
import nz.co.abrahams.asithappens.core.DBException;
import java.util.*;
import org.apache.log4j.Logger;

/**
 * A set of labels with their positions on a graph.  This is implemented
 * as a vector of DataLabel objects.
 * <p>
 * If labels are stored in the database, then the database will generate
 * a unique key for each label to identify this in the database.
 *
 * @author mark
 */
public class DataLabels {
    
    /** Logging provider */
    protected Logger logger;
    
    /** Vector of labels */
    protected Vector<DataLabel> labels;
    
    /** Indicies to identify labels in database */
    protected Vector<Integer> dbIndex;
    
    /** The database session ID that corresponds to these labels */
    protected int sessionID;
    
    /** Creates a new instance of DataLabels. */
    public DataLabels(int sessionID) {
        logger = Logger.getLogger(this.getClass().getName());
        labels = new Vector();
        this.sessionID = sessionID;
        if ( this.sessionID != -1 ) {
            dbIndex = new Vector();
        }
    }
    
    /**
     * Retrieve the DataLabel at the given index in the vector.
     *
     * @param index  the index of the label to retrieve
     * @return       the label at the given index
     */
    public DataLabel getLabel(int index) {
        return labels.elementAt(index);
    }
    
    /**
     * Adds a DataLabel to the end of the vector of labels.
     *
     * @param label    the label to add
     * @param loading  true if label information is being loaded from the database
     */
    public void addLabel(DataLabel label, boolean loading) {
        //DBAccess dba;
        int index;
        
        try {
            labels.add(label);
            if ( sessionID != -1 && ! loading ) {
                //dba = new DBAccess();
                index = DAOFactory.getDataLabelsDAO().create(sessionID, label);
                dbIndex.add(new Integer(index));
            }
        } catch (DBException e) {
            logger.warn("Cannot add label \"" + label.getLabel() + "\" for session ID " + sessionID + " to database");
        }
    }
    
    /**
     * Deletes the DataLabel at the given index in the vector.
     *
     * @param index  the index of the label to delete
     */
    public void deleteLabel(int index) {
        DataLabel label;
        int removedIndex;
        
        //label = getLabel(index);
        labels.remove(index);
        
        try {
            
            if ( sessionID != -1 ) {
                removedIndex = dbIndex.elementAt(index);
                dbIndex.remove(index);
                DAOFactory.getDataLabelsDAO().delete(sessionID, removedIndex);
            }
        } catch (DBException e) {
            //logger.warn("Cannot delete label \"" + label.getLabel() + "\" for session ID " + sessionID + " from database");
            logger.warn("Cannot update labels for session ID " + sessionID + " in database");
        }
        /*
        finally {
            labels.remove(index);
        }
         */
    }
    
    /** @return the number of labels in the vector */
    public int getLabelCount() {
        return labels.size();
    }
    
    /** @return an array of label strings */
    public String[] getLabelStrings() {
        String[] strings;
        
        strings = new String[getLabelCount()];
        for ( int i = 0; i < getLabelCount(); i++ ) {
            strings[i] = getLabel(i).getLabel();
        }
        return strings;
    }
    
    /** @return a string representation of the label names in the vector */
    public String toString() {
        StringBuffer buffer;
        
        buffer = new StringBuffer();
        for (int i = 0; i < getLabelCount(); i++ ) {
            buffer.append(labels.elementAt(i).getLabel());
            if ( i < getLabelCount() - 1 )
                buffer.append(",");
        }
        return buffer.toString();
    }
}
