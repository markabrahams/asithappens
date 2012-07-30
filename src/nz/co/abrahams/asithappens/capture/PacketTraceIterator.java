/*
 * PacketTraceIterator.java
 *
 * Created on 30 April 2005, 16:24
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

import nz.co.abrahams.asithappens.storage.Device;
import nz.co.abrahams.asithappens.flow.Flow;
import nz.co.abrahams.asithappens.*;
import java.net.*;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.sql.Timestamp;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;

import com.voytechs.jnetstream.io.StreamFormatException;
import com.voytechs.jnetstream.io.EOPacketStream;
import com.voytechs.jnetstream.io.EOPacket;
import com.voytechs.jnetstream.io.RawformatInputStream;
import com.voytechs.jnetstream.io.PacketInputStream;
import com.voytechs.jnetstream.io.RandomAccessCaptureFile;
import com.voytechs.jnetstream.npl.SyntaxError;
import com.voytechs.jnetstream.primitive.MacAddressPrimitive;
import com.voytechs.jnetstream.primitive.IpAddressPrimitive;
import com.voytechs.jnetstream.primitive.PrimitiveException;
import com.voytechs.jnetstream.codec.Packet;
import com.voytechs.jnetstream.codec.Decoder;
import com.voytechs.jnetstream.io.StreamFormatException;
import com.voytechs.jnetstream.io.meta.MetaValue;
import com.voytechs.jnetstream.io.meta.CaptureMetaEnumerator;

import org.doomdark.uuid.EthernetAddress;

/**
 * Parses a packet trace file, providing information useful for building a
 * DataSets object with.
 *
 * @author mark
 */
public class PacketTraceIterator {
    
    /** Logging provider */
    protected Logger logger;
    /** Packet stream */
    protected PacketInputStream in;
    /** Name of packet trace file */
    protected String fileName;
    
    
    /**
     * Creates a new PacketTraceIterator.
     *
     * @param fileName  name of the packet trace file to parse
     */
    public PacketTraceIterator(String fileName) throws FileNotFoundException, IOException, EOPacketStream, StreamFormatException, SyntaxError {
        logger = Logger.getLogger(this.getClass().getName());
        //logger = Logger.getLogger("PacketTraceIterator");
        
        this.fileName = fileName;
        in = new RawformatInputStream(fileName);
        logger.info("Packet trace file: " + fileName);
        
    }
    
    /** @return a Flow object corresponding to the next packet in the trace file */
    public Flow next() {
        Flow flow;
        Device sourceDevice;
        Device destinationDevice;
        IpAddressPrimitive source = null;
        IpAddressPrimitive destination = null;
        MacAddressPrimitive src = null;
        MacAddressPrimitive dst = null;
        int etherProtocol = -1;
        int ipProtocol = -1;
        int srcPort = -1;
        int dstPort = -1;
        
        try {
            
            /* Aligns the position of the stream at beginning of packet */
            in.nextPacket();
            
            /* Returns the name of the first header */
            String linkType = in.getLinkType();
            int length = (int)in.getPacketLength();
            long timestamp = in.getCaptureTimestamp().getTime();
            
            if (linkType.equals("Ethernet") == true) {
                
                dst = new MacAddressPrimitive();
                dst.setValue(in);
                
                src = new MacAddressPrimitive();
                src.setValue(in);
                
                etherProtocol = in.readUnsignedShort();
                
                // Now check if its IP protocol
                if (etherProtocol == 0x800) {
                    int version =       in.readBits(4);
                    int hlen =          in.readBits(4);
                    int precedence =    in.readBits(3);
                    //int delay =         in.readBits(1);
                    //int throughtput =   in.readBits(1);
                    //int reliability =   in.readBits(1);
                    //in.readBits(2);     // Reserved 2 bits
                    
                    //int ipLength =      in.readUnsignedShort();
                    //int id =            in.readUnsignedShort();
                    
                    //in.readBits(1);     // Reserved 1 flag bit
                    
                    //int doNotFragment = in.readBits(1);
                    //int moreFragments = in.readBits(1);
                    
                    in.readBits(32);
                    in.readBits(8);
                    int offset =        in.readBits(13);
                    int timeToLive =    in.readUnsignedByte();
                    
                    ipProtocol =    in.readUnsignedByte();
                    int checksum =      in.readUnsignedShort();
                    
                    source = new IpAddressPrimitive();
                    source.setValue(in);
                    
                    destination = new IpAddressPrimitive();
                    destination.setValue(in);
                    
                    // Skip all the options
                    if ( hlen > 5 )
                        in.readBits((hlen - 5) * 32);
                    
                    //logger.debug("Ethernet " + src + " -> " + dst);
                    //logger.debug("IP " + source + " -> " + destination);
                    
                    if (ipProtocol == 6) {
                        srcPort = in.readUnsignedShort();
                        dstPort = in.readUnsignedShort();
                        long seqNumber = in.readUnsignedInt();
                        long ackNumber = in.readUnsignedInt();
                        
                        //int ipOffset = in.readBits(4);
                        // reserved
                        //in.readBits(6);
                        //int flags = in.readBits(6);
                        //int window = in.readUnsignedShort();
                        
                        in.readBits(32);
                        if ( offset > 5 )
                            in.readBits((offset - 5) * 32);
                        logger.debug("TCP: " + source + "(" + srcPort + ") -> "
                                + destination + "(" + dstPort + ")");
                        
                    } else if (ipProtocol == 17) {
                        srcPort = in.readUnsignedShort();
                        dstPort = in.readUnsignedShort();
                        
                        //int udpLength = in.readUnsignedShort();
                        //int udpChecksum = in.readUnsignedShort();
                        in.readBits(32);
                        logger.debug("UDP: " + source + "(" + srcPort + ") -> "
                                + destination + "(" + dstPort + ")");
                        
                    } else { // For all other IP protocols display number
                        logger.debug(
                                "IP: " + source + " -> " + destination + " protocol=0x"
                                + Integer.toHexString(ipProtocol) );
                    }
                    sourceDevice = new Device(InetAddress.getByName(source.toString()), new EthernetAddress(src.toString()));
                    destinationDevice = new Device(InetAddress.getByName(destination.toString()), new EthernetAddress(dst.toString()));
                    
                    return new Flow(timestamp, sourceDevice, destinationDevice, etherProtocol, ipProtocol, srcPort, dstPort, length);
                    
                } else {
                    logger.debug("Ethernet " + src + " -> " + dst
                            + " Ethertype=0x"
                            + Integer.toHexString(etherProtocol) );
                    sourceDevice = new Device(new EthernetAddress(src.toString()));
                    destinationDevice = new Device(new EthernetAddress(dst.toString()));
                    return new Flow(timestamp, sourceDevice, destinationDevice, etherProtocol, length);
                }
            } else {
                logger.debug("Unsupported packet type: " + linkType);
                return next();
            }
        } catch (StreamFormatException t) {
            t.printStackTrace();
        } catch (EOPacket eo) {
            eo.printStackTrace();
        } catch (PrimitiveException e) {
            e.printStackTrace();
        } catch (EOPacketStream eos) {
            // This is normal condition
            return null;
        } catch(IOException ie) {
            System.out.println("caught eof exception");
            ie.printStackTrace();
            return null;
        }
        return null;
    }
    
    /** @return true if there are more packets in the trace file */
    public boolean hasNext() {
        return in.isPacketReady();
    }
    
    /** @return the time of the first packet in the trace file */
    public long getStartTime() throws FileNotFoundException, IOException, EOPacketStream, StreamFormatException, SyntaxError {
        RawformatInputStream tempIn;
        tempIn = new RawformatInputStream(fileName);
        tempIn.nextPacket();
        return tempIn.getCaptureTimestamp().getTime();
    }
    
    /** @return the time of the last packet in the trace file */
    public long getFinishTime() throws FileNotFoundException, IOException, EOPacketStream, StreamFormatException, SyntaxError {
        RawformatInputStream tempIn;
        long startTime;
        
        startTime = System.currentTimeMillis();
        
        tempIn = new RawformatInputStream(fileName);
        int count = 0;
        try {
            while ( true ) {
                count++;
                tempIn.nextPacket();
                //System.out.println("Count: " + count + " at time " + tempIn.getCaptureTimestamp().getTime());
            }
        } catch (EOPacketStream e) {
        } catch (StreamFormatException e) {
        }
        logger.debug("Time taken: " + (System.currentTimeMillis() - startTime));
        return tempIn.getCaptureTimestamp().getTime();
    }
    
    /*
    public long getFinishTimeFailedExperiment() {
        RawformatInputStream tempIn;
        long startTime;
        File file;
        long backTrack;
        long returnTime;
        int count;
        
        backTrack = 50000;
        startTime = System.currentTimeMillis();
        file = new File(fileName);
        returnTime = -1;
        tempIn = null;
        count = 0;
        
        try {
            try {
            tempIn = new RawformatInputStream(fileName);
            //tempIn.skip(Math.max(0, file.length() - backTrack));
            tempIn.skip(2000);
            tempIn.nextPacket();
            }
            catch (Exception e) {}
            
            while ( true ) {
                count++;
                tempIn.nextPacket();
                //System.out.println("Count: " + count + " at time " + tempIn.getCaptureTimestamp().getTime());
            }
        } catch (EOPacketStream e) {
            logger.debug("EOPS, Time taken: " + (System.currentTimeMillis() - startTime) + ", count=" + count);
            returnTime = tempIn.getCaptureTimestamp().getTime();
        } catch (StreamFormatException e) {
            logger.debug("SF, Time taken: " + (System.currentTimeMillis() - startTime) + ", count=" + count);
            returnTime = tempIn.getCaptureTimestamp().getTime();            
        } catch (FileNotFoundException e) {
            logger.debug("FNF, Time taken: " + (System.currentTimeMillis() - startTime) + ", count=" + count);
            returnTime = tempIn.getCaptureTimestamp().getTime();            
        } catch (IOException e) {
            logger.debug("IO, Time taken: " + (System.currentTimeMillis() - startTime) + ", count=" + count);
            returnTime = tempIn.getCaptureTimestamp().getTime();            
        //} catch (SyntaxError e) {
        //    logger.debug("SE, Time taken: " + (System.currentTimeMillis() - startTime) + ", count=" + count);
        //    returnTime = tempIn.getCaptureTimestamp().getTime();            
        } finally {
            return returnTime;
        }
        
        
    }
    */
    
    protected void dumpMetaData() throws FileNotFoundException, IOException, EOPacketStream, StreamFormatException, SyntaxError {
        RawformatInputStream stream;
        java.util.Enumeration metaData;
        //CaptureMetaEnumerator metaData;
        MetaValue value;
        
        stream = new RawformatInputStream(fileName);
        metaData = stream.getMetaEnumeration();
        
        while ( metaData.hasMoreElements() ) {
            value = ((CaptureMetaEnumerator)metaData).nextMetaValue();
            logger.info("Meta value: " + value);
        }
    }
    
    /*
    public long getFinishTime() throws FileNotFoundException, IOException, EOPacketStream, StreamFormatException, SyntaxError {
        Decoder tempIn;
        Packet packet;
        tempIn = new Decoder(fileName);
        try {
            while ( true ) {
                packet = tempIn.nextPacketWithEOP();
            }
        } catch (EOPacketStream e) {
        } catch (StreamFormatException e) {
        } catch (Exception e) {
        }
        //return ((Timestamp)(packet.getProperty(Packet.CAPTURE_TIMESTAMP))).getTime();
        return 5;
    }
     */
}
