package org.opengis.cite.om20.level1;

import java.util.List;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

/**
 * Includes various tests of capability 1.
 */
public class MeasurementDataValidation extends DataFixture {

	// A.2 Conformance class: Measurement data
	/**
	 * <p>
	 * MeasurementData.
	 * </p>
	 */
	@Test(groups = "A.2 Conformance class: Measurement data",
			description = "Verify that the XML element om:result has a value that matches the content model defined by gml:MeasureType")
	public void MeasurementData() {

		// boolean(//om:resultTime)
		String hasResultTime = this.CheckXPath2("boolean(//om:resultTime)");
		if (hasResultTime.equals("false"))
			throw new SkipException("Not measurement data.");

		List<String> href = GetResultTypeHref();
		if (!href.contains(this.observation_type_measurement)) {
			throw new SkipException("Not measurement data.");
		}

		// throw new SkipException("Not measurement data.");
		// try {

		List<String> results = CheckObservationTypeMeasurement(this.observation_type_measurement);
		if (results.contains("false")) {
			Assert.assertTrue(false,
					"XML element om:result has a value that matches the content model defined by gml:MeasureType.");
		}
		else {
			Assert.assertTrue(true,
					"XML element om:result has a value that matches the content model defined by gml:MeasureType.");
		}
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
	}

}
