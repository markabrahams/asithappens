/*
 * DataHeadings.java
 *
 * Created on 26 October 2005, 22:21
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

import java.util.*;

/**
 * Stores a list of headings for data sets.
 *
 * @author mark
 */
public class DataHeadings {
    
    /** Column heading names */
    protected Vector<String> name;
    
    /** Mapping from "key" to column index */
    protected Hashtable<String,Integer> map;
    
    /**
     * Creates a new instance of DataHeadings with given headings.
     *
     * @param headings initial set of headings
     */
    public DataHeadings(String[] headings) {
        name = new Vector();
        map = new Hashtable();
        
        for (int i = 0; i < headings.length; i++ ) {
            add(headings[i]);
        }
    }
    
    /** Creates a new instance of DataHeadings with no headings. */
    public DataHeadings() {
        name = new Vector();
        map = new Hashtable();
    }
    
    /** Adds a new heading to the end of the list. */
    public void add(String title) {
        map.put(title, new Integer(name.size()));
        name.add(title);
    }
    
    /** Retrieves the heading at the specified index. */
    public String getHeading(int index) {
        return name.get(index);
    }
    
    /** Retrieves the ordered list of headings. */
    public String[] getHeadings() {
        return (String[])name.toArray(new String[0]);
    }
    
    /** Retrieves the index of a specified heading string. */
    public int getIndex(String key) {
        return map.get(key).intValue();
    }
    
    /** Returns the number of headings in the list. */
    public int size() {
        return name.size();
    }
}
