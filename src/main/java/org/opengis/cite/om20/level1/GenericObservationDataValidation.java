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
	
	//A.2 Conformance class: Measurement data
	@Test(groups = "A.2 Conformance class: Measurement data", description = "Verify that the XML element om:result has a value that matches the content model defined by gml:MeasureType")
	public void MeasurementData() {
		//boolean(//om:resultTime)
		String hasResultTime = this.CheckXPath2("boolean(//om:resultTime)");
		if (hasResultTime.equals("false"))
			throw new SkipException("Not measurement data.");
		
		List<String> href = GetResultTypeHref();
		if (!href.contains(this.observation_type_measurement)) {
			throw new SkipException("Not measurement data.");
		}
		
		try {
			List<String> results = CheckObservationTypeMeasurement(this.observation_type_measurement);
			if (results.contains("false")) {
				Assert.assertTrue(false,
						"XML element om:result has a value that matches the content model defined by gml:MeasureType.");
			}else {
				Assert.assertTrue(true,
						"XML element om:result has a value that matches the content model defined by gml:MeasureType.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test(groups = "A.3 Conformance class: Category observation data", description = "Verify that the XML element om:result has a value that matches the content model defined by gml:ReferenceType")
	public void CategoryObservation() {
		//must has resultTime element
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
			}else {
				Assert.assertTrue(true,
						"element om:result has a value that matches the content model defined by gml:ReferenceType.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test(groups = "A.4 Conformance class: Count observation data", description = "Verify that the XML element om:result has a value that matches the content model defined by xs:integer.")
	public void CountObservation() {
		//must has resultTime element
		String hasResultTime = this.CheckXPath2("boolean(//om:resultTime)");
		if (hasResultTime.equals("false"))
			throw new SkipException("Not count observation.");
		
		List<String> href = GetResultTypeHref();
		if (!href.contains(this.observation_type_count)) {
			throw new SkipException("Not count data.");
		}
		
		try {
			List<String> results = this.CheckObservationTypeCount(this.observation_type_count);
			if (results.contains("false")) {
				Assert.assertTrue(false,
						"element om:result has a value that matches the content model defined by xs:integer.");
			}else {
				Assert.assertTrue(true,
						"element om:result has a value that matches the content model defined by xs:integer.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test(groups = "A.5 Conformance class: Truth observation data", description = "Verify that the XML element om:result has a value that matches the content model defined by xs:boolean.")
	public void TruthObservation() {
		//must has resultTime element
		String hasResultTime = this.CheckXPath2("boolean(//om:resultTime)");
		if (hasResultTime.equals("false"))
			throw new SkipException("Not truth observation.");
		
		List<String> href = GetResultTypeHref();
		if (!href.contains(this.observation_type_truth)) {
			throw new SkipException("Not truth data.");
		}		
		
		try {
			List<String> results = this.CheckObservationTypeTruth(this.observation_type_truth);
			if (results.contains("false")) {
				Assert.assertTrue(false,
						"element om:result has a value that matches the content model defined by xs:boolean.");
			}else {
				Assert.assertTrue(true,
						"element om:result has a value that matches the content model defined by xs:boolean.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * A.6 Geometry observation data. Verify that XML element om:result contains
	 * a sub-element in the substitution group of gml:AbstractGeometry
	 */
	@Test(groups = "A.6. Geometry observation data", description = "Validate the XML document using the Schematron document http://schemas.opengis.net/om/2.0/geometryObservation.sch")
	public void ResultGeometry() {
		
		List<String> href = GetResultTypeHref();
		if (!href.contains(this.observation_type_geometry)) {
			throw new SkipException("Not geometry data.");
		}
		
		String candidateNode = CheckXPath2("//om:result/*[1]/name()");
		String nodeName = "gml:AbstractGeometry";

		try {
			File schemaFile = GetFileViaResourcePath(this.Resource_GML_Path);
			String result = SchemaElement(candidateNode, nodeName, schemaFile);
			Assert.assertTrue(result.equals("true"),
					"XML element om:result must contains a sub-element in the substitution group of gml:AbstractGeometry.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private List<String> GetResultTypeHref() {
		int count_observation = Integer.parseInt((CheckXPath2("count(//om:OM_Observation/om:type/@xlink:href)")));
		List<String> list_href = new ArrayList<String>();
		for (int i=1; i <= count_observation; i++ ) {
			list_href.add(CheckXPath2(String.format("string((//om:OM_Observation/om:type/@xlink:href)[%s])", i)));
		}
		return list_href;
	}

	/**
	 * A.7 Temporal observation data. Verify that XML element om:result contains
	 * a sub-element in the substitution group of gml:AbstractTimeObject
	 */
	@Test(groups = "A.7. Temporal observation data", description = "Validate the XML document using the Schematron document http://schemas.opengis.net/om/2.0/temporalObservation.sch")
	public void ResultTimeObject() {
		
		List<String> href = GetResultTypeHref();
		if (!href.contains(this.observation_type_temporal)) {
			throw new SkipException("Not temporal data.");
		}
		
		String candidateNode = CheckXPath2("//om:result/*[1]/name()");
		String nodeName = "gml:AbstractTimeObject";

		try {
			File schemaFile = this.GetFileViaResourcePath(Resource_GML_Path);
			String result = SchemaElement(candidateNode, nodeName, schemaFile);
			Assert.assertTrue(result.equals("true"),
					"XML element om:result must contains a sub-element in the substitution group of gml:AbstractTimeObject.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * A.8 Complex observation data. Verify that the XML element om:result
	 * contains a sub-element swe:DataRecord or swe:Vector with inline values.
	 */
	@Test(groups = "A.8. Complex observation data", description = "Validate the XML document using the Schematron document http://schemas.opengis.net/om/2.0/complexObservation.sch.")
	public void ResultSWErecord() {
		
		List<String> href = GetResultTypeHref();
		if (!href.contains(this.observation_type_complex)) {
			throw new SkipException("Not complex data.");
		}
		
		String candidateNode = CheckXPath2("//om:result/*[1]/name()");
		String nodeName_1 = "swe:DataRecord";
		String nodeName_2 = "swe:Vector";
		String result = "";

		try {
			File schemaFile = GetFileViaResourcePath(this.Resource_SWE_Path);
			// File schemaFile = new File(xsdPath.toString().substring(5));
			String result_nodeName_1 = SchemaElement(candidateNode, nodeName_1, schemaFile);
			String result_nodeName_2 = SchemaElement(candidateNode, nodeName_2, schemaFile);
			if (result_nodeName_1.equals("true") || result_nodeName_2.equals("true")) {
				result = "true";
			} else {
				result = "false";
			}
			Assert.assertTrue(result.equals("true"),
					"result must contain an element in the substitution group headed by swe:DataRecord or swe:Vector.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * A.9 SWE scalar observation data. Verify that the XML element om:result
	 * contains a concrete sub-element in the substitution group
	 * swe:AbstractScalarComponent containing an inline value
	 */
	@Test(groups = "A.9. SWE scalar observation data", description = "Validate the XML document using the Schematron document http://schemas.opengis.net/om/2.0/SWEScalarObservation.sch")
	public void ResultSWEscalar() {
		
		List<String> href = GetResultTypeHref();
		if (!href.contains(this.observation_type_swe_simple)) {
			throw new SkipException("Not SWE scalar observation.");
		}

		String candidateNode = CheckXPath2("//om:result/*[1]/name()");
		String inline_value = CheckXPath2("boolean(//om:result/*[1]/swe:value)");
		String nodeName = "swe:AbstractSimpleComponent";

		String final_result = "";
		try {
			File schemaFile = GetFileViaResourcePath(this.Resource_SWE_Path);
			String result = SchemaElement(candidateNode, nodeName, schemaFile);
			if (result.equals("true") && inline_value.equals("true")) {
				final_result = "true";
			} else {
				final_result = "false";
			}
			Assert.assertTrue(final_result.equals("true"),
					"XML element om:result must contain an element in the substitution group headed by swe:AbstractSimpleComponent with an inline value.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * A.10 SWE array observation data. Verify that the XML element om:result
	 * contains a sub-element swe:DataArray, swe:Matrix or swe:DataStream
	 * containing inline values.
	 */
	@Test(groups = "A.10. SWE array observation data", description = "Validate the XML document using the Schematron document http://schemas.opengis.net/om/2.0/SWEArrayObservation.sch")
	public void ResultSWEBlock() {

		List<String> href = GetResultTypeHref();
		if (!href.contains(this.observation_type_swe_array)) {
			throw new SkipException("Not SWE array observation.");
		}
		
		String candidateNode = CheckXPath2("//om:result/*[1]/name()");
		String nodeName_1 = "swe:DataArray";
		String nodeName_2 = "swe:Matrix";
		String nodeName_3 = "swe:DataStream";
		String inline_value = CheckXPath2("boolean(//om:result/*[1]/swe:values)");

		String final_result = "";
		try {
			File schemaFile = GetFileViaResourcePath(this.Resource_SWE_Path);
			String result_1 = SchemaElement(candidateNode, nodeName_1, schemaFile);
			String result_2 = SchemaElement(candidateNode, nodeName_2, schemaFile);
			String result_3 = SchemaElement(candidateNode, nodeName_3, schemaFile);
			if ((result_1.equals("true") || result_2.equals("true") || result_3.equals("true"))
					&& inline_value.equals("true")) {
				final_result = "true";
			} else {
				final_result = "false";
			}
			Assert.assertTrue(final_result.equals("true"),
					"result must contain an element in the substitution group headed by swe:DataArray, swe:Matrix or swe:DataStream and with inline values.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * A.11 Spatial observation data. Verify that the observation has exactly
	 * one sampling geometry encoded as XML element om:parameter/om:NamedValue
	 * in an observation, and that its sub-element om:name has an xlink:href
	 * attribute with the value
	 * http://www.opengis.net/def/paramname/OGC-OM/2.0/samplingGeometry, and its
	 * sub-element om:value contains a sub-element in the substitution group of
	 * gml:AbstractGeometry
	 */
	@Test(groups = "A.11. Spatial observation data", description = "Validate the XML document using the Schematron document http://schemas.opengis.net/om/2.0/spatialObservation.sch")
	public void SpatialParameter() {
		// According to "spatialObservation.sch", A spatial observation shall
		// have exactly one sampling geometry
		// encoded as XML element om:parameter in an observation
		if (CheckXPath2("boolean(//*[om:resultTime]/om:parameter)").equals("false")) {
			throw new SkipException("Not Spatial observation data.");
		}
		
		if (CheckXPath2("boolean(//om:parameter/om:NamedValue/om:name[@xlink:href = 'http://www.opengis.net/req/omxml/2.0/data/samplingGeometry'] | /om:parameter/om:NamedValue[om:name/@xlink:href= 'http://www.opengis.net/req/omxml/2.0/data/samplingGeometry'])"
						).equals("false")) {
			throw new SkipException("Not Spatial observation data.");
		}
		// ---Test rule 1---
		// The xlink:href attribute in the XML element om:name of
		// the om:parameter/om:NamedValue element that carries the sampling
		// geometry SHALL have the value
		// 'http://www.opengis.net/req/omxml/2.0/data/samplingGeometry'
		String result_1 = "true";
		String xpath_rule1 = "count(//om:parameter/om:NamedValue/om:name[@xlink:href = 'http://www.opengis.net/req/omxml/2.0/data/samplingGeometry']) = 1";
		if (CheckXPath2(xpath_rule1).equals("false")) {
			result_1 = "false";
		}

		// ---Test rule 2---
		// The XML element om:value in the om:parameter/om:NamedValue element
		// which carries the sampling
		// geometry shall have a value of type gml:AbstractGeometry
		String result_2 = "true";
		String xpath_rule2 = "//*[om:resultTime]/om:parameter/om:NamedValue[om:name/@xlink:href= 'http://www.opengis.net/req/omxml/2.0/data/samplingGeometry']/om:value/*[1]/name()";
		String candidateNode = CheckXPath2(xpath_rule2);
		String nodeName = "gml:AbstractGeometry";
		if (candidateNode.contains("XdmEmptySequence")) {
			result_2 = "false";
		} else {
			try {
				File schemaFile = this.GetFileViaResourcePath(this.Resource_GML_Path);
				result_2 = SchemaElement(candidateNode, nodeName, schemaFile);
			} catch (SaxonApiException e) {
				e.printStackTrace();
			}
		}

		// Get final testing result
		String final_result = "";
		if (result_1.equals("true") && result_2.equals("true")) {
			final_result = "true";
		} else {
			final_result = "false";
		}
		Assert.assertTrue(final_result.equals("true"),
				"A spatial observation MUST have exactly one sampling geometry encoded as XML element om:parameter in an observation."
						+ "	The xlink:href attribute in the XML element om:name of the om:parameter/om:NamedValue element that "
						+ "	carries the sampling geometry MUST have the value 'http://www.opengis.net/req/omxml/2.0/data/samplingGeometry'. "
						+ "	And, the XML element om:value in the om:parameter/om:NamedValue element which carries the sampling "
						+ "	geometry MUST have a value of type gml:AbstractGeometry");
	}

	/**
	 * A.12 Sampling feature data. Verify that any XML element in the
	 * substitution group of sam:SF_SamplingFeature is well-formed and valid
	 */
	@Test(groups = "A.12. Sampling feature data", description = "Validate the XML document using the XML schema document http://schemas.opengis.net/sampling/2.0/samplingFeature.xsd")
	public void SamplingValid() {
		if (CheckXPath2("boolean(//sam:SF_SamplingFeature)").equals("false")) {
			throw new SkipException("Not Sampling feature data.");
		} else {
			URL entityCatalog = this.getClass()
					.getResource("/org/opengis/cite/om20/xsd/opengis/om/2.0/samplingFeature.xsd");
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

	/**
	 * A.13 Spatial Sampling feature data. Verify that any XML element in the
	 * substitution group of sams:SF_SpatialSamplingFeature is well-formed and
	 * valid.
	 */
	@Test(groups = "A.13. Spatial Sampling feature data", description = "Validate the XML document using the XML schema document http://schemas.opengis.net/samplingSpatial/2.0/spatialSamplingFeature.xsd")
	public void SpatialSamplingValid() {
		if (CheckXPath2("boolean(//sams:SF_SpatialSamplingFeature)").equals("true")) {
			URL entityCatalog = this.getClass()
					.getResource("/org/opengis/cite/om20/xsd/opengis/om/2.0/spatialSamplingFeature.xsd");
			Validator validator;
			try {
				validator = CreateValidator(entityCatalog);
				Source source = new DOMSource(this.testSubject);
				ETSAssert.assertSchemaValid(validator, source);
			} catch (XMLStreamException | SAXException | IOException e) {
				e.printStackTrace();
			}
		} else {
			throw new SkipException("Not Spatial Sampling feature data.");
		}

	}

	/**
	 * A.13 Spatial Sampling feature data. Verify that the content model of any
	 * sams:shape element is consistent with the value of the xlink:href
	 * attribute of the sam:type element if one is present as a sub-element of
	 * the parent sams:SF_SpatialSamplingFeature
	 */
	@Test(groups = "A.13. Spatial Sampling feature data", description = "Validate the XML document using the Schematron document http://schemas.opengis.net/samplingSpatial/2.0/shapeTypeConsistent.sch")
	public void ShapeTypeConsistent() {
		if (CheckXPath2("boolean(//sams:SF_SpatialSamplingFeature)").equals("false")) {
			throw new SkipException("Not Spatial Sampling feature data.");
		} else {
			final String sampling_feature_type_point = "http://www.opengis.net/def/samplingFeatureType/OGC-OM/2.0/SF_SamplingPoint";
			final String sampling_feature_type_curve = "http://www.opengis.net/def/samplingFeatureType/OGC-OM/2.0/SF_SamplingCurve";
			final String sampling_feature_type_surface = "http://www.opengis.net/def/samplingFeatureType/OGC-OM/2.0/SF_SamplingSurface";
			final String sampling_feature_type_solid = "http://www.opengis.net/def/samplingFeatureType/OGC-OM/2.0/SF_SamplingSolid";

			String href = CheckXPath2("string(//sams:SF_SpatialSamplingFeature/sam:type/@xlink:href)");
			if (href.equals(sampling_feature_type_point)) {
				String final_result = testA13("gml:Point");
				Assert.assertTrue(final_result.equals("true"),
						"a member of the substitution group headed by gml:Point or an xlink must be present as child of sams:shape");
			} else if (href.equals(sampling_feature_type_curve)) {
				String final_result = testA13("gml:AbstractCurve");
				Assert.assertTrue(final_result.equals("true"),
						"a member of the substitution group headed by gml:Curve or an xlink must be present as child of sams:shape");
			} else if (href.equals(sampling_feature_type_surface)) {
				String final_result = testA13("gml:AbstractSurface");
				Assert.assertTrue(final_result.equals("true"),
						"a member of the substitution group headed by gml:AbstractSurface or an xlink must be present as child of sams:shape");
			} else if (href.equals(sampling_feature_type_solid)) {
				String final_result = testA13("gml:AbstractSolid");
				Assert.assertTrue(final_result.equals("true"),
						"a member of the substitution group headed by gml:AbstractSolid or an xlink must be present as child of sams:shape");
			} else {
				String final_result = "false";
				Assert.assertTrue(final_result.equals("true"), "Invalid xlink:href attribute of sam:type element");
			}
		}
	}

	private String testA13(String schema_element_Value) {
		// condition 1: sams:shape/schema-element(schema_element_Value)
		String result_1 = "";
		String candidateNode = CheckXPath2("//sams:SF_SpatialSamplingFeature/sams:shape/*[1]/name()");
		String nodeName = schema_element_Value;
		try {
			File schemaFile = GetFileViaResourcePath(this.Resource_GML_Path);
			result_1 = SchemaElement(candidateNode, nodeName, schemaFile);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// condition 2: sams:shape/@xlink:href
		String result_2 = CheckXPath2("boolean(//sams:SF_SpatialSamplingFeature/sams:shape/@xlink:href)");

		// get final testing result
		String final_result = "";
		if (result_1.equals("true") || result_2.equals("true")) {
			final_result = "true";
		} else {
			final_result = "false";
		}
		return final_result;
	}

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

	private File GetFileViaResourcePath(String resourcePath) {
		try {
        InputStream in = this.getClass().getResourceAsStream(resourcePath);
        if (in == null) {
            return null;
        }

        File tempFile = File.createTempFile(String.valueOf(in.hashCode()), ".tmp");
        tempFile.deleteOnExit();

        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
        return tempFile;
    } catch (IOException e) {
			URL xsdPath = this.getClass().getResource(resourcePath);
			File file = new File(xsdPath.toString().substring(5));
			return file;
    }
	}

	/**
	 * Create Validator for checking XML file against XML Schema file
	 * 
	 * @param xsdPath URL path of the XSD file
	 * @return schema validator
	 * @throws XMLStreamException XMLStreamException
	 * @throws IOException IOException
	 * @throws SAXException SAX Error
	 */
	public Validator CreateValidator(URL xsdPath) throws XMLStreamException, SAXException, IOException {
		Schema schema;
		Set<URI> schemaRefs;
		schemaRefs = ValidationUtils.extractSchemaReferences(new StreamSource(this.dataFile),
				this.testSubjectUri.toString());
		XmlSchemaCompiler xsdCompiler = new XmlSchemaCompiler(xsdPath);
		schema = xsdCompiler.compileXmlSchema(schemaRefs.toArray(new URI[schemaRefs.size()]));
		return schema.newValidator();
	}

	/**
	 * Evaluates an XPath 2.0 expression using the Saxon s9api interfaces
	 * modified version
	 * 
	 * @param xmlFile
	 *            The XML file.
	 * @param expr
	 *            The XPath expression to be evaluated.
	 * @param nsBindings
	 *            A collection of namespace bindings required to evaluate the
	 *            XPath expression, where each entry maps a namespace URI (key)
	 *            to a prefix (value); this may be {@code null} if not needed.
	 * @return An XdmValue object representing a value in the XDM data model;
	 *         this is a sequence of zero or more items, where each item is
	 *         either an atomic value or a node.
	 * @throws SaxonApiException SaxonApiException
	 */
	public static XdmValue evaluateXPath2Modified(File xmlFile, String expr, Map<String, String> nsBindings)
			throws SaxonApiException {
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
	 * 
	 * @param xpath2
	 *            String denoting an xpath syntax
	 * @param xmlFile
	 *            the File xml
	 * @return XdmValue converted to string
	 */
	public XdmValue CheckXPath2Modified(String xpath2, File xmlFile) {
		XdmValue xdmValue = null;
		try {
			xdmValue = evaluateXPath2Modified(xmlFile, xpath2, NamespaceBindings.getStandardBindings());
		} catch (SaxonApiException e) {
			e.printStackTrace();
		}
		return xdmValue;
	}

	/**
	 * Return <code>"true"</code> if candidate element node satisfy all three of
	 * the following conditions: Condition 1: The name of the candidate node
	 * matches the specified <code>nodeName</code> or matches the name of an
	 * element in a substitution group headed by an element named
	 * <code>nodeName</code>. Condition 2: derives-from(AT, ET) is true, where
	 * AT is the type annotation of the candidate node and ET is the schema type
	 * declared for element <code>nodeName</code> in the in-scope element
	 * declarations. Condition 3: If the element declaration for
	 * <code>nodeName</code> in the in-scope element declarations is not
	 * nillable, then the nilled property of the <code>candidateNode</code> is
	 * false
	 * 
	 * @param candidateNode
	 *            the candidate node for testing
	 * @param nodeName
	 *            name of node for comparing
	 * @param schemaFile
	 *            the location of schema used for testing
	 * @return return string value "true" or "false"
	 * @throws SaxonApiException SaxonApiException
	 */
	public String SchemaElement(String candidateNode, String nodeName, File schemaFile) throws SaxonApiException {
		// ---Test condition 1--- candidateNode matches the nodeName, or matches
		// the name of an element in a substitution group headed by nodeName
		String test1_result = "true";

		///// check whether or not "candidateNode" matches the specified
		///// nodeName, if it matches,
		///// the attribute "abstract" of nodeName must be false or does not
		///// exist.
		if (candidateNode.equals(nodeName)) {
			String xpath_t1_1 = String.format("//xs:schema/xs:element[@name=\"%s\"]/@abstract", nodeName.split(":")[1]);
			String bool_xpath_t1_1 = String.format("boolean(%s)", xpath_t1_1);
			if (CheckXPath2Modified(bool_xpath_t1_1, schemaFile).equals("true")) {
				if (CheckXPath2Modified(xpath_t1_1, schemaFile).toString().split("=")[1].equals("\"true\"")) {
					test1_result = "false";
				}
			}
		} else {
			///// check whether or not "candidateNode" matches the name of an
			///// element
			///// in a substitution group headed by "nodeName".
			String sub_name = "";
			String name = String.format("\"%s\"", candidateNode.split(":")[1]);
			do {
				String xpath_t1_2 = String.format("//xs:schema/xs:element[@name=%s]/@substitutionGroup", name);
				String result_xpath_t1_2 = CheckXPath2Modified(xpath_t1_2, schemaFile).toString();
				if (result_xpath_t1_2.contains("XdmEmptySequence")) {
					test1_result = "false";
					break;
				}
				sub_name = result_xpath_t1_2.split("=")[1];
				if (sub_name.equals(String.format("\"%s\"", nodeName))) {
					break;
				} else {
					if (sub_name.equals("\"gml:AbstractGML\"") || sub_name.equals("\"gml:AbstractObject\"")) {
						test1_result = "false";
						break;
					}
					if (sub_name.equals("\"swe:AbstractSWE\"")) {
						test1_result = "false";
						break;
					}
					name = "\"".concat(sub_name.split(":")[1]);
				}
			} while (true);
		}

		if (test1_result.equals("false")) {
			return "false";
		}

		// ---Test condition 2--- derives-from(AT, ET) is true.
		// According to
		// "https://www.w3.org/TR/xpath20/#prod-xpath-SchemaElementTest",
		// section 2.5.4,
		// derives-from(AT, ET) returns true if ET IS A KNOWN TYPE and ANY OF
		// the following three conditions is true:
		// condition 1 - AT is a schema type found in the in-scope schema
		// definitions, and is the same as ET or is derived by restriction or
		// extension from ET
		// or, condition 2 - AT is a schema type not found in the in-scope
		// schema definitions, and an implementation-dependent mechanism is able
		// to determine that AT is derived by restriction from ET
		// or, condition 3 - There exists some schema type IT such that
		// derives-from(IT, ET) and derives-from(AT, IT) are true.
		String test2_result = "true";

		///// Get the type of candidateNode (AT-Actual Type) and of nodeName
		///// (ET-Expected Type).
		String xpath_t2_1 = String.format("//xs:schema/xs:element[@name=\"%s\"]/@type", candidateNode.split(":")[1]);
		String candidateNode_type = CheckXPath2Modified(xpath_t2_1, schemaFile).toString();
		String xpath_t2_2 = String.format("//xs:schema/xs:element[@name=\"%s\"]/@type", nodeName.split(":")[1]);
		String nodeName_type = CheckXPath2Modified(xpath_t2_2, schemaFile).toString();

		///// Check whether or not ET is a known type
		String xpath_t2_3 = String.format("boolean(//*[@name=%s])", String.format("\"%s", nodeName_type.split(":")[1]));
		if (CheckXPath2Modified(xpath_t2_3, schemaFile).equals("false")) {
			test2_result = "false";
		}

		///// Check whether or not AT is a schema type found in the in-scope
		///// schema definitions
		String xpath_t2_4 = String.format("boolean(//*[@name=%s])",
				String.format("\"%s", candidateNode_type.split(":")[1]));
		if (CheckXPath2Modified(xpath_t2_4, schemaFile).equals("false")) {
			test2_result = "false";
		}

		///// check whether or not AT is the same as ET or is derived by
		///// restriction or extension from ET
		if (!candidateNode_type.equals(nodeName_type)) {
			String type_element = "";
			String name_element = String.format("\"%s", candidateNode_type.split(":")[1]);
			do {
				String xpath_t2_5 = String.format("//*[@name=%s]//@base", name_element);
				String result_xpath_t2_5 = "";
				if (CheckXPath2Modified(xpath_t2_5, schemaFile).size() > 1) {
					result_xpath_t2_5 = CheckXPath2Modified(xpath_t2_5, schemaFile).itemAt(0).getStringValue();
					type_element = String.format("\"%s\"", result_xpath_t2_5);
				} else if (CheckXPath2Modified(xpath_t2_5, schemaFile).size() == 1) {
					result_xpath_t2_5 = CheckXPath2Modified(xpath_t2_5, schemaFile).toString();
					type_element = result_xpath_t2_5.split("=")[1];
				} else {
					test2_result = "false";
					break;
				}
				if (type_element.equals(String.format("%s", nodeName_type.split("=")[1]))) {
					break;
				} else {
					if (type_element.equals("\"gml:AbstractGMLType\"")) {
						test2_result = "false";
						break;
					}
					name_element = "\"".concat(type_element.split(":")[1]);
				}
			} while (true);
		}

		if (test2_result.equals("false")) {
			return "false";
		}

		// ---Test condition 3---
		// If "candidateNode" is not nillable, then the "nodeName" is not
		// nillable,
		// and vice versa
		String test3_result = "false";

		String xpath_t3_1 = String.format("//xs:schema/xs:element[@name=\"%s\"]/@nillable", nodeName.split(":")[1]);
		String xpath_t3_2 = String.format("//xs:schema/xs:element[@name=\"%s\"]/@nillable",
				candidateNode.split(":")[1]);
		String result_xpath_t3_1 = CheckXPath2Modified(xpath_t3_1, schemaFile).toString();
		String result_xpath_t3_2 = CheckXPath2Modified(xpath_t3_2, schemaFile).toString();

		if (result_xpath_t3_1.contains("XdmEmptySequence") && result_xpath_t3_2.contains("XdmEmptySequence")) {
			test3_result = "true";
		} else if (result_xpath_t3_1.equals(result_xpath_t3_2)) {
			test3_result = "true";
		}

		if (test3_result.equals("false")) {
			return "false";
		}

		String final_result = "";
		if (test1_result.equals("true") && test2_result.equals("true") && test3_result.equals("true")) {
			final_result = "true";
		} else {
			final_result = "false";
		}

		return final_result;
	}

	/**
	 * Create validator from xsd file
	 * 
	 * @param xsdPath
	 *            A URL that denotes the location of a XML schema.
	 * @throws SAXException SAXException
	 * @throws URISyntaxException URISyntaxException
	 * @throws IOException IOException
	 * @return schema validator
	 */
	public Validator CreateValidatorFromXSD(URL xsdPath) throws SAXException, IOException, URISyntaxException {
		XmlSchemaCompiler compiler = new XmlSchemaCompiler(xsdPath);
		Schema schema = compiler.compileXmlSchema(xsdPath.toURI());
		return schema.newValidator();
	}

	/**
	 * Check XPath2.0
	 * 
	 * @param xpath
	 *            String denoting an xpath syntax
	 * @return XdmValue converted to string
	 */
	public String CheckXPath2(String xpath) {
		XdmValue xdmValue = null;
		try {
			xdmValue = XMLUtils.evaluateXPath2(new DOMSource(this.testSubject), xpath,
					NamespaceBindings.getStandardBindings());
		} catch (SaxonApiException e) {
			e.printStackTrace();
		};
		return xdmValue.toString();
	}

	/**
	 * Check Observation Type Measurement in schematron document
	 * resultTypeConsistent.sch
	 * 
	 * @param href
	 *            the value of om:type/xlink:href to make the context
	 * @return return list of values containing "true" or "false"
	 */
	public List<String> CheckObservationTypeMeasurement(String href) {
		List<String> results = new ArrayList<String>();
		String context = String.format("//om:OM_Observation[om:type/@xlink:href='%s']", href);
	
		int count_observation = Integer.parseInt((CheckXPath2(String.format("count(%s)", context))));
		for (int i = 1; i <= count_observation; i++) {
			String uom_value = String.format("(%s/om:result/@uom)[%s]", context,i);
			String result_text = String.format("(%s/om:result/text())[%s]", context,i);
			String xpath = String.format(
					"(((%s castable as xs:string) and (string-length(%s) > 0) and (not(matches(%s, \"[: \\n\\r\\t]+\"))))  or ((%s castable as xs:anyURI) and matches(%s , \"([a-zA-Z][a-zA-Z0-9\\-\\+\\.]*:|\\.\\./|\\./|#).*\"))) and (%s castable as xs:double)",
					uom_value, uom_value, uom_value, uom_value, uom_value, result_text);
			results.add(CheckXPath2(xpath));
		}
		return results;
	}

	/**
	 * Check Observation Type Category in schematron document
	 * resultTypeConsistent.sch
	 * 
	 * @param href
	 *            the value of om:type/xlink:href to make the context
	 * @return return list of values containing "true" or "false"
	 */
	public List<String> CheckObservationTypeCategory(String href) {
		List<String> results = new ArrayList<String>();	
		String context = String.format("//om:OM_Observation[om:type/@xlink:href='%s']", href);
		int count_observation = Integer.parseInt((CheckXPath2(String.format("count(%s)", context))));
		
		for (int i = 1; i <= count_observation; i++) {
			boolean test1 = true;
			boolean test2 = false;
			
			if (CheckXPath2(String.format("(%s/om:result/@xlink:href)[%s]", context, i)).contains("XdmEmptySequence")
					|| CheckXPath2(String.format("(%s/om:result/@xlink:title)[%s]", context, i)).contains("XdmEmptySequence")) {
				test1 = false;
			}
			//the result cannot have any child element nor text
			
			if (CheckXPath2(String.format("(%s/om:result/*)[%s]", context, i)).contains("XdmEmptySequence")
					&& CheckXPath2(String.format("(%s/om:result/text())[%s]", context, i)).contains("XdmEmptySequence")) {
				test2 = true;
			}
			if (test1 && test2) {
				results.add("true");
			} else {
				results.add("false");
			}
		}
		return results;
	}

	/**
	 * Check Observation Type Count in schematron document
	 * resultTypeConsistent.sch
	 * 
	 * @param href
	 *            the value of om:type/xlink:href to make the context
	 * @return return list of values containing "true" or "false"
	 */
	public List<String> CheckObservationTypeCount(String href) {
		List<String> results = new ArrayList<String>();
		String context = String.format("//om:OM_Observation[om:type/@xlink:href='%s']", href);
		int count_observation = Integer.parseInt((CheckXPath2(String.format("count(%s)", context))));
		for (int i = 1; i <= count_observation; i++) {
			results.add(CheckXPath2(String.format("(%s/om:result/text())[%s] castable as xs:integer)", context, i)));
		}
		return results;
	}

	/**
	 * Check Observation Type Truth in schematron document
	 * resultTypeConsistent.sch
	 * 
	 * @param href
	 *            the value of om:type/xlink:href to make the context
	 * @return return list of values containing "true" or "false"
	 */
	public List<String> CheckObservationTypeTruth(String href) {
		List<String> results = new ArrayList<String>();
		String context = String.format("//om:OM_Observation[om:type/@xlink:href='%s']", href);
		int count_observation = Integer.parseInt((CheckXPath2(String.format("count(%s)", context))));
		for (int i = 1; i<= count_observation; i++) {
			results.add(CheckXPath2(String.format("(%s/om:result/text())[%s] castable as xs:boolean", context, i)));
		}
		return results;
	}

}
