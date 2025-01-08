package org.opengis.cite.om20.level1;

import java.io.File;
import java.util.List;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

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
