[//]: # " Copyright (c) 2018, 2020 Oracle and/or its affiliates. All rights reserved. "
[//]: # "  "
[//]: # " This program and the accompanying materials are made available under the "
[//]: # " terms of the Eclipse Distribution License v. 1.0, which is available at "
[//]: # " http://www.eclipse.org/org/documents/edl-v10.php. "
[//]: # "  "
[//]: # " SPDX-License-Identifier: BSD-3-Clause "

Progress Server
---------------
Whenever it receives a message from client, it sends n number of progress
messages to the client

Progress Client
---------------
Sends a message and listens for 5 progress messages from server.

Steps to run the sample :
------------------------------------------

1. Run server:

```shell script
    mvn exec:java@server
```

2. While server is up run the client:

```shell script
    mvn exec:java@client
```
