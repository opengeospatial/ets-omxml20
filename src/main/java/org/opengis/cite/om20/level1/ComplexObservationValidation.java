package org.opengis.cite.om20.level1;

import java.io.File;
import java.util.List;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

/**
 * Includes various tests of capability 1.
 */
public class ComplexObservationValidation extends DataFixture {

	/**
	 * A.8 Complex observation data. Verify that the XML element om:result contains a
	 * sub-element swe:DataRecord or swe:Vector with inline values.
	 */
	@Test(groups = "A.8. Complex observation data",
			description = "Validate the XML document using the Schematron document http://schemas.opengis.net/om/2.0/complexObservation.sch.")
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
			}
			else {
				result = "false";
			}
			Assert.assertTrue(result.equals("true"),
					"result must contain an element in the substitution group headed by swe:DataRecord or swe:Vector.");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
