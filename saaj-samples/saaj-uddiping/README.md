[//]: # " Copyright (c) 2018, 2020 Oracle and/or its affiliates. All rights reserved. "
[//]: # "  "
[//]: # " This program and the accompanying materials are made available under the "
[//]: # " terms of the Eclipse Distribution License v. 1.0, which is available at "
[//]: # " http://www.eclipse.org/org/documents/edl-v10.php. "
[//]: # "  "
[//]: # " SPDX-License-Identifier: BSD-3-Clause "

UDDIPing Client
--------------------

 Generates a simple query into a UDDI registry and prints the response out.

Steps to run the uddi client application:
------------------------------------------

1. Start local UDDI registry

```shell script
mvn jetty:run-war@run-jUDDI
```

2. Check if jUDDI is running, user name is `uddi_user` and password is `pass`

    http://localhost:8080/juddiv3-gui

3. Check services in the jUDDI, there should be **UDDI Inquiry REST Service**

    http://localhost:8080/juddiv3-gui/serviceBrowse.jsp

4. Run the sample, second parameter is the name of service we are looking for:

```shell script
mvn exec:java@uddi-ping -Dexec.args='http://localhost:8080/juddiv3/services/inquiryv2 "UDDI Inquiry REST Service"'
```

You should see result like this:
```xml
   ----------- Request Message ----------

   <?xml version="1.0" encoding="UTF-8" standalone="no"?>
   <SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
       <SOAP-ENV:Header/>
       <SOAP-ENV:Body>
           <uddi:find_service xmlns:uddi="urn:uddi-org:api_v2" generic="2.0" maxRows="100">
               <uddi:name>UDDI Inquiry REST Service</uddi:name>
           </uddi:find_service>
       </SOAP-ENV:Body>
   </SOAP-ENV:Envelope>
   Received reply from: http://localhost:8080/juddiv3/services/inquiryv2

   ----------- Reply Message ----------

   <?xml version="1.0" encoding="UTF-8"?><soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
       <soap:Body>
           <ns2:serviceList xmlns:ns2="urn:uddi-org:api_v2" generic="2.0" operator="uddi:juddi.apache.org:node1" truncated="false">
               <ns2:serviceInfos>
                   <ns2:serviceInfo serviceKey="uddi:juddi.apache.org:services-inquiry-rest" businessKey="uddi:juddi.apache.org:node1">
                       <ns2:name xml:lang="en">UDDI Inquiry REST Service</ns2:name>
                   </ns2:serviceInfo>
               </ns2:serviceInfos>
           </ns2:serviceList>
       </soap:Body>
   </soap:Envelope>
```
