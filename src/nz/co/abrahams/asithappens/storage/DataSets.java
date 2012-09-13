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

import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.*;
import nz.co.abrahams.asithappens.cartgraph.TimeSeriesContext;
import nz.co.abrahams.asithappens.collectors.*;
import nz.co.abrahams.asithappens.core.Configuration;
import nz.co.abrahams.asithappens.core.DAOFactory;
import nz.co.abrahams.asithappens.core.DBException;
import nz.co.abrahams.asithappens.core.DataType;
import nz.co.abrahams.asithappens.snmputil.SNMPException;
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

    /* Logging provider */
    protected static Logger logger = Logger.getLogger(DataSets.class);
    /** User-supplied title for data */
    //protected String title;
    /** Data type */
    //protected DataType dataType;
    /** Y-axis value units */
    //protected String valueUnits;
    /** Database session ID */
    protected int sessionID;
    /** Data collector */
    protected DataCollector collector;
    /** Indicates whether the DataSets object is currently collecting data */
    protected boolean collecting;
    /** Flag for database storing */
    //protected boolean storing;
    /** The time of the first collection event in the data sets */
    protected long startTime;
    /** The time of the final collection event in the data sets */
    protected long finishTime;
    /** Data attributes */
    protected DataAttributes attributes;
    /** The column heading titles */
    protected DataHeadings headings;
    /** The collected data is stored in an array of data sets */
    protected Vector<DataSet> data;
    /** Text labels */
    protected DataLabels labels;
    /** Collector runs in separate thread */
    protected Thread collectorThread;
    /** DataSets DAO for updating database for collecting sessions */
    protected DataSetsDAO dataSetsDAO;
    /** DataPoint DAO for writing newly collected data to database */
    protected DataPointDAO dataPointDAO;

    /**
     * Creates a new instance of DataSets that has an active collector.
     *
     * @param dataType      the type of data contained
     * @param collector     the collector that populates the data sets
     * @param title         the title of these sets of data
     * @param storing       indicates that data will be stored into the database
     */
      public DataSets(DataCollector collector)
            throws DBException, UnknownHostException, SNMPException {
        //this.dataType = dataType;
        //this.title = title;
        this.collector = collector;
        //this.storing = storing;
        
        //attributes = new DataAttributesCollector(collector.getDefinition(), title);
        attributes = collector.getDefinition();
        this.collecting = true;

        if (collector.getDefinition().getStoring()) {
            dataSetsDAO = DAOFactory.getDataSetsDAO();
            dataPointDAO = DAOFactory.getDataPointDAO();
            sessionID = DAOFactory.getDataSetsDAO().createSession(this);
        } else {
            dataSetsDAO = null;
            dataPointDAO = null;
            sessionID = -1;
        }

        initializeDataSets();
        for (int set = 0; set < collector.getDefinition().getInitialHeadings().length; set++) {
            addSet(collector.getDefinition().getInitialHeadings()[set]);
        }

        startCollecting();
    }

    /** Creates a new empty instance of DataSets */
    public DataSets() {
        //this.storing = false;
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
        this.startTime = startTime;
        this.finishTime = finishTime;
        //this.storing = false;
        this.collector = null;
        this.collecting = false;
        sessionID = -1;
        //dataType = DataType.CAPTURE;
        //title = getShortDescription();
        initializeDataSets();
    }

    /**
     * Initializes data sets variables.
     */
    protected void initializeDataSets() {

        data = new Vector();
        headings = new DataHeadings();
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
                    while (datum != null && datum.getTime() < startInterval) {
                        //System.out.println("Time ignored: " + findTime(element));
                        datum = iterator.next();
                    }
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
        if (isCollector() && collector.getDefinition().getStoring()) {
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
     * <li> invokes getNextValues of the collector and adds the resulting
     *      values to the end of each data set
     * <li> adds the collected values to the database if database storing is
     *      enabled
     * <li> removes the oldest data values if the size of the data sets exceeds
     *      the maximum limit
     * </ul>
     */
    public void collect() {
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
            if (collector.getDefinition().getStoring() && ! data.isEmpty() ) {
                for (int set = 0; set < data.size(); set++) {
                    dataPointDAO.create(sessionID, set, data.elementAt(set).lastElement());
                }
                if (dataSetsDAO.retrieveSessionStartTime(sessionID) == 0)
                {
                    dataSetsDAO.updateSessionStartTime(sessionID, data.lastElement().lastElement().getTime());
                }
                dataSetsDAO.updateSessionFinishTime(sessionID, data.lastElement().lastElement().getTime());
            }

        } catch (DBException e) {
            logger.warn("Cannot store values in database");
        }
    }
    
    /** @return retrievedAttributes for the data */
    public DataAttributes getAttributes() {
        return attributes;
    }
    
    public void setAttributes(DataAttributes newAttributes) {
        attributes = newAttributes;
    }
    
    /** @return the polling interval */
    public synchronized long getPollInterval() {
        //return ((DataAttributesCollector)attributes).getDefinition().getPollInterval();
        return collector.getDefinition().getPollInterval();
    }

    public void setPollInterval(long pollInterval) {
        //((DataAttributesCollector)attributes).getDefinition().setPollInterval(pollInterval);
        //this.pollInterval = pollInterval;
        collector.getDefinition().setPollInterval(pollInterval);
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
        return attributes.getTitle();
    }

    /** @param value  the new title for the data sets */
    public synchronized void setTitle(String title) throws DBException {
        attributes.setTitle(title);
        if (isCollector() && collector.getDefinition().getStoring()) {
            DAOFactory.getDataSetsDAO().updateSession(this);
        }
    }

    /** @return units for the data set values */
    public synchronized String getValueUnits() {
        return attributes.getUnits();
    }

    /** @param units for the data set values */
    public synchronized void setValueUnits(String units) {
        attributes.setUnits(units);
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
        return attributes.getDataType();
    }

    public void setDataType(DataType dataType) {
        attributes.setDataType(dataType);
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
        return collector != null && collector.getDefinition().getStoring();
    }

    public String getShortDescription() {
        return attributes.getDescription();
    }
    
    /** @return a full description for the data sets */
    public String getDescription() {
        return DataSets.getDescription(attributes.getDataType(), attributes, startTime, finishTime);
    }

    /**
     * Creates a description of the database session with the given session ID.
     *
     * @param  session ID
     * @return a full description for the data sets
     */
    public static String retrieveDescription(DataSetsDAO dataSetsDAO, int sessionID) {
        CollectorDefinitionDAO definitionDAO;
        DataType retrievedDataType;
        int collectorID;
        CollectorDefinition retrievedAttributes;
        long retrievedStartTime;
        long retrievedFinishTime;

        try {
            retrievedDataType = DataType.types[dataSetsDAO.retrieveSessionDataTypeID(sessionID)];
            collectorID = dataSetsDAO.retrieveSessionCollectorID(sessionID);
            definitionDAO = DAOFactory.getCollectorDefinitionDAO(dataSetsDAO.getConnection(), collectorID);
            retrievedAttributes = definitionDAO.retrieve(collectorID);
            retrievedStartTime = dataSetsDAO.retrieveSessionStartTime(sessionID);
            retrievedFinishTime = dataSetsDAO.retrieveSessionFinishTime(sessionID);

            return getDescription(retrievedDataType, retrievedAttributes, retrievedStartTime, retrievedFinishTime);
        } catch (UnknownHostException e) {
            logger.error("Unknown host in database " + sessionID, e);
            return "Unknown host";
        }
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
    public static String getDescription(DataType dataType, DataAttributes attributes, long startTime, long finishTime) {
        StringBuffer description;
        SimpleDateFormat dateFormat, timeFormat;
        GregorianCalendar startCalendar, finishCalendar;

        dateFormat = new SimpleDateFormat("MMM d yyyy, H:mm:ss");
        timeFormat = new SimpleDateFormat("H:mm:ss");
        startCalendar = new GregorianCalendar();
        finishCalendar = new GregorianCalendar();
        description = new StringBuffer();
        
        description.append(attributes.getDescription());
        
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
            if (isStoring() && dataSetsDAO != null) //dba.setCollectingState(sessionID, false);
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

    /*
    public void deleteSession() throws DBException {
        DataSetsDAO dataSetsDAO;

        dataSetsDAO = DAOFactory.getDataSetsDAO();
        dataSetsDAO.deleteSession(sessionID);
        dataSetsDAO.closeConnection();
    }
    */
}
