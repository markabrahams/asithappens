/*
 * DataType.java
 *
 * Created on 18 December 2005, 22:24
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

package nz.co.abrahams.asithappens.core;

import nz.co.abrahams.asithappens.uiutil.SetDisplay;
import org.apache.log4j.Logger;
import nz.co.abrahams.asithappens.cartgraph.TimeSeriesContext.XAxisScaling;
import nz.co.abrahams.asithappens.cartgraph.TimeSeriesContext.Aggregation;
import nz.co.abrahams.asithappens.cartgraph.TimeSeriesContext.Interpolation;

/**
 * Contains type information for a DataSets instance.
 *
 * @author mark
 */
public class DataType {
    
    public static final DataType BANDWIDTH = new DataType(0, "Bandwidth", new String[]{"In", "Out"}, "bps", true, true, SetDisplay.Positioning.Grounded, SetDisplay.BANDWIDTH_DEFAULTS, XAxisScaling.ConstantPixelWidth, Aggregation.Maximum, Interpolation.Flat);
    public static final DataType RESPONSE = new DataType(1, "Response", new String[]{"Response"}, "ms", false, true, SetDisplay.Positioning.Grounded, SetDisplay.RESPONSE_DEFAULTS, XAxisScaling.ConstantPixelWidth, Aggregation.Maximum, Interpolation.Flat);
    public static final DataType NBAR = new DataType(2, "NBAR", new String[]{}, "bps", true, false, SetDisplay.Positioning.Stacked, SetDisplay.DYNAMIC_DEFAULTS, XAxisScaling.ConstantPixelWidth, Aggregation.Average, Interpolation.None);
    public static final DataType NETFLOW = new DataType(3, "NetFlow", new String[]{}, "bps", true, false, SetDisplay.Positioning.Stacked, SetDisplay.DYNAMIC_DEFAULTS, XAxisScaling.ConstantPixelWidth, Aggregation.Average, Interpolation.None);
    public static final DataType CAPTURE = new DataType(4, "Capture", new String[]{}, "bps", true, false, SetDisplay.Positioning.Stacked, SetDisplay.DYNAMIC_DEFAULTS, XAxisScaling.AbsoluteBoundaries, Aggregation.Summed, Interpolation.None);
    public static final DataType PROCESSOR = new DataType(5, "Processor load", new String[]{"Load"}, "%age", false, true, SetDisplay.Positioning.Grounded, SetDisplay.PROCESSOR_DEFAULTS, XAxisScaling.ConstantPixelWidth, Aggregation.Maximum, Interpolation.Flat);
    public static final DataType NETSNMP_PROCESSOR = new DataType(6, "Net-SNMP processor load", new String[]{"User", "Nice", "System", "Wait", "Kernel", "Interrupt"}, "%age", false, false, SetDisplay.Positioning.Stacked, SetDisplay.NETSNMP_PROCESSOR_DEFAULTS, XAxisScaling.ConstantPixelWidth, Aggregation.Maximum, Interpolation.Flat);
    public static final DataType STORAGE = new DataType(7, "Storage/Memory", new String[]{"Used"}, "bytes", true, true, SetDisplay.Positioning.Grounded, SetDisplay.STORAGE_DEFAULTS, XAxisScaling.ConstantPixelWidth, Aggregation.Maximum, Interpolation.Flat);
    public static final DataType OID = new DataType(8, "Custom OID", new String[]{}, "?", true, false, SetDisplay.Positioning.Stacked, SetDisplay.DYNAMIC_DEFAULTS, XAxisScaling.ConstantPixelWidth, Aggregation.Maximum, Interpolation.Flat);
    public static final DataType ACCOUNTING = new DataType(9, "IP Accounting", new String[]{}, "bps", true, false, SetDisplay.Positioning.Stacked, SetDisplay.DYNAMIC_DEFAULTS, XAxisScaling.ConstantPixelWidth, Aggregation.Average, Interpolation.None);
    public static final DataType MAC_ACCOUNTING = new DataType(10, "MAC Accounting", new String[]{}, "bps", true, false, SetDisplay.Positioning.Stacked, SetDisplay.DYNAMIC_DEFAULTS, XAxisScaling.ConstantPixelWidth, Aggregation.Average, Interpolation.None);
    public static final DataType IPPREC_ACCOUNTING = new DataType(11, "IP Precedence Accounting", new String[]{}, "bps", true, false, SetDisplay.Positioning.Stacked, SetDisplay.DYNAMIC_DEFAULTS, XAxisScaling.ConstantPixelWidth, Aggregation.Average, Interpolation.None);

    /** Ordered array of types */
    public static DataType[] types = { BANDWIDTH, RESPONSE, NBAR, NETFLOW, CAPTURE, PROCESSOR, NETSNMP_PROCESSOR, STORAGE, OID, ACCOUNTING, MAC_ACCOUNTING, IPPREC_ACCOUNTING };
    
    /** Logging provider */
    protected Logger logger;
    
    /** Unique ID assigned to type */
    public int id;
    
    /** Text description of type */
    public String description;
    
    /** Initial heading descriptions for sets */
    public String[] initialHeadings;
    
    /** Units for type */
    public String units;
    
    /** Flag for formatting y-axis units (with K, M, G, etc.) */
    public boolean formatUnits;
    
    /** Legend positioning */
    public boolean bottomLeftLegend;
            
    /** Default display positioning of each set */
    public SetDisplay.Positioning positioningDefault;
    
    /** Strategy for calculating x-axis time scaling */
    public XAxisScaling xAxisScaling;
    
    /** Aggregation policy */
    public Aggregation aggregation;
    
    /** Interpolation policy */
    public Interpolation interpolation;
    
    /** Set displays */
    public SetDisplay[] setDisplayDefaults;

    /** Collector Data Access Object for DB read/write */
    public Class collectorDAOClass;
    
    /** Creates a new DataType instance */
    public DataType(int id, String description, String[] initialHeadings, String units, boolean formatUnits,
            boolean bottomLeftLegend, SetDisplay.Positioning positioningDefault, SetDisplay[] setDisplayDefaults,
            XAxisScaling xAxisScaling, Aggregation aggregation, Interpolation interpolation
            /* Class collectorDAOClass */) {
        logger = Logger.getLogger(this.getClass().getName());
        
        this.id = id;
        this.description = description;
        this.initialHeadings = initialHeadings;
        this.units = units;
        this.formatUnits = formatUnits;
        this.bottomLeftLegend = bottomLeftLegend;
        this.positioningDefault = positioningDefault;
        this.setDisplayDefaults = setDisplayDefaults;
        this.xAxisScaling = xAxisScaling;
        this.aggregation = aggregation;
        this.interpolation = interpolation;
        logger.debug("New data type instantiated: " + toString());
    }
    
    public int initialSetCount() {
        return initialHeadings.length;
    }

    public static DataType getType(int id) {
        for ( int i = 0 ; i < types.length ; i++ ) {
            if ( types[i].id == id )
                return types[i];
        }
        return null;
    }

    public String toString() {
        StringBuffer buffer;
        
        buffer = new StringBuffer();
        buffer.append("Data type " + description + ": id=" + id + ",initialSetCount=" + initialHeadings.length +
                ",initialHeadings={");
        for ( int i = 0; i < initialHeadings.length; i++ ) {
            buffer.append(initialHeadings[i]);
            if ( i < initialHeadings.length - 1 )
                buffer.append(",");
        }
        buffer.append("},units=" + units);
        return buffer.toString();
    }

}
