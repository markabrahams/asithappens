#!/bin/bash

# asithappens.sh - start the AsItHappens java application

########################################################################
#
# Section 1: User-supplied variables - change to match environment

# Enter the path to your Java 1.5 executable
JAVA_EXE=/opt/jdk1.5.0_06/jre/bin/java

# Enter the location of the unpacked install
ASITHAPPENS_HOME=/opt/asithappens


########################################################################
#
# Section 2: System variables - do not change

ASITHAPPENS_JAR=$ASITHAPPENS_HOME/dist/asithappens.jar
ASITHAPPENS_LIBS=$ASITHAPPENS_HOME/lib
ASITHAPPENS_CONFIG=$ASITHAPPENS_HOME/config
MAIN_CLASS=nz.co.abrahams.asithappens.DatabaseCreate


########################################################################
#
# Section 3: Start-up script

cd $ASITHAPPENS_HOME

for file in `ls $ASITHAPPENS_LIBS` ; do
    ASITHAPPENS_CLASSPATH=$ASITHAPPENS_CLASSPATH:$ASITHAPPENS_LIBS/$file
done

ASITHAPPENS_CLASSPATH=$ASITHAPPENS_CLASSPATH:$ASITHAPPENS_JAR

#echo Classpath $ASITHAPPENS_CLASSPATH

$JAVA_EXE -classpath $ASITHAPPENS_CLASSPATH $MAIN_CLASS

