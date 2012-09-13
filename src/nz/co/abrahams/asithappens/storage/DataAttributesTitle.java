/*
 * DataAttributes.java
 *
 * Created on 31 August 2012, 22:21
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

/**
 *
 * @author mark
 */
public abstract class DataAttributesTitle implements DataAttributes {

    /** Title for data */
    public String title;
    
    public DataAttributesTitle(String title) {
        this.title = title;
    }
    
    public String getTitle() {
        if (title == null) {
            return this.getDescription();
        } else {
            return title;
        }        
    }
    
    public void setTitle(String newTitle) {
        title = newTitle;
    }
}
