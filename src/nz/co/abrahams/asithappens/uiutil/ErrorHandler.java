/*
 * ErrorHandler.java
 *
 * Created on 18 January 2005, 22:07
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

package nz.co.abrahams.asithappens.uiutil;

import java.awt.Component;
import javax.swing.JOptionPane;
import javax.swing.JFrame;

/**
 * Provides generic error handling.
 *
 * @author mark
 */
public class ErrorHandler {
    
    /** Creates a new instance of ErrorHandler */
    public ErrorHandler() {
    }

    /**
     * Creates a simple modal error dialog.
     *
     * @param parent  component that dialog is modal on
     * @param body    body text of dialog
     * @param title   title of dialog
     */
    public static void modalError(Component parent, String body, String title) {
        JOptionPane errorDialog = new JOptionPane();
        errorDialog.showMessageDialog(parent, body, title, JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Creates a simple modal error dialog.
     *
     * @param parent  component that dialog is modal on
     * @param body    body text of dialog
     * @param title   title of dialog
     * @param e       exception to dump
     */
    public static void modalError(Component parent, String body, String title, Exception e) {
        e.printStackTrace();
        modalError(parent, body, title);
    }
    
    public static void nonModalError(String body, String title, Exception e) {
        JOptionPane errorDialog = new JOptionPane();
        errorDialog.showMessageDialog(null, body, title, JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Dumps a list of active threads in the current thread's ThreadGroup.
     */
    public static void threadDump() {
        ThreadGroup group;
        Thread[] threads;
        int numThreads;
        
        group = Thread.currentThread().getThreadGroup();
        numThreads = group.activeCount();
        System.out.println("Number of threads: " + numThreads);
        threads = new Thread[numThreads];
        group.enumerate(threads);
        
        for ( int i = 0 ; i < numThreads ; i++ ) {
            System.out.println("Thread " + i + ": " + threads[i]);
        }
    }
    
}
