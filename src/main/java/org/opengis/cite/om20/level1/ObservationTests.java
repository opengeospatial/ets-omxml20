package org.opengis.cite.om20.level1;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.opengis.cite.om20.ErrorMessage;
import org.opengis.cite.om20.ErrorMessageKeys;
import org.opengis.cite.om20.SuiteAttribute;
import org.opengis.cite.om20.util.NamespaceBindings;
import org.opengis.cite.om20.util.ValidationUtils;
import org.opengis.cite.om20.util.XMLUtils;
import org.opengis.cite.om20.Namespaces;
import org.opengis.cite.om20.ETSAssert;
import org.opengis.cite.validation.RelaxNGValidator;
import org.opengis.cite.validation.SchematronValidator;
import org.opengis.cite.validation.ValidationErrorHandler;
import org.opengis.cite.validation.XmlSchemaCompiler;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XdmValue;
/**
 * Includes various tests of capability 1.
 */
public class ObservationTests extends DataFixture {

	static final String BASIC_RESULT_TYPE_PHASE = "BasicResultTypePhase";
    private static final String SCHEMATRON_METADATA = "resultTypeConsistent.sch";
	
    /**
     * Checks the behavior of the trim function.
     */
    @Test(description = "A.1 Generic observation data. Verify that any XML element in the substitution group of om:OM_Observation is well-formed and valid ", priority=1)
    public void Observation() {    	
		try {
			// load a remote OM schema, represented by a Schema instance
//	    	// create a SchemaFactory capable of understanding WXS schemas
//	        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
//	        String filePath = "http://schemas.opengis.net/om/2.0/observation.xsd";	        
//	    	Source schemaSource = new StreamSource(filePath);
//	        Schema schema = factory.newSchema(schemaSource);
	        //load a local schema
	        URL localXsdUrl = ValidationUtils.class.getResource(ValidationUtils.ROOT_PKG + "xsd/opengis/om/2.0/observation.xsd");
	        XmlSchemaCompiler xsdCompiler = new XmlSchemaCompiler(localXsdUrl);
	        Schema schema = xsdCompiler.compileXmlSchema(localXsdUrl.toURI());
				        
			// create a Validator instance, which can be used to validate an
	        // instance document
	        Validator validator = schema.newValidator();

	        // validate the DOM tree
	        //validator.validate(new DOMSource(this.testSubject));
	        Source xmlSource = new DOMSource(this.testSubject);
	        ETSAssert.assertSchemaValid(validator, xmlSource);
		} catch (SAXException | IOException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    @Test(description="A.1 The content model of any om:result element SHALL be consistent with the value of the xlink:href attribute of the om:type element if one is present as a sub-element of the parent om:OM_Observation")
    public void ResultTypeConsistent(){
    	/** using built-in schematron validator **/
    	/*
    	SchematronValidator validator = ValidationUtils.buildSchematronValidator(SCHEMATRON_METADATA, BASIC_RESULT_TYPE_PHASE);
        Result result = validator.validate(new DOMSource(this.testSubject, this.testSubject.getDocumentURI()));
        Assert.assertFalse(validator.ruleViolationsDetected(), ErrorMessage.format(ErrorMessageKeys.NOT_SCHEMA_VALID,
                validator.getRuleViolationCount(), XMLUtils.resultToString(result)));
        */
    	
    	XdmValue xdmValue = null;
        String xpath = "//om:OM_Observation/om:type";
        try {
        	xdmValue = XMLUtils.evaluateXPath2(new DOMSource(this.testSubject), xpath, NamespaceBindings.getStandardBindings());
        } catch (SaxonApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Assert.assertTrue(xdmValue.size() > 0, ErrorMessage.format(ErrorMessageKeys.XPATH_RESULT,
                this.testSubject.getDocumentElement().getNodeName(), xpath));
    }

    /**
     * Verify the test subject is a valid Atom feed.
     *
     * @throws SAXException
     *             If the resource cannot be parsed.
     * @throws IOException
     *             If the resource is not accessible.
     */
    /*
    @Test(description = "Implements ATC 1-3")
    public void docIsValidAtomFeed() throws SAXException, IOException {
        URL schemaRef = getClass().getResource(
                "/org/opengis/cite/om20/rnc/atom.rnc");
        RelaxNGValidator rngValidator = new RelaxNGValidator(schemaRef);
        Source xmlSource = (null != testSubject)
                ? new DOMSource(testSubject) : null;
        rngValidator.validate(xmlSource);
        ValidationErrorHandler err = rngValidator.getErrorHandler();
        Assert.assertFalse(err.errorsDetected(),
                ErrorMessage.format(ErrorMessageKeys.NOT_SCHEMA_VALID,
                err.getErrorCount(), err.toString()));
    }
    */
}
