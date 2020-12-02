package org.opengis.cite.om20.level1;

import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.opengis.cite.om20.ETSAssert;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

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
        URL schemaUrl = this.getClass().getResource("/org/opengis/cite/om20/xsd/opengis/om/2.0/observation.xsd");
        Source source = new DOMSource(this.testSubject);
        Validator validator;
        try {
            Schema schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(schemaUrl);
            validator = schema.newValidator();
            ETSAssert.assertSchemaValid(validator, source);
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

}
