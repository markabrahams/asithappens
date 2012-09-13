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

import nz.co.abrahams.asithappens.core.DataType;

/**
 *
 * @author mark
 */
public interface DataAttributes {
    public DataType getDataType();
    public void setDataType(DataType dataType);
    public String[] getInitialHeadings();
    public String getUnits();
    public void setUnits(String units);
    public String getDescription();
    public String getTitle();
    public void setTitle(String title);
}
