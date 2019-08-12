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
public class SpecimenObservationValidation extends DataFixture {
	
	/**
	 * A.18 Specimen data. Verify that any XML element in the substitution group
	 * of spec:SF_Specimen is well-formed and valid
	 */
	@Test(groups = "A.18. Specimen data", description = "Validate the XML document using the XML Schema document http://schemas.opengis.net/samplingSpecimen/2.0/specimen.xsd")
	public void SpecimenValid() {
		if (CheckXPath2("boolean(//spec:SF_Specimen)").equals("false")) {
			throw new SkipException("Not Specimen data.");
		} else {
			URL entityCatalog = this.getClass().getResource("/org/opengis/cite/om20/xsd/opengis/om/2.0/specimen.xsd");
			Validator validator = null;
			try {
				validator = CreateValidator(entityCatalog);
			} catch (XMLStreamException | SAXException | IOException e) {
				e.printStackTrace();
			}
			Source source = new DOMSource(this.testSubject);
			ETSAssert.assertSchemaValid(validator, source);
		}
	}	
}
