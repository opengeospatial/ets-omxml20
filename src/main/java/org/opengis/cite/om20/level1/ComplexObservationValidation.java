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
public class ComplexObservationValidation extends DataFixture {
	
	/**
	 * A.8 Complex observation data. Verify that the XML element om:result
	 * contains a sub-element swe:DataRecord or swe:Vector with inline values.
	 */
	@Test(groups = "A.8. Complex observation data", description = "Validate the XML document using the Schematron document http://schemas.opengis.net/om/2.0/complexObservation.sch.")
	public void ResultSWErecord() {
		
		List<String> href = GetResultTypeHref();
		if (!href.contains(this.observation_type_complex)) {
			throw new SkipException("Not complex data.");
		}
		
		String candidateNode = CheckXPath2("//om:result/*[1]/name()");
		String nodeName_1 = "swe:DataRecord";
		String nodeName_2 = "swe:Vector";
		String result = "";

		try {
			File schemaFile = GetFileViaResourcePath(this.Resource_SWE_Path);
			// File schemaFile = new File(xsdPath.toString().substring(5));
			String result_nodeName_1 = SchemaElement(candidateNode, nodeName_1, schemaFile);
			String result_nodeName_2 = SchemaElement(candidateNode, nodeName_2, schemaFile);
			if (result_nodeName_1.equals("true") || result_nodeName_2.equals("true")) {
				result = "true";
			} else {
				result = "false";
			}
			Assert.assertTrue(result.equals("true"),
					"result must contain an element in the substitution group headed by swe:DataRecord or swe:Vector.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
