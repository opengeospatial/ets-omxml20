package org.opengis.cite.om20.util;

import java.util.Iterator;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;

import org.w3c.dom.Document;

/**
 * <p>
 * NamespaceResolver class.
 * </p>
 */
public class NamespaceResolver implements NamespaceContext {

	private final Document document;

	/**
	 * <p>
	 * Constructor for NamespaceResolver.
	 * </p>
	 * @param document a {@link org.w3c.dom.Document} object
	 */
	public NamespaceResolver(Document document) {
		this.document = document;
	}

	/** {@inheritDoc} */
	public String getNamespaceURI(String prefix) {
		if (prefix.equals("abc")) {
			// here is where you set your namespace
			return "http://www.w3.org/1999/xhtml";
		}
		else if (prefix.equals(XMLConstants.DEFAULT_NS_PREFIX)) {
			return document.lookupNamespaceURI(null);
		}
		else {
			return document.lookupNamespaceURI(prefix);
		}
	}

	/** {@inheritDoc} */
	public String getPrefix(String namespaceURI) {
		return document.lookupPrefix(namespaceURI);
	}

	/** {@inheritDoc} */
	@SuppressWarnings("rawtypes")
	public Iterator getPrefixes(String namespaceURI) {
		// not implemented
		return null;
	}

}
