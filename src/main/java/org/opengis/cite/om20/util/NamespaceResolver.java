package org.opengis.cite.om20.util;

import java.util.Iterator;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import org.w3c.dom.Document;

public class NamespaceResolver implements NamespaceContext {

    private final Document document;

    public NamespaceResolver(Document document) {
        this.document = document;
    }

    public String getNamespaceURI(String prefix) {
        if(prefix.equals("abc")) {
            // here is where you set your namespace
            return "http://www.w3.org/1999/xhtml";
        } else if (prefix.equals(XMLConstants.DEFAULT_NS_PREFIX)) {
            return document.lookupNamespaceURI(null);
        } else {
            return document.lookupNamespaceURI(prefix);
        }
    }

    public String getPrefix(String namespaceURI) {
        return document.lookupPrefix(namespaceURI);
    }

    @SuppressWarnings("rawtypes")
    public Iterator getPrefixes(String namespaceURI) {
        // not implemented
        return null;
    }

}