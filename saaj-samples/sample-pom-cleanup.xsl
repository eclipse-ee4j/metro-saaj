<!--
    Copyright (c) 2026 Contributors to the Eclipse Foundation.
    Copyright (c) 2019, 2020 Oracle and/or its affiliates. All rights reserved.

    This program and the accompanying materials are made available under the
    terms of the Eclipse Distribution License v. 1.0, which is available at
    http://www.eclipse.org/org/documents/edl-v10.php.

    SPDX-License-Identifier: BSD-3-Clause

-->

<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:pom="http://maven.apache.org/POM/4.0.0"
>

    <xsl:strip-space elements="*"/>
    <xsl:output method="xml" omit-xml-declaration="yes" indent="yes"/>

    <xsl:param name="version"/>

    <xsl:template match="node()|@*">
        <xsl:copy>
            <xsl:apply-templates select="node()|@*"/>
        </xsl:copy>
    </xsl:template>

    <!-- Replace parent pom declarations -->
    <xsl:template match="/*[local-name()='project']/*[local-name()='parent']">
        <xsl:variable name="thisns" select="namespace-uri()"/>
        <xsl:element name="parent" namespace="{$thisns}">
            <xsl:element name="groupId" namespace="{$thisns}">
                <xsl:text>org.eclipse.ee4j</xsl:text>
            </xsl:element>
            <xsl:element name="artifactId" namespace="{$thisns}">
                <xsl:text>project</xsl:text>
            </xsl:element>
            <xsl:element name="version" namespace="{$thisns}">
                <xsl:text>2.0.0</xsl:text>
            </xsl:element>
        </xsl:element>
    </xsl:template>

    <!-- Update versions -->
    <xsl:template match="//pom:version[../pom:groupId/text()='com.sun.xml.messaging.saaj.samples']/text()">
        <xsl:text><xsl:value-of select="$version"/></xsl:text>
    </xsl:template>

    <xsl:template match="//pom:version[../pom:groupId/text()='com.sun.xml.messaging.saaj']/text()">
        <xsl:text><xsl:value-of select="$version"/></xsl:text>
    </xsl:template>
</xsl:stylesheet>
