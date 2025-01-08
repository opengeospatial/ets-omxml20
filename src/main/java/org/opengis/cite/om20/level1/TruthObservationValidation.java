package org.opengis.cite.om20.level1;

import java.util.List;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

/**
 * Includes various tests of capability 1.
 */
public class TruthObservationValidation extends DataFixture {

	/**
	 * <p>
	 * TruthObservation.
	 * </p>
	 */
	@Test(groups = "A.5 Conformance class: Truth observation data",
			description = "Verify that the XML element om:result has a value that matches the content model defined by xs:boolean.")
	public void TruthObservation() {
		// must has resultTime element
		String hasResultTime = this.CheckXPath2("boolean(//om:resultTime)");
		if (hasResultTime.equals("false"))
			throw new SkipException("Not truth observation.");

		List<String> href = GetResultTypeHref();
		if (!href.contains(this.observation_type_truth)) {
			throw new SkipException("Not truth data.");
		}

		try {
			List<String> results = this.CheckObservationTypeTruth(this.observation_type_truth);
			if (results.contains("false")) {
				Assert.assertTrue(false,
						"element om:result has a value that matches the content model defined by xs:boolean.");
			}
			else {
				Assert.assertTrue(true,
						"element om:result has a value that matches the content model defined by xs:boolean.");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
