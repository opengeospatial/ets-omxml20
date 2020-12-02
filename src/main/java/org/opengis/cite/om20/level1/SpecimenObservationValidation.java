package org.opengis.cite.om20.level1;

import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.opengis.cite.om20.ETSAssert;
import org.testng.SkipException;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

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
            URL schemaUrl = this.getClass().getResource("/org/opengis/cite/om20/xsd/opengis/om/2.0/specimen.xsd");
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
}
