package org.opengis.cite.om20.level1;

import java.io.File;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

/**
 * Includes various tests of capability 1.
 */
public class SamplingPointDataValidation extends DataFixture {

	/**
	 * A.14 Verify that XML element sams:shape contains a subelement in the substitution
	 * group of gml:Point or a link to a representation of a point.
	 */
	@Test(groups = "A.14. Sampling point data",
			description = "Validate the XML document using the Schematron document http://schemas.opengis.net/samplingSpatial/2.0/samplingPoint.sch")
	public void ShapeTypeConsistent() {
		if (CheckXPath2("boolean(//sams:SF_SpatialSamplingFeature)").equals("false")) {
			throw new SkipException("Not Spatial Sampling feature data.");
		}
		else {
			final String sampling_feature_type_point = "http://www.opengis.net/def/samplingFeatureType/OGC-OM/2.0/SF_SamplingPoint";
			String href = CheckXPath2("string(//sams:SF_SpatialSamplingFeature/sam:type/@xlink:href)");
			if (href.equals(sampling_feature_type_point)) {
				String final_result = testA14("gml:Point");
				Assert.assertTrue(final_result.equals("true"),
						"a member of the substitution group headed by gml:Point or an xlink must be present as child of sams:shape");
			}
			else {
				throw new SkipException("Not Sampling point data.");
			}
		}
	}

	private String testA14(String schema_element_Value) {
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
