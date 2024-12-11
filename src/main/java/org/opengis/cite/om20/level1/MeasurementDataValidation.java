package org.opengis.cite.om20.level1;

import java.io.File;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.stream.XMLStreamException;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;

import org.opengis.cite.om20.ETSAssert;
import org.opengis.cite.om20.util.NamespaceBindings;
import org.opengis.cite.om20.util.ValidationUtils;
import org.opengis.cite.om20.util.XMLUtils;
import org.opengis.cite.validation.XmlSchemaCompiler;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XPathCompiler;
import net.sf.saxon.s9api.XPathSelector;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XdmValue;
import net.sf.saxon.s9api.DocumentBuilder;

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
