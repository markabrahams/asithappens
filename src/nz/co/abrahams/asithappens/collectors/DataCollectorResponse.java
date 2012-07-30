/*
 * DataCollectorResponse.java
 *
 * Created on 7 May 2007, 23:00
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

import nz.co.abrahams.asithappens.storage.DataPoint;
import nz.co.abrahams.asithappens.*;

/**
 *
 * @author mark
 */
public class DataCollectorResponse {

    public DataPoint[] values;
    
    public String[] newHeadings;
    
    public int totalHeadings;
    
    /** Creates a new instance of DataCollectorResponse */
    public DataCollectorResponse(DataPoint[] values, String[] newHeadings, int totalHeadings) {
        this.values = values;
        this.newHeadings = newHeadings;
        this.totalHeadings = totalHeadings;
    }
    
}
