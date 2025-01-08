package org.opengis.cite.om20.level1;

import java.io.File;
import java.util.List;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

/**
 * Includes various tests of capability 1.
 */
public class TemporalObservationValidation extends DataFixture {

	/**
	 * A.7 Temporal observation data. Verify that XML element om:result contains a
	 * sub-element in the substitution group of gml:AbstractTimeObject
	 */
	@Test(groups = "A.7. Temporal observation data",
			description = "Validate the XML document using the Schematron document http://schemas.opengis.net/om/2.0/temporalObservation.sch")
	public void ResultTimeObject() {

		List<String> href = GetResultTypeHref();
		if (!href.contains(this.observation_type_temporal)) {
			throw new SkipException("Not temporal data.");
		}

		String candidateNode = CheckXPath2("//om:result/*[1]/name()");
		String nodeName = "gml:AbstractTimeObject";

		try {
			File schemaFile = this.GetFileViaResourcePath(Resource_GML_Path);
			String result = SchemaElement(candidateNode, nodeName, schemaFile);
			Assert.assertTrue(result.equals("true"),
					"XML element om:result must contains a sub-element in the substitution group of gml:AbstractTimeObject.");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
