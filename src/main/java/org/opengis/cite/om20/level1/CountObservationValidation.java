package org.opengis.cite.om20.level1;

import java.util.List;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

/**
 * Includes various tests of capability 1.
 */
public class CountObservationValidation extends DataFixture {

	/**
	 * <p>
	 * CountObservation.
	 * </p>
	 */
	@Test(groups = "A.4 Conformance class: Count observation data",
			description = "Verify that the XML element om:result has a value that matches the content model defined by xs:integer.")
	public void CountObservation() {
		// must has resultTime element
		String hasResultTime = this.CheckXPath2("boolean(//om:resultTime)");
		if (hasResultTime.equals("false"))
			throw new SkipException("Not count observation.");

		List<String> href = GetResultTypeHref();
		if (!href.contains(this.observation_type_count)) {
			throw new SkipException("Not count data.");
		}

		try {
			List<String> results = this.CheckObservationTypeCount(this.observation_type_count);
			if (results.contains("false")) {
				Assert.assertTrue(false,
						"element om:result has a value that matches the content model defined by xs:integer.");
			}
			else {
				Assert.assertTrue(true,
						"element om:result has a value that matches the content model defined by xs:integer.");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
