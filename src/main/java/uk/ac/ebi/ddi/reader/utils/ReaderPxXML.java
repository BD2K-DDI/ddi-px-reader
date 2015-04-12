package uk.ac.ebi.ddi.reader.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import uk.ac.ebi.ddi.reader.model.Project;
import uk.ac.ebi.ddi.reader.xml.px.io.PxReader;
import uk.ac.ebi.ddi.reader.xml.px.model.ProteomeXchangeDatasetType;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import java.io.InputStream;
import java.io.StringReader;


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
    public static Project readProject(String page) throws Exception {

        Project project = null;

        Document document = getDomElement(page);

        if(document != null){
            project = parsedDocument(document);
            project = parseDocument(page);
        }



        return project;
    }

    /**
     * Parse a Document using dom and the current data model and fill the information of
     * the project, including the medatadata.
     * @param document XML Document
     * @return Project
     */

    private static Project parsedDocument(Document document) throws Exception {

        Project proj = new Project();

        document.getDocumentElement().normalize();

        NodeList nList = document.getElementsByTagName(Constants.DATASET_SUMMARY_TAG);

        proj = parseDatasetSummary(nList, proj);



//        for (int iNode = 0; iNode < nList.getLength(); iNode++) {
//
//            Node nNode = nList.item(iNode);
//
//            if (nNode.getNodeName().equalsIgnoreCase(Constants.DATASET_SUMMARY_TAG)) {
//
//                Element eElement = (Element) nNode;
//
//                System.out.println("Staff id : " + eElement.getAttribute("id"));
//                System.out.println("First Name : " + eElement.getElementsByTagName("firstname").item(0).getTextContent());
//                System.out.println("Last Name : " + eElement.getElementsByTagName("lastname").item(0).getTextContent());
//                System.out.println("Nick Name : " + eElement.getElementsByTagName("nickname").item(0).getTextContent());
//                System.out.println("Salary : " + eElement.getElementsByTagName("salary").item(0).getTextContent());
//
//            }
//        }
        return proj;
    }

    /**
     * Parse dataset summary for the project from the PX XML structure
     * @param nList SAX object
     * @param proj Project
     * @return   Updated project
     */
    private static Project parseDatasetSummary(NodeList nList, Project proj) throws Exception {

        if(nList.getLength() != 1)
            throw new Exception("Problem in the PX submission schema");

        Element node = (Element) nList.item(0);

        // Set Title of the dataset
        String title = node.getAttribute(Constants.PXTITLE_TAG);
        proj.setTitle(title);

        // Set Repository
        String repository = node.getAttribute(Constants.PXREPO_TAG);
        proj.setRepositoryName(repository);

        // Set Repository
        String announceDate = node.getAttribute(Constants.PXANOUNDATE_TAG);
        //Todo
        // proj.setSubmissionDate(new Date(announceDate));

        //Set project description
        if(nList.item(0).hasChildNodes()){
            NodeList nListDesc = ((Element) nList.item(0)).getElementsByTagName(Constants.PXDESC_TAG);
            if(nListDesc.getLength() == 1){
                Element elementDesc = (Element) nListDesc.item(0);
                String description  = elementDesc.getTextContent();
                proj.setProjectDescription(description);
            }

            NodeList nListReview = ((Element) nList.item(0)).getElementsByTagName(Constants.PXREVIEW_TAG);
            if(nListReview.getLength() == 1){

            }
        }



        return proj;
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

    public static Project parseDocument(String page) throws IOException, JAXBException {
        Project proj = new Project();
        InputStream in = org.apache.commons.io.IOUtils.toInputStream(page, "UTF-8");
        PxReader reader = new PxReader(in);
        ProteomeXchangeDatasetType dataset = reader.getDataset();
        return proj;
    }


}
