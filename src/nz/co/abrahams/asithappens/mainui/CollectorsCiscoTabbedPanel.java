/*
 * CollectorsCiscoTabbedPanel.java
 *
 * Created on 13 August 2012, 09:38
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

package nz.co.abrahams.asithappens.mainui;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import nz.co.abrahams.asithappens.accounting.MainMACPrecAccountingPanel;
import nz.co.abrahams.asithappens.accounting.MainIPAccountingPanel;
import nz.co.abrahams.asithappens.bandwidth.MainBandwidthPanel;
import nz.co.abrahams.asithappens.host.MainHostPanel;
import nz.co.abrahams.asithappens.nbar.MainNBARPanel;
import nz.co.abrahams.asithappens.netflow.MainNetFlowPanel;
import nz.co.abrahams.asithappens.oid.MainOIDPanel;
import nz.co.abrahams.asithappens.response.MainResponsePanel;

/**
 * The collectors JPanel containing the tabbed pane components.
 *
 * @author mark
 */
public class CollectorsCiscoTabbedPanel extends JPanel {
    
    JTabbedPane pane;
    MainNBARPanel nbarPanel;
    MainNetFlowPanel netflowPanel;
    MainIPAccountingPanel ipAccountingPanel;
    MainMACPrecAccountingPanel accountingPanel;
    
    /** Creates a new MainTabbedPanel. */
    public CollectorsCiscoTabbedPanel() {
        super(new java.awt.GridLayout(1, 1));
        initComponents();
    }
    
    /** Lays out GUI components. */
    private void initComponents() {
        pane = new JTabbedPane();
        nbarPanel = new MainNBARPanel();
        netflowPanel = new MainNetFlowPanel();
        ipAccountingPanel = new MainIPAccountingPanel();
        accountingPanel = new MainMACPrecAccountingPanel();
                
        pane.addTab("NBAR", null, nbarPanel, "NBAR Top-N collector");
        pane.addTab("NetFlow", null, netflowPanel, "NetFlow Top-N collector");
        pane.addTab("IP Acc", null, ipAccountingPanel, "IP accounting collector");
        pane.addTab("MAC/Prec Acc", null, accountingPanel, "MAC / IP Precedence accounting collector");
                
        add(pane);
    }
    
}
