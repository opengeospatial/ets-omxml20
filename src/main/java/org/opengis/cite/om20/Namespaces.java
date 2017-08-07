package org.opengis.cite.om20;

import java.net.URI;

/**
 * XML namespace names.
 * 
 * @see <a href="http://www.w3.org/TR/xml-names/">Namespaces in XML 1.0</a>
 *
 */
public class Namespaces {

    private Namespaces() {
    }

    /** SOAP 1.2 message envelopes. */
    public static final String SOAP_ENV = "http://www.w3.org/2003/05/soap-envelope";
    /** W3C XLink */
    public static final String XLINK = "http://www.w3.org/1999/xlink";
    /** OGC 06-121r3 (OWS 1.1) */
    public static final String OWS = "http://www.opengis.net/ows/1.1";
    /** ISO 19136 (GML 3.2) */
    public static final String GML = "http://www.opengis.net/gml/3.2";
    public static final String OM="http://www.opengis.net/om/2.0";
    public static final String SWE="http://www.opengis.net/swe/2.0";
    public static final String SML="http://www.opengis.net/sensorml/2.0";
    public static final String SAMS="http://www.opengis.net/samplingSpatial/2.0";
    public static final String SAM="http://www.opengis.net/sampling/2.0";
    public static final String SPEC="http://www.opengis.net/samplingSpecimen/2.0";
    
    public static final URI XSD = URI
            .create("http://www.w3.org/2001/XMLSchema");
    /** Schematron (ISO 19757-3) namespace */
    public static final URI SCH = URI
            .create("http://purl.oclc.org/dsdl/schematron");
}
