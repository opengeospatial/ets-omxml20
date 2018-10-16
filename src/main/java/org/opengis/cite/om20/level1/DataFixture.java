package org.opengis.cite.om20.level1;

import java.io.File;
import java.net.URI;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.xerces.xs.XSModel;
import org.opengis.cite.om20.SuiteAttribute;
//import org.opengis.cite.iso19136.general.GML32;
import org.opengis.cite.om20.Namespaces;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.w3c.dom.Document;

/**
 * A supporting base class that provides a common fixture for validating data
 * sets. The configuration methods are invoked before any that may be defined in
 * a subclass.
 */
public class DataFixture {

    /**
     * Files containing tested subject.
     */
    protected File dataFile;
    protected Document originalSubject;
    protected Document testSubject;
    protected URI testSubjectUri;
    
    protected String Resource_GML_Path = "/org/opengis/cite/om20/xsd/opengis/gml/3.2.1/gml-3.2.1.xsd";
    protected String Resource_SWE_Path = "/org/opengis/cite/sweCommon/2.0/swe_2.0.1_flatten/swe_2.0.1.xsd";
    /**
     * An XSModel object representing a GML application schema.
     */
    protected XSModel model;

    public DataFixture() {
    }
    
    @BeforeClass(alwaysRun = true)
    public void obtainTestSubject(ITestContext testContext){
    	Assert.assertTrue(
                testContext.getSuite().getAttributeNames()
                        .contains(SuiteAttribute.XML.getName()),
                "No data to validate.");
        this.dataFile = (File) testContext.getSuite().getAttribute(
                SuiteAttribute.XML.getName());
        this.model = (XSModel) testContext.getSuite().getAttribute(
                SuiteAttribute.XSMODEL.getName());
        
        Object obj = testContext.getSuite().getAttribute(
                SuiteAttribute.TEST_SUBJECT.getName());
        if ((null != obj) && Document.class.isAssignableFrom(obj.getClass())) {
            this.testSubject = Document.class.cast(obj);
            originalSubject = Document.class.cast(obj);
        }
        
        Object uriObj = testContext.getSuite().getAttribute(
                SuiteAttribute.TEST_SUBJECT_URI.getName());
        if ((null != uriObj)){        	
            this.testSubjectUri = URI.class.cast(uriObj);
            //System.out.println(this.testSubjectUri.toString());        	
        }
    }
    /**
     * A configuration method ({@code BeforeClass}) that initializes the test
     * fixture as follows:
     * <ol>
     * <li>Obtain the GML data set from the test context. The suite attribute
     * {@link org.opengis.cite.iso19136.SuiteAttribute#GML} should evaluate to a
     * {@code File} object containing the GML data. If no such file reference
     * exists the tests are skipped.</li>
     * <li>Obtain the schema model from the test context. The suite attribute
     * {@link org.opengis.cite.iso19136.SuiteAttribute#XSMODEL model} should
     * evaluate to an {@code XSModel} object representing the GML application
     * schema.</li>
     * </ol>
     * 
     * @param testContext
     *            The test (group) context.
     */
//    @BeforeClass(alwaysRun = true)
//    public void initDataFixture(ITestContext testContext) {
//        Assert.assertTrue(
//                testContext.getSuite().getAttributeNames()
//                        .contains(SuiteAttribute.XML.getName()),
//                "No data to validate.");
//        this.dataFile = (File) testContext.getSuite().getAttribute(
//                SuiteAttribute.XML.getName());
//        this.model = (XSModel) testContext.getSuite().getAttribute(
//                SuiteAttribute.XSMODEL.getName());
//        
//        Object obj = testContext.getSuite().getAttribute(
//                SuiteAttribute.TEST_SUBJECT.getName());
//        if ((null != obj) && Document.class.isAssignableFrom(obj.getClass())) {
//            this.testSubject = Document.class.cast(obj);
//        }
//    }

    /**
     * Sets the data file. This is a convenience method intended to facilitate
     * unit testing.
     * 
     * @param dataFile
     *            A File containing the data to be validated.
     */
    public void setDataFile(File dataFile) {
        this.dataFile = dataFile;
    }

    /**
     * Sets the schema model (for unit testing purposes).
     * 
     * @param xsModel
     *            An XSModel object representing a GML application schema.
     */
    public void setSchemaModel(XSModel xsModel) {
        this.model = xsModel;
    }

    /**
     * Generates an XPath expression to find all instances of the given elements
     * in the data being validated. The supplied namespace bindings will be
     * supplemented if necessary.
     * 
     * @param elemNames
     *            A list of qualified names corresponding to element
     *            declarations.
     * @param namespaceBindings
     *            A collection of namespace bindings required to evaluate the
     *            XPath expression, where each entry maps a namespace URI (key)
     *            to a prefix (value).
     * @return An XPath (1.0) expression.
     */
    public String generateXPathExpression(List<QName> elemNames,
            Map<String, String> namespaceBindings) {
        StringBuilder xpath = new StringBuilder();
        ListIterator<QName> itr = elemNames.listIterator();
        while (itr.hasNext()) {
            QName qName = itr.next();
            String namespace = qName.getNamespaceURI();
            String prefix = namespaceBindings.get(namespace);
            if (null == prefix) {
                prefix = (namespace.equals(Namespaces.OM)) ? "om" : "ns"
                        + itr.previousIndex();
                namespaceBindings.put(namespace, prefix);
            }
            xpath.append("//").append(prefix).append(":");
            xpath.append(qName.getLocalPart());
            if (itr.hasNext())
                xpath.append(" | "); // union operator
        }
        return xpath.toString();
    }
}