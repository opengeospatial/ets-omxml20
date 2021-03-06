﻿<?xml version="1.0" encoding="UTF-8"?>
<schema
    fpi="http://schemas.opengis.net/om/2.0/measurement.sch"
    see="http://www.opengis.net/doc/IP/OMXML/2.0"
    xmlns="http://purl.oclc.org/dsdl/schematron"
    queryBinding="xslt2"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <title>Measurement validation</title>
    <p>Verifies that all instances of OM_Observation or elements derived from OM_Observation (i.e. having an om:resultTime property) have a result that matches the pattern for Measurements</p>
    <ns
        prefix="gml"
        uri="http://www.opengis.net/gml/3.2"/>
    <ns
        prefix="om"
        uri="http://www.opengis.net/om/2.0"/>
    <ns
        prefix="xsi"
        uri="http://www.w3.org/2001/XMLSchema-instance"/>
    <ns
        prefix="xlink"
        uri="http://www.w3.org/1999/xlink"/>

    <xsl:import-schema
        schema-location="http://schemas.opengis.net/gml/3.2.1/gml.xsd"/>

    <pattern
        id="observation-type-measurement">
        <rule
            context="//*[om:resultTime]">
            <include href="./result-measure.sch"/>
        </rule>
    </pattern>

</schema>
