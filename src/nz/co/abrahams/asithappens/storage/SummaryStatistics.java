/*
 * SummaryStatistics.java
 *
 * Created on 12 August 2006, 19:22
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

/**
 * Summary statistics for a set of data.
 *
 * @author mark
 */
public class SummaryStatistics {

    public int samples;
    public long firstTime;
    public long lastTime;
    public double minimum;
    public double maximum;
    public double average;
    public double sum;
    public double sumSquares;

    /** Creates a new instance of SummaryStatistics */
    public SummaryStatistics(int samples, long firstTime, long lastTime, double minimum, double maximum, double average) {
        this.samples = samples;
        this.firstTime = firstTime;
        this.lastTime = lastTime;
        this.minimum = minimum;
        this.maximum = maximum;
        this.average = average;
    }

    /** Creates a new instance of SummaryStatistics with default values */
    public SummaryStatistics() {
        this(0, -1, -1, 0, 0, 0);
    }

    public void addDataPoint(DataPoint point) {
        if (point.isDefined()) {
            samples++;
            if (firstTime == -1) {
                firstTime = point.getTime();
            }
            lastTime = point.getTime();
            minimum = Math.min(point.getValue(), minimum);
            maximum = Math.max(point.getValue(), maximum);
            sum += point.getValue();
            sumSquares += point.getValue() * point.getValue();
        }
    }
}
