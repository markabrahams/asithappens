/*
 * InterfaceDirectionCollectorDefinition.java
 *
 * Created on 1 September 2012, 10:28
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
import nz.co.abrahams.asithappens.storage.Device;
import nz.co.abrahams.asithappens.storage.Direction;

public abstract class InterfaceDirectionCollectorDefinition extends CollectorDefinition {

    /** Units */
    public static final String UNITS = "bps";
    
    /** Initial headings */
    public static final String[] INITIAL_HEADINGS = {};
    
    /** Interface to collect from */
    protected String ifDescr;
    
    /** Direction relevant to collection */
    protected Direction direction;

    /**
     * Creates a new InterfaceDirectionCollector.
     *
     * @param device the name or IP address of the target device
     * @param pollInterval the polling interval in milliseconds
     */
    public InterfaceDirectionCollectorDefinition(String title, Device device,
            long pollInterval, DataType dataType, boolean storing,
            String ifDescr, Direction direction) {
        super(title, device, pollInterval, dataType, UNITS, storing);
        this.ifDescr = ifDescr;
        this.direction = direction;
    }
    
    public String[] getInitialHeadings() {
        return INITIAL_HEADINGS;
    }
    
    public String getIfDescr() {
        return ifDescr;
    }
    
    public Direction getDirection() {
        return direction;
    }
    
    public String getDescription() {
        return super.getDescription() + " (" + ifDescr + "," + direction.toString() + ")";
    }

}
