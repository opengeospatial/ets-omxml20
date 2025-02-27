package org.opengis.cite.om20.level1;

import java.io.File;
import java.util.List;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

/**
 * Includes various tests of capability 1.
 */
public class ArrayObservationValidation extends DataFixture {

	/**
	 * A.10 SWE array observation data. Verify that the XML element om:result contains a
	 * sub-element swe:DataArray, swe:Matrix or swe:DataStream containing inline values.
	 */
	@Test(groups = "A.10. SWE array observation data",
			description = "Validate the XML document using the Schematron document http://schemas.opengis.net/om/2.0/SWEArrayObservation.sch")
	public void ResultSWEBlock() {

		List<String> href = GetResultTypeHref();
		if (!href.contains(this.observation_type_swe_array)) {
			throw new SkipException("Not SWE array observation.");
		}

		String candidateNode = CheckXPath2("//om:result/*[1]/name()");
		String nodeName_1 = "swe:DataArray";
		String nodeName_2 = "swe:Matrix";
		String nodeName_3 = "swe:DataStream";
		String inline_value = CheckXPath2("boolean(//om:result/*[1]/swe:values)");

		String final_result = "";
		try {
			File schemaFile = GetFileViaResourcePath(this.Resource_SWE_Path);
			String result_1 = SchemaElement(candidateNode, nodeName_1, schemaFile);
			String result_2 = SchemaElement(candidateNode, nodeName_2, schemaFile);
			String result_3 = SchemaElement(candidateNode, nodeName_3, schemaFile);
			if ((result_1.equals("true") || result_2.equals("true") || result_3.equals("true"))
					&& inline_value.equals("true")) {
				final_result = "true";
			}
			else {
				final_result = "false";
			}
			Assert.assertTrue(final_result.equals("true"),
					"result must contain an element in the substitution group headed by swe:DataArray, swe:Matrix or swe:DataStream and with inline values.");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
