package org.opengis.cite.om20.level1;

import java.io.File;
import java.util.List;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

/**
 * Includes various tests of capability 1.
 */
public class ScalarObservationValidation extends DataFixture {

	/**
	 * A.9 SWE scalar observation data. Verify that the XML element om:result contains a
	 * concrete sub-element in the substitution group swe:AbstractScalarComponent
	 * containing an inline value
	 */
	@Test(groups = "A.9. SWE scalar observation data",
			description = "Validate the XML document using the Schematron document http://schemas.opengis.net/om/2.0/SWEScalarObservation.sch")
	public void ResultSWEscalar() {

		List<String> href = GetResultTypeHref();
		if (!href.contains(this.observation_type_swe_simple)) {
			throw new SkipException("Not SWE scalar observation.");
		}

		String candidateNode = CheckXPath2("//om:result/*[1]/name()");
		String inline_value = CheckXPath2("boolean(//om:result/*[1]/swe:value)");
		String nodeName = "swe:AbstractSimpleComponent";

		String final_result = "";
		try {
			File schemaFile = GetFileViaResourcePath(this.Resource_SWE_Path);
			String result = SchemaElement(candidateNode, nodeName, schemaFile);
			if (result.equals("true") && inline_value.equals("true")) {
				final_result = "true";
			}
			else {
				final_result = "false";
			}
			Assert.assertTrue(final_result.equals("true"),
					"XML element om:result must contain an element in the substitution group headed by swe:AbstractSimpleComponent with an inline value.");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
