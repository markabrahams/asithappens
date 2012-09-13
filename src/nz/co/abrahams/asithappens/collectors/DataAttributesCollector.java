/*
 * DataAttributesCollector.java
 *
 * Created on 1 September 2012, 08:14
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
package nz.co.abrahams.asithappens.collectors;

import nz.co.abrahams.asithappens.core.DataType;
import nz.co.abrahams.asithappens.storage.DataAttributes;
import nz.co.abrahams.asithappens.storage.DataAttributesTitle;

/**
 *
 * @author mark
 */
public class DataAttributesCollector extends DataAttributesTitle {
    
    protected CollectorDefinition definition;
    
    //protected String title;
    
    public DataAttributesCollector(CollectorDefinition definition, String title) {
        super(title);
        this.definition = definition;
    }
    
    public CollectorDefinition getDefinition() {
        return definition;
    }
    
    public DataType getDataType() {
        return definition.getDataType();
    }
    
    public void setDataType(DataType dataType) {
        definition.setDataType(dataType);
    }
    
    public String[] getInitialHeadings() {
        return definition.getInitialHeadings();
    }
    
    public String getUnits() {
        return definition.getUnits();
    }
    
    public void setUnits(String newUnits) {
        definition.setUnits(newUnits);
    }
    
    public String getDescription() {
        return definition.getDescription();
    }
    
    /*
    public String getTitle() {
        return title;
    }
    */
}
