package org.opengis.cite.om20.level1;

import java.io.File;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.stream.XMLStreamException;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;

import org.opengis.cite.om20.ETSAssert;
import org.opengis.cite.om20.util.NamespaceBindings;
import org.opengis.cite.om20.util.ValidationUtils;
import org.opengis.cite.om20.util.XMLUtils;
import org.opengis.cite.validation.XmlSchemaCompiler;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XPathCompiler;
import net.sf.saxon.s9api.XPathSelector;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XdmValue;
import net.sf.saxon.s9api.DocumentBuilder;

/**
 * Includes various tests of capability 1.
 */
public class SpatialObservationValidation extends DataFixture {
	
	/**
	 * A.11 Spatial observation data. Verify that the observation has exactly
	 * one sampling geometry encoded as XML element om:parameter/om:NamedValue
	 * in an observation, and that its sub-element om:name has an xlink:href
	 * attribute with the value
	 * http://www.opengis.net/def/paramname/OGC-OM/2.0/samplingGeometry, and its
	 * sub-element om:value contains a sub-element in the substitution group of
	 * gml:AbstractGeometry
	 */
	@Test(groups = "A.11. Spatial observation data", description = "Validate the XML document using the Schematron document http://schemas.opengis.net/om/2.0/spatialObservation.sch")
	public void SpatialParameter() {
		// According to "spatialObservation.sch", A spatial observation shall
		// have exactly one sampling geometry
		// encoded as XML element om:parameter in an observation
		if (CheckXPath2("boolean(//*[om:resultTime]/om:parameter)").equals("false")) {
			throw new SkipException("Not Spatial observation data.");
		}
		
		if (CheckXPath2("boolean(//om:parameter/om:NamedValue/om:name[@xlink:href = 'http://www.opengis.net/req/omxml/2.0/data/samplingGeometry'] | /om:parameter/om:NamedValue[om:name/@xlink:href= 'http://www.opengis.net/req/omxml/2.0/data/samplingGeometry'])"
						).equals("false")) {
			throw new SkipException("Not Spatial observation data.");
		}
		// ---Test rule 1---
		// The xlink:href attribute in the XML element om:name of
		// the om:parameter/om:NamedValue element that carries the sampling
		// geometry SHALL have the value
		// 'http://www.opengis.net/req/omxml/2.0/data/samplingGeometry'
		String result_1 = "true";
		String xpath_rule1 = "count(//om:parameter/om:NamedValue/om:name[@xlink:href = 'http://www.opengis.net/req/omxml/2.0/data/samplingGeometry']) = 1";
		if (CheckXPath2(xpath_rule1).equals("false")) {
			result_1 = "false";
		}

		// ---Test rule 2---
		// The XML element om:value in the om:parameter/om:NamedValue element
		// which carries the sampling
		// geometry shall have a value of type gml:AbstractGeometry
		String result_2 = "true";
		String xpath_rule2 = "//*[om:resultTime]/om:parameter/om:NamedValue[om:name/@xlink:href= 'http://www.opengis.net/req/omxml/2.0/data/samplingGeometry']/om:value/*[1]/name()";
		String candidateNode = CheckXPath2(xpath_rule2);
		String nodeName = "gml:AbstractGeometry";
		if (candidateNode.contains("XdmEmptySequence")) {
			result_2 = "false";
		} else {
			try {
				File schemaFile = this.GetFileViaResourcePath(this.Resource_GML_Path);
				result_2 = SchemaElement(candidateNode, nodeName, schemaFile);
			} catch (SaxonApiException e) {
				e.printStackTrace();
			}
		}

		// Get final testing result
		String final_result = "";
		if (result_1.equals("true") && result_2.equals("true")) {
			final_result = "true";
		} else {
			final_result = "false";
		}
		Assert.assertTrue(final_result.equals("true"),
				"A spatial observation MUST have exactly one sampling geometry encoded as XML element om:parameter in an observation."
						+ "	The xlink:href attribute in the XML element om:name of the om:parameter/om:NamedValue element that "
						+ "	carries the sampling geometry MUST have the value 'http://www.opengis.net/req/omxml/2.0/data/samplingGeometry'. "
						+ "	And, the XML element om:value in the om:parameter/om:NamedValue element which carries the sampling "
						+ "	geometry MUST have a value of type gml:AbstractGeometry");
	}
}
