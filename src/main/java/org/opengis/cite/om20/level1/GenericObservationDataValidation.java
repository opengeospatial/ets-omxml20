package org.opengis.cite.om20.level1;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
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
	@Test(groups="A.1. Generic observation data - by various Schema References", description="Validate the XML document using the XML schema document observation.xsd")
	 public void ObservationValidation(){  //URL entityCatalog = this.getClass().getResource("/org/opengis/cite/om20/schema-catalog.xml");
	  URL entityCatalog = this.getClass().getResource("/org/opengis/cite/om20/xsd/opengis/om/2.0/observation.xsd");  //Schema schema = ValidationUtils.CreateSchema("observation.xsd", "/org/opengis/cite/om20/xsd/opengis/om/2.0/");
	  Schema schema;
	  Set<URI> schemaRefs;  
	  Validator validator = null;
	  Source source = new DOMSource(this.testSubject);
	  try {
	   schemaRefs = ValidationUtils.extractSchemaReferences(new StreamSource(this.dataFile), this.testSubjectUri.toString());   //validator = CreateValidatorFromXSD(entityCatalog);
	   XmlSchemaCompiler xsdCompiler = new XmlSchemaCompiler(entityCatalog);
	   schema = xsdCompiler.compileXmlSchema(schemaRefs.toArray(new URI[schemaRefs.size()]));
	   validator = schema.newValidator();  
	  }catch (SAXException | IOException | XMLStreamException e) {
		  e.printStackTrace();
	  }
	  
	  ETSAssert.assertSchemaValid(validator, source);
	 }
	
	/**
	 * A.6 Geometry observation data.
	 * Verify that XML element om:result contains a sub-element in
	 * the substitution group of gml:AbstractGeometry 
	 */
	@Test(groups="A.6. Geometry observation data", description="Validate the XML document using the Schematron document http://schemas.opengis.net/om/2.0/geometryObservation.sch")
	public void ResultGeometry() {
		String href = GetResultTypeHref();
		//Only check those match to the type definition
		if (!href.equals(this.observation_type_geometry)){
			throw new SkipException("Not geometry observations.");
		}
		String candidateNode = CheckXPath2("//om:result/*[1]/name()");
		String nodeName = "gml:AbstractGeometry";
		
		try {
			File schemaFile = GetFileViaResourcePath(this.Resource_GML_Path);
			String result = SchemaElement(candidateNode, nodeName, schemaFile);
			Assert.assertTrue(result.equals("true"), "XML element om:result must contains a sub-element in the substitution group of gml:AbstractGeometry.");
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	private String GetResultTypeHref() {
		return CheckXPath2("string(//om:OM_Observation/om:type/@xlink:href)");
	}

	/**
	 * A.7 Temporal observation data.
	 * Verify that XML element om:result contains a sub-element 
	 * in the substitution group of gml:AbstractTimeObject
	 */
	@Test(groups="A.7. Temporal observation data", description="Validate the XML document using the Schematron document http://schemas.opengis.net/om/2.0/temporalObservation.sch")
	public void ResultTimeObject() {
		String href = GetResultTypeHref();
		//Only check those match to the type definition
		if (!href.equals(this.observation_type_temporal)){
			throw new SkipException("Not temporal observations.");
		}
		String candidateNode = CheckXPath2("//om:result/*[1]/name()");
		String nodeName = "gml:AbstractTimeObject";
		

		try {
			File schemaFile = this.GetFileViaResourcePath(Resource_GML_Path);			
			String result = SchemaElement(candidateNode, nodeName, schemaFile);
			Assert.assertTrue(result.equals("true"), "XML element om:result must contains a sub-element in the substitution group of gml:AbstractTimeObject.");
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * A.8 Complex observation data.
	 * Verify that the XML element om:result contains a sub-element 
	 * swe:DataRecord or swe:Vector with inline values. 
	 */
	@Test(groups="A.8. Complex observation data", description="Validate the XML document using the Schematron document http://schemas.opengis.net/om/2.0/complexObservation.sch.")
	public void ResultSWErecord() {
		String href = GetResultTypeHref();
		//Only check those match to the type definition
		if (!href.equals(this.observation_type_complex)){
			throw new SkipException("Not complex observations.");
		}
		String candidateNode = CheckXPath2("//om:result/*[1]/name()");
		String nodeName_1 = "swe:DataRecord"; 
		String nodeName_2 = "swe:Vector";
		String result = "";
				
		try {
			File schemaFile = GetFileViaResourcePath(this.Resource_SWE_Path);
			//File schemaFile = new File(xsdPath.toString().substring(5));
			String result_nodeName_1 = SchemaElement(candidateNode, nodeName_1, schemaFile);
			String result_nodeName_2 = SchemaElement(candidateNode, nodeName_2, schemaFile);
			if(result_nodeName_1.equals("true") || result_nodeName_2.equals("true")){
				result = "true";
			}else{
				result = "false";
			}
			Assert.assertTrue(result.equals("true"), "result must contain an element in the substitution group headed by swe:DataRecord or swe:Vector.");
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

private File GetFileViaResourcePath(String resourcePath) {
	URL xsdPath = this.getClass().getResource(resourcePath);
	File file = new File(xsdPath.toString().substring(5));
	return file;
}
//==============================================================================================//	
	/**
	 * Evaluates an XPath 2.0 expression using the Saxon s9api interfaces modified version
	 * @param xmlFile The XML file.
	 * @param expr The XPath expression to be evaluated.
	 * @param nsBindings A collection of namespace bindings required to evaluate the
     *        XPath expression, where each entry maps a namespace URI (key)
     *        to a prefix (value); this may be {@code null} if not needed.
	 * @return An XdmValue object representing a value in the XDM data model;
     *         this is a sequence of zero or more items, where each item is
     *         either an atomic value or a node.
	 * @throws SaxonApiException
	 */
	public static XdmValue evaluateXPath2Modified(File xmlFile, String expr, Map<String, String> nsBindings) throws SaxonApiException {
        Processor proc = new Processor(false);
        XPathCompiler compiler = proc.newXPathCompiler();
        if (null != nsBindings) {
            for (String nsURI : nsBindings.keySet()) {
            	compiler.declareNamespace(nsBindings.get(nsURI), nsURI);
            }
        }
        XPathSelector xpath = compiler.compile(expr).load();
        DocumentBuilder builder = proc.newDocumentBuilder();
        XdmNode node = builder.build(xmlFile);
        
        xpath.setContextItem(node);
        return xpath.evaluate();
	}
	/**
	 * Check XPath2.0 modified version
	 * @param xpath2 String denoting an xpath syntax
	 * @param xmlFile the File xml
	 * @return XdmValue converted to string
	 */
	public XdmValue CheckXPath2Modified(String xpath2, File xmlFile){
		XdmValue xdmValue = null;
        try {
        	xdmValue = evaluateXPath2Modified(xmlFile, xpath2, NamespaceBindings.getStandardBindings());
        } catch (SaxonApiException e) {
			e.printStackTrace();
		}
        return xdmValue;
	}
	
	/** 
	 * Return <code>"true"</code> if candidate element node satisfy all three of the following conditions:
	 * Condition 1: The name of the candidate node matches the specified <code>nodeName</code> or matches the name of an element in a 
	 * substitution group headed by an element named <code>nodeName</code>.
	 * Condition 2: derives-from(AT, ET) is true, where AT is the type annotation of the candidate node and ET is the schema type 
	 * declared for element <code>nodeName</code> in the in-scope element declarations.
	 * Condition 3: If the element declaration for <code>nodeName</code> in the in-scope element declarations is not nillable, then the nilled 
	 * property of the <code>candidateNode</code> is false
	 * @param candidateNode the candidate node for testing
	 * @param nodeName name of node for comparing
	 * @param schemaFile the location of schema used for testing
	 * @return return string value "true" or "false"
	 * @throws SaxonApiException 
	 */
	public String SchemaElement(String candidateNode, String nodeName, File schemaFile) throws SaxonApiException{

		//---Test condition 1--- candidateNode matches the nodeName, or matches the name of an element in a substitution group headed by nodeName
		String test1_result = "true";
		
			/////check whether or not "candidateNode" matches the specified nodeName, if it matches, 
			/////the attribute "abstract" of nodeName must be false or does not exist.
			if(candidateNode.equals(nodeName)){
				String xpath_t1_1 = String.format("//xs:schema/xs:element[@name=\"%s\"]/@abstract", nodeName.split(":")[1]);
				String bool_xpath_t1_1 = String.format("boolean(%s)", xpath_t1_1);
				if(CheckXPath2Modified(bool_xpath_t1_1, schemaFile).equals("true")){
					if(CheckXPath2Modified(xpath_t1_1, schemaFile).toString().split("=")[1].equals("\"true\"")){
						test1_result = "false";
					}
				}
			}else{
				/////check whether or not "candidateNode" matches the name of an element 
				/////in a substitution group headed by "nodeName".
				String sub_name = "";
				String name = String.format("\"%s\"", candidateNode.split(":")[1]);	
				do{
					String xpath_t1_2 = String.format("//xs:schema/xs:element[@name=%s]/@substitutionGroup", name);
					String result_xpath_t1_2 = CheckXPath2Modified(xpath_t1_2, schemaFile).toString();
					if(result_xpath_t1_2.contains("XdmEmptySequence")){
						test1_result = "false";
						break;
					}
					sub_name = result_xpath_t1_2.split("=")[1];
					if(sub_name.equals(String.format("\"%s\"", nodeName))){
						break;
					}else{
						if(sub_name.equals("\"gml:AbstractGML\"") || sub_name.equals("\"gml:AbstractObject\"")){
							test1_result = "false";
							break;
						}
						if(sub_name.equals("\"swe:AbstractSWE\"")){
							test1_result = "false";
							break;
						}
						name = "\"".concat(sub_name.split(":")[1]);
					}
				}while(true);
			}
			
		if(test1_result.equals("false")){
			return "false";
		}
	
		//---Test condition 2--- derives-from(AT, ET) is true. 
		//According to "https://www.w3.org/TR/xpath20/#prod-xpath-SchemaElementTest", section 2.5.4, 
		//derives-from(AT, ET) returns true if ET IS A KNOWN TYPE and ANY OF the following three conditions is true:
		//condition 1 - AT is a schema type found in the in-scope schema definitions, and is the same as ET or is derived by restriction or extension from ET
		//or, condition 2 -  AT is a schema type not found in the in-scope schema definitions, and an implementation-dependent mechanism is able to determine that AT is derived by restriction from ET
		//or, condition 3 - There exists some schema type IT such that derives-from(IT, ET) and derives-from(AT, IT) are true.
		String test2_result = "true";
		
			/////Get the type of candidateNode (AT-Actual Type) and of nodeName (ET-Expected Type).
			String xpath_t2_1 = String.format("//xs:schema/xs:element[@name=\"%s\"]/@type", candidateNode.split(":")[1]);		
			String candidateNode_type = CheckXPath2Modified(xpath_t2_1, schemaFile).toString();
			String xpath_t2_2 = String.format("//xs:schema/xs:element[@name=\"%s\"]/@type", nodeName.split(":")[1]);	
			String nodeName_type = CheckXPath2Modified(xpath_t2_2, schemaFile).toString();
			
			/////Check whether or not ET is a known type
			String xpath_t2_3 = String.format("boolean(//*[@name=%s])", String.format("\"%s", nodeName_type.split(":")[1]));
			if(CheckXPath2Modified(xpath_t2_3, schemaFile).equals("false")){
				test2_result = "false";
			}

			/////Check whether or not AT is a schema type found in the in-scope schema definitions
			String xpath_t2_4 = String.format("boolean(//*[@name=%s])", String.format("\"%s", candidateNode_type.split(":")[1]));
			if(CheckXPath2Modified(xpath_t2_4, schemaFile).equals("false")){
				test2_result = "false";
			}
				
			/////check whether or not AT is the same as ET or is derived by restriction or extension from ET
			if(!candidateNode_type.equals(nodeName_type)){
				String type_element = "";
				String name_element = String.format("\"%s", candidateNode_type.split(":")[1]);
				do{
					String xpath_t2_5 = String.format("//*[@name=%s]//@base", name_element);
					String result_xpath_t2_5 = "";
					if(CheckXPath2Modified(xpath_t2_5, schemaFile).size() > 1){
						result_xpath_t2_5 = CheckXPath2Modified(xpath_t2_5, schemaFile).itemAt(0).getStringValue();
						type_element = String.format("\"%s\"", result_xpath_t2_5);
					}else if(CheckXPath2Modified(xpath_t2_5, schemaFile).size() == 1){
						result_xpath_t2_5 = CheckXPath2Modified(xpath_t2_5, schemaFile).toString();
						type_element = result_xpath_t2_5.split("=")[1];
					}else{
						test2_result = "false";
						break;
					}
					if(type_element.equals(String.format("%s", nodeName_type.split("=")[1]))){
						break;
					}else{
						if(type_element.equals("\"gml:AbstractGMLType\"")){
							test2_result = "false";
							break;
						}
						name_element = "\"".concat(type_element.split(":")[1]);
					}
				}while(true);
			}
				
		if(test2_result.equals("false")){
			return "false";
		}
			
		//---Test condition 3--- 
		//If "candidateNode" is not nillable, then the "nodeName" is not nillable, 
		//and vice versa 	
		String test3_result = "false";
		
			String xpath_t3_1 = String.format("//xs:schema/xs:element[@name=\"%s\"]/@nillable", nodeName.split(":")[1]);
			String xpath_t3_2 = String.format("//xs:schema/xs:element[@name=\"%s\"]/@nillable", candidateNode.split(":")[1]);
			String result_xpath_t3_1 = CheckXPath2Modified(xpath_t3_1, schemaFile).toString();
			String result_xpath_t3_2 = CheckXPath2Modified(xpath_t3_2, schemaFile).toString();
			
			if(result_xpath_t3_1.contains("XdmEmptySequence") && result_xpath_t3_2.contains("XdmEmptySequence")){
				test3_result = "true";
			}else if(result_xpath_t3_1.equals(result_xpath_t3_2)){
				test3_result = "true";
			}
			
		if(test3_result.equals("false")){
			return "false";
		}
		
		String final_result = "";
		if(test1_result.equals("true") && test2_result.equals("true") && test3_result.equals("true")){
			final_result = "true";
		}else{
			final_result = "false";
		}
		
		return final_result;
	}
	
	
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
	
}
