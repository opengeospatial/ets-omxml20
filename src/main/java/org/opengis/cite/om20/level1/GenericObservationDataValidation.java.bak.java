package org.opengis.cite.om20.level1;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
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
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XdmValue;

/**
 * Includes various tests of capability 1.
 */
public class GenericObservationDataValidation extends DataFixture{
	private final String observation_type_measurement = "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Measurement";
	private final String observation_type_category = "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_CategoryObservation";
	private final String observation_type_count = "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_CountObservation";
	private final String observation_type_truth = "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_TruthObservation";
	private final String observation_type_complex = "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_ComplexObservation";
	private final String observation_type_geometry = "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_GeometryObservation";
	private final String observation_type_temporal = "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_TemporalObservation";
	private final String observation_type_swe_simple = "http://www.opengis.net/def/observationType/OGC-OM/2.0/SWEScalarObservation";
	private final String observation_type_swe_array = "http://www.opengis.net/def/observationType/OGC-OM/2.0/SWEArrayObservation";
	
	/**
	 * A.1 Generic observation data.
	 * Verify that any XML element in the substitution group of 
	 * om:OM_Observation is well-formed and valid
	 */
	@Test(groups="A.1. Generic observation data", description="Validate the XML document using the XML schema document observation.xsd")
	public void ObservationValid(){
		URL xsdPath = this.getClass().getResource("/org/opengis/cite/om20/xsd/opengis/om/2.0/observation.xsd");		
		Validator validator = null;
		Source source = new DOMSource(this.testSubject);
		try {
			validator = CreateValidatorFromXSD(xsdPath);
					
		} catch (SAXException | IOException | URISyntaxException  e) {
			e.printStackTrace();
		}
		
		ETSAssert.assertSchemaValid(validator, source);
	}
	@Test(groups="A.1. Generic observation data - by various Schema References", description="Validate the XML document using the XML schema document observation.xsd")
	public void ObservationValidation(){		//URL entityCatalog = this.getClass().getResource("/org/opengis/cite/om20/schema-catalog.xml");
		URL entityCatalog = this.getClass().getResource("/org/opengis/cite/om20/xsd/opengis/om/2.0/observation.xsd");		//Schema schema = ValidationUtils.CreateSchema("observation.xsd", "/org/opengis/cite/om20/xsd/opengis/om/2.0/");
		Schema schema;
		Set<URI> schemaRefs;		
		Validator validator = null;
		Source source = new DOMSource(this.testSubject);
		try {
			schemaRefs = ValidationUtils.extractSchemaReferences(new StreamSource(this.dataFile), this.testSubjectUri.toString());			//validator = CreateValidatorFromXSD(entityCatalog);
			XmlSchemaCompiler xsdCompiler = new XmlSchemaCompiler(entityCatalog);
			schema = xsdCompiler.compileXmlSchema(schemaRefs.toArray(new URI[schemaRefs.size()]));
			validator = schema.newValidator();
					
		} catch (SAXException | IOException | XMLStreamException  e) {
			e.printStackTrace();
		}
		
		ETSAssert.assertSchemaValid(validator, source);
	}
	
	/**
	 * A.1 Generic observation data.
	 * Verify that the content model of any om:result element is
	 * consistent with the value of the xlink:href attribute of the
	 * om:type element if one is present as a sub-element of the parent
	 * om:OM_Observation
	 */
	@Test(groups="A.1. Generic observation data - ResultTypeConsistent", description="Validate the XML document using the Schematron document resultTypeConsistent.sch")
	public void ResultTypeConsistent(){
		String href = CheckXPath2("//om:OM_Observation/om:type/@xlink:href");
		if (href.equals("xlink:href=".concat(String.format("\"%s\"", observation_type_measurement)))){
			String result = CheckObservationTypeMeasurement(href);
			Assert.assertTrue(result.equals("true"), "result model must match gml:MeasureType");
		}else if(href.equals("xlink:href=".concat(String.format("\"%s\"", observation_type_category)))){
			String result = CheckObservationTypeCategory(href);
			Assert.assertTrue(result.equals("true"), "result model must match gml:ReferenceType");
		}else if(href.equals("xlink:href=".concat(String.format("\"%s\"", observation_type_count)))){
			String result = CheckObservationTypeCount(href);
			Assert.assertTrue(result.equals("true"), "result type must be xs:integer");
		}else if(href.equals("xlink:href=".concat(String.format("\"%s\"", observation_type_truth)))){
			String result = CheckObservationTypeTruth(href);
			Assert.assertTrue(result.equals("true"), "result type must be xs:boolean");
		}else if(href.equals("xlink:href=".concat(String.format("\"%s\"", observation_type_complex)))){
			String result = CheckObservationTypeComplex(href);
			Assert.assertTrue(result.equals("true"), "result must contain an element in the substitution group headed by swe:DataRecord or swe:Vector");
		}
		
		else{
			boolean result = false;
			Assert.assertTrue(result, "xlink:href attribute of om:type could not match");
		}
	}
	
	


//==============================================================================================//	
	/**
	 * Create validator from xsd file
	 * @param xsdPath A URL that denotes the location of a XML schema.
	 * @throws URISyntaxException 
	 * @throws IOException 
	 */
	public Validator CreateValidatorFromXSD(URL xsdPath) throws SAXException, IOException, URISyntaxException{
		XmlSchemaCompiler compiler = new XmlSchemaCompiler(xsdPath);
        Schema schema = compiler.compileXmlSchema(xsdPath.toURI());
        return schema.newValidator();
	}
	
	/**
	 * Check XPath2.0
	 * @param xpath String denoting an xpath syntax
	 * @return XdmValue converted to string
	 */
	public String CheckXPath2(String xpath){
		XdmValue xdmValue = null;
        try {
        	xdmValue = XMLUtils.evaluateXPath2(new DOMSource(this.testSubject), xpath, NamespaceBindings.getStandardBindings());
        } catch (SaxonApiException e) {
			e.printStackTrace();
		}
        return xdmValue.toString();
	}
	
	public XdmValue GetXdmValue(String xpath){
		XdmValue xdmValue = null;
        try {
        	xdmValue = XMLUtils.evaluateXPath2(new DOMSource(this.testSubject), xpath, NamespaceBindings.getStandardBindings());
        } catch (SaxonApiException e) {
			e.printStackTrace();
		}
        return xdmValue;
	}
	
	/**
	 * Check Observation Type Measurement in schematron document resultTypeConsistent.sch
	 * @param href the value of om:type/xlink:href to make the context
	 * @return return string value "true" or "false"
	 */
	public String CheckObservationTypeMeasurement(String href){
		String context = String.format("//om:OM_Observation[om:type/@%s]", href); 
		String uom_value = context.concat("/om:result/@uom");
		String result_text = context.concat("/om:result/text()");
        String xpath = String.format("(((%s castable as xs:string) and (string-length(%s) > 0) and (not(matches(%s, \"[: \\n\\r\\t]+\"))))  or ((%s castable as xs:anyURI) and matches(%s , \"([a-zA-Z][a-zA-Z0-9\\-\\+\\.]*:|\\.\\./|\\./|#).*\"))) and (%s castable as xs:double)",uom_value,uom_value,uom_value,uom_value,uom_value,result_text);
        return CheckXPath2(xpath);
	}
	
	/**
	 * Check Observation Type Category in schematron document resultTypeConsistent.sch
	 * @param href the value of om:type/xlink:href to make the context
	 * @return return string value "true" or "false"
	 */
	public String CheckObservationTypeCategory(String href){
		String context = String.format("//om:OM_Observation[om:type/@%s]", href);
		boolean test1 = true;
		boolean test2 = false;
		if(CheckXPath2(context.concat("/om:result/@xlink:href")).contains("XdmEmptySequence") || CheckXPath2(context.concat("/om:result/@xlink:title")).contains("XdmEmptySequence")){
			test1 = false;
		}
		if(CheckXPath2(context.concat("/om:result/*")).contains("XdmEmptySequence") && CheckXPath2(context.concat("/om:result/text()")).contains("XdmEmptySequence")){
			test2 = true;
		}
		if(test1 && test2){
			return "true";
		}else{
			return "false";
		}
	}
	
	/**
	 * Check Observation Type Count in schematron document resultTypeConsistent.sch
	 * @param href the value of om:type/xlink:href to make the context
	 * @return return string value "true" or "false"
	 */
	public String CheckObservationTypeCount(String href){
		String context = String.format("//om:OM_Observation[om:type/@%s]", href);
		return CheckXPath2(context.concat("/om:result/text() castable as xs:integer"));
	}
	
	/**
	 * Check Observation Type Truth in schematron document resultTypeConsistent.sch
	 * @param href the value of om:type/xlink:href to make the context
	 * @return return string value "true" or "false"
	 */
	public String CheckObservationTypeTruth(String href){
		String context = String.format("//om:OM_Observation[om:type/@%s]", href);
		return CheckXPath2(context.concat("/om:result/text() castable as xs:boolean"));
	}
	
	/**
	 * Check Observation Type Complex in schematron document resultTypeConsistent.sch
	 * @param href the value of om:type/xlink:href to make the context
	 * @return return string value "true" or "false"
	 */
	public String CheckObservationTypeComplex(String href){
		//get the result element
//		String context = String.format("//om:OM_Observation[om:type/@%s]", href);
//		XdmValue val = this.GetXdmValue(context);
		
		//check the result element meets the definition of swe:DataRecord or swe:Vector
		
		
		return "true";
	}
}
