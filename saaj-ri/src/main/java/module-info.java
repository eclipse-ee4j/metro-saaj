/*
 * Copyright (c) 2018, 2021 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

/**
 * Jakarta SOAP Implementation, an implementation of Jakarta SOAP Specification
 * <br>
 * <br>
 * <a id="properties"><strong>Properties</strong></a>
 * <p>
 * The following properties are supported by the EE4J implementation of Jakarta SOAP, but are not currently a required part of the specification.
 * These must be set as System properties. The names, types, defaults, and semantics of these properties may change in future releases.
 * </p>
 * <table border="1">
 * <caption>Jakarta SOAP implementation System properties</caption>
 * <tr>
 *  <th>Name</th>
 *  <th>Type</th>
 *  <th>Description</th>
 * </tr>
 *
 * <tr>
 *  <td><a id="com.sun.xml.messaging.saaj.soap.saxParserPoolSize">com.sun.xml.messaging.saaj.soap.saxParserPoolSize</a></td>
 *  <td>integer</td>
 *  <td>The {@code com.sun.xml.messaging.saaj.soap.saxParserPoolSize} property allows to specify another value for the size
 * of the parser pool used for SAX-parsing. The default when not specified is {@code 5}, but applications with high load and/or
 * concurrency may benefit from setting this to a higher value. The default is 5.</td>
 * </tr>
 *
 * <tr>
 *  <td><a id="mail.mime.charset">mail.mime.charset</a></td>
 *  <td>String</td>
 *  <td>The {@code mail.mime.charset} System property can be used to specify the default MIME charset to use for encoded words
 * and text parts that don't otherwise specify a charset. Normally, the default MIME charset is derived from the default Java charset,
 * as specified in the {@code file.encoding} System property. Most applications will have no need to explicitly set the default MIME
 * charset. In cases where the default MIME charset to be used for mail messages is different than the charset used for files stored on
 * the system, this property should be set.</td>
 * </tr>
 *
 * <tr>
 *  <td><a id="mail.mime.decodetext.strict">mail.mime.decodetext.strict</a></td>
 *  <td>boolean</td>
 *  <td>The {@code mail.mime.decodetext.strict} property controls decoding of MIME encoded words. The MIME spec requires
 * that encoded words start at the beginning of a whitespace separated word. Some mailers incorrectly include encoded words
 * in the middle of a word. If the {@code mail.mime.decodetext.strict} System property is set to {@code "false"},
 * an attempt will be made to decode these illegal encoded words. The default is true.</td>
 * </tr>
 *
 * <tr>
 *  <td><a id="mail.mime.encodeeol.strict">mail.mime.encodeeol.strict</a></td>
 *  <td>boolean</td>
 *  <td>The {@code mail.mime.encodeeol.strict} property controls the choice of Content-Transfer-Encoding for MIME parts
 * that are not of type "text". Often such parts will contain textual data for which an encoding that allows normal
 * end of line conventions is appropriate. In rare cases, such a part will appear to contain entirely textual data,
 * but will require an encoding that preserves CR and LF characters without change. If the {@code mail.mime.encodeeol.strict}
 * System property is set to {@code "true"}, such an encoding will be used when necessary. The default is false.</td>
 * </tr>
 *
 * <tr>
 *  <td><a id="mail.mime.foldencodedwords">mail.mime.foldencodedwords</a></td>
 *  <td>boolean</td>
 *  <td><strong>DEPRECATED</strong> If set to {@code "true"}, {@code Content-Description} header field will be folded
 * (broken into 76 character lines) during encoding and unfolded during decoding. The default is false.</td>
 * </tr>
 *
 * <tr>
 *  <td><a id="mail.mime.foldtext">mail.mime.foldtext</a></td>
 *  <td>boolean</td>
 *  <td><strong>DEPRECATED</strong> If set to {@code "true"}, header fields containing just text such as the {@code Subject}
 * and {@code Content-Description} header fields, and long parameter values in structured headers such as {@code Content-Type}
 * will be folded (broken into 76 character lines) when set and unfolded when read. The default is true.</td>
 * </tr>
 *
 * <tr>
 *  <td><a id="saaj.connect.timeout">saaj.connect.timeout</a></td>
 *  <td>integer</td>
 *  <td>Sets a specified timeout value, in milliseconds, to be used when opening a communications link to the resource
 * referenced by {@code SOAPConnection}. If the timeout expires before the connection can be established,
 * a {@code SOAPException} is raised. A timeout of zero is interpreted as an infinite timeout.
 * The default timeout value is 0.</td>
 * </tr>
 *
 * <tr>
 *  <td><a id="saaj.lazy.contentlength">saaj.lazy.contentlength</a></td>
 *  <td>boolean</td>
 *  <td>The {@code saaj.lazy.contentlength} property can be set by applications that do not care
 * about the {@code Content-Length} property. This allows SAAJ to handle large Payloads. The default is false.</td>
 * </tr>
 *
 * <tr>
 *  <td><a id="saaj.lazy.mime.optimization">saaj.lazy.mime.optimization</a></td>
 *  <td>boolean</td>
 *  <td>Turning this optimization off has limited utility in the absence of MimePull especially when seen
 * in the context of {@code SOAPMessage.getAttachments()}  and {@code SOAPMessage.getAttachments(...)} APIs.
 * However it does help a scenario where an intermediate (SOAP Handler) receives a large {@code MimeMessage}
 * but never needs to look at the Attachments (but only needs to read/manipulate the SOAP Envelope/Body)
 * before resending the message to an ultimate recipient. The default is true.</td>
 * </tr>
 *
 * <tr>
 *  <td><a id="saaj.mime.multipart.ignoremissingendboundary">saaj.mime.multipart.ignoremissingendboundary</a></td>
 *  <td>boolean</td>
 *  <td>Normally, when parsing a multipart MIME message, a message that is missing the final end boundary line
 * is not considered an error. The data simply ends at the end of the input. Note that messages of this form
 * violate the MIME specification. If the property {@code mail.mime.multipart.ignoremissingendboundary} is set
 * to @{code false}, such messages are considered an error and a {@code MesagingException} will be thrown
 * when parsing such a message. Note that this feature does not work when one enables {@code saaj.use.mimepull}.
 * The default is false.</td>
 * </tr>
 *
 * <tr>
 *  <td><a id="saaj.mime.optimization">saaj.mime.optimization</a></td>
 *  <td>boolean</td>
 *  <td>The {@code saaj.mime.optimization} can be used to switch-off Boyer-Moore algorithm for efficient
 * Mime-Boundary Parsing of a Mime Packaged Message and fallback to the old Jakarta Mail Mime-Boundary parsing
 * implementation when set to {@code false}. The default is true.</td>
 * </tr>
 *
 * <tr>
 *  <td><a id="saaj.mime.soapBodyPartSizeLimit">saaj.mime.soapBodyPartSizeLimit</a></td>
 *  <td>integer</td>
 *  <td>The {@code saaj.mime.soapBodyPartSizeLimit} property is used to specify the max amount in bytes a SOAP body part
 * is allowed to contain. This property can (should) be used to protect against maliciously crafted requests which
 * can take down the server with {@code OutOfMemoryError}. The default is to not enforce any limit.</td>
 * </tr>
 *
 * <tr>
 *  <td><a id="saaj.read.timeout">saaj.read.timeout</a></td>
 *  <td>integer</td>
 *  <td>Sets the read timeout to a specified timeout, in milliseconds. A non-zero value specifies the timeout when reading
 * from Input stream when a {@code SOAPConnection} is established to a resource. If the timeout expires before there is data
 * available for read, a {@code SOAPException} is raised. A timeout of zero is interpreted as an infinite timeout.
 * The default timeout value is 0.</td>
 * </tr>
 *
 * <tr>
 *  <td><a id="saaj.use.mimepull">saaj.use.mimepull</a></td>
 *  <td>boolean</td>
 *  <td>The {@code saaj.use.mimepull} property instruct the runtime to use the MimePull parser allowing handling of
 * large attachments in incoming messages. The default is false.</td>
 * </tr>
 *
 * </table>
 *
 * @see <a href="https://jakarta.ee/specifications/soap-attachments">Jakarta SOAP Specification</a>
 * @see <a href="https://community.oracle.com/blogs/kumarjayanti/2009/12/09/summary-proprietary-features-saaj-ri-134">Proprietary features</a>
 */
module com.sun.xml.messaging.saaj {

    requires transitive jakarta.activation;
    requires transitive jakarta.xml.soap;
    requires java.logging;
    requires java.desktop;
    requires java.xml;
    requires transitive org.jvnet.staxex;

    requires static jakarta.xml.bind;
    requires static com.sun.xml.fastinfoset;
    requires static org.jvnet.mimepull;

    exports com.sun.xml.messaging.saaj;
    exports com.sun.xml.messaging.saaj.packaging.mime;
    exports com.sun.xml.messaging.saaj.packaging.mime.internet;
    exports com.sun.xml.messaging.saaj.packaging.mime.util;
    exports com.sun.xml.messaging.saaj.soap;
    exports com.sun.xml.messaging.saaj.soap.dynamic;
    exports com.sun.xml.messaging.saaj.soap.name;
    exports com.sun.xml.messaging.saaj.util;
    exports com.sun.xml.messaging.saaj.util.stax;
    exports com.sun.xml.messaging.saaj.util.transform;
    exports com.sun.xml.messaging.saaj.soap.impl;

    provides jakarta.xml.soap.MessageFactory
            with com.sun.xml.messaging.saaj.soap.ver1_1.SOAPMessageFactory1_1Impl,
                 com.sun.xml.messaging.saaj.soap.ver1_2.SOAPMessageFactory1_2Impl;
    provides jakarta.xml.soap.SAAJMetaFactory
            with com.sun.xml.messaging.saaj.soap.SAAJMetaFactoryImpl;
    provides jakarta.xml.soap.SOAPConnectionFactory
            with com.sun.xml.messaging.saaj.client.p2p.HttpSOAPConnectionFactory;
    provides jakarta.xml.soap.SOAPFactory
            with com.sun.xml.messaging.saaj.soap.ver1_1.SOAPFactory1_1Impl,
                 com.sun.xml.messaging.saaj.soap.ver1_2.SOAPFactory1_2Impl;
}
