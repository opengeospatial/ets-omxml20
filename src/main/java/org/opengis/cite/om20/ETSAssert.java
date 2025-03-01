package org.opengis.cite.om20;

import java.net.URL;
import java.util.Map;
import java.util.logging.Level;

import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Validator;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.opengis.cite.om20.util.NamespaceBindings;
import org.opengis.cite.om20.util.TestSuiteLogger;
import org.opengis.cite.om20.util.XMLUtils;
import org.opengis.cite.validation.SchematronValidator;
import org.opengis.cite.validation.ValidationErrorHandler;
import org.testng.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Provides a set of custom assertion methods.
 */
public class ETSAssert {

	private ETSAssert() {
	}

	/**
	 * Asserts that the qualified name of a DOM Node matches the expected value.
	 * @param node The Node to check.
	 * @param qName A QName object containing a namespace name (URI) and a local part.
	 */
	public static void assertQualifiedName(Node node, QName qName) {
		Assert.assertEquals(node.getLocalName(), qName.getLocalPart(), ErrorMessage.get(ErrorMessageKeys.LOCAL_NAME));
		Assert.assertEquals(node.getNamespaceURI(), qName.getNamespaceURI(),
				ErrorMessage.get(ErrorMessageKeys.NAMESPACE_NAME));
	}

	/**
	 * Asserts that an XPath 1.0 expression holds true for the given evaluation context.
	 * The following standard namespace bindings do not need to be explicitly declared:
	 *
	 * <ul>
	 * <li>ows: {@value org.opengis.cite.om20.Namespaces#OWS}</li>
	 * <li>xlink: {@value org.opengis.cite.om20.Namespaces#XLINK}</li>
	 * <li>gml: {@value org.opengis.cite.om20.Namespaces#GML}</li>
	 * </ul>
	 * @param expr A valid XPath 1.0 expression.
	 * @param context The context node.
	 * @param namespaceBindings A collection of namespace bindings for the XPath
	 * expression, where each entry maps a namespace URI (key) to a prefix (value). It may
	 * be {@code null}.
	 */
	public static void assertXPath(String expr, Node context, Map<String, String> namespaceBindings) {
		if (null == context) {
			throw new NullPointerException("Context node is null.");
		}
		NamespaceBindings bindings = NamespaceBindings.withStandardBindings();
		bindings.addAllBindings(namespaceBindings);
		XPath xpath = XPathFactory.newInstance().newXPath();
		xpath.setNamespaceContext(bindings);
		Boolean result;
		try {
			result = (Boolean) xpath.evaluate(expr, context, XPathConstants.BOOLEAN);
		}
		catch (XPathExpressionException xpe) {
			String msg = ErrorMessage.format(ErrorMessageKeys.XPATH_ERROR, expr);
			TestSuiteLogger.log(Level.WARNING, msg, xpe);
			throw new AssertionError(msg);
		}
		Assert.assertTrue(result, ErrorMessage.format(ErrorMessageKeys.XPATH_RESULT, context.getNodeName(), expr));
	}

	/**
	 * Asserts that an XML resource is schema-valid.
	 * @param validator The Validator to use.
	 * @param source The XML Source to be validated.
	 */
	public static void assertSchemaValid(Validator validator, Source source) {
		ValidationErrorHandler errHandler = new ValidationErrorHandler();
		validator.setErrorHandler(errHandler);
		try {
			validator.validate(source);
		}
		catch (Exception e) {
			throw new AssertionError(ErrorMessage.format(ErrorMessageKeys.XML_ERROR, e.getMessage()));
		}
		Assert.assertFalse(errHandler.errorsDetected(), ErrorMessage.format(ErrorMessageKeys.NOT_SCHEMA_VALID,
				errHandler.getErrorCount(), errHandler.toString()));
	}

	/**
	 * Asserts that an XML resource satisfies all applicable constraints specified in a
	 * Schematron (ISO 19757-3) schema. The "xslt2" query language binding is supported.
	 * All patterns are checked.
	 * @param schemaRef A URL that denotes the location of a Schematron schema.
	 * @param xmlSource The XML Source to be validated.
	 */
	public static void assertSchematronValid(URL schemaRef, Source xmlSource) {
		SchematronValidator validator;
		try {
			validator = new SchematronValidator(new StreamSource(schemaRef.toString()), "#ALL");
		}
		catch (Exception e) {
			StringBuilder msg = new StringBuilder("Failed to process Schematron schema at ");
			msg.append(schemaRef).append('\n');
			msg.append(e.getMessage());
			throw new AssertionError(msg);
		}
		DOMResult result = (DOMResult) validator.validate(xmlSource);
		Assert.assertFalse(validator.ruleViolationsDetected(), ErrorMessage.format(ErrorMessageKeys.NOT_SCHEMA_VALID,
				validator.getRuleViolationCount(), XMLUtils.writeNodeToString(result.getNode())));
	}

	// public static void assertSchematron(File schFile, File xmlFile) {
	// try {
	// boolean isValid = ValidationUtils.validateXMLViaPureSchematron(schFile, xmlFile);
	// System.out.println("Schematron is valid? " + isValid);
	//// Assert.assertTrue(ValidationUtils.validateXMLViaPureSchematron(schFile, xmlFile),
	// ErrorMessage
	//// .format(ErrorMessageKeys.NOT_SCHEMA_VALID,
	//// 0, ""));
	// Assert.assertTrue(isValid, "HELLO");
	// } catch (IOException e1) {
	// // TODO Auto-generated catch block
	// e1.printStackTrace();
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// System.out.println("error in assertSchematron:" + e.toString());
	// e.printStackTrace();
	// }
	//
	// }

	/**
	 * Asserts that the given XML entity contains the expected number of descendant
	 * elements having the specified name.
	 * @param xmlEntity A Document representing an XML entity.
	 * @param elementName The qualified name of the element.
	 * @param expectedCount The expected number of occurrences.
	 */
	public static void assertDescendantElementCount(Document xmlEntity, QName elementName, int expectedCount) {
		NodeList features = xmlEntity.getElementsByTagNameNS(elementName.getNamespaceURI(), elementName.getLocalPart());
		Assert.assertEquals(features.getLength(), expectedCount,
				String.format("Unexpected number of %s descendant elements.", elementName));
	}

}
