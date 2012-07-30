/*
 * FlowData.java
 *
 * Created on 6 May 2005, 22:14
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

package nz.co.abrahams.asithappens.capture;

import nz.co.abrahams.asithappens.storage.DataSets;
import nz.co.abrahams.asithappens.storage.DataPoint;
import nz.co.abrahams.asithappens.flow.FlowOptions;
import nz.co.abrahams.asithappens.flow.Flow;
import nz.co.abrahams.asithappens.*;
import nz.co.abrahams.asithappens.core.DBException;
import java.io.*;
import java.util.*;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.voytechs.jnetstream.io.StreamFormatException;
import com.voytechs.jnetstream.npl.SyntaxError;
import com.voytechs.jnetstream.io.EOPacketStream;

/**
 * This holds flow information read from a packet capture.
 *
 * @author mark
 */
public class FlowData extends DataSets {
    
    Logger logger;
    String fileName;
    int direction;
    Flow[] flows;
    FlowOptions options;
    
    /**
     * Creates a new FlowData instance.
     *
     * @param fileName    name of packet capture file
     * @param title       title for the data sets
     * @param direction   direction of interest
     * @param startTime   the time that the first packet was observed
     * @param finishTime  the time that the last packet was observed
     * @param options     mask options
     */
    public FlowData(String fileName, String title, int direction, long startTime, long finishTime, FlowOptions options) throws FileNotFoundException, IOException, EOPacketStream, StreamFormatException, SyntaxError,
            ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        super(startTime, finishTime, fileName);
        logger = Logger.getLogger(this.getClass().getName());
        
        flows = new Flow[0];
        this.fileName = fileName;
        this.direction = direction;
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.options = options;
        
        logger.debug(options.toString());
        try {
            parseTrace();
        } catch (NullPointerException e) {
            logger.debug(flowDescriptions());
            e.printStackTrace();
        } catch (DBException e) {
            e.printStackTrace();
        }
        //logger.debug(flowDescriptions());
    }
    
    public FlowData(String fileName, long startTime, long finishTime, FlowOptions options) {
        super(startTime, finishTime, fileName);
        logger = Logger.getLogger(this.getClass().getName());
        
        flows = new Flow[0];
        this.fileName = fileName;
        this.direction = direction;
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.options = options;        
    }
    
    /**
     * Parses a packet trace file and populates the data sets.
     */
    public void parseTrace() throws FileNotFoundException, IOException, EOPacketStream, StreamFormatException, SyntaxError, DBException {
        PacketTraceIterator source;
        Flow flow;
        boolean foundMatch;
        boolean firstPacket;
        Flow[] temporaryFlows;
        Vector<DataPoint>[] temporaryData;
        
        source = new PacketTraceIterator(fileName);
        firstPacket = true;
        
        while ( (flow = source.next()) != null ) {
            
            if ( flow.timestamp >= startTime && flow.timestamp <= finishTime ) {
                
                foundMatch = false;
                for (int set = 0; set < data.size(); set++) {
                    if ( flow.matches(flows[set], options) ) {
                        foundMatch = true;
                        data.elementAt(set).add(new DataPoint(flow.timestamp, flow.length * 8));
                        logger.debug("Adding data to flow" + set + ": " + flow.timestamp + " " + flow.length);
                    }
                }
                
                if ( foundMatch == false ) {
                    temporaryFlows = new Flow[data.size() + 1];
                    System.arraycopy(flows, 0, temporaryFlows, 0, data.size());
                    flows = temporaryFlows;
                    flows[data.size()] = flow;
                    
                    logger.debug("Adding new data set " + flow.printable(options));
                    addSet(flow.printable(options));
                    data.elementAt(data.size() - 1).add(new DataPoint(flow.timestamp, flow.length * 8));
                }
            }
            /*
            if ( firstPacket ) {
                startTime = flow.timestamp;
                firstPacket = false;
            }
            finishTime = flow.timestamp;
             */
        }
        
    }
    
    /**
     * Returns a list of the first n flow descriptions.
     *
     * @param numberFlows  the number of flows desired
     * @return             the list of descriptions
     */
    /*
    public String[] getDescriptions(int numberFlows) {
        String[] descriptions = new String[numberFlows];
        
        for (int i = 0; i < numberFlows; i++) {
            descriptions[i] = flows[i].printable();
        }
        
        return descriptions;
    }
    */
    
    /**
     * Dump the data structures for debugging purposes.
     *
     * @return  a text dump of the data
     */
    public String flowDescriptions() {
        StringBuilder returnBuffer;
        Iterator<DataPoint> iterator;
        DataPoint point;
        
        returnBuffer = new StringBuilder();
        returnBuffer.append(options.toString());
        for (int set = 0; set < data.size(); set++ ) {
            returnBuffer.append("\nFlow " + set + ": "
                    + flows[set].toString() + " (size=" + flows[set].length + ")\n");
            iterator = data.elementAt(set).iterator();
            while ( iterator.hasNext() ) {
                point = iterator.next();
                returnBuffer.append("  " + point.getValue() + "\n");
            }
        }
        return returnBuffer.toString();
    }
}
