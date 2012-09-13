/*
 * TextAccessObject.java
 *
 * Created on 07 September 2012, 17:48
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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import nz.co.abrahams.asithappens.storage.*;

/**
 *
 * @author mark
 */
public class TextAccessObject {

    public TextAccessObject() {
    }

    public void exportSession(int sessionID, File savedFile) throws IOException {
        FileWriter out;
        Connection connection;
        DataSetsDAO dataSetsDAO;
        DataSetDAO dataSetDAO;
        DataHeadingsDAO dataHeadingsDAO;
        String[] headings;
        DataSet dataSet;
        DataPoint point;

        connection = DBUtil.getConnection();
        dataSetsDAO = DAOFactory.getDataSetsDAO(connection);
        dataSetDAO = DAOFactory.getDataSetDAO(connection);

        out = new FileWriter(savedFile);

        out.write("Title: " + dataSetsDAO.retrieveSessionTitle(sessionID) + "\n");
        //out.write("Description: " + DataSets.retrieveDescription(sessionID) + "\n\n");

        dataHeadingsDAO = DAOFactory.getDataHeadingsDAO(connection);
        headings = dataHeadingsDAO.retrieve(sessionID);

        for (int set = 0; set < headings.length; set++) {
            out.write("Set " + set + ": " + headings[set] + "\n");
        }
        out.write("\n");

        out.write("Set,Time,Value\n");
        for (int set = 0; set < headings.length; set++) {
            dataSet = dataSetDAO.retrieve(sessionID, set);
            //out.write("Set " + set + ": " + headings[set] + "\n");
            for (int i = 0; i < dataSet.size(); i++) {
                point = dataSet.elementAt(i);
                out.write(set + "," + point.getTime() + "," + point.getValue() + "\n");
            }
            //out.write("\n");
        }

        out.close();
        dataSetsDAO.closeConnection();
    }
    
    public DataSets importSession(int sessionID) {
        return null;
    }
}
