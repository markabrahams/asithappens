/*
 * LoadDataTask.java
 *
 * Created on 13 September 2006, 16:23
 */

package nz.co.abrahams.asithappens.mainui;

import nz.co.abrahams.asithappens.storage.DataSetsDAO;
import nz.co.abrahams.asithappens.storage.DataSetDAO;
import nz.co.abrahams.asithappens.storage.DataSet;
import nz.co.abrahams.asithappens.storage.DataSets;
import nz.co.abrahams.asithappens.storage.Device;
import nz.co.abrahams.asithappens.core.DataType;
import nz.co.abrahams.asithappens.core.DAOFactory;
import nz.co.abrahams.asithappens.core.DBException;
import nz.co.abrahams.asithappens.core.Configuration;
import nz.co.abrahams.asithappens.cartgraph.TimeSeriesContext;
import nz.co.abrahams.asithappens.uiutil.ErrorHandler;
import nz.co.abrahams.asithappens.uiutil.SwingWorker;
import nz.co.abrahams.asithappens.uiutil.LongTask;
import java.awt.*;
import javax.swing.*;
import org.apache.log4j.Logger;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

/**
 *
 * @author mark
 */
public class DataSetsLoadTask extends SwingWorker<DataSets> implements LongTask {
    
    /* Logging provider */
    protected final Logger logger;
    
    /* Length of task */
    protected int taskLength;
    
    /* Current progress of task */
    protected int taskProgress;
    
    /** Parent component that controls graph creation */
    protected final Component parent;
    
    /** Database session ID */
    protected final int sessionID;
    
    /** The time of the first collection event in the data sets */
    protected final long startTime;
    
    /** The time of the final collection event in the data sets */
    protected final long finishTime;
    
    /** Data type of graph */
    //protected final int dataTypeID;
    
    /** Aggregation policy to use when loading data */
    protected final TimeSeriesContext.Aggregation aggregation;
    
    /** Initial interpolation policy for graph */
    protected final TimeSeriesContext.Interpolation interpolation;
    
    /** Resulting loaded data */
    protected final DataSets dataSets;
    
    //protected final SwingWorker worker;
    
    /** Creates a new instance of LoadDataTask */
    //public DataSetsLoadTask(final Component parent, final int sessionID, final long startTime, final long finishTime, final int dataTypeID, final int aggregation, final int interpolation) {
    public DataSetsLoadTask(final Component parent, final int sessionID, final long startTime, final long finishTime, final TimeSeriesContext.Aggregation aggregation, final TimeSeriesContext.Interpolation interpolation) {
        super();
        logger = Logger.getLogger(this.getClass().getName());
        
        taskProgress = 0;
        taskLength = LongTask.COMPLETED;
        
        this.parent = parent;
        this.sessionID = sessionID;
        this.startTime = startTime;
        this.finishTime = finishTime;
        //this.dataTypeID = dataTypeID;
        this.aggregation = aggregation;
        this.interpolation = interpolation;
        
        dataSets = new DataSets();
    }
    
    public DataSets construct() {
        try {
            loadData();
            return dataSets;
        } catch (DBException e) {
            ErrorHandler.modalError(parent, "Cannot retrieve data from database", "Please check database connectivity", e);
            return null;
        } catch (CancellationException e) {
            return null;
        }
    }

    @Override
    public void finished() {
        try {
            if ( ! isCancelled() && get() != null ) {
                // old way - no graph context loading
                //MainDatabasePanel.createGraph(parent, dataSets, dataTypeID, aggregation, interpolation);
                // new way - load graph context
                MainSessionsPanel.createGraphWithContext(parent, sessionID, dataSets, aggregation, interpolation);
            }
        } catch (InterruptedException e) {
        } catch (ExecutionException e) {
        }
        //ErrorHandler.threadDump();
    }

    /*
    public void cancel() {
        worker.cancel(true);
        taskProgress = taskLength;
    }
     */
    
    public int getProgress() {
        //return -1;
        return taskProgress * LongTask.COMPLETED / taskLength;
    }
    
    /**
     * Retrieves data sets information from the database.  The object's session
     * ID must be set before calling this.
     * <p>
     * The results in a given set will be aggregated if there are more than the
     * specified maximum in the configuration option
     * "data.points.fetched.maximum".
     *
     */
    public void loadData() throws DBException {
        //long time;
        DataSet results;
        //DatabaseAccess db;
        DataSetsDAO dataSetsDAO;
        DataSetDAO dataSetDAO;
        int sets;
        String[] headings;
        long intervalStart;
        long intervalEnd;
        long intervalTime;
        int numValues;
        //double pointValue;
        int fetchedMaximum;
        
        dataSets.setSessionID(sessionID);
        dataSets.setStartTime(startTime);
        dataSets.setFinishTime(finishTime);
        try {
            dataSetsDAO = DAOFactory.getDataSetsDAO();
            dataSetDAO = DAOFactory.getDataSetDAO();
            dataSets.setDataType(DataType.types[dataSetsDAO.retrieveSessionDataTypeID(sessionID)]);
            dataSets.setDevice(new Device(dataSetsDAO.retrieveSessionDevice(sessionID)));
            dataSets.setPortString(dataSetsDAO.retrieveSessionPort(sessionID));
            dataSets.setPollInterval(dataSetsDAO.retrieveSessionPollInterval(sessionID));
            dataSets.setTitle(dataSetsDAO.retrieveSessionTitle(sessionID));
            dataSets.setDirection(dataSetsDAO.retrieveSessionDirection(sessionID));
            headings = DAOFactory.getDataHeadingsDAO().retrieve(sessionID);
            for ( int set = 0; set < headings.length; set++ ) {
                dataSets.addSet(headings[set]);
            }
            fetchedMaximum = Configuration.getPropertyInt("data.points.fetched.maximum");
            
            logger.debug("Fetching data for " + headings.length + " data sets ("
                    + startTime + " to " + finishTime + ")");
            
            sets = dataSets.getDataSetCount();
            taskLength = sets * fetchedMaximum;
            for ( int set = 0; set < sets; set++ ) {
                taskProgress = set * fetchedMaximum;
                //logger.debug("Current: " + current);
                numValues = dataSetsDAO.retrieveNumberOfPoints(sessionID, set, startTime, finishTime);
                if ( numValues <= fetchedMaximum ) {
                    dataSets.setDataSet(set, dataSetDAO.retrieve(sessionID, set, startTime, finishTime + 1));
                    checkCancelled();
                } else {
                    intervalTime = ( finishTime - startTime ) / fetchedMaximum;
                    for ( int interval = 0; interval < fetchedMaximum; interval++ ) {
                        intervalStart = startTime + intervalTime * interval;
                        intervalEnd = startTime + intervalTime * (interval + 1);
                        results = dataSetDAO.retrieve(sessionID, set, intervalStart, intervalEnd);
                        dataSets.getDataSet(set).add(results.aggregate(aggregation, intervalStart, intervalEnd));
                        taskProgress = set * fetchedMaximum + interval;
                        checkCancelled();
                        //logger.debug("Point " + interval + " aggregation of " + results + " : (" + results.aggregate(aggregation, intervalStart, intervalEnd) + ")");
                    }
                }
            }
            dataSets.setLabels(DAOFactory.getDataLabelsDAO().retrieve(sessionID));
            taskProgress = taskLength;
            logger.debug("Finished loading data from database");
        } catch (DBException e) {
            logger.error("Error loading data from database");
            throw new DBException("Error loading data from database", e);
        }
    }
    
    protected void checkCancelled() {
        if ( isCancelled() )
            throw new CancellationException();
    }
}
