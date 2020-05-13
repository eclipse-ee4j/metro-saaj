[//]: # " Copyright (c) 2018, 2020 Oracle and/or its affiliates. All rights reserved. "
[//]: # "  "
[//]: # " This program and the accompanying materials are made available under the "
[//]: # " terms of the Eclipse Distribution License v. 1.0, which is available at "
[//]: # " http://www.eclipse.org/org/documents/edl-v10.php. "
[//]: # "  "
[//]: # " SPDX-License-Identifier: BSD-3-Clause "

Hash service
-----------------

This is a sample application using the SAAJ API.

The Hashing service creates hash in every digest type available.

The hashes can be returned either as attachments to a SAAJ message or
within the SOAPBody depending on which option is chosen
in the radio button from the application's index.html.

Running the Sample
-------------------

1. Start tomcat server

```shell script
$CATALINA_HOME/bin/startup.sh
```

2. Run sample

```shell script
mvn tomcat7:deploy
```

2. Open in browser

http://localhost:8080/saaj-hash