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
public class GeometryObservationValidation extends DataFixture {

	/**
	 * A.6 Geometry observation data. Verify that XML element om:result contains a
	 * sub-element in the substitution group of gml:AbstractGeometry
	 */
	@Test(groups = "A.6. Geometry observation data",
			description = "Validate the XML document using the Schematron document http://schemas.opengis.net/om/2.0/geometryObservation.sch")
	public void ResultGeometry() {

		List<String> href = GetResultTypeHref();
		if (!href.contains(this.observation_type_geometry)) {
			throw new SkipException("Not geometry data.");
		}

		String candidateNode = CheckXPath2("//om:result/*[1]/name()");
		String nodeName = "gml:AbstractGeometry";

		try {
			File schemaFile = GetFileViaResourcePath(this.Resource_GML_Path);
			String result = SchemaElement(candidateNode, nodeName, schemaFile);
			Assert.assertTrue(result.equals("true"),
					"XML element om:result must contains a sub-element in the substitution group of gml:AbstractGeometry.");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
