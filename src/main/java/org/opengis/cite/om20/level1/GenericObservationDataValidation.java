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
public class GenericObservationDataValidation extends DataFixture {

	/**
	 * A.1 Generic observation data. Verify that any XML element in the
	 * substitution group of om:OM_Observation is well-formed and valid
	 */
	@Test(groups = "A.1. Generic observation data - by various Schema References", description = "Validate the XML document using the XML schema document observation.xsd")
	public void ObservationValidation() {
		URL entityCatalog = this.getClass().getResource("/org/opengis/cite/om20/xsd/opengis/om/2.0/observation.xsd");
		Source source = new DOMSource(this.testSubject);
		Validator validator;
		try {
			validator = CreateValidator(entityCatalog);
			ETSAssert.assertSchemaValid(validator, source);
		} catch (XMLStreamException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	

	

	

	

}
