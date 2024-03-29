<br/>


Jakarta SOAP with Attachments defines an API enabling developers to produce
and consume messages conforming to the SOAP 1.1, SOAP 1.2, and SOAP Attachments Feature.

This project is part of [Eclipse Metro](https://projects.eclipse.org/projects/ee4j.metro)

### Table of Contents

* [Latest News](#Latest_News)
* [Download Jakarta SOAP Release](#Download_Jakarta_SOAP_Release)
* [SOAP API Overview](#overview)
* [Release Documentation](#rlsdoc)
* [Samples](#Samples)
* [Related Information](#links)

# <a name="Latest_News"></a>Latest News


### May 12, 2023 - Eclipse Implementation of Jakarta SOAP with Attachments 3.0.2 Final Release ###

The 3.0.2 release is a bugfix release of the Implementation in 3.0.x release stream fixing
inconsistent owner element for `setIdAttributeNode`, unfolding possibly folded header values,
prevention of `StringIndexOutOfBoundsException` and deprecating `com.sun.xml.messaging.saaj.util.Base64`
in favor of `java.util.Base64`.

### March 30, 2023 - Eclipse Implementation of Jakarta SOAP with Attachments 3.0.1 Final Release ###

The 3.0.1 release is a bugfix release of the Implementation in 3.0.x release stream fixing
the regression in `MessageImpl.saveChanges()`.

### September 22, 2022 - Eclipse Implementation of Jakarta SOAP with Attachments 3.0.0 Final Release ###

Jakarta SOAP with Attachments Specification 3.0 is a major update of the specification containing
following changes:

* addition of `SOAPEnvelope.createName(String, String): Name` method
* not allowing null arguments in `SOAPFault.createFault(String, String)`
* extension of `SOAPConnection` to implement `java.io.Autocloseable`
* addition of an API allowing setting timeouts for set timeout for `SOAPConnection.call`
* drop of all references to JAXM
* drops of the search through Java SE installation in the implementation lookup
* removal of deprecated `SOAPElementFactory`

See [the specification](https://jakarta.ee/specifications/soap-attachments/3.0/) for details.

The version 3.0.0 of the Implementation of the specification adopts these changes.

### April 10, 2021 - Eclipse Implementation of Jakarta SOAP with Attachments 2.0.1 Final Release ###

The 2.0.1 release is a bugfix release integrating fixes in Jakarta EE
APIs the implementation depends on.

### November 4, 2020 - Eclipse Implementation of Jakarta SOAP with Attachments 2.0.0 Final Release ###

First release of Eclipse Implementation of Jakarta SOAP with Attachments fully moved
to and supporting the Jakarta SOAP with Attachments Specification 2.0 with *jakarta.xml.soap* namespace.

### October 26, 2020 - Jakarta SOAP with Attachments Specification 2.0 Final Release ###

Jakarta SOAP with Attachments Specification 2.0 is an update using *jakarta.xml.soap* namespace.
See [the specification](https://jakarta.ee/specifications/soap-attachments/2.0/).

### June 2, 2020 - Eclipse Implementation of Jakarta SOAP with Attachments 2.0.0-M1 Milestone Release ###

First milestone of Eclipse Implementation of Jakarta SOAP with Attachments to accommodate the move
of the Jakarta SOAP with Attachments Specification to *jakarta* namespace.
See [release notes](docs/relnotes-2.0.html).

### April 30, 2020 - Eclipse Implementation of Jakarta SOAP with Attachments 1.5.2 Release ###

Updated Eclipse Implementation of Jakarta SOAP with Attachments to accommodate updated version
of the Jakarta SOAP with Attachments Specification. See [release notes](docs/relnotes-1.5.html).

### February 28, 2020 - Jakarta SOAP with Attachments Specification 1.4.2 Release ###

First release of the Jakarta SOAP with Attachments Specification going through
the [Jakarta EE Specification Process](https://jakarta.ee/about/jesp/).

### July 20, 2019 - Jakarta SOAP with Attachments is the new name for SAAJ ###

The SAAJ technology contributed to the Eclipse Foundation has been renamed
to "Jakarta SOAP with Attachments" to reflect its role in the
[Jakarta EE platform](https://jakarta.ee/).

### December 28, 2018 - SAAJ RI 1.5.1 Final Release ###

The 1.5.1 release is the first release of the SAAJ Reference Implementation under Eclipse Foundation.

### September 14, 2018 - SAAJ RI project moves to the Eclipse Foundation! ###

The SAAJ project is now hosted at the Eclipse Foundation as part of
the [EE4J project](https://projects.eclipse.org/projects/ee4j).

By contributing to this project, you agree to these additional terms of
use, described in [CONTRIBUTING](CONTRIBUTING.md).

# <a name="Download_Jakarta_SOAP_Release"></a>Download Jakarta SOAP Release

The latest release of Jakarta SOAP with Attachments Specification and API is ${xml.soap-api.version}.
```
        <dependencies>
            <dependency>
                <groupId>jakarta.xml.soap</groupId>
                <artifactId>jakarta.xml.soap-api</artifactId>
                <version>${xml.soap-api.version}</version>
            </dependency>
        </dependencies>
```

The latest release of Eclipse Implementation of Jakarta SOAP with Attachments is ${saaj.version}.
```
        <dependencies>
            <dependency>
                <groupId>com.sun.xml.messaging.saaj</groupId>
                <artifactId>saaj-impl</artifactId>
                <version>${saaj.version}</version>
            </dependency>
        </dependencies>
```

You can find all of the Jakarta SOAP with Attachments releases in
[Maven Central](http://search.maven.org).


# <a name="overview">SOAP API Overview</a>

The Jakarta SOAP with Attachments provides the API for creating and sending SOAP messages by means of the `jakarta.xml.soap`
package. It is used for the SOAP messaging that goes on behind the scenes in Jakarta XML-based RPC and Jakarta XML Registries
implementations. SOAP Handlers in Jakarta XML Web Services use Jakarta SOAP APIs to access the SOAP Message.
Developers can also use it to write SOAP messaging applications directly instead of using Jakarta XML-based RPC/Jakarta XML Web Services.

The Jakarta SOAP API allows a client to send messages directly to the ultimate recipient using a `SOAPConnection` object,
which provides a point-to-point connection to the intended recipient. Response messages are received _synchronously_
using a _request-response_ model. `SOAPConnection` (and its related classes) is a pure library implementation
that lets you send SOAP messages directly to a remote party. A standalone client, that is, one that does not run in a container
such as a servlet, must include client-side libraries in its CLASSPATH or MODULEPATH. This model is simple to get started
but has limited possibilities for reliability and message delivery guarantees. For instance, the point-to-point message
exchange model relies largely on the reliability of the underlying transport for delivering a message.

Please send feedback on this Jakarta SOAP API implementation release to [metro-dev@eclipse.org](mailto:metro-dev@eclipse.org)

# <a name="rlsdoc">Release Documentation</a>

The Jakarta SOAP with Attachments 2.0 API is defined through the
[Jakarta EE Specification Process](https://jakarta.ee/about/jesp/).

The Jakarta SOAP with Attachments 2.0 specification and API documentation are available
[here](https://jakarta.ee/specifications/soap-attachments/2.0/).

The Jakarta SOAP with Attachments 2.0.0 Implementation API documentation is available
[here](https://javadoc.io/doc/com.sun.xml.messaging.saaj/saaj-impl/2.0.0).

# <a name="Samples">Samples</a>

Some sample programs showing how to use the Jakarta SOAP with Attachments APIs are available
[here](https://github.com/eclipse-ee4j/metro-saaj/tree/master/saaj-samples)

#### [simple](https://github.com/eclipse-ee4j/metro-saaj/tree/master/saaj-samples/saaj-simple)
  This sample application demonstrates how to send a simple `SOAPMessage` from a sender (servlet) to a receiver (servlet).
  This shows a simple round-trip message; the receiver responds with a reply. The messages appear in the directory from which you started the Web container.
#### [book](https://github.com/eclipse-ee4j/metro-saaj/tree/master/saaj-samples/saaj-book)
  This sample Web application demonstrates how to create and send a `SOAPMessage` from a sender (servlet) to a receiver (servlet).
  This sample demonstrates how a DOM document can be used in conjunction with SAAJ. 
  A DOM document is created from information read from an XML file, and the content is added to the body of a message.
  The messages appear in the directory from which you started the Web container.
#### [uddiping](https://github.com/eclipse-ee4j/metro-saaj/tree/master/saaj-samples/saaj-uddiping)
  This sample application is a real-time application that talks to a live UDDI registry. It sends a `SOAPMessage` query to the registry and prints out the message response using the SAAJ API.<br>
  Instructions for running this sample are in a README file in the `uddiping` directory.
#### [soapprocessor](https://github.com/eclipse-ee4j/metro-saaj/tree/master/saaj-samples/saaj-soapprocessor)
  This sample provides an API that implements a `SOAPProcessor` object, which implements the processing model described in [The SOAP Message Exchange Model](https://www.w3.org/TR/2000/NOTE-SOAP-20000508/#_Toc478383491).
  The sample consists of a JAR file, which can be used by other sample applications, and the associated source code.<br>
  For more information about this sample, see the README file in the soapprocessor directory.


# <a name="links">Related Information</a>

* [Security](security.html)
* [Proprietary features](https://javadoc.io/doc/com.sun.xml.messaging.saaj/saaj-impl/latest/com.sun.xml.messaging.saaj/module-summary.html) ([old blog](https://community.oracle.com/blogs/kumarjayanti/2009/12/09/summary-proprietary-features-saaj-ri-134))
* [Maintenance Release 4 of JSR-000067](http://jcp.org/aboutJava/communityprocess/mrel/jsr067/index4.html)
* [Jakarta EE Specification Process](https://jakarta.ee/about/jesp/)
* [Jakarta SOAP with Attachments specification](https://jakarta.ee/specifications/soap-attachments/)

* * *

**NOTE:**
The SOAP with Attachments API for Java 1.4 and earlier API is defined through the Java Community Process following
the process described at [jcp.org](http://www.jcp.org/en/procedures/overview). This process involves an Expert Group
with a lead that is responsible for delivering the specification, a reference implementation (RI) and a test compatibility kit (TCK).
The primary goal of an RI is to support the development of the specification and to validate it.
Specific RIs can have additional goals; the SAAJ RI is a production-quality implementation that is used directly
in a number of products by Sun and other vendors. To emphasize the quality of the implementation we call it a _Standard Implementation_.

The SAAJ expert group has wide industry participation with Sun Microsystems as the EG lead.
The initial API was part of JAXM 1.0 in [JSR-67](http://jcp.org/en/jsr/detail?id=67) and was released in December 2001;
the specification was later separated from JAXM in a maintenance release in June 2002.

* * *

