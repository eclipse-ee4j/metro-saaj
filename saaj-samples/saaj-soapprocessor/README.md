[//]: # " Copyright (c) 2018, 2020 Oracle and/or its affiliates. All rights reserved. "
[//]: # "  "
[//]: # " This program and the accompanying materials are made available under the "
[//]: # " terms of the Eclipse Distribution License v. 1.0, which is available at "
[//]: # " http://www.eclipse.org/org/documents/edl-v10.php. "
[//]: # "  "
[//]: # " SPDX-License-Identifier: BSD-3-Clause "

SOAPProcessor
-----------

This sample provides an API that implements a SOAPProcessor, which implements
the processing model described in "The SOAP Message Exchange Model" at
http://www.w3.org/TR/2000/NOTE-SOAP-20000508/#_Toc478383491.

A SOAPProcessor consists of a set of soap recipients and a set of soap
annotators.

When a message is received, the SOAPProcessor reads its headers. It looks
at the role (actor) of each header element and gives the header to a targeted
SOAPRecipient for processing. A targeted SOAPRecipient is a recipient whose
actor attribute matches the role (actor) of the particular header element.

The SOAPProcessor also provides a mechanism for registering SOAPAnnotator
objects with it. A SOAPAnnotator is the reverse of a SOAPRecipient. When a
message is being prepared for sending, the SOAP header is passed to each of the
SOAPAnnotators, which can add their specific header elements to it.

The purpose of this sample is to showcase how the SOAP processing model can be
implemented.

This sample consists of an API and its implementation only.

Sample and runnable usage of this processor is being shown in the [saaj-soapnode](../saaj-soapnode/README.md)

