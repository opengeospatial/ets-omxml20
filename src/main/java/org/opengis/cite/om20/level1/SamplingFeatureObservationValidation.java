package org.opengis.cite.om20.level1;

import java.io.File;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.opengis.cite.om20.ETSAssert;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

/**
 * Includes various tests of capability 1.
 */
public class SamplingFeatureObservationValidation extends DataFixture {

	/**
	 * A.12 Sampling feature data. Verify that any XML element in the substitution group
	 * of sam:SF_SamplingFeature is well-formed and valid
	 */
	@Test(groups = "A.12. Sampling feature data",
			description = "Validate the XML document using the XML schema document http://schemas.opengis.net/sampling/2.0/samplingFeature.xsd")
	public void SamplingValid() {
		if (CheckXPath2("boolean(//sam:SF_SamplingFeature)").equals("false")) {
			throw new SkipException("Not Sampling feature data.");
		}
		else {
			URL schemaUrl = this.getClass()
				.getResource("/org/opengis/cite/om20/xsd/opengis/om/2.0/samplingFeature.xsd");
			Source source = new DOMSource(this.testSubject);
			Validator validator;
			try {
				Schema schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(schemaUrl);
				validator = schema.newValidator();
				ETSAssert.assertSchemaValid(validator, source);
			}
			catch (SAXException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * A.13 Spatial Sampling feature data. Verify that any XML element in the substitution
	 * group of sams:SF_SpatialSamplingFeature is well-formed and valid.
	 */
	@Test(groups = "A.13. Spatial Sampling feature data",
			description = "Validate the XML document using the XML schema document http://schemas.opengis.net/samplingSpatial/2.0/spatialSamplingFeature.xsd")
	public void SpatialSamplingValid() {
		if (CheckXPath2("boolean(//sams:SF_SpatialSamplingFeature)").equals("false")) {
			throw new SkipException("Not Spatial Sampling feature data.");
		}
		else {
			URL schemaUrl = this.getClass()
				.getResource("/org/opengis/cite/om20/xsd/opengis/om/2.0/spatialSamplingFeature.xsd");
			Source source = new DOMSource(this.testSubject);
			Validator validator;
			try {
				Schema schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(schemaUrl);
				validator = schema.newValidator();
				ETSAssert.assertSchemaValid(validator, source);
			}
			catch (SAXException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * A.13 Spatial Sampling feature data. Verify that the content model of any sams:shape
	 * element is consistent with the value of the xlink:href attribute of the sam:type
	 * element if one is present as a sub-element of the parent
	 * sams:SF_SpatialSamplingFeature
	 */
	@Test(groups = "A.13. Spatial Sampling feature data",
			description = "Validate the XML document using the Schematron document http://schemas.opengis.net/samplingSpatial/2.0/shapeTypeConsistent.sch")
	public void ShapeTypeConsistent() {
		if (CheckXPath2("boolean(//sams:SF_SpatialSamplingFeature)").equals("false")) {
			throw new SkipException("Not Spatial Sampling feature data.");
		}
		else {
			final String sampling_feature_type_point = "http://www.opengis.net/def/samplingFeatureType/OGC-OM/2.0/SF_SamplingPoint";
			final String sampling_feature_type_curve = "http://www.opengis.net/def/samplingFeatureType/OGC-OM/2.0/SF_SamplingCurve";
			final String sampling_feature_type_surface = "http://www.opengis.net/def/samplingFeatureType/OGC-OM/2.0/SF_SamplingSurface";
			final String sampling_feature_type_solid = "http://www.opengis.net/def/samplingFeatureType/OGC-OM/2.0/SF_SamplingSolid";

			String href = CheckXPath2("string(//sams:SF_SpatialSamplingFeature/sam:type/@xlink:href)");

			if (href.equals(sampling_feature_type_point)) {
				String final_result = testA13("gml:Point");
				Assert.assertTrue(final_result.equals("true"),
						"a member of the substitution group headed by gml:Point or an xlink must be present as child of sams:shape");
			}
			else if (href.equals(sampling_feature_type_curve)) {
				String final_result = testA13("gml:AbstractCurve");
				Assert.assertTrue(final_result.equals("true"),
						"a member of the substitution group headed by gml:Curve or an xlink must be present as child of sams:shape");
			}
			else if (href.equals(sampling_feature_type_surface)) {
				String final_result = testA13("gml:AbstractSurface");
				Assert.assertTrue(final_result.equals("true"),
						"a member of the substitution group headed by gml:AbstractSurface or an xlink must be present as child of sams:shape");
			}
			else if (href.equals(sampling_feature_type_solid)) {
				String final_result = testA13("gml:AbstractSolid");
				Assert.assertTrue(final_result.equals("true"),
						"a member of the substitution group headed by gml:AbstractSolid or an xlink must be present as child of sams:shape");
			}
			else {
				String final_result = "false";
				Assert.assertTrue(final_result.equals("true"), "Invalid xlink:href attribute of sam:type element");
			}
		}
	}

	private String testA13(String schema_element_Value) {
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
