[//]: # " Copyright (c) 2018, 2020 Oracle and/or its affiliates. All rights reserved. "
[//]: # "  "
[//]: # " This program and the accompanying materials are made available under the "
[//]: # " terms of the Eclipse Distribution License v. 1.0, which is available at "
[//]: # " http://www.eclipse.org/org/documents/edl-v10.php. "
[//]: # "  "
[//]: # " SPDX-License-Identifier: BSD-3-Clause "

Book sample
--------------------

 Create, send and receive simple SOAP request

Steps to run the sample :
------------------------------------------

**Using tomcat**

1. Start tomcat server

```shell script
$CATALINA_HOME/bin/startup.sh
```

2. Deploy sample
    
```shell script
mvn tomcat7:deploy -Ptomcat
```
    
3. Open in browser following url and follow instructions
    
    http://localhost:8080/saaj-book
        
**Using Jetty**

1. Deploy sample
        
```shell script
mvn jetty:run
```

2. Open in browser following url and follow instructions

    http://localhost:8080/
