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
public class SamplingSurfaceDataValidation extends DataFixture {

	/**
	 * A.16 Verify that the XML element sams:shape contains a subelement in the
	 * substitution group of gml:AbstractSurface or a link to a representation of a
	 * surface.
	 */
	@Test(groups = "A.16. Sampling surface data",
			description = "Validate the XML document using the Schematron document http://schemas.opengis.net/samplingSpatial/2.0/samplingSurface.sch")
	public void ShapeTypeConsistent() {
		if (CheckXPath2("boolean(//sams:SF_SpatialSamplingFeature)").equals("false")) {
			throw new SkipException("Not Spatial Sampling feature data.");
		}
		else {
			final String sampling_feature_type_surface = "http://www.opengis.net/def/samplingFeatureType/OGC-OM/2.0/SF_SamplingSurface";
			String href = CheckXPath2("string(//sams:SF_SpatialSamplingFeature/sam:type/@xlink:href)");
			if (href.equals(sampling_feature_type_surface)) {
				String final_result = testA16("gml:AbstractSurface");
				Assert.assertTrue(final_result.equals("true"),
						"a member of the substitution group headed by gml:AbstractSurface or an xlink must be present as child of sams:shape");
			}
			else {
				throw new SkipException("Not Sampling surface data.");
			}
		}
	}

	private String testA16(String schema_element_Value) {
		// condition 1: sams:shape/schema-element(schema_element_Value)
		String result_1 = "";
		String candidateNode = CheckXPath2("//sams:SF_SpatialSamplingFeature/sams:shape/*[1]/name()");
		String nodeName = schema_element_Value;
		try {
			File schemaFile = GetFileViaResourcePath(this.Resource_GML_Path);
			result_1 = SchemaElement(candidateNode, nodeName, schemaFile);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		// condition 2: sams:shape/@xlink:href
		String result_2 = CheckXPath2("boolean(//sams:SF_SpatialSamplingFeature/sams:shape/@xlink:href)");

		// get final testing result
		String final_result = "";
		if (result_1.equals("true") || result_2.equals("true")) {
			final_result = "true";
		}
		else {
			final_result = "false";
		}
		return final_result;
	}

}
