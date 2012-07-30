-- MySQL dump 9.11
--
-- Host: localhost    Database: asithappens
-- ------------------------------------------------------
-- Server version	4.0.23_Debian-3-log

--
-- Table structure for table `data`
--

CREATE DATABASE asithappens;
USE asithappens;

CREATE TABLE `data` (
  `sessionID` int(11) NOT NULL default '0',
  `position` int(11) NOT NULL default '0',
  `time` bigint(20) NOT NULL default '0',
  `value` double default NULL,
  PRIMARY KEY  (`sessionID`,`time`,`position`)
);

CREATE TABLE `headings` (
  `sessionID` int(11) NOT NULL default '0',
  `position` int(11) NOT NULL default '0',
  `name` varchar(255) NOT NULL default '',
  PRIMARY KEY  (`sessionID`,`position`)
);

CREATE TABLE `labels` (
  `sessionID` int(11) NOT NULL default '0',
  `index` int(11) NOT NULL auto_increment,
  `time` bigint(20) NOT NULL default '0',
  `value` double NOT NULL default '0',
  `name` varchar(255) NOT NULL default '',
  PRIMARY KEY  (`index`)
);

CREATE TABLE `sessions` (
  `sessionID` int(11) NOT NULL auto_increment,
  `dataTypeID` int(11) NOT NULL default '0',
  `userVisible` tinyint(1) NOT NULL default '1',
  `device` varchar(255) NOT NULL default '',
  `pollInterval` bigint(20) default NULL,
  `port` varchar(255) default NULL,
  `startTime` bigint(20) NOT NULL default '0',
  `finishTime` bigint(20) default NULL,
  `collecting` tinyint(1) NOT NULL default '1',
  `title` varchar(255) default NULL,
  `direction` int(11) default NULL,
  `storing` tinyint(1) NOT NULL default '0',
  PRIMARY KEY  (`sessionID`)
);

CREATE TABLE `devices` (
  `name` varchar(255) NOT NULL,
  `ipAddress` varchar(255) default NULL,
  `hardwareAddress` varchar(255) default NULL,
  `communityRead` varchar(255) default NULL,
  `communityWrite` varchar(255) default NULL,
  PRIMARY KEY (`name`)
);

CREATE TABLE `layouts` (
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`name`)
);

CREATE TABLE `graphs` (
  `graphID` int(11) NOT NULL auto_increment,
  `layout` varchar(255) NOT NULL,
  `sessionID` int(11) NOT NULL,
  `x` int(11) default '0',
  `y` int(11) default '0',
  `width` int(11) default '200',
  `height` int(11) default '200',
  `xAxisScaling` int(11) default '0',
  `aggregation` int(11) default '0',
  `interpolation` int(11) default '0',
  `setsPositioning` varchar(255) default 'Grounded',
  `yAxisFormatUnits` tinyint(1) default '0',
  `bottomLeftLegend` tinyint(1) default '0',
  `autoGraphTop` double default '100',
  `fixedGraphTop` double default '0',
  `fixedGraphTopUnits` character(1) default ' ',
  `legendPanelWidth` int(11) default '0',
  `xAxisAbsoluteTimes` tinyint(1) default '1',
  `horizontalGridLines` tinyint(1) default '1',
  `horizontalMinorLines` tinyint(1) default '1',
  `verticalGridLines` tinyint(1) default '1',
  `verticalMinorLines` tinyint(1) default '1',
  `linesInFront` tinyint(1) default '1',
  `stickyWindow` tinyint(1) default '1',
  `showLabels` tinyint(1) default '1',
  `showTrim` tinyint(1) default '1',
  PRIMARY KEY (`graphID`)
);

CREATE TABLE `bandwidthCollectors` (
  `sessionID` int(11) NOT NULL,
  `prefer64BitCounters` tinyint(1) NOT NULL default '0',
  PRIMARY KEY (`sessionID`)
);

CREATE TABLE `bandwidthCollectorPorts` (
  `sessionID` int(11) NOT NULL,
  `port` varchar(255) NOT NULL,
  PRIMARY KEY (`sessionID`, `port`)
);

CREATE TABLE `nbarCollectors` (
  `sessionID` int(11) NOT NULL,
  `tableSize` int(11) NOT NULL,
  PRIMARY KEY (`sessionID`)
);

CREATE TABLE `netflowCollectors` (
  `sessionID` int(11) NOT NULL,
  `tableSize` int(11) NOT NULL,
  PRIMARY KEY (`sessionID`)
);

CREATE TABLE `netflowMatchCriteria` (
  `sessionID` int(11) NOT NULL,
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
  PRIMARY KEY (`sessionID`)
);

CREATE TABLE `flowOptions` (
  `sessionID` int(11) NOT NULL,
  `ipProtocol` tinyint(1) default '1',
  `ipSourceAddress` tinyint(1) default '1',
  `ipDestinationAddress` tinyint(1) default '1',
  `tosByte` tinyint(1) default '1',
  `tcpUdpSourcePort` tinyint(1) default '1',
  `tcpUdpDestinationPort` tinyint(1) default '1',
  PRIMARY KEY (`sessionID`)
);

CREATE TABLE `processorCollectors` (
  `sessionID` int(11) NOT NULL,
  `collectorType` varchar(255) NOT NULL,
  `snmpIndex` int(11) default '-1',
  PRIMARY KEY (`sessionID`)
);

CREATE TABLE `memoryCollectors` (
  `sessionID` int(11) NOT NULL,
  `collectorType` varchar(255) NOT NULL,
  `snmpIndex` int(11) default '-1',
  `ucdMemoryType` int(11) default '-1',
  PRIMARY KEY (`sessionID`)
);

CREATE TABLE `customOIDTemplate` (
  `templateID` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `graphType` int(11) NOT NULL,
  `yAxisUnits` varchar(255) NOT NULL,
  PRIMARY KEY (`templateID`)
);

CREATE TABLE `customOID` (
  `oidID` int(11) NOT NULL,
  `label` varchar(255) NOT NULL,
  `oid` varchar(255) NOT NULL,
  `oidType` varchar(255) NOT NULL,
  `description` varchar(255),
  `setDisplayType` varchar(255),
  `setDisplayColor` varchar(255),
);


GRANT ALL ON asithappens.* TO asithappens@'%' IDENTIFIED BY 'oneway';
GRANT ALL ON asithappens.* TO asithappens@'localhost' IDENTIFIED BY 'oneway';
FLUSH PRIVILEGES;
