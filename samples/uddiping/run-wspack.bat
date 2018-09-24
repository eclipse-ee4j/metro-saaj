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
set JAXP_LIB=..\..\..\jaxp\lib\endorsed
set SHARED_LIB=..\..\..\jwsdp-shared\lib

set CLASSPATH=%SHARED_LIB%\activation.jar;%SAAJ_LIB%\saaj-api.jar;%SHARED_LIB%\mail.jar;%SAAJ_LIB%\saaj-impl.jar;%JAXP_LIB%\..\jaxp-api.jar;%JAXP_LIB%\sax.jar;%JAXP_LIB%\dom.jar;%JAXP_LIB%\xercesImpl.jar;%JAXP_LIB%\xalan.jar;

java -classpath %CLASSPATH% UddiPing %1 %2
