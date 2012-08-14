/*
 * DataSets.java
 *
 * Created on 27 May 2004, 00:46
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
import nz.co.abrahams.asithappens.oid.CustomOIDCollector;
import nz.co.abrahams.asithappens.collectors.DataCollectorResponse;
import nz.co.abrahams.asithappens.collectors.DataCollector;
import nz.co.abrahams.asithappens.core.DAOFactory;
import nz.co.abrahams.asithappens.snmputil.SNMPException;
import nz.co.abrahams.asithappens.core.DBException;
import nz.co.abrahams.asithappens.core.Configuration;
import nz.co.abrahams.asithappens.cartgraph.TimeSeriesContext;
import java.util.*;
import java.text.*;
import java.net.*;
import java.io.*;
import org.apache.log4j.Logger;

/**
 * Stores sets of collected data.  The following assumptions are made about the
 * nature of the data.
 * <ol>
 * <li> An arbitrary number of data "sets" can be stored.
 * <li> Each set has a corresponding textual heading.
 * <li> Data values in a set have at some point been collected periodically over
 *      some length of time.
 * <li> Data values in a set are ordered.  Conceptually, each value within a
 *      data set has an index.  A value with a lower index has always been
 *      collected before a value with a higher index.  Indicies start at zero.
 * <li> The time of each collection "event" is stored in a set of times.  Each
 *      time value is represented by the number of milliseconds since midnight,
 *      Jan 1, 1970, GMT.
 * </ol>
 * <p>
 * DataSets objects may keep a handle to a database.  This is useful for
 * storing values in the database as collected, or querying the database to
 * create data sets from previously stored data.
 *
 * @author  mark
 */
public class DataSets implements Runnable {

    /** Direction not relevant */
    public static final int DIRECTION_NONE = 0;
    /** Inbound direction */
    public static final int DIRECTION_IN = 1;
    /** Outbound direction */
    public static final int DIRECTION_OUT = 2;
    /** Both directions */
    public static final int DIRECTION_BOTH = 3;
    /** Directions */
    public static final String[] DIRECTIONS = {"None", "In", "Out", "Both"};
    /* Logging provider */
    protected Logger logger;
    /** Data type */
    protected DataType dataType;
    /** Y-axis value units */
    protected String valueUnits;
    /** Collection target device */
    protected Device device;
    /** The desired time interval in milliseconds between successive polling instances */
    protected long pollInterval;
    /** An index into the ifTable specifying which interface for collection */
    //protected int port;
    /** A textual description of the interface for collection */
    protected String portString;
    /** The direction of information for collection */
    protected int direction;
    /** The column heading titles */
    protected DataHeadings headings;
    /** User-supplied title for data */
    protected String title;
    /** The collected data is stored in an array of data sets */
    protected Vector<DataSet> data;
    /** Data collector */
    protected DataCollector collector;
    /** Collector runs in separate thread */
    protected Thread collectorThread;
    /** Indicates whether the DataSets object is currently collecting data */
    protected boolean collecting;
    /** Access for database querying or updating */
    //protected DBAccess dba;
    /** DataSets DAO for updating database for collecting sessions */
    protected DataSetsDAO dataSetsDAO;
    /** DataPoint DAO for writing newly collected data to database */
    protected DataPointDAO dataPointDAO;
    /** Flag for database storing */
    protected boolean storing;
    /** Database session ID */
    protected int sessionID;
    /** The time of the first collection event in the data sets */
    protected long startTime;
    /** The time of the final collection event in the data sets */
    protected long finishTime;
    /** Text labels */
    protected DataLabels labels;

    /**
     * Creates a new instance of DataSets that has an active collector.
     *
     * @param dataType      the type of data contained
     * @param collector     the collector that populates the data sets
     * @param device        the target collection device
     * @param pollInterval  the polling interval in milliseconds
     * @param portString    the text description of the associated port if any
     * @param direction     the direction of data flow for the collection
     * @param title         the title of these sets of data
     * @param storing       indicates that data will be stored into the database
     */
    public DataSets(DataType dataType, DataCollector collector, Device device, int pollInterval, String portString, int direction, String title, boolean storing)
            throws DBException, UnknownHostException, SNMPException {
        logger = Logger.getLogger(this.getClass().getName());
        this.dataType = dataType;
        this.collector = collector;
        this.device = device;
        this.pollInterval = pollInterval;
        this.portString = portString;
        this.direction = direction;
        this.title = title;
        this.storing = storing;
        
        // Nasty - fix it up please
        if (dataType == DataType.OID) {
            valueUnits = ((CustomOIDCollector) collector).getValueUnits();
        } else {
            valueUnits = dataType.units;
        }

        if (title == null) {
            this.title = getShortDescription();
        } else {
            this.title = title;
        }
        this.collecting = true;

        //collector.setData(this);

        if (storing) {
            // Old school
            //dba = new DBAccess();
            //sessionID = dba.newSession(device.getName(), pollInterval, dataType.id,
            //        portString, this.title, direction);
            // New school
            dataSetsDAO = DAOFactory.getDataSetsDAO();
            dataPointDAO = DAOFactory.getDataPointDAO();
            sessionID = DAOFactory.getDataSetsDAO().createSession(this);
        } else {
            //dba = null;
            dataSetsDAO = null;
            dataPointDAO = null;
            sessionID = -1;
        }

        initializeDataSets();
        for (int set = 0; set < dataType.initialHeadings.length; set++) {
            addSet(dataType.initialHeadings[set]);
        }

        startCollecting();
    }

    /**
     * Creates a new instance of DataSets from a previously stored session in
     * the database, over a given time interval.
     *
     * @param sessionID     index in database that uniquely identifies session
     * @param startTime     beginning of desired time period
     * @param finishTime    end of desired time period
     * @param aggregation   the aggregation strategy employed for larger data sets
     */
    /*
    public DataSets(int sessionID, long startTime, long finishTime, int aggregation)
    throws DatabaseException {
    logger = Logger.getLogger(this.getClass().getName());
    this.sessionID = sessionID;
    this.startTime = startTime;
    this.finishTime = finishTime;
    this.storing = true;
    this.collector = null;
    this.collecting = false;
    initializeDataSets();
    parseResults(aggregation);
    
    }
     */
    /** Creates a new empty instance of DataSets */
    public DataSets() {
        logger = Logger.getLogger(this.getClass().getName());
        //this.storing = true;
        this.storing = false;
        this.collector = null;
        this.collecting = false;
        initializeDataSets();
    }

    /**
     * Creates a new instance of DataSets from a packet capture file over the
     * given time interval.
     *
     * @param startTime   beginning of desired time period
     * @param finishTime  end of desired time period
     * @param fileName    name of packet capture file
     */
    public DataSets(long startTime, long finishTime, String fileName) {
        logger = Logger.getLogger(this.getClass().getName());
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.portString = fileName.substring(fileName.lastIndexOf(File.separatorChar) + 1);
        this.storing = false;
        this.collector = null;
        this.collecting = false;
        sessionID = -1;
        dataType = DataType.CAPTURE;
        title = getShortDescription();
        initializeDataSets();
    }

    /**
     * Initializes data sets variables.
     */
    protected void initializeDataSets() {

        data = new Vector();
        headings = new DataHeadings();
        //headingsName = new Vector();
        //headingsMap = new Hashtable();
        labels = new DataLabels(sessionID);

    }

    /**
     * Summarizes the data into a set of values over a given time interval.
     * Assumes external synchronization of DataSets.
     *
     * @param points         the number of data points in the data summary
     * @param firstTime      the beginning of the time interval
     * @param lastTime       the end of the time interval
     * @param aggregation    the aggregation strategy employed
     * @param interpolation  the interpolation strategy employed
     * @return               the data summary
     */
    public SummaryData summarize(int points, double firstTime, double lastTime, TimeSeriesContext.Aggregation aggregation, TimeSeriesContext.Interpolation interpolation, boolean interpolateBeforeData) {
        int set, point;
        int offset;
        int element, numValues;
        double startInterval, endInterval, pixelTime;
        double pointValue;
        SummaryData summary;
        Iterator<DataPoint> iterator;
        DataPoint datum;
        double[] totals;

        summary = new SummaryData(data.size(), points, firstTime, lastTime, aggregation, interpolation, interpolateBeforeData);
        totals = new double[data.size()];
        pixelTime = summary.getPixelTime();

        for (set = 0; set < data.size(); set++) {
            /*
            // Fill empty points on the left with NaN's
            for ( int fill = 0 ; fill < offset ; fill++ ) {
            summary.setValue(set, fill, Double.NaN);
            //logger.info("Fill: " + fill + ", value: " + summary.getValue(set, fill));
            }
             **/
            iterator = data.elementAt(set).iterator();
            datum = null;
            totals[set] = 0;
            if (iterator.hasNext()) {
                datum = iterator.next();
            }
            element = 0;
            for (point = 0; point < points; point++) {
                startInterval = firstTime + point * pixelTime;
                endInterval = firstTime + (point + 1) * pixelTime;
                pointValue = 0;
                numValues = 0;
                //System.out.println("startInterval: " + (long)startInterval + " endInterval: " + (long)endInterval);

                try {
                    //while ( element < getNumberOfDataPoints() && ((DataPoint)(data[set].elementAt(element))).time < startInterval ) {
                    while (datum != null && datum.getTime() < startInterval) {
                        //System.out.println("Time ignored: " + findTime(element));
                        datum = iterator.next();
                    //element++;
                    }
                    //while ( element < getNumberOfDataPoints() && ((DataPoint)(data[set].elementAt(element))).time < endInterval ) {
                    while (datum != null && datum.getTime() < endInterval) {
                        if (datum.isDefined() == true) {
                            numValues++;
                            if (aggregation == TimeSeriesContext.Aggregation.Maximum) {
                                pointValue = Math.max(pointValue, datum.getValue());
                            } else if (aggregation == TimeSeriesContext.Aggregation.Average) {
                                pointValue += datum.getValue();
                            } else if (aggregation == TimeSeriesContext.Aggregation.Summed) {
                                pointValue += datum.getValue();
                            // System.out.println("Time chosen: " + findTime(element));
                            }
                        }
                        datum = iterator.next();
                    //element++;
                    }
                } // Thrown by iterator.next() if at end of data
                catch (NoSuchElementException e) {
                } finally {
                    if (aggregation == TimeSeriesContext.Aggregation.Average && numValues > 0) {
                        pointValue /= numValues;
                    } else if (aggregation == TimeSeriesContext.Aggregation.Summed && numValues > 0) {
                        pointValue = pointValue * 1000 / pixelTime;
                    }
                    if (numValues == 0) {
                        summary.setValue(set, point, Double.NaN);
                    } else {
                        summary.setValue(set, point, pointValue);
                        totals[set] += pointValue;
                    }
                }
            //System.out.println("startInterval: " + (long)startInterval + " endInterval: " + (long)endInterval);
            //System.out.println("pointValue: " + pointValue);
            }
        }

        summary.setTotals(totals);
        summary.generateRanks();
        summary.interpolate();
        return summary;
    }

    /**
     * Adds the named set to the list of data sets.  The new set is at the end
     * of the list of data sets.
     *
     * @param title name of data set to add
     * @return      the index of the new set added
     */
    public synchronized int addSet(String title) throws DBException {
        DataHeadingsDAO headingsDAO;

        logger.debug("Adding data set " + title);
        headings.add(title);
        data.add(new DataSet());

        // Database recording if enabled
        if (isCollector() && storing) {
            headingsDAO = DAOFactory.getDataHeadingsDAO();
            headingsDAO.create(sessionID, data.size() - 1, title);
            headingsDAO.closeConnection();
        }

        // Consistency check
        if (data.size() != headings.size()) {
            logger.error("Number of headings is " + headings.size() + " but number of data columns is " + data.size());
        }

        return data.size() - 1;
    }

    /** @return the headings for the data sets */
    public synchronized DataHeadings getHeadings() {
        return headings;
    }

    public int getSessionID() {
        return sessionID;
    }

    public void setSessionID(int sessionID) {
        this.sessionID = sessionID;
    }

    /**
     * Returns the time of the collection event in the first data set for the
     * index that is the given number of collection events before the most
     * recent collection event.
     *
     * @param index the number to subtract from the last time index
     * @return      the time of the collection event at that position
     */
    public synchronized long findNPollsAgoTime(int index) {
        if (index <= data.elementAt(0).size()) // return data[0].toArray(new DataPoint[data[0].size()])[data[0].size() - index].getTime();
        {
            return data.elementAt(0).get(data.elementAt(0).size() - index).getTime();
        } else {
            return -1;
        }
    }

    /**
     * Returns the time of the most recent collection event for the first data set.
     *
     * @return the time of the most recent collection event
     */
    public synchronized long findLastTime() {
        if (data.elementAt(0).size() != 0) {
            return data.elementAt(0).lastElement().getTime();
        } else {
            return -1;
        }
    }

    public synchronized long findFirstTime() {
        if (data.elementAt(0).size() != 0) {
            return data.elementAt(0).firstElement().getTime();
        } else {
            return -1;
        }
    }

    /** @return the number of data sets */
    public synchronized int getNumberOfSets() {
        return data.size();
    }

    /** @return the number of data points in the first data set */
    public synchronized int getNumberOfDataPoints() {
        try {
            return data.elementAt(0).size();
        } catch (ArrayIndexOutOfBoundsException e) {
            return 0;
        }
    }

    /** Begin the data collection.  This occurs in a separate thread. */
    public void startCollecting() {

        logger.debug("Attempting to start collector thread");
        if (isCollector() && collectorThread == null) {
            collectorThread = new Thread(this, "Collector");
            collectorThread.start();
        } else if (!isCollector()) {
            logger.warn("This is not a collector");
        } else if (collectorThread != null) {
            logger.warn("Collector thread already started");
        }
    }

    /**
     * The data collection thread.  This is invoked if the graph contains
     * a DataSets object that is a collector.  The thread collects data
     * roughly every polling interval as defined by the DataSets instance, to
     * which the new collected data is added.
     */
    public void run() {
        long timeAdjustment;
        long sleepTime;
        long interval;

        logger.debug("Starting collector thread");
        if (!isCollector()) {
            logger.error("DataSets is not a collector - cannot start collector thread");
            return;
        }
        interval = getPollInterval();
        timeAdjustment = 0;

        while (isCollecting()) {

            // Wait time interval for next poll
            sleepTime = interval - timeAdjustment;
            if (sleepTime < 0) {
                logger.warn("Negative sleep time (" + sleepTime + ") due to elapsed time exceeding polling interval (" + interval + ")");
            } else {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    //ErrorHandler.modalError(this, e.toString(), "Interrupted sleep", e);
                    logger.error("Interrupted sleep");
                }
            }
            timeAdjustment = System.currentTimeMillis();

            //logger.debug("Collecting data");
            collect();

            interval = getPollInterval();
            timeAdjustment = System.currentTimeMillis() - timeAdjustment;
        // logger.debug("Elapsed Time: " + Long.toString(timeAdjustment));
        }
        stopCollecting();

    }

    /**
     * Collects a single data value for each data set.
     * <p>
     * This method undertakes the following tasks as a part of collecting:
     * <ul>
     * <li> invokes getNextValues() of the collector and adds the resulting
     *      values to the end of each data set
     * <li> adds the collected values to the database if database storing is
     *      enabled
     * <li> removes the oldest data values if the size of the data sets exceeds
     *      the maximum limit
     * </ul>
     */
    public void collect() {
        //DataPoint[] newData;
        DataCollectorResponse response;

        try {
            response = collector.getNextValues(headings);
            //logger.debug("DataSets description: " + toString());
            //logger.debug(dumpData());

            synchronized (this) {

                // Add any new data sets created by collector
                for (int set = 0; set < response.newHeadings.length; set++) {
                    addSet(response.newHeadings[set]);
                }

                // Check for data set count consistency with collector
                if (data.size() != response.totalHeadings) {
                    logger.error("Number of data sets (" + data.size() + ") is different from collector (" + response.totalHeadings + ")");
                }

                // Add new data values
                for (int set = 0; set < data.size(); set++) {
                    //data.elementAt(set).add(response.values[set]);
                    data.elementAt(set).addDataPoint(response.values[set]);
                }
                // Remove data values that will not be displayed
                for (int set = 0; set < data.size(); set++) {
                    if (data.elementAt(set).size() >= Configuration.getPropertyInt("data.points.collecting.maximum")) {
                        data.elementAt(set).remove(0);
                    }
                }

                // Notify waiting threads that there is more data in the sets
                notifyAll();
            }

            // Database recording if enabled
            if (storing) {
                for (int set = 0; set < data.size(); set++) // Old school
                //dba.addValues(sessionID, set, data.elementAt(set).lastElement().getTime(),
                //        data.elementAt(set).lastElement().getValue() );
                // New school
                {
                    dataPointDAO.create(sessionID, set, data.elementAt(set).lastElement());
                }
                if (dataSetsDAO.retrieveSessionStartTime(sessionID) == 0) //dba.setStartTime(sessionID, data.lastElement().lastElement().getTime());
                {
                    dataSetsDAO.updateSessionStartTime(sessionID, data.lastElement().lastElement().getTime());
                //dba.setFinishTime(sessionID, data.lastElement().lastElement().getTime());
                }
                dataSetsDAO.updateSessionFinishTime(sessionID, data.lastElement().lastElement().getTime());

            }

        } catch (DBException e) {
            logger.warn("Cannot store values in database");
        }
    }

    /**
     * Adds a value to the specified data set.
     *
     * @param set    the index of the data set to add the data point to
     * @param point  the data point
     */
    /*
    public synchronized void addValue(int set, DataPoint point) throws DatabaseException {
    data.elementAt(set).add(point);
    if ( storing )
    dba.addValues(sessionID, set, data.elementAt(set).lastElement().getTime(),
    data.elementAt(set).lastElement().getValue() );
    }
     */

    /** @return the target device for data collection */
    public synchronized Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    /** @return the port(s) on the target device */
    public synchronized String getPortString() {
        return portString;
    }

    public void setPortString(String portString) {
        this.portString = portString;
    }

    /** @return the port(s) on the target device */
    public synchronized int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    /** @return the polling interval */
    public synchronized long getPollInterval() {
        return pollInterval;
    }

    public void setPollInterval(long pollInterval) {
        this.pollInterval = pollInterval;
    }

    /** @return the time of the first collection event */
    public synchronized long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    /** @return the time of the most recent collection event */
    public synchronized long getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(long finishTime) {
        this.finishTime = finishTime;
    }

    /** @return the title of the data sets */
    public synchronized String getTitle() {
        return title;
    }

    /** @param value  the new title for the data sets */
    public synchronized void setTitle(String value) throws DBException {
        title = value;
        if (storing) {
            DAOFactory.getDataSetsDAO().updateSession(this);
        }
    }

    /** @return units for the data set values */
    public synchronized String getValueUnits() {
        return valueUnits;
    }

    /** @param units for the data set values */
    public synchronized void setValueUnits(String units) {
        valueUnits = units;
    }

    /**
     * Returns an ordered set of data points for the given set.
     *
     * @param set index of set
     * @return    the ordered set of data points
     */
    public synchronized Vector getDataSetVector(int set) {
        return data.elementAt(set).getVector();
    }

    public synchronized DataSet getDataSet(int set) {
        return data.elementAt(set);
    }

    public synchronized void setDataSet(int index, DataSet dataSet) {
        data.set(index, dataSet);
    }

    public synchronized int getDataSetCount() {
        return data.size();
    }

    /**
     * Returns summary statistics for the given set.
     *
     * @param set index of set
     * @return    the summary statistics
     */
    public SummaryStatistics getSummaryStatistics(int set) {
        if (data.elementAt(set).size() > 0) {
            return data.elementAt(set).generateSummaryStatistics(data.elementAt(set).firstElement().getTime(), data.elementAt(set).lastElement().getTime());
        } else {
            return new SummaryStatistics();
        }
    }

    /** @return data type held */
    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    /** @return true if the data sets is a collector */
    public boolean isCollector() {
        return collector != null;
    }

    /** @return collector for data sets */
    public DataCollector getCollector() {
        return collector;
    }

    /** @return true if the data sets has an active collector */
    public boolean isCollecting() {
        return collecting;
    }

    /** @return true if the data is being stored in the database */
    public boolean isStoring() {
        return storing;
    }

    /** @return a short description of the data sets */
    public String getShortDescription() {
        StringBuffer description;

        if (dataType == DataType.CAPTURE) {
            return "Packet capture for " + portString;
        }
        description = new StringBuffer();
        description.append(dataType.description + " for " + device.getName());
        if (dataType == DataType.BANDWIDTH || dataType == DataType.NBAR || dataType == DataType.NETFLOW || dataType == DataType.STORAGE && portString != null) {
            description.append(" (" + portString);
            if (dataType == DataType.NBAR || dataType == DataType.NETFLOW) {
                description.append(",");
                description.append(DataSets.DIRECTIONS[direction]);
            }
            description.append(")");
        }
        return description.toString();
    }

    /** @return a full description for the data sets */
    public String getDescription() {
        if (device != null) {
            return DataSets.getDescription(dataType, device.getName(), portString, pollInterval, direction, startTime, finishTime);
        } else {
            return DataSets.getDescription(dataType, null, portString, pollInterval, direction, startTime, finishTime);
        }
    }

    /**
     * Creates a description of the database session with the given session ID.
     *
     * @param  session ID
     * @return a full description for the data sets
     */
    public static String retrieveDescription(int sessionID) throws DBException {
        DataSetsDAO dataSetsDAO;
        DataType retrievedDataType;
        String retrievedDevice;
        String retrievedPort;
        long retrievedPollInterval;
        int retrievedDirection;
        long retrievedStartTime;
        long retrievedFinishTime;

        dataSetsDAO = DAOFactory.getDataSetsDAO();

        retrievedDataType = DataType.types[dataSetsDAO.retrieveSessionDataTypeID(sessionID)];
        retrievedDevice = dataSetsDAO.retrieveSessionDevice(sessionID);
        retrievedPort = dataSetsDAO.retrieveSessionPort(sessionID);
        retrievedPollInterval = dataSetsDAO.retrieveSessionPollInterval(sessionID);
        retrievedDirection = dataSetsDAO.retrieveSessionDirection(sessionID);
        retrievedStartTime = dataSetsDAO.retrieveSessionStartTime(sessionID);
        retrievedFinishTime = dataSetsDAO.retrieveSessionFinishTime(sessionID);

        dataSetsDAO.closeConnection();

        return getDescription(retrievedDataType, retrievedDevice, retrievedPort, retrievedPollInterval,
                retrievedDirection, retrievedStartTime, retrievedFinishTime);
    }

    /**
     * Return a full description of the data sets described by the given information.
     *
     * @param dataType     type of data sets
     * @param deviceName   name of target collection device
     * @param portName     name of target port on device
     * @param pollInterval polling interval
     * @param direction    direction that the collection pertains to
     * @param startTime    time of initial collection event
     * @param finishTime   time of most recent collection event
     * @return             a full description for the data sets
     */
    public static String getDescription(DataType dataType, String deviceName, String portName, long pollInterval, int direction, long startTime, long finishTime) {
        StringBuffer description;
        SimpleDateFormat dateFormat, timeFormat;
        GregorianCalendar startCalendar, finishCalendar;

        dateFormat = new SimpleDateFormat("MMM d yyyy, H:mm:ss");
        timeFormat = new SimpleDateFormat("H:mm:ss");
        startCalendar = new GregorianCalendar();
        finishCalendar = new GregorianCalendar();
        description = new StringBuffer();
        description.append(dataType.description);
        if (deviceName != null) {
            description.append(" for " + deviceName);
        }
        if (dataType == DataType.BANDWIDTH || dataType == DataType.NBAR || dataType == DataType.NETFLOW || dataType == DataType.STORAGE && portName != null) {
            description.append(" (" + portName);
            if (dataType == DataType.NBAR || dataType == DataType.NETFLOW) {
                description.append(",");
                description.append(DataSets.DIRECTIONS[direction]);
            }
            description.append(")");
        }
        startCalendar.setTimeInMillis(startTime);
        description.append(" from " + dateFormat.format(startCalendar.getTime()) + " to ");
        finishCalendar.setTimeInMillis(finishTime);
        if (startCalendar.get(Calendar.DAY_OF_YEAR) == finishCalendar.get(Calendar.DAY_OF_YEAR) && startCalendar.get(Calendar.YEAR) == finishCalendar.get(Calendar.YEAR)) {
            description.append(timeFormat.format(finishCalendar.getTime()));
        } else {
            description.append(dateFormat.format(finishCalendar.getTime()));
        //description.append(" (" + pollInterval + " ms)");
        }
        return description.toString();
    }

    /**
     * Stops the collector.  This includes releasing any collector resources
     * and marking the session as "not collecting" in the database if the
     * session is being stored.
     */
    public void stopCollecting() {
        try {
            if (collector != null) {
                collector.releaseCollector();
            }
            collecting = false;
            if (storing && dataSetsDAO != null) //dba.setCollectingState(sessionID, false);
            {
                dataSetsDAO.updateSessionCollectingState(sessionID, false);
            }
        } catch (DBException e) {
            logger.warn("Cannot change collecting state of session in database");
        }
    }

    public void addInterpolatedDataPoints(SummaryData summary) {
        Iterator<DataPoint> iterator;
        DataPoint dataPoint;
        int insertionIndex;
        long pointTime;
        long previousPointTime;
        boolean firstPoint;

        for (int set = 0; set < summary.getNumberOfSets(); set++) {
            iterator = data.elementAt(set).iterator();
            insertionIndex = 0;
            previousPointTime = 0;
            firstPoint = false;
            for (int point = 0; point < summary.getNumberOfPoints(); point++) {
                if (firstPoint && summary.isInterpolated(set, point)) {
                    //if ( summary.isInterpolated(set, point) ) {
                    pointTime = (long) (summary.getTimeAtPoint(point) + summary.getPixelTime() / 2);
                    while (insertionIndex < data.elementAt(set).size() && pointTime > data.elementAt(set).elementAt(insertionIndex).getTime()) {
                        insertionIndex++;
                    }
                    //logger.debug("Added interpolated point: set=" + set + ",point=" + point + ",index=" + insertionIndex + ",time=" + DataPoint.formatTime(pointTime));
                    data.elementAt(set).add(insertionIndex, new DataPoint(pointTime, summary.getValue(set, point)));
                } else if (!summary.isInterpolated(set, point)) {
                    firstPoint = true;
                }
            }
        }
    }

    /** @return labels for this data */
    public synchronized DataLabels getLabels() {
        return labels;
    }

    public synchronized void setLabels(DataLabels labels) {
        this.labels = labels;
    }

    public String getCollectorName() {
        return collector.getClass().getSimpleName();
    }

    public String toString() {
        StringBuffer buffer;
        buffer = new StringBuffer();
        buffer.append("Device=" + device + ",");
        buffer.append("Interval=" + pollInterval + ",");
        buffer.append("DataSize=" + data.size() + ",");
        buffer.append("HeadingsSize=" + headings.size() + ",");
        buffer.append("Headings={");

        for (int set = 0; set < headings.size(); set++) {
            buffer.append(headings.getHeading(set));
            if (set != headings.size() - 1) {
                buffer.append(",");
            }
        }
        buffer.append("}");

        return buffer.toString();
    }

    /** @return a text dump of the data sets */
    public String dumpData() {
        StringBuffer buffer;

        buffer = new StringBuffer();
        for (int set = 0; set < data.size(); set++) {
            buffer.append("Set " + set + " (" + headings.getHeading(set) + "): ");
            for (int i = 0; i < data.elementAt(set).size(); i++) {
                buffer.append(data.elementAt(set).elementAt(i) + " ");
            }
            buffer.append("\n");
        }

        return buffer.toString();
    }

    public void deleteSession() throws DBException {
        DataSetsDAO dataSetsDAO;

        dataSetsDAO = DAOFactory.getDataSetsDAO();
        dataSetsDAO.deleteSession(sessionID);
        dataSetsDAO.closeConnection();
    }

}
