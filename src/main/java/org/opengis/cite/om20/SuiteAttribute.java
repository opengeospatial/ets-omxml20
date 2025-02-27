package org.opengis.cite.om20;

import java.io.File;
import java.net.URI;
import java.util.Set;

import org.apache.xerces.xs.XSModel;
import org.w3c.dom.Document;

/**
 * An enumerated type defining ISuite attributes that may be set to constitute a shared
 * test fixture.
 */
@SuppressWarnings("rawtypes")
public enum SuiteAttribute {

	SCHEMA_LOC_SET("schema-loc-set", Set.class), XML("xml-data", File.class), SCHEMATRON("schematron", URI.class),
	/**
	 * Contains the XML Schema components comprising an application schema.
	 */
	XSMODEL("xsmodel", XSModel.class),
	/**
	 * A DOM Document representation of the test subject or metadata about it.
	 */
	TEST_SUBJECT("testSubject", Document.class),
	/**
	 * An absolute URI referring to a DOM Document schema.
	 */
	TEST_SUBJECT_URI("testSubjectUri", URI.class);

	private final Class attrType;

	private final String attrName;

	private SuiteAttribute(String attrName, Class attrType) {
		this.attrName = attrName;
		this.attrType = attrType;
	}

	/**
	 * <p>
	 * getType.
	 * </p>
	 * @return a {@link java.lang.Class} object
	 */
	public Class getType() {
		return attrType;
	}

	/**
	 * <p>
	 * getName.
	 * </p>
	 * @return a {@link java.lang.String} object
	 */
	public String getName() {
		return attrName;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(attrName);
		sb.append('(').append(attrType.getName()).append(')');
		return sb.toString();
	}

}
