/*
 * PacketTraceLoadTask.java
 *
 * Created on 17 September 2006, 00:01
 */

package nz.co.abrahams.asithappens.capture;

import nz.co.abrahams.asithappens.storage.DataPoint;
import nz.co.abrahams.asithappens.flow.FlowOptions;
import nz.co.abrahams.asithappens.flow.Flow;
import nz.co.abrahams.asithappens.core.DBException;
import nz.co.abrahams.asithappens.uiutil.ErrorHandler;
import nz.co.abrahams.asithappens.uiutil.SwingWorker;
import nz.co.abrahams.asithappens.uiutil.LongTask;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.sql.SQLException;
//import javax.swing.*;

import org.apache.log4j.Logger;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import org.apache.log4j.Logger;

import com.voytechs.jnetstream.io.StreamFormatException;
import com.voytechs.jnetstream.npl.SyntaxError;
import com.voytechs.jnetstream.io.EOPacketStream;
import com.slytechs.jnetstream.file.PacketCounter;
import com.slytechs.jnetstream.file.PacketCounterModel;

/**
 *
 * @author mark
 */
public class PacketTraceLoadTask extends SwingWorker<FlowData> implements LongTask {
    
    /* Logging provider */
    protected final Logger logger;
    
    /* Length of task */
    protected long taskLength;
    
    /* Current progress of task */
    protected long taskProgress;
    
    /** Parent component that controls graph creation */
    protected final Component parent;
    
    /** The time of the first collection event in the data sets */
    protected final long startTime;
    
    /** The time of the final collection event in the data sets */
    protected final long finishTime;
    
    /** Packet trace file name */
    protected final String fileName;
    
    /** Flow options */
    protected final FlowOptions options;
    
    /** Resulting loaded data */
    protected final FlowData dataSets;
    
    /** Creates a new instance of LoadDataTask */
    public PacketTraceLoadTask(final Component parent, String fileName, long startTime, long finishTime, FlowOptions options)
    throws FileNotFoundException, IOException, EOPacketStream, StreamFormatException, SyntaxError,
            ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        super();
        logger = Logger.getLogger(this.getClass().getName());
        
        taskProgress = 0;
        taskLength = LongTask.COMPLETED;
        
        this.parent = parent;
        this.fileName = fileName;
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.options = options;
        
        dataSets = new FlowData(fileName, startTime, finishTime, options);
    }
    
    public FlowData construct() {
        try {
            parseTrace();
            return dataSets;
        } catch (Exception e) {
            ErrorHandler.modalError(parent, e.toString(), "Problem reading packet capture file", e);
            return null;
        }
    }
    
    public void finished() {
        try {
            if ( ! isCancelled() && get() != null )
                MainCapturePanel.createGraph(parent, dataSets);
        } catch (InterruptedException e) {
        } catch (ExecutionException e) {
        }
    }
    
    public int getProgress() {
        //return -1;
        return (int)(taskProgress * LongTask.COMPLETED / taskLength);
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
        
        taskLength = PacketCounter.newInstance(new File(fileName), PacketCounterModel.statistical).getCount();
        
        while ( (flow = source.next()) != null && ! isCancelled() ) {
            
            taskProgress++;
            if ( flow.timestamp >= startTime && flow.timestamp <= finishTime ) {

                foundMatch = false;
                for (int set = 0; set < dataSets.getDataSetCount(); set++) {
                    if ( flow.matches(dataSets.flows[set], options) ) {
                        foundMatch = true;
                        dataSets.getDataSet(set).add(new DataPoint(flow.timestamp, flow.length * 8));
                        logger.debug("Adding data to flow" + set + ": " + flow.timestamp + " " + flow.length);
                    }
                }
                
                if ( foundMatch == false ) {
                    temporaryFlows = new Flow[dataSets.getDataSetCount() + 1];
                    System.arraycopy(dataSets.flows, 0, temporaryFlows, 0, dataSets.getDataSetCount());
                    dataSets.flows = temporaryFlows;
                    dataSets.flows[dataSets.getDataSetCount()] = flow;
                    
                    logger.debug("Adding new data set " + flow.printable(options));
                    dataSets.addSet(flow.printable(options));
                    dataSets.getDataSet(dataSets.getDataSetCount() - 1).add(new DataPoint(flow.timestamp, flow.length * 8));
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
        taskProgress = taskLength;
    }
    
    protected void checkCancelled() {
        if ( isCancelled() )
            throw new CancellationException();
    }
}
