REM
REM  Copyright (c) 1997, 2018 Oracle and/or its affiliates. All rights reserved.
REM
REM  This program and the accompanying materials are made available under the
REM  terms of the Eclipse Distribution License v. 1.0, which is available at
REM  http://www.eclipse.org/org/documents/edl-v10.php.
REM
REM  SPDX-License-Identifier: BSD-3-Clause
REM

set SAAJ_LIB=..\..\lib
set JAXP_HOME=..\..\lib
set CLASSPATH=%SAAJ_LIB%\activation.jar;%SAAJ_LIB%\saaj-api.jar;%SAAJ_LIB%\saaj-impl.jar;%SAAJ_LIB%\jax-qname.jar;%JAXP_HOME%\jaxp-api.jar;%JAXP_HOME%\sax.jar;%JAXP_HOME%\dom.jar;%JAXP_HOME%\xercesImpl.jar;%JAXP_HOME%\xalan.jar;

java -classpath %CLASSPATH% Client
