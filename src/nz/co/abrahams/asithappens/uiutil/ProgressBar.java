/*
 * ProgressBar.java
 *
 * Created on 11 September 2006, 22:20
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

import nz.co.abrahams.asithappens.core.Configuration;
import nz.co.abrahams.asithappens.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import org.apache.log4j.Logger;

/**
 *
 * @author mark
 */
public class ProgressBar extends JFrame {
    
    /** Format for presenting start date/time */
    protected static final SimpleDateFormat startDateFormat = new SimpleDateFormat("MMM d, H:mm:ss");
    
    /** Delay in milliseconds after which progress bar is displayed */
    public final static int DISPLAY_AFTER = 1000;
    
    /** Delay in milliseconds between progress bar updates */
    public final static int TIMER_DELAY = 200;
    
    /** Logging provider */
    protected Logger logger;
    
    /** Title of progress frame */
    protected String title;
    
    /** Progress bar component*/
    private JProgressBar progressBar;
    
    /** Swing timer that fires events to update progress */
    private Timer timer;
    
    /** Asynchronous task to complete */
    private LongTask task;
    
    /** Task description */
    protected String description;
    
    /** Time task commenced in milliseconds since Jan 1, 1970 GMT */
    protected long startedTime;
    
    /** Creates a new ProgressBar frame. */
    public ProgressBar(String title, String description, LongTask task) {
        super(title);
        logger = Logger.getLogger(this.getClass().getName());
        this.title = title;
        this.task = task;
        this.description = description;
        
        startedTime = System.currentTimeMillis();
        initComponents();
        
        task.start();
        timer.start();
    }
    
    private void initComponents() {
        JPanel contentPane;
        JLabel descriptionLabel;
        JLabel startedLabel;
        JButton cancelButton;
        
        setIconImage(Toolkit.getDefaultToolkit().getImage(Configuration.FRAME_ICON));
        
        descriptionLabel = new JLabel(description);
        descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        startedLabel = new JLabel("Launched at: " + getStartedTime());
        startedLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        progressBar = new JProgressBar(0, LongTask.COMPLETED);
        
        cancelButton = new JButton("Cancel");
        cancelButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                task.cancel(true);
                timer.stop();
                dispose();
            }
        });
        
        contentPane = new JPanel();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
        contentPane.add(descriptionLabel);
        contentPane.add(Box.createRigidArea(new Dimension(0,5)));
        contentPane.add(startedLabel);
        contentPane.add(Box.createRigidArea(new Dimension(0,5)));
        contentPane.add(progressBar);
        contentPane.add(Box.createRigidArea(new Dimension(0,5)));
        contentPane.add(cancelButton);
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        
        //create a timer
        timer = new Timer(TIMER_DELAY, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                int progress;
                
                if ( ! isVisible() && System.currentTimeMillis() - startedTime > DISPLAY_AFTER ) {
                    pack();
                    setVisible(true);
                }
                progress = task.getProgress();
                if ( progress == -1 )
                    progressBar.setIndeterminate(true);
                else {
                    progressBar.setIndeterminate(false);
                    progressBar.setStringPainted(true);
                    progressBar.setValue(progress);
                }
                logger.debug("Current progress: " + progress + "%");
                if (task.isDone()) {
                    logger.debug("Finished task");
                    timer.stop();
                    if ( isVisible() )
                        dispose();
                }
            }
        });
    }
    
    protected String getStartedTime() {
        GregorianCalendar startCalendar;
        
        startCalendar = new GregorianCalendar();
        startCalendar.setTimeInMillis(startedTime);
        return startDateFormat.format(startCalendar.getTime());
    }
    
}
