/*
 * ResponseWindowsCollector.java
 *
 * Created on 11 November 2003, 17:07
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

package nz.co.abrahams.asithappens.response;

import nz.co.abrahams.asithappens.core.DataType;
import nz.co.abrahams.asithappens.storage.DataHeadings;
import nz.co.abrahams.asithappens.storage.DataPoint;
import nz.co.abrahams.asithappens.storage.Device;
import nz.co.abrahams.asithappens.collectors.DataCollectorResponse;
import nz.co.abrahams.asithappens.collectors.DataCollector;
import nz.co.abrahams.asithappens.snmputil.SNMPException;
import nz.co.abrahams.asithappens.core.DBException;
import org.apache.log4j.Logger;
import estadisticas.icmp.PingICMP;
import java.net.*;
import java.util.*;

/**
 * Collects response data from a device using ICMP.  This class only works when
 * the collector is running under Windows, as it uses a Windows native library.
 * <p>
 * This class uses a separate thread to listen to all responses, and store each
 * response in a list for the sender threads to interrogate for their particular
 * response.
 *
 * @author  mark
 */
public class ResponseWindowsCollector extends DataCollector implements Runnable {
    
    /**
     * Response result inner class for this collector.  Used to pass response
     * results from listening thread to sender thread.
     */
    public class ResponseResult {
        
        /** name of target device */
        String device;
        /** ID of ICMP packet required by library */
        int id;
        /** sequence of ICMP packet required by library */
        int sequence;
        /** response value */
        double value;
        
        public ResponseResult(String device, int id, int sequence, double value) {
            this.device = device;
            this.id = id;
            this.sequence = sequence;
            this.value = value;
        }
    }
    
    /** Maximum timeout interval in milliseconds */
    public static final long MAX_TIMEOUT = 5000;

    /** Logging provider */
    private static Logger logger = Logger.getLogger(ResponseWindowsCollector.class);

    /** time to wait for each poll response before giving up */
    private long timeout;
    /** address of target device */
    private InetAddress deviceAddress;
    /** name of device */
    private String deviceString;
    
    // BEGIN PingICMP variables
    /** Indication that listener thread should stop */
    private static boolean stopListening;
    /** number of these collectors started, each of which will be sending ICMP */
    private static int collectorCount;
    
    /** ICMP library object */
    private PingICMP ping;
    /** ID of ICMP packet required by library */
    private int id;
    /** sequence number of ICMP packet required by library */
    private int sequence;
    //private Double response;
    //private HashMap<Integer,Double> responseMap;
    
    //private Integer indexInteger;
    /** thread that listens to and stores ICMP responses */
    private static Thread listener;
    /** the list of results observed by the listener thread */
    private static Vector<ResponseResult> resultList;
    // END PingICMP variables
    
    static {
        collectorCount = 0;
        stopListening = true;
    }
    
    /** Creates a new instance of ResponseCollector */
    public ResponseWindowsCollector(Device device, long pollInterval) throws UnknownHostException {
        super(device, pollInterval, DataType.RESPONSE);
        
        deviceAddress = device.getResolvedAddress();
        timeout = pollInterval / 2;
        
        // PingICMP initializer
        initCollector();
    }
    
    /** Initializes the collector. */
    public void initCollector() {
        
        deviceString = deviceAddress.getHostAddress();
        //deviceAddress = InetAddress.getByName(device).getHostAddress();
        if ( ping == null ) {
            ping = new PingICMP();
            ping.begin();
        }
        timeout = Math.min(pollInterval / 2, MAX_TIMEOUT);
        // Bug note: small chance of picking an id already assigned to another PingICMP object
        // In this case, the response times of both PingICMP objects seem to grow without bound
        id = (int)(Math.random() * 65535);
        sequence = 1;
        //responseMap = new HashMap();
        collectorCount++;
        
        if ( stopListening == true ) {
            stopListening = false;
            listener = new Thread(this);
            listener.start();
        }
        if ( resultList == null )
            resultList = new Vector();
    }
    
    /**
     * Returns an array containing a single data point - the round-trip response
     * in milliseconds to the destination device.
     *
     * @return an array containing the single response data point
     */
    public DataCollectorResponse getNextValues(DataHeadings headings) {
        Thread listener;
        DataPoint[] point;
        ResponseResult responseItem;
        long currentTime;
        
        point = new DataPoint[1];
        point[0] = null;
        
        currentTime = System.currentTimeMillis();
        
        try {
            
            //response = null;
            //indexInteger = new Integer(sequence);
            //ping.ping(deviceAddress, id, sequence);
            logger.debug("Request for id " + id + " seq " + sequence);
            ping.ping(deviceString, id, sequence);
            Thread.sleep(timeout);
            
            for (int i = 0; i < resultList.size(); i++) {
                responseItem = resultList.elementAt(i);
                if ( id == responseItem.id && sequence == responseItem.sequence ) {
                    point[0] = new DataPoint(currentTime, responseItem.value);
                    resultList.removeElementAt(i);
                    i = resultList.size() + 1;
                }
            }
            if ( point[0] == null )
                point[0] = new DataPoint(currentTime, timeout);
            sequence++;
            return new DataCollectorResponse(point, new String[0], dataType.initialSetCount());
        } catch (InterruptedException e) {
            e.printStackTrace();
            point[0] = new DataPoint(currentTime, 0);
            return new DataCollectorResponse(point, new String[0], dataType.initialSetCount());
        }
    }
    
    /**
     * Starts the listener thread.  This listens for all ICMP packets received
     * by the PingICMP library and add the results to a static vector that
     * is shared by all response collectors.  The collectors can then examine
     * the vector for their particular response packet.
     */
    public void run() {
        int resultID;
        int resultSequence;
        double resultValue;
        String[] result;
        
        result = new String[4];
        result[1] = "-1";
        result[2] = "-1";
        resultID = -1;
        resultSequence = -1;
        resultValue = -1;
        
        while ( stopListening == false ) {
            ping.pong(result);
            //resultDevice = result[0];
            
            try {
                resultID = Integer.parseInt(result[1]);
                resultSequence = Integer.parseInt(result[2]);
                resultValue = Double.parseDouble(result[3]);
                resultList.add(0, new ResponseResult(result[0], resultID, resultSequence, resultValue));
                logger.debug("Reply for device " + result[0] + " id " + resultID + " seq " + resultSequence + ": "  + resultValue + " ms");
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("Unable to parse ICMP response: device=" + result[0] + ",id=" + resultID + ",seq=" + resultSequence + ",value(ms)=" + resultValue);
            }
            result = new String[4];
            result[1] = "-1";
            result[2] = "-1";
            resultID = -1;
            resultSequence = -1;
            resultValue = -1;
        }
        
    }
    
    
    /*
    public DataPoint[] getNextValues() {
        long startTime;
        long responseTime;
        DataPoint[] point;
     
        point = new DataPoint[1];
        startTime = System.currentTimeMillis();
     
        try {
            if ( deviceAddress.isReachable((int)timeout) ) {
                responseTime = System.currentTimeMillis() - startTime;
                //System.out.println("Response" + responseTime);
            }
            else {
                responseTime = timeout;
                //responseTime = System.currentTimeMillis() - startTime;
                //System.out.println("Timeout " + responseTime);
            }
            point[0] = new DataPoint(startTime, (double)responseTime);
     
            return point;
        }
        catch (IOException e) {
            point[0] = new DataPoint(startTime, timeout);
        }
        return point;
    }
     */

    /**
     * Decreases the collector count and stops the listener thread if there
     * are no more active collectors.
     */
    public void releaseCollector() {
        collectorCount--;
        if ( collectorCount == 0 ) {
            stopListening = true;
        }
    }
    
}
