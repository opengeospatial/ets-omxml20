package org.opengis.cite.om20.level1;

import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.testng.ISuite;
import org.testng.ITestContext;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Verifies the behavior of the ObservationTests test class. Test stubs replace
 * fixture constituents where appropriate.
 */
public class VerifyObservationTests {

    private static final String SUBJ = "testSubject";
    private static DocumentBuilder docBuilder;
    private static ITestContext testContext;
    private static ISuite suite;

    public VerifyObservationTests() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        testContext = mock(ITestContext.class);
        suite = mock(ISuite.class);
        when(testContext.getSuite()).thenReturn(suite);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        docBuilder = dbf.newDocumentBuilder();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
/*
    @Test(expected = AssertionError.class)
    public void testIsEmpty() {
        ObservationTests iut = new ObservationTests();
        iut.isEmpty();
    }
*/
    @Test
    public void testTrim() {
        ObservationTests iut = new ObservationTests();
        iut.Observation();
    }
/*
    @Test(expected = NullPointerException.class)
    public void supplyNullTestSubject() throws SAXException, IOException {
        ObservationTests iut = new ObservationTests();
        iut.docIsValidAtomFeed();
    }
*//*
    @Test
    public void supplyValidAtomFeedViaTestContext() throws SAXException,
            IOException {
        Document doc = docBuilder.parse(this.getClass().getResourceAsStream(
                "/atom-feed.xml"));
        when(suite.getAttribute(SUBJ)).thenReturn(doc);
        ObservationTests iut = new ObservationTests();
        iut.obtainTestSubject(testContext);
        iut.docIsValidAtomFeed();
    }*/
}
