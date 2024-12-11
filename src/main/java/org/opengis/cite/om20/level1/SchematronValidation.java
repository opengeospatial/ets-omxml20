// package org.opengis.cite.om20.level1;
//
// import java.io.File;
// import java.io.FileInputStream;
// import java.io.IOException;
// import java.net.URI;
// import java.net.URL;
// import java.util.HashMap;
// import java.util.Map;
// import java.util.Set;
// import java.util.logging.Level;
//
// import javax.xml.stream.XMLInputFactory;
// import javax.xml.stream.XMLStreamReader;
//
// import org.opengis.cite.om20.ETSAssert;
// import org.opengis.cite.om20.ErrorMessage;
// import org.opengis.cite.om20.ErrorMessageKeys;
// import org.opengis.cite.om20.Namespaces;
// import org.opengis.cite.om20.SuiteAttribute;
// import org.opengis.cite.om20.TestRunArg;
// import org.opengis.cite.om20.util.XMLUtils;
// import org.opengis.cite.om20.util.NamespaceBindings;
// import org.opengis.cite.om20.util.NamespaceResolver;
// import org.opengis.cite.om20.util.TestSuiteLogger;
// import org.opengis.cite.om20.util.URIUtils;
// import org.opengis.cite.validation.SchematronValidator;
// import org.testng.Assert;
// import org.testng.ITestContext;
// import org.testng.SkipException;
// import org.testng.annotations.BeforeClass;
// import org.testng.annotations.Test;
// import org.testng.log4testng.Logger;
// import org.w3c.dom.Node;
// import org.w3c.dom.NodeList;
//
// import javax.xml.transform.sax.SAXSource;
// import javax.xml.transform.stream.StreamSource;
// import javax.xml.xpath.XPath;
// import javax.xml.xpath.XPathConstants;
// import javax.xml.xpath.XPathExpression;
// import javax.xml.xpath.XPathExpressionException;
// import javax.xml.xpath.XPathFactory;
// import javax.xml.xpath.XPathFactoryConfigurationException;
//
// import net.sf.saxon.Configuration;
// import net.sf.saxon.lib.NamespaceConstant;
// import net.sf.saxon.om.DocumentInfo;
// import net.sf.saxon.om.NodeInfo;
// import net.sf.saxon.trans.XPathException;
// import net.sf.saxon.xpath.XPathFactoryImpl;
//
//
//// import org.apache.xpath.jaxp.XPathFactoryImpl;
/// **
// * Verifies that a GML instance document adheres to the constraints defined in
// * Schematron schemas. An application-specific schema may be associated with the
// * instance document by either of the following means:
// *
// * <ol>
// * <li>Specify the schema location using the <code>xml-model</code> processing
// * instruction, where the value of the "schematypens" data item is the name of
// * the Schematron namespace (see sample listing below).</li>
// * <li>Specify the schema location as the value of the {@link TestRunArg#SCH
// * sch} test run argument;</li>
// *
// * </ol>
// * <p>
// * <strong>Using the xml-model PI to refer to a Schematron schema</strong>
// * </p>
// *
// * <pre>
// * {@code
// * <?xml version="1.0" encoding="UTF-8"?>
// * <?xml-model href="http://example.org/constraints.sch"
// * schematypens="http://purl.oclc.org/dsdl/schematron"
// * phase="#ALL"?>
// * }
// * </pre>
// *
// * The processing instruction takes precedence if multiple schema references are
// * found.
// *
// * <p style="margin-bottom: 0.5em">
// * <strong>Sources</strong>
// * </p>
// * <ul>
// * <li><a href=
// *
// "http://standards.iso.org/ittf/PubliclyAvailableStandards/c040833_ISO_IEC_19757-3_2006(E).zip"
// * >ISO 19757-3:2006</a></li>
// * <li><a href="http://www.w3.org/TR/xml-model/">Associating Schemas with XML
// * documents 1.0 (Second Edition)</a></li>
// * </ul>
// *
// */
// public class SchematronValidation extends DataFixture {
//
// private SchematronValidator dataValidator;
//
// /**
// * Attempts to construct a Schematron validator from a schema reference
// * given in (a) the GML data file, or (b) a test run argument (in the ISuite
// * context).
// *
// * @param testContext
// * The test set context.
// */
// @BeforeClass
// public void createSchematronValidator(ITestContext testContext) {
//// Map<String, String> piData = getXmlModelPIData(this.dataFile);
//// String phase = "#ALL";
//// URI schematronURI;
//// Source schema = null;
//// if (isSchematronReference(piData)) {
//// schematronURI = URI.create(piData.get("href"));
//// if (!schematronURI.isAbsolute()) {
//// // resolve relative URI against location of GML data
//// String dataURI = testContext.getSuite().getParameter(TestRunArg.GML.toString());
//// URI baseURI = URI.create(dataURI);
//// schematronURI = baseURI.resolve(schematronURI);
//// }
//// schema = new StreamSource(schematronURI.toString());
//// if (piData.containsKey("phase"))
//// phase = piData.get("phase");
//// } else { // look for suite attribute (test run argument)
//// Set<String> suiteAttrs = testContext.getSuite().getAttributeNames();
//// if (suiteAttrs.contains(SuiteAttribute.SCHEMATRON.getName())) {
//// schematronURI = (URI)
// testContext.getSuite().getAttribute(SuiteAttribute.SCHEMATRON.getName());
//// schema = new StreamSource(schematronURI.toString());
//// }
//// }
//// if (null != schema) {
//// try {
//// this.dataValidator = new SchematronValidator(schema, phase);
//// } catch (Exception e) {
//// Logger.getLogger(getClass()).warn("Failed to create SchematronValidator.\n", e);
//// }
//// }
// }
// // A.3
// /**
// * Verify that the XML element om:result has a value that matches the
// * content model defined by gml:ReferenceType
// * @throws XPathExpressionException
// * @throws XPathFactoryConfigurationException
// */
// @Test(description = "Checks categoryObservation Schematron rules")
// public void checkCategoryObservation() throws XPathExpressionException,
// XPathFactoryConfigurationException {
// URL schRef =
// this.getClass().getResource("/org/opengis/cite/om20/sch/categoryObservation.sch");
// this.validate(schRef);
//// URL schRef =
// this.getClass().getResource("/org/opengis/cite/om20/xsd/opengis/om/2.0/observation.xsd");
// //ETSAssert.assertSchematronValid(schRef, new StreamSource(this.dataFile));
// }
// /**
// * [{@code Test}] Verifies that a GML instance satisfies the additional
// * Schematron constraints specified in ISO 19136.
// *
// * <p style="margin-bottom: 0.5em">
// * <strong>Sources</strong>
// * </p>
// * <ul>
// * <li><a href=
// * "http://schemas.opengis.net/gml/3.2.1/SchematronConstraints.xml">
// * Schematron constraints for ISO 19136</a></li>
// * </ul>
// */
//
// @Test(description = "Checks general Schematron rules specified in ISO 19136")
// public void checkGMLSchematronConstraints() {
// URL schRef = this.getClass().getResource("/org/opengis/cite/om20/sch/gml-3.2.1.sch");
// //ETSAssert.assertSchematron(schRef, this.dataFile);
// }
//
// // A.1
// /**
// * Verify that any XML element in the substitution group of
// * om:OM_Observation is well-formed and valid
// */
// @Test(description = "Checks Observation XSD")
// public void checkObservation() {
// URL schRef =
// this.getClass().getResource("/org/opengis/cite/om20/xsd/opengis/om/2.0/observation.xsd");
// //ETSAssert.assertSchematronValid(schRef, new StreamSource(this.dataFile));
// }
//
// /**
// * Verify that the content model of any om:result element is consistent with
// * the value of the xlink:href attribute of the om:type element if one is
// * present as a sub-element of the parent om:OM_Observation, according to
// * the mapping given in Table 5.
// */
// @Test(description = "Checks resultTypeConsistent Schematron")
// public void checkResultTypeConsistent() {
// URL schRef =
// this.getClass().getResource("/org/opengis/cite/om20/sch/resultTypeConsistent.sch");
// //ETSAssert.assertSchematronValid(schRef, new StreamSource(this.dataFile));
// }
//
// // A.2
// /**
// * Verify that the XML element om:result has a value that matches the
// * content model defined by gml:MeasureType
// */
// @Test(description = "Checks measurement Schematron")
// public void checkMeasurement() {
// URL schRef = this.getClass().getResource("/org/opengis/cite/om20/sch/measurement.sch");
// //ETSAssert.assertSchematronValid(schRef, new StreamSource(this.dataFile));
// }
//
// // A.4
// /**
// * Verify that the XML element om:result has a value that matches the
// * content model defined by xs:integer.
// */
// @Test(description = "Checks countObservation Schematron rules")
// public void checkCountObservation() {
// //URL schRef =
// this.getClass().getResource("/org/opengis/cite/om20/sch/countObservation.sch");
// //validate(schRef);
//// File schFile;
//// try {
//// schFile = URIUtils.resolveURIAsFile(URI.create(schRef.toString()));
//// ETSAssert.assertSchematron(schFile, this.dataFile);
//// } catch (IOException e) {
//// // TODO Auto-generated catch block
//// e.printStackTrace();
//// }
// //ETSAssert.assertSchematronValid(schRef, new StreamSource(this.dataFile));
//
// }
//
// void validate(URL schematronRef){
// File schFile;
// try {
// schFile = URIUtils.resolveURIAsFile(URI.create(schematronRef.toString()));
// ETSAssert.assertSchematron(schFile, this.dataFile);
// } catch (IOException e) {
// // TODO Auto-generated catch block
// e.printStackTrace();
// }catch (Exception e){
// e.printStackTrace();
// }
// }
//
//// // A.5
//// /**
//// * Verify that the XML element om:result has a value that matches the
//// * content model defined by xs:boolean.
//// */
//// @Test(description = "Checks truthObservation Schematron rules")
//// public void checkTruthObservation() {
//// URL schRef =
// this.getClass().getResource("/org/opengis/cite/om20/sch/truthObservation.sch");
//// ETSAssert.assertSchematronValid(schRef, new StreamSource(this.dataFile));
//// }
////
//// // A.6
//// /**
//// * Verify that XML element om:result contains a subelement in the
//// * substitution group of gml:AbstractGeometry
//// */
//// @Test(description = "Checks geometryObservation Schematron rules")
//// public void checkGeometryObservation() {
//// URL schRef =
// this.getClass().getResource("/org/opengis/cite/om20/sch/geometryObservation.sch");
//// ETSAssert.assertSchematronValid(schRef, new StreamSource(this.dataFile));
//// }
////
//// // A.7
//// /**
//// * Verify that XML element om:result contains a subelement in the
//// * substitution group of gml:AbstractTimeObject.
//// */
//// @Test(description = "Checks temporalObservation Schematron rules")
//// public void checkTemporalObservation() {
//// URL schRef =
// this.getClass().getResource("/org/opengis/cite/om20/sch/temporalObservation.sch");
//// ETSAssert.assertSchematronValid(schRef, new StreamSource(this.dataFile));
//// }
////
//// // A.8
//// /**
//// * Verify that the XML element om:result contains a subelement
//// * swe:DataRecord or swe:Vector with inline values
//// */
//// @Test(description = "Checks complexObservation Schematron rules")
//// public void checkComplexObservation() {
//// URL schRef =
// this.getClass().getResource("/org/opengis/cite/om20/sch/complexObservation.sch");
//// ETSAssert.assertSchematronValid(schRef, new StreamSource(this.dataFile));
//// }
////
//// // A.9
//// /**
//// * Verify that the XML element om:result contains a concrete subelement in
//// * the substitution group swe:AbstractScalarComponent containing an inline
//// * value..
//// */
//// @Test(description = "Checks SWEScalarObservation Schematron rules")
//// public void checkSWEScalarObservation() {
//// URL schRef =
// this.getClass().getResource("/org/opengis/cite/om20/sch/SWEScalarObservation.sch");
//// ETSAssert.assertSchematronValid(schRef, new StreamSource(this.dataFile));
//// }
////
//// // A.10
//// /**
//// * Verify that the XML element om:result contains a subelement
//// * swe:DataArray, swe:Matrix or swe:DataStream containing inline values
//// */
//// @Test(description = "Checks SWEArrayObservation Schematron rules")
//// public void checkSWEArrayObservation() {
//// URL schRef =
// this.getClass().getResource("/org/opengis/cite/om20/sch/SWEArrayObservation.sch");
//// ETSAssert.assertSchematronValid(schRef, new StreamSource(this.dataFile));
//// }
////
//// // A.11
//// /**
//// * Verify that the observation has exactly one sampling geometry encoded as
//// * XML element om:parameter/om:NamedValue in an observation, and that its
//// * sub-element om:name has an xlink:href attribute with the value
//// * http://www.opengis.net/def/paramname/ OGC-OM/2.0/samplingGeometry, and
//// * its sub-element om:value contains a subelement in the substitution group
//// * of gml:AbstractGeometry
//// */
//// @Test(description = "Checks spatialObservation Schematron rules")
//// public void checkSpatialObservation() {
//// URL schRef =
// this.getClass().getResource("/org/opengis/cite/om20/sch/spatialObservation.sch");
//// ETSAssert.assertSchematronValid(schRef, new StreamSource(this.dataFile));
//// }
////
//// // A.12
//// /**
//// * Verify that any XML element in the substitution group of
//// * sam:SF_SamplingFeature is well-formed and valid
//// */
//// @Test(description = "Checks samplingFeature XSD")
//// public void checkSamplingFeature() {
//// URL schRef =
// this.getClass().getResource("/org/opengis/cite/om20/xsd/opengis/om/2.0/samplingFeature.xsd");
//// ETSAssert.assertSchematronValid(schRef, new StreamSource(this.dataFile));
//// }
////
//// // A.13
//// /**
//// * Verify that any XML element in the substitution group of
//// * sams:SF_SpatialSamplingFeature is well-formed and valid
//// */
//// @Test(description = "Checks spatialSamplingFeature XSD")
//// public void checkSpatialSamplingFeature() {
//// URL schRef = this.getClass()
//// .getResource("/org/opengis/cite/om20/xsd/opengis/om/2.0/spatialSamplingFeature.xsd");
//// ETSAssert.assertSchematronValid(schRef, new StreamSource(this.dataFile));
//// }
////
//// // A.14
//// /**
//// * Verify that XML element sams:shape contains a subelement in the
//// * substitution group of gml:Point or a link to a representation of a point.
//// */
//// @Test(description = "Checks samplingPoint Schematron rules")
//// public void checkSamplingPoint() {
//// URL schRef =
// this.getClass().getResource("/org/opengis/cite/om20/sch/samplingPoint.sch");
//// ETSAssert.assertSchematronValid(schRef, new StreamSource(this.dataFile));
//// }
////
//// // A.15
//// /**
//// * Verify that the XML element sams:shape contains a subelement in the
//// * substitution group of gml:AbstractCurve or a link to a representation of
//// * a curve.
//// */
//// @Test(description = "Checks samplingCurve Schematron rules")
//// public void checkSamplingCurve() {
//// URL schRef =
// this.getClass().getResource("/org/opengis/cite/om20/sch/samplingCurve.sch");
//// ETSAssert.assertSchematronValid(schRef, new StreamSource(this.dataFile));
//// }
////
//// // A.16
//// /**
//// * Verify that the XML element sams:shape contains a subelement in the
//// * substitution group of gml:AbstractSurface or a link to a representation
//// * of a surface.
//// */
//// @Test(description = "Checks samplingSurface Schematron rules")
//// public void checkSamplingSurface() {
//// URL schRef =
// this.getClass().getResource("/org/opengis/cite/om20/sch/samplingSurface.sch");
//// ETSAssert.assertSchematronValid(schRef, new StreamSource(this.dataFile));
//// }
////
//// // A.17
//// /**
//// * Verify that the XML element sams:shape contains a subelement in the
//// * substitution group of gml:AbstractSolid or a link to a representation of
//// * a solid.
//// */
//// @Test(description = "Checks samplingSolid Schematron rules")
//// public void checkSamplingSolid() {
//// URL schRef =
// this.getClass().getResource("/org/opengis/cite/om20/sch/samplingSolid.sch");
//// ETSAssert.assertSchematronValid(schRef, new StreamSource(this.dataFile));
//// }
////
//// // A.18
//// /**
//// * Verify that any XML element in the substitution group of spec:SF_Specimen
//// * is well-formed and valid
//// */
//// @Test(description = "Checks specimen XSD")
//// public void checkSpecimen() {
//// URL schRef =
// this.getClass().getResource("/org/opengis/cite/om20/xsd/opengis/om/2.0/specimen.xsd");
//// ETSAssert.assertSchematronValid(schRef, new StreamSource(this.dataFile));
//// }
////
//// /**
//// * [{@code Test}] Checks for the presence of any deprecated GML elements. A
//// * warning is issued for each occurrence.
//// *
//// * @see "ISO 19136, Annex I: Backwards compatibility with earlier versions of GML"
//// */
//// @Test(description = "See ISO 19136: Annex I")
//// public void checkForDeprecatedGMLElements() {
//// URL schRef =
// this.getClass().getResource("/org/opengis/cite/iso19136/sch/gml-deprecated-3.2.1.sch");
//// ETSAssert.assertSchematronValid(schRef, new StreamSource(this.dataFile));
//// }
////
//// /**
//// * [{@code Test}] Validates a GML document against a set of Schematron
//// * constraints associated with it using either the {@code xml-model}
//// * processing instruction or the {@code sch} test run argument.
//// */
//// @Test(description = "Checks application-specific Schematron rules")
//// public void checkSchematronConstraints() {
//// if (null == this.dataValidator) {
//// throw new SkipException("Schematron schema reference not found.");
//// }
//// Source gmlSource = new StreamSource(this.dataFile);
//// DOMResult result = dataValidator.validate(gmlSource);
//// Assert.assertFalse(dataValidator.ruleViolationsDetected(),
//// ErrorMessage.format(ErrorMessageKeys.NOT_SCHEMA_VALID,
// dataValidator.getRuleViolationCount(),
//// XMLUtils.writeNodeToString(result.getNode())));
//// }
//
// /**
// * Indicates whether or not the given PI data includes a Schematron schema
// * reference.
// *
// * @param piData
// * A Map containing PI data (pseudo-attributes).
// * @return {@code true} if the "schematypens" pseudo-attribute has the value
// * {@value org.opengis.cite.iso19136.Namespaces#SCH}; {@code false}
// * otherwise;
// */
// boolean isSchematronReference(Map<String, String> piData) {
// if (null != piData && null != piData.get("schematypens")) {
// return piData.get("schematypens").equals(Namespaces.SCH);
// }
// return false;
// }
//
// /**
// * Extracts the data items from the {@code xml-model} processing
// * instruction. The PI must appear before the document element.
// *
// * @param dataFile
// * A File containing the GML instance.
// * @return A Map containing the supplied pseudo-attributes, or {@code null}
// * if the PI is not present.
// */
// Map<String, String> getXmlModelPIData(File dataFile) {
// Map<String, String> piData = null;
// XMLStreamReader reader = null;
// FileInputStream input = null;
// try {
// input = new FileInputStream(dataFile);
// XMLInputFactory factory = XMLInputFactory.newInstance();
// reader = factory.createXMLStreamReader(input);
// int event = reader.getEventType();
// // Now in START_DOCUMENT state. Stop at document element.
// while (event != XMLStreamReader.START_ELEMENT) {
// event = reader.next();
// if (event == XMLStreamReader.PROCESSING_INSTRUCTION) {
// if (reader.getPITarget().equals("xml-model")) {
// String[] pseudoAttrs = reader.getPIData().split("\\s+");
// piData = new HashMap<String, String>();
// for (String pseudoAttr : pseudoAttrs) {
// String[] nv = pseudoAttr.split("=");
// piData.put(nv[0].trim(), nv[1].replace('"', ' ').trim());
// }
// break;
// }
// }
// }
// } catch (Exception e) {
// TestSuiteLogger.log(Level.WARNING, "Failed to parse document at " +
// dataFile.getAbsolutePath(), e);
// return null; // not an XML document
// } finally {
// try {
// if (null != reader)
// reader.close();
// if (null != input)
// input.close();
// } catch (Exception x) {
// TestSuiteLogger.log(Level.INFO, x.getMessage());
// }
// }
// return piData;
// }
//
// }
