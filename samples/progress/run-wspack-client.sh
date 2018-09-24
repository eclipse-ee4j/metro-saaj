#! /bin/sh
#
# Copyright (c) 1997, 2018 Oracle and/or its affiliates. All rights reserved.
#
# This program and the accompanying materials are made available under the
# terms of the Eclipse Distribution License v. 1.0, which is available at
# http://www.eclipse.org/org/documents/edl-v10.php.
#
# SPDX-License-Identifier: BSD-3-Clause
#



if [ -z "$JAVA_HOME" ]
then
JAVACMD=`which java`
if [ -z "$JAVACMD" ]
then
echo "Cannot find JAVA. Please set your PATH."
exit 1
fi
JAVA_BINDIR=`dirname $JAVACMD`
JAVA_HOME=$JAVA_BINDIR/..
fi

JAVACMD=$JAVA_HOME/bin/java

oldCP=$CLASSPATH
 
SAAJ_LIB=../../lib
JAXP_LIB=../../../jaxp/lib/endorsed
SHARED_LIB=../../../jwsdp-shared/lib

unset CLASSPATH
for i in $SAAJ_LIB/*.jar ; do
  if [ "$CLASSPATH" != "" ]; then
       CLASSPATH=${CLASSPATH}:$i
  else
    CLASSPATH=$i
  fi
done

CLASSPATH=$CLASSPATH:.:$JAVA_HOME/lib/tools.jar:$SHARED_LIB/activation.jar:$SHARED_LIB/jax-qname.jar:$JAXP_LIB/../jaxp-api.jar:$JAXP_LIB/sax.jar:$JAXP_LIB/dom.jar:$JAXP_LIB/xercesImpl.jar:$JAXP_LIB/xalan.jar:$oldCP

$JAVACMD -classpath $CLASSPATH Client
CLASSPATH=${oldCP}
export CLASSPATH
