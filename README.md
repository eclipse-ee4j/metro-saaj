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

### April 14, 2020 - Jakarta SOAP with Attachments Implementation 1.5.2 Release ###

Updated Jakarta SOAP with Attachments Implementation to accommodate updated version
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

The latest release of Jakarta SOAP with Attachments Specification and API is 1.4.
```
        <dependencies>
            <dependency>
                <groupId>jakarta.xml.soap</groupId>
                <artifactId>jakarta.xml.soap-api</artifactId>
                <version>1.4.2</version>
            </dependency>
        </dependencies>
```

The latest release of Jakarta SOAP with Attachments Implementation is 1.5.2.
```
        <dependencies>
            <dependency>
                <groupId>com.sun.xml.messaging.saaj</groupId>
                <artifactId>saaj-impl</artifactId>
                <version>1.5.2</version>
            </dependency>
        </dependencies>
```
[Latest Release Notes](docs/relnotes-1.5.html)

You can find all of the Jakarta SOAP with Attachments releases in
[Maven Central](http://search.maven.org).


# <a name="overview">SOAP API Overview</a>

The Jakarta SOAP with Attachments provides the API for creating and sending SOAP messages by means of the `javax.xml.soap`
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

The Jakarta SOAP with Attachments 1.4 API is defined through the
[Jakarta EE Specification Process](https://jakarta.ee/about/jesp/).

The Jakarta SOAP with Attachments 1.4 specification and API documentation are available
[here](https://jakarta.ee/specifications/soap-attachments/1.4/).

The Jakarta SOAP with Attachments 1.5 Implementation API documentation is available
[here](https://javadoc.io/doc/com.sun.xml.messaging.saaj/saaj-impl/1.5.2).

# <a name="Samples">Samples</a>

Some sample programs showing how to use the Jakarta SOAP with Attachments APIs are available
[here](https://github.com/eclipse-ee4j/metro-saaj/tree/master/samples)

#### [simple](https://github.com/eclipse-ee4j/metro-saaj/tree/master/samples/simple)
  This sample application demonstrates how to send a simple `SOAPMessage` from a sender (servlet) to a receiver (servlet).
  This shows a simple round-trip message; the receiver responds with a reply. The messages appear in the directory from which you started the Web container.
#### [book](https://github.com/eclipse-ee4j/metro-saaj/tree/master/samples/book)
  This sample Web application demonstrates how to create and send a `SOAPMessage` from a sender (servlet) to a receiver (servlet).
  This sample demonstrates how a DOM document can be used in conjunction with SAAJ. 
  A DOM document is created from information read from an XML file, and the content is added to the body of a message.
  The messages appear in the directory from which you started the Web container.
#### [translator](https://github.com/eclipse-ee4j/metro-saaj/tree/master/samples/translator)
  This sample uses Altavista's babelfish translation service to translate text. The text to be translated is sent within a `SOAPMessage` to the TranslationService.<br>
  The TranslationService, in turn, talks to babelfish.altavista.com, making an HTTP connection, and extracts the translations of the input text string from the response. The text is translated into German, Italian, and French.<br>
  The translations can be returned either as attachments to a SAAJ message or within the `SOAPBody`, depending on which option is chosen through the radio button from the application's `index.html` file. If you use a proxy, the proxy settings (both host and port) need to be entered.
#### [uddiping](https://github.com/eclipse-ee4j/metro-saaj/tree/master/samples/uddiping)
  This sample application is a real-time application that talks to a live UDDI registry. It sends a `SOAPMessage` query to the registry and prints out the message response using the SAAJ API.<br>
  Instructions for running this sample are in a README file in the `uddiping` directory.
#### [soapprocessor](https://github.com/eclipse-ee4j/metro-saaj/tree/master/samples/soapprocessor)
  This sample provides an API that implements a `SOAPProcessor` object, which implements the processing model described in [The SOAP Message Exchange Model](https://www.w3.org/TR/2000/NOTE-SOAP-20000508/#_Toc478383491).
  The sample consists of a JAR file, which can be used by other sample applications, and the associated source code.<br>
  For more information about this sample, see the README file in the soapprocessor directory.


# <a name="links">Related Information</a>

* [Security](security.html)
* [Proprietary features](https://javadoc.io/doc/com.sun.xml.messaging.saaj/saaj-impl/latest/com.sun.xml.messaging.saaj/module-summary.html) ([old blog](https://community.oracle.com/blogs/kumarjayanti/2009/12/09/summary-proprietary-features-saaj-ri-134))
* [Maintenance Release 4 of JSR-000067](http://jcp.org/aboutJava/communityprocess/mrel/jsr067/index4.html)

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

