#!/bin/sh

AIH_HOME=/home/mark/asithappens/asithappens

cd $AIH_HOME
mysql -u root < sql/db-create.sql
