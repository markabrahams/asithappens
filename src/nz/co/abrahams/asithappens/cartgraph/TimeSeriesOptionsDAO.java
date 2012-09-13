/*
 * TimeSeriesOptionsDAO.java
 *
 * Created on 25 June 2008, 20:41
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
 *
 */

package nz.co.abrahams.asithappens.cartgraph;

import nz.co.abrahams.asithappens.core.DAOFactory;
import nz.co.abrahams.asithappens.core.DBUtil;
import nz.co.abrahams.asithappens.core.DBException;
import nz.co.abrahams.asithappens.*;
import nz.co.abrahams.asithappens.uiutil.SetDisplayDAO;
import nz.co.abrahams.asithappens.uiutil.SetDisplay;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import org.apache.log4j.Logger;

/**
 *
 * @author mark
 */
public class TimeSeriesOptionsDAO {
    
    public static final String CREATE_OPTIONS = "INSERT INTO GraphOptions (" +
            "aggregation, interpolation, useFixedGraphTop, fixedGraphTop, fixedGraphTopUnits, " +
            "xAxisAbsoluteTimes, yAxisFormatUnits, bottomLeftLegend, " +
            "horizontalGridLines, horizontalMinorLines, verticalGridLines, verticalMinorLines, " +
            "linesInFront, stickyWindow, showLabels, showTrim, setsPositioning) VALUES " +
            "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    
    public static final String CREATE_OPTIONS_SETDISPLAYS = "INSERT INTO GraphOptionsSetDisplays (graphOptionsID, setNumber, setDisplayID) VALUES (?,?,?)";
    
    public static final String UPDATE_OPTIONS = "UPDATE GraphOptions SET " +
            "aggregation = ?, interpolation = ?, useFixedGraphTop = ?, fixedGraphTop = ?, fixedGraphTopUnits = ?, " +
            "xAxisAbsoluteTimes = ?, yAxisFormatUnits = ?, bottomLeftLegend = ?, " +
            "horizontalGridLines = ?, horizontalMinorLines = ?, verticalGridLines = ?, verticalMinorLines = ?, " +
            "linesInFront = ?, stickyWindow = ?, showLabels = ?, showTrim = ?, setsPositioning = ? " +
            "WHERE graphOptionsID = ?";
    
    public static final String RETRIEVE_GRAPH_ID = "SELECT graphID FROM Graphs WHERE sessionID = ?";
    
    public static final String RETRIEVE_SESSION_ID = "SELECT sessionID FROM Graphs WHERE graphID = ?";
    
    public static final String RETRIEVE_SETDISPLAYIDS = "SELECT setNumber, setDisplayID FROM GraphOptionsSetDisplays " +
            "WHERE graphOptionsID = ? ORDER BY setNumber";
    
    public static final String RETRIEVE_SETDISPLAYID = "SELECT setDisplayID FROM GraphOptionsSetDisplays " +
            "WHERE graphOptionsID = ? AND setNumber = ?";
    
    public static final String RETRIEVE_OPTIONS = "SELECT " +
            "aggregation, interpolation, useFixedGraphTop, fixedGraphTop, fixedGraphTopUnits, " +
            "xAxisAbsoluteTimes, yAxisFormatUnits, bottomLeftLegend, " +
            "horizontalGridLines, horizontalMinorLines, verticalGridLines, verticalMinorLines, " +
            "linesInFront, stickyWindow, showLabels, showTrim, setsPositioning FROM GraphOptions WHERE graphOptionsID = ?";
    
    /*
    public static final String RETRIEVE_SETDISPLAYS = "SELECT setNumber, style, color FROM " +
            "TimeSeriesOptions, GraphOptionsSetDisplays, SetDisplays WHERE TimeSeriesOptions.graphID = GraphOptionsSetDisplays.graphID " +
            "AND GraphOptionsSetDisplays.setDisplayID = SetDisplays.setDisplayID " +
            "AND TimeSeriesOptions.graphID = ? ORDER BY GraphOptionsSetDisplays.setNumber";
    */
    
    /** Logging provider */
    public static Logger logger = Logger.getLogger(DataGraphDAO.class);
    
    /** Database connection */
    Connection connection;
    
    /** Creates a new DataGraph DAO */
    public TimeSeriesOptionsDAO(Connection connection) {
        this.connection = connection;
    }
    
    public int createGraphOptions(TimeSeriesOptions options) throws DBException {
        SetDisplayDAO displayDAO;
        PreparedStatement statement;
        ResultSet results;
        int graphOptionsID;
        int setDisplayID;
        
        try {
            statement = connection.prepareStatement(CREATE_OPTIONS, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, options.getAggregation().toString());
            statement.setString(2, options.getInterpolation().toString());
            statement.setInt(3, options.getUseFixedGraphTop() ? 1 : 0);
            statement.setDouble(4, options.getFixedGraphTop());
            statement.setString(5, Character.toString(options.getFixedGraphTopUnits()));
            statement.setInt(6, options.getXAxisAbsoluteTimes() ? 1 : 0);
            statement.setInt(7, options.getYAxisFormattedUnits() ? 1 : 0);
            statement.setInt(8, options.getBottomLeftLegend() ? 1 : 0);
            statement.setInt(9, options.getHorizontalGridLines() ? 1 : 0);
            statement.setInt(10, options.getHorizontalMinorLines() ? 1 : 0);
            statement.setInt(11, options.getVerticalGridLines() ? 1 : 0);
            statement.setInt(12, options.getVerticalMinorLines() ? 1 : 0);
            statement.setInt(13, options.getLinesInFront() ? 1 : 0);
            statement.setInt(14, options.getStickyWindow() ? 1 : 0);
            statement.setInt(15, options.getShowLabels() ? 1 : 0);
            statement.setInt(16, options.getShowTrim() ? 1 : 0);
            statement.setString(17, options.getSetsPositioning().toString());
            statement.executeUpdate();
            results = statement.getGeneratedKeys();
            results.next();
            graphOptionsID = results.getInt(1);
            results.close();
            statement.close();
            logger.debug("Adding new graph options with ID " + graphOptionsID);
            
            displayDAO = DAOFactory.getSetDisplayDAO();
            for ( int set = 0 ; set < options.getSetDisplays().size() ; set++ ) {
                //setDisplayID = options.getSetDisplay(set).store();
                setDisplayID = displayDAO.create(options.getSetDisplay(set));
                statement = connection.prepareStatement(CREATE_OPTIONS_SETDISPLAYS);
                statement.setInt(1, graphOptionsID);
                statement.setInt(2, set);
                statement.setInt(3, setDisplayID);
                statement.executeUpdate();
                statement.close();
            }
            displayDAO.closeConnection();
            return graphOptionsID;
        } catch (SQLException e) {
            logger.error("Problem creating graph options in database");
            throw new DBException("Problem creating graph options in database", e);
        }
    }
    
    public void updateGraphOptions(int graphOptionsID, TimeSeriesOptions options) throws DBException {
        SetDisplayDAO displayDAO;
        PreparedStatement statement;
        ResultSet results;
        int graphID;
        int sessionID;
        int setDisplayID;
        
        try {
            statement = connection.prepareStatement(UPDATE_OPTIONS);
            statement.setString(1, options.getAggregation().toString());
            statement.setString(2, options.getInterpolation().toString());
            statement.setInt(3, options.getUseFixedGraphTop() ? 1 : 0);
            statement.setDouble(4, options.getFixedGraphTop());
            statement.setString(5, Character.toString(options.getFixedGraphTopUnits()));
            statement.setInt(6, options.getXAxisAbsoluteTimes() ? 1 : 0);
            statement.setInt(7, options.getYAxisFormattedUnits() ? 1 : 0);
            statement.setInt(8, options.getBottomLeftLegend() ? 1 : 0);
            statement.setInt(9, options.getHorizontalGridLines() ? 1 : 0);
            statement.setInt(10, options.getHorizontalMinorLines() ? 1 : 0);
            statement.setInt(11, options.getVerticalGridLines() ? 1 : 0);
            statement.setInt(12, options.getVerticalMinorLines() ? 1 : 0);
            statement.setInt(13, options.getLinesInFront() ? 1 : 0);
            statement.setInt(14, options.getStickyWindow() ? 1 : 0);
            statement.setInt(15, options.getShowLabels() ? 1 : 0);
            statement.setInt(16, options.getShowTrim() ? 1 : 0);
            statement.setString(17, options.getSetsPositioning().toString());
            statement.setInt(18, graphOptionsID);
            statement.executeUpdate();
            statement.close();
            logger.debug("Updating graph options with ID " + graphOptionsID);
            
            displayDAO = DAOFactory.getSetDisplayDAO();
            for ( int set = 0 ; set < options.getSetDisplays().size() ; set++ ) {
                statement = connection.prepareStatement(RETRIEVE_SETDISPLAYID);
                statement.setInt(1, graphOptionsID);
                statement.setInt(2, set);
                results = statement.executeQuery();
                results.next();
                logger.info("SetDisplayID: " + results.getInt(1));
                displayDAO.update(results.getInt(1), options.getSetDisplay(set));
                results.close();
                statement.close();
            }
            displayDAO.closeConnection();
            
        } catch (SQLException e) {
            logger.error("Problem Updating graph options in database");
            throw new DBException("Problem Updating graph options in database", e);
        }
        
    }
    
    /**
     * Loads graph ID for context information for the given session.
     *
     * @param sessionID  the desired session ID
     * @return           the graph ID
     */
    public int retrieveGraphIDForSession(int sessionID) throws DBException {
        return ((Integer)(DBUtil.retrieveSingleAttributeWithPK(connection, RETRIEVE_GRAPH_ID, sessionID))).intValue();
    }
    
    public TimeSeriesOptions retrieveGraphOptions(int graphOptionsID) throws DBException {
        PreparedStatement statement;
        ResultSet results;
        TimeSeriesOptions options;
        Vector<SetDisplay> setDisplays;
        SetDisplayDAO setDisplayDAO;
        
        try {
            statement = connection.prepareStatement(RETRIEVE_OPTIONS);
            statement.setInt(1, graphOptionsID);
            results = statement.executeQuery();
            results.next();
            options = new TimeSeriesOptions();
            options.setAggregation(TimeSeriesContext.Aggregation.valueOf(results.getString("aggregation")));
            options.setInterpolation(TimeSeriesContext.Interpolation.valueOf(results.getString("interpolation")));
            options.setSetsPositioning(SetDisplay.Positioning.valueOf(results.getString("setsPositioning")));
            options.setUseFixedGraphTop(results.getBoolean("useFixedGraphTop"));
            options.setFixedGraphTop(results.getDouble("fixedGraphTop"));
            // unimplemented, but add if needed:
            options.setYAxisFormattedUnits(results.getInt("yAxisFormatUnits") == 1);
            options.setBottomLeftLegend(results.getInt("bottomLeftLegend") == 1);
            try {
                options.setFixedGraphTopUnits(results.getString("fixedGraphTopUnits").charAt(0));
            } catch (StringIndexOutOfBoundsException e) {
                options.setFixedGraphTopUnits(' ');
            }
            options.setXAxisAbsoluteTimes(results.getInt("xAxisAbsoluteTimes") == 1);
            options.setHorizontalGridLines(results.getInt("horizontalGridLines") == 1);
            options.setHorizontalMinorLines(results.getInt("horizontalMinorLines") == 1);
            options.setVerticalGridLines(results.getInt("verticalGridLines") == 1);
            options.setVerticalMinorLines(results.getInt("verticalMinorLines") == 1);
            options.setLinesInFront(results.getInt("linesInFront") == 1);
            options.setStickyWindow(results.getInt("stickyWindow") == 1);
            options.setShowLabels(results.getInt("showLabels") == 1);
            options.setShowTrim(results.getInt("showTrim") == 1);
            results.close();
            statement.close();
            
            setDisplayDAO = DAOFactory.getSetDisplayDAO(connection);
            setDisplays = setDisplayDAO.retrieve(graphOptionsID);
            options.setSetDisplays(setDisplays);
            logger.debug(options.toString());
            return options;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("Problem loading graph options with id: " + graphOptionsID);
            throw new DBException("Problem loading graph options with id: " + graphOptionsID);
        }
    }
    
    /**
     * Closes database connection opened for this DAO.
     */
    public void closeConnection() throws DBException {
        try {
            connection.close();
        } catch (SQLException e) {
            logger.error("Error closing database connection for DataGraph DAO");
            throw new DBException("Error closing database connection for DataGraph DAO", e);
        }
    }
    
}
