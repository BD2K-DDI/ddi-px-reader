package uk.ac.ebi.ddi.reader.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import uk.ac.ebi.ddi.reader.model.Project;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;

/**
 * Reader using SAX the XML file
 * @author ypriverol
 */
public class ReaderPxXML {

    private static final Logger logger = LoggerFactory.getLogger(ReaderPxXML.class);

    /**
     * This method read the PX summary file and return a project structure to be use by the
     * EBE exporter.
     * @param page PX XML file
     * @return Project object model
     */
    public static Project readProject(String page){

        Project project = null;

        Document document = getDomElement(page);

        if(document != null){
            project = parsedDocument(document);
        }

        return project;
    }

    /**
     * Parse a Document using dom and the current data model
     * @param document
     * @return
     */

    private static Project parsedDocument(Document document) {
        return null;
    }

    /**
     * Get a document from an String page.
     * @param xml XML as string
     * @return the Document
     */
    private static Document getDomElement(String xml){
        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {

            DocumentBuilder db = dbf.newDocumentBuilder();

            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            doc = db.parse(is);

        } catch (ParserConfigurationException e) {
            logger.error("Error: ", e.getMessage());
            return null;
        } catch (SAXException e) {
            logger.error("Error: ", e.getMessage());
            return null;
        } catch (IOException e) {
            logger.error("Error: ", e.getMessage());
            return null;
        }
        // return DOM
        return doc;
    }


}
