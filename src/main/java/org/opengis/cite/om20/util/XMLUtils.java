package org.opengis.cite.om20.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.StartElement;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import net.sf.saxon.s9api.DOMDestination;
import net.sf.saxon.s9api.DocumentBuilder;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XPathCompiler;
import net.sf.saxon.s9api.XPathSelector;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XdmValue;
import net.sf.saxon.s9api.XsltCompiler;
import net.sf.saxon.s9api.XsltExecutable;
import net.sf.saxon.s9api.XsltTransformer;

/**
 * Provides various utility methods for accessing or manipulating XML representations.
 */
public class XMLUtils {

	/**
	 * Writes the content of a DOM Node to a String. The XML declaration is always
	 * omitted.
	 * @param node The DOM Node to be serialized.
	 * @return A String representing the content of the given node.
	 */
	public static String writeNodeToString(Node node) {
		StringWriter writer = null;
		try {
			Transformer idTransformer = TransformerFactory.newInstance().newTransformer();
			Properties outProps = new Properties();
			outProps.setProperty("encoding", "UTF-16");
			outProps.setProperty("omit-xml-declaration", "yes");
			outProps.setProperty("indent", "yes");
			idTransformer.setOutputProperties(outProps);
			writer = new StringWriter();
			idTransformer.transform(new DOMSource(node), new StreamResult(writer));
		}
		catch (TransformerException ex) {
			TestSuiteLogger.log(Level.WARNING, "Failed to serialize DOM node: " + node.getNodeName(), ex);
		}
		return writer.toString();
	}

	/**
	 * Writes the content of a DOM Node to a byte stream. An XML declaration is always
	 * omitted.
	 * @param node The DOM Node to be serialized.
	 * @param outputStream The destination OutputStream reference.
	 */
	public static void writeNode(Node node, OutputStream outputStream) {
		try {
			Transformer idTransformer = TransformerFactory.newInstance().newTransformer();
			Properties outProps = new Properties();
			outProps.setProperty(OutputKeys.METHOD, "xml");
			outProps.setProperty(OutputKeys.ENCODING, "UTF-8");
			outProps.setProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			outProps.setProperty(OutputKeys.INDENT, "yes");
			idTransformer.setOutputProperties(outProps);
			idTransformer.transform(new DOMSource(node), new StreamResult(outputStream));
		}
		catch (TransformerException ex) {
			String nodeName = (node.getNodeType() == Node.DOCUMENT_NODE)
					? Document.class.cast(node).getDocumentElement().getNodeName() : node.getNodeName();
			TestSuiteLogger.log(Level.WARNING, "Failed to serialize DOM node: " + nodeName, ex);
		}
	}

	/**
	 * Evaluates an XPath 1.0 expression using the given context and returns the result as
	 * a node set.
	 * @param context The context node.
	 * @param expr An XPath expression.
	 * @param namespaceBindings A collection of namespace bindings for the XPath
	 * expression, where each entry maps a namespace URI (key) to a prefix (value).
	 * Standard bindings do not need to be declared (see
	 * {@link org.opengis.cite.om20.util.NamespaceBindings#withStandardBindings()}.
	 * @return A NodeList containing nodes that satisfy the expression (it may be empty).
	 * @throws javax.xml.xpath.XPathExpressionException If the expression cannot be
	 * evaluated for any reason.
	 */
	public static NodeList evaluateXPath(Node context, String expr, Map<String, String> namespaceBindings)
			throws XPathExpressionException {
		return (NodeList) evaluateXPath(context, expr, namespaceBindings, XPathConstants.NODESET);
	}

	/**
	 * Evaluates an XPath 1.0 expression using the given context and returns the result as
	 * the specified type.
	 * @param context The context node.
	 * @param expr An XPath expression.
	 * @param namespaceBindings A collection of namespace bindings for the XPath
	 * expression, where each entry maps a namespace URI (key) to a prefix (value).
	 * Standard bindings do not need to be declared (see
	 * {@link org.opengis.cite.om20.util.NamespaceBindings#withStandardBindings()}.
	 * @param returnType The desired return type (as declared in
	 * {@link javax.xml.xpath.XPathConstants} ).
	 * @return The result converted to the desired returnType.
	 * @throws javax.xml.xpath.XPathExpressionException If the expression cannot be
	 * evaluated for any reason.
	 */
	public static Object evaluateXPath(Node context, String expr, Map<String, String> namespaceBindings,
			QName returnType) throws XPathExpressionException {
		NamespaceBindings bindings = NamespaceBindings.withStandardBindings();
		bindings.addAllBindings(namespaceBindings);
		XPath xpath = XPathFactory.newInstance().newXPath();
		xpath.setNamespaceContext(bindings);
		return xpath.evaluate(expr, context, returnType);
	}

	/**
	 * Evaluates an XPath 2.0 expression using the Saxon s9api interfaces.
	 * @param xmlSource The XML Source.
	 * @param expr The XPath expression to be evaluated.
	 * @param nsBindings A collection of namespace bindings required to evaluate the XPath
	 * expression, where each entry maps a namespace URI (key) to a prefix (value); this
	 * may be {@code null} if not needed.
	 * @return An XdmValue object representing a value in the XDM data model; this is a
	 * sequence of zero or more items, where each item is either an atomic value or a
	 * node.
	 * @throws net.sf.saxon.s9api.SaxonApiException If an error occurs while evaluating
	 * the expression; this always wraps some other underlying exception.
	 */
	public static XdmValue evaluateXPath2(Source xmlSource, String expr, Map<String, String> nsBindings)
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
		XdmNode node = null;
		if (DOMSource.class.isInstance(xmlSource)) {
			DOMSource domSource = (DOMSource) xmlSource;
			node = builder.wrap(domSource.getNode());
		}
		else {
			node = builder.build(xmlSource);
		}
		xpath.setContextItem(node);
		return xpath.evaluate();
	}

	/**
	 * Creates a new Element having the specified qualified name. The element must be
	 * {@link org.w3c.dom.Document#adoptNode(Node) adopted} when inserted into another
	 * Document.
	 * @param qName A QName object.
	 * @return An Element node (with a Document owner but no parent).
	 */
	public static Element createElement(QName qName) {
		Document doc = null;
		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		}
		catch (ParserConfigurationException e) {
			throw new RuntimeException(e);
		}
		Element elem = doc.createElementNS(qName.getNamespaceURI(), qName.getLocalPart());
		return elem;
	}

	/**
	 * Returns a List of all descendant Element nodes having the specified [namespace
	 * name] property. The elements are listed in document order.
	 * @param node The node to search from.
	 * @param namespaceURI An absolute URI denoting a namespace name.
	 * @return A List containing elements in the specified namespace; the list is empty if
	 * there are no elements in the namespace.
	 */
	public static List<Element> getElementsByNamespaceURI(Node node, String namespaceURI) {
		List<Element> list = new ArrayList<Element>();
		NodeList children = node.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (child.getNodeType() != Node.ELEMENT_NODE)
				continue;
			if (child.getNamespaceURI().equals(namespaceURI))
				list.add((Element) child);
		}
		return list;
	}

	/**
	 * Transforms the content of a DOM Node using a specified XSLT stylesheet.
	 * @param xslt A Source object representing a stylesheet (XSLT 1.0 or 2.0).
	 * @param source A Node representing the XML source. If it is an Element node it will
	 * be imported into a new DOM Document.
	 * @return A DOM Document containing the result of the transformation.
	 */
	public static Document transform(Source xslt, Node source) {
		Document sourceDoc = null;
		Document resultDoc = null;
		try {
			resultDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			if (source.getNodeType() == Node.DOCUMENT_NODE) {
				sourceDoc = (Document) source;
			}
			else {
				sourceDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
				sourceDoc.appendChild(sourceDoc.importNode(source, true));
			}
		}
		catch (ParserConfigurationException pce) {
			throw new RuntimeException(pce);
		}
		Processor processor = new Processor(false);
		XsltCompiler compiler = processor.newXsltCompiler();
		try {
			XsltExecutable exec = compiler.compile(xslt);
			XsltTransformer transformer = exec.load();
			transformer.setSource(new DOMSource(sourceDoc));
			transformer.setDestination(new DOMDestination(resultDoc));
			transformer.transform();
		}
		catch (SaxonApiException e) {
			throw new RuntimeException(e);
		}
		return resultDoc;
	}

	/**
	 * <p>
	 * isXMLSchema.
	 * </p>
	 * @param file a {@link java.io.File} object
	 * @return a boolean
	 */
	public static boolean isXMLSchema(File file) {
		if (!file.exists() || (file.length() == 0)) {
			return false;
		}
		QName docElemName = QName.valueOf("");
		InputStream inStream = null;
		XMLEventReader reader = null;
		try {
			inStream = new FileInputStream(file);
			XMLInputFactory factory = XMLInputFactory.newInstance();
			reader = factory.createXMLEventReader(inStream);
			StartElement docElem = reader.nextTag().asStartElement();
			docElemName = docElem.getName();
		}
		catch (Exception e1) {
			return false;
		}
		finally {
			try {
				reader.close();
				inStream.close();
			}
			catch (Exception e2) {
				TestSuiteLogger.log(Level.INFO, "Error closing resource.", e2);
			}
		}
		return docElemName.getNamespaceURI().equals(XMLConstants.W3C_XML_SCHEMA_NS_URI);
	}

	/**
	 * Writes the result of a transformation to a String. An XML declaration is always
	 * omitted.
	 * @param result An object (DOMResult or StreamResult) that holds the result of a
	 * transformation, which may be XML or plain text.
	 * @return A String representing the content of the result; it may be empty if the
	 * content could not be read.
	 */
	public static String resultToString(Result result) {
		if (null == result) {
			throw new IllegalArgumentException("Result is null.");
		}
		StringWriter writer = new StringWriter();
		if (result instanceof DOMResult) {
			Node node = DOMResult.class.cast(result).getNode();
			Properties outProps = new Properties();
			outProps.setProperty(OutputKeys.ENCODING, "UTF-8");
			outProps.setProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			outProps.setProperty(OutputKeys.INDENT, "yes");
			Transformer idTransformer;
			try {
				idTransformer = TransformerFactory.newInstance().newTransformer();
				idTransformer.setOutputProperties(outProps);
				idTransformer.transform(new DOMSource(node), new StreamResult(writer));
			}
			catch (TransformerFactoryConfigurationError | TransformerException e) {
				TestSuiteLogger.log(Level.WARNING, e.getMessage());
			}
		}
		else if (result instanceof StreamResult) {
			StreamResult streamResult = StreamResult.class.cast(result);
			OutputStream os = streamResult.getOutputStream();
			if (null != os) {
				writer.write(os.toString()); // probably ByteArrayOutputStream
			}
			else { // try system id or writer
				Path path = Paths.get(URI.create(streamResult.getSystemId()));
				try {
					byte[] data = Files.readAllBytes(path);
					writer.write(new String(data));
				}
				catch (IOException e) {
					TestSuiteLogger.log(Level.WARNING, e.getMessage());
				}
			}
		}
		else {
			throw new IllegalArgumentException("Unsupported Result type:" + result.getClass());
		}
		return writer.toString();
	}

}
