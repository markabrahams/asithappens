CREATE TABLE `Sessions` (
  `sessionID` int(11) NOT NULL auto_increment,
  `dataTypeID` int(11) NOT NULL default '0',
  `collectorID` int(11) default NULL,
--  `userVisible` tinyint(1) NOT NULL default '1',
  `startTime` bigint(20) NOT NULL default '0',
  `finishTime` bigint(20) default NULL,
  `collecting` tinyint(1) NOT NULL default '1',
  `title` varchar(255) default NULL,
  PRIMARY KEY  (`sessionID`)
);

CREATE TABLE `Headings` (
  `sessionID` int(11) NOT NULL default '0',
  `position` int(11) NOT NULL default '0',
  `name` varchar(255) NOT NULL default '',
  PRIMARY KEY  (`sessionID`,`position`)
);

CREATE TABLE `Data` (
  `sessionID` int(11) NOT NULL default '0',
  `position` int(11) NOT NULL default '0',
  `time` bigint(20) NOT NULL default '0',
  `value` double default NULL,
  PRIMARY KEY  (`sessionID`,`time`,`position`)
);

CREATE TABLE `Labels` (
  `sessionID` int(11) NOT NULL default '0',
  `index` int(11) NOT NULL auto_increment,
  `time` bigint(20) NOT NULL default '0',
  `value` double NOT NULL default '0',
  `name` varchar(255) NOT NULL default '',
  PRIMARY KEY  (`index`)
);

CREATE TABLE `Devices` (
  `name` varchar(255) NOT NULL,
  `ipAddress` varchar(255) default NULL,
  `hardwareAddress` varchar(255) default NULL,
  `snmpVersionRead` int(11) NOT NULL default '1',
  `snmpVersionWrite` int(11) NOT NULL default '1',
  `communityRead` varchar(255) default NULL,
  `communityWrite` varchar(255) default NULL,
  `userReadID` int(11) default NULL,
  `userWriteID` int(11) default NULL,
  PRIMARY KEY (`name`)
);

CREATE TABLE `UsmUsers` (
  `userID` int(11) NOT NULL auto_increment,
  `userName` varchar(255) default NULL,
  `userLevel` int(11) NOT NULL default '1',
  `userAuthProtocol` int(11) NOT NULL default '1',
  `userAuthKey` varchar(255) default NULL,
  `userPrivProtocol` int(11) NOT NULL default '1',
  `userPrivKey` varchar(255) default NULL,
  PRIMARY KEY (`userID`)
);

CREATE TABLE `Layouts` (
  `layoutName` varchar(255) NOT NULL,
  PRIMARY KEY (`layoutName`)
);

CREATE TABLE `Graphs` (
  `graphID` int(11) NOT NULL auto_increment,
  `isTemplate` tinyint(1) NOT NULL default '0',
  `sessionID` int(11) NOT NULL,
  `collectorID` int(11) NOT NULL,
  `graphOptionsID` int(11) NOT NULL,
  `x` int(11) default '0',
  `y` int(11) default '0',
  `width` int(11) default '200',
  `height` int(11) default '200',
  PRIMARY KEY (`graphID`)
);

CREATE TABLE `GraphOptions` (
  `graphOptionsID` int(11) NOT NULL auto_increment,
  `aggregation` varchar(255) default 'Average',
  `interpolation` varchar(255) default 'None',
  `useFixedGraphTop` tinyint(1) default '0',
  `fixedGraphTop` double default '0',
  `fixedGraphTopUnits` character(1) default ' ',
  `xAxisAbsoluteTimes` tinyint(1) default '1',
  `yAxisFormatUnits` tinyint(1) default '0',
  `bottomLeftLegend` tinyint(1) default '0',
  `horizontalGridLines` tinyint(1) default '1',
  `horizontalMinorLines` tinyint(1) default '1',
  `verticalGridLines` tinyint(1) default '1',
  `verticalMinorLines` tinyint(1) default '1',
  `linesInFront` tinyint(1) default '1',
  `stickyWindow` tinyint(1) default '1',
  `showLabels` tinyint(1) default '1',
  `showTrim` tinyint(1) default '1',
  `setsPositioning` varchar(255) default 'Grounded',
  PRIMARY KEY (`graphOptionsID`)
);

CREATE TABLE `LayoutGraphs` (
  `layoutName` varchar(255) NOT NULL,
  `graphID` int(11) NOT NULL,
  PRIMARY KEY (`layoutName`,`graphID`)
);

CREATE TABLE `SetDisplays` (
  `setDisplayID` int(11) NOT NULL auto_increment,
  `style` varchar(255),
  `color` int(11),
  PRIMARY KEY (`setDisplayID`)
);

CREATE TABLE `GraphOptionsSetDisplays` (
  `graphOptionsID` int(11) NOT NULL,
  `setNumber` int(11) NOT NULL,
  `setDisplayID` int(11) NOT NULL,
  PRIMARY KEY (`graphOptionsID`,`setNumber`)
);

CREATE TABLE `CollectorDefinitions` (
  `collectorID` int(11) NOT NULL auto_increment,
  `collectorType` int(11) default NULL,
  `title` varchar(255) default NULL,
  `storing` tinyint(1) NOT NULL default '0',
  `device` varchar(255) NOT NULL default '',
  `pollInterval` bigint(20) default NULL,
  `dataType` int(11) NOT NULL,
  `units` varchar(255) NOT NULL,
  PRIMARY KEY(`collectorID`)
);

CREATE TABLE `InterfaceDirectionCollectorDefinitions` (
  `collectorID` int(11) NOT NULL,
  `ifDescr` varchar(255) NOT NULL default '',
  `direction` int(11) NOT NULL default '0',
  PRIMARY KEY(`collectorID`)
);

CREATE TABLE `BandwidthCollectors` (
  `collectorID` int(11) NOT NULL,
  `prefer64BitCounters` tinyint(1) NOT NULL default '0',
  PRIMARY KEY (`collectorID`)
);

CREATE TABLE `BandwidthCollectorPorts` (
  `collectorID` int(11) NOT NULL,
  `port` varchar(255) NOT NULL,
  PRIMARY KEY (`collectorID`, `port`)
);

CREATE TABLE `ResponseCollectors` (
  `collectorID` int(11) NOT NULL,
  PRIMARY KEY (`collectorID`)
);

CREATE TABLE `NbarCollectors` (
  `collectorID` int(11) NOT NULL,
  `tableSize` int(11) NOT NULL,
  PRIMARY KEY (`collectorID`)
);

CREATE TABLE `NetflowCollectors` (
  `collectorID` int(11) NOT NULL,
  `tableSize` int(11) NOT NULL,
  PRIMARY KEY (`collectorID`)
);

CREATE TABLE `NetflowMatchCriteria` (
  `collectorID` int(11) NOT NULL,
  `srcAddressType` int(11) default '0',
  `srcAddress` varchar(255) default NULL,
  `srcAddressMask` int(11) default '0',
  `dstAddressType` int(11) default '0',
  `dstAddress` varchar(255) default NULL,
  `dstAddressMask` int(11) default '0',
  `srcPortLo` int(11) default '-1',
  `srcPortHi` int(11) default '-1',
  `dstPortLo` int(11) default '-1',
  `dstPortHi` int(11) default '-1',
  `srcAS` int(11) default '-1',
  `dstAS` int(11) default '-1',
  `protocol` int(11) default '-1',
  `tosByte` int(11) default '-1',
  `nhAddressType` int(11) default '0',
  `nhAddress` varchar(255) default NULL,
  `nhAddressMask` int(11) default '0',
  `inputIf` int(11) default '0',
  `outputIf` int(11) default '0',
  `sampler` varchar(255) default '',
  `classMap` varchar(255) default '',
  `minPackets` int(11) default '0',
  `maxPackets` int(11) default '0',
  `minBytes` int(11) default '0',
  `maxBytes` int(11) default '0',
  PRIMARY KEY (`collectorID`)
);

CREATE TABLE `FlowOptions` (
  `collectorID` int(11) NOT NULL,
  `ipProtocol` tinyint(1) default '1',
  `ipSourceAddress` tinyint(1) default '1',
  `ipDestinationAddress` tinyint(1) default '1',
  `tosByte` tinyint(1) default '1',
  `tcpUdpSourcePort` tinyint(1) default '1',
  `tcpUdpDestinationPort` tinyint(1) default '1',
  PRIMARY KEY (`collectorID`)
);

CREATE TABLE `ProcessorCiscoCollectors` (
  `collectorID` int(11) NOT NULL,
  PRIMARY KEY (`collectorID`)
);

CREATE TABLE `ProcessorHRCollectors` (
  `collectorID` int(11) NOT NULL,
  `processorDescr` varchar(255) NOT NULL default '',
  `snmpIndex` int(11) default '-1',
  PRIMARY KEY (`collectorID`)
);

CREATE TABLE `ProcessorUCDCollectors` (
  `collectorID` int(11) NOT NULL,
  PRIMARY KEY (`collectorID`)
);

CREATE TABLE `MemoryCiscoCollectors` (
  `collectorID` int(11) NOT NULL,
  `memoryDescr` varchar(255) NOT NULL default '',
  `snmpIndex` int(11) default '-1',
  PRIMARY KEY (`collectorID`)
);

CREATE TABLE `MemoryHRCollectors` (
  `collectorID` int(11) NOT NULL,
  `memoryDescr` varchar(255) NOT NULL default '',
  `snmpIndex` int(11) default '-1',
  PRIMARY KEY (`collectorID`)
);

CREATE TABLE `MemoryUCDCollectors` (
  `collectorID` int(11) NOT NULL,
  `memoryType` int(11) default '-1',
  PRIMARY KEY (`collectorID`)
);

CREATE TABLE `CustomOIDCollectors` (
  `collectorID` int(11) NOT NULL,
  `valueUnits` varchar(255) NOT NULL default '',
  PRIMARY KEY (`collectorID`)
);

CREATE TABLE `CustomOIDCollectorOIDs` (
  `collectorID` int(11) NOT NULL,
  `position` int(11) NOT NULL,
  `oidID` int(11) NOT NULL,
  PRIMARY KEY (`collectorID`,`position`)
);

CREATE TABLE `CustomOIDTemplates` (
  `templateID` int(11) NOT NULL auto_increment,
  `name` varchar(255) NOT NULL,
--  `setsPositioning` varchar(255) NOT NULL,
--  `yAxisUnits` varchar(255) NOT NULL,
  PRIMARY KEY (`templateID`)
);

CREATE TABLE `CustomOIDs` (
  `oidID` int(11) NOT NULL auto_increment,
  `label` varchar(255) NOT NULL,
  `oid` varchar(255) NOT NULL,
  `oidType` varchar(255) NOT NULL,
  `description` varchar(255),
  PRIMARY KEY (`oidID`)
);

CREATE TABLE `CustomOIDTemplateOIDs` (
  `templateID` int(11) NOT NULL,
  `position` int(11) NOT NULL,
  `oidID` int(11) NOT NULL,
  `setDisplayID` int(11) NOT NULL,
  PRIMARY KEY (`templateID`,`position`)
);

CREATE TABLE `IPAccountingActiveCollectors` (
  `collectorID` int(11) NOT NULL,
  PRIMARY KEY (`collectorID`)
);

CREATE TABLE `IPAccountingCheckpointCollectors` (
  `collectorID` int(11) NOT NULL,
  PRIMARY KEY (`collectorID`)
);

CREATE TABLE `MACAccountingCollectors` (
  `collectorID` int(11) NOT NULL,
  PRIMARY KEY (`collectorID`)
);

CREATE TABLE `IPPrecAccountingCollectors` (
  `collectorID` int(11) NOT NULL,
  PRIMARY KEY (`collectorID`)
);

CREATE TABLE `SNMPTableCollectors` (
  `collectorID` int(11) NOT NULL,
  `dataType` int(11) NOT NULL default '0',
  `snmpType` int(11) NOT NULL default '0',
  PRIMARY KEY (`collectorID`)
);
