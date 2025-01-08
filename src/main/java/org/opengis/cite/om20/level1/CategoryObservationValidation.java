package org.opengis.cite.om20.level1;

import java.util.List;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

/**
 * Includes various tests of capability 1.
 */
public class CategoryObservationValidation extends DataFixture {

	/**
	 * <p>
	 * CategoryObservation.
	 * </p>
	 */
	@Test(groups = "A.3 Conformance class: Category observation data",
			description = "Verify that the XML element om:result has a value that matches the content model defined by gml:ReferenceType")
	public void CategoryObservation() {
		// must has resultTime element
		String hasResultTime = this.CheckXPath2("boolean(//om:resultTime)");
		if (hasResultTime.equals("false"))
			throw new SkipException("Not category observation.");

		List<String> href = GetResultTypeHref();
		if (!href.contains(this.observation_type_category)) {
			throw new SkipException("Not category data.");
		}

		try {
			List<String> results = CheckObservationTypeCategory(this.observation_type_category);
			if (results.contains("false")) {
				Assert.assertTrue(false,
						"element om:result has a value that matches the content model defined by gml:ReferenceType.");
			}
			else {
				Assert.assertTrue(true,
						"element om:result has a value that matches the content model defined by gml:ReferenceType.");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
