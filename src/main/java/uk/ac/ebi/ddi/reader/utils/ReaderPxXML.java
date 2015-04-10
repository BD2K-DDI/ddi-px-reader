package uk.ac.ebi.ddi.reader.utils;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import uk.ac.ebi.ddi.reader.model.Project;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;

/**
 * Reader using SAX the XML file
 * @author ypriverol
 */
public class ReaderPxXML {

    public static Project readProject(String url){

        Project project = null;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        try {
            Document document = factory.newDocumentBuilder().parse(new URL(url).openStream());
            if(document != null){
                project = parsedDocument(document);
            }
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return project;
    }

    private static Project parsedDocument(Document document) {
        return null;
    }


}
