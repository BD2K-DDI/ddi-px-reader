package uk.ac.ebi.ddi.reader.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import uk.ac.ebi.ddi.reader.model.CvParam;
import uk.ac.ebi.ddi.reader.model.Project;
import uk.ac.ebi.ddi.reader.model.Submitter;
import uk.ac.ebi.ddi.reader.xml.px.io.PxReader;
import uk.ac.ebi.ddi.reader.xml.px.model.*;
import uk.ac.ebi.pride.utilities.term.CvTermReference;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


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

        if(document != null)
            project = parseDocument(page);

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

    /**
     * Parse the XML JAXB file into a Prject data model. It allows to map the information in the common
     * data model for exporting.
     * @param page the JAXB XML object
     * @return  Project the project
     * @throws IOException
     * @throws JAXBException
     */
    public static Project parseDocument(String page) throws IOException, JAXBException {
        Project proj = new Project();

        InputStream in = org.apache.commons.io.IOUtils.toInputStream(page, "UTF-8");
        PxReader reader = new PxReader(in);

        //Set public


        //Set accession
        proj.setAccession(reader.getAccession());

        //Set repository Name
        proj.setRepositoryName(reader.getRepositoryName());

        //Set title of the dataset
        proj.setTitle(reader.getTitle());

        //Set Project Description
        proj.setProjectDescription(reader.getDescription());

        //Set Instrument
        proj.setInstruments(transformInstruments(reader.getInstruments()));

        //Set Modifications
        proj.setPtms(transformCVParamTypeList(reader.getPtms()));

        //Set Species
        proj.setSpecies(transformSpecies(reader.getSpecies()));

        //Set Taxonomies
        proj.setTaxonomies(transformTaxonomies(reader.getSpecies()));

        //Set Submitter
        proj.setSubmitter(selectSubmitterFromContacts(reader.getContactList()));

        //Set Lab heads
        proj.setLabHeads(selectLabHeadsFromContacts(reader.getContactList()));

        //Set Publication date
        proj.setPublicationDate(transformDate(reader.getAnnounceDate()));

        //Set Data Files
        proj.setDataFiles(transformDataFiles(reader.getDataFiles()));

        //Set Submitter keywords
        proj.setKeywords(transformSubmitterKeywords(reader.getSubmitterKeywords()));

        //Set Curator Keywords
        proj.setProjectTags(transformCuratorKeywords(reader.getSubmitterKeywords()));
        proj.addCuratorKey(reader.getReviewLevel());

        //Set DatasetLink
        proj.setDatasetLink(transformGetDatasetLink(reader.getFullDatasetLink()));

        proj.setExperimentTypes(transformExperimentTypes(reader.getSubmitterKeywords()));

        return proj;

    }

    private static List<CvParam> transformExperimentTypes(List<CvParamType> submitterKeywords) {
        List<CvParam> experimentTypes = new ArrayList<CvParam>();
        for(CvParamType cvParamType: submitterKeywords){
            if(cvParamType.getValue().contains(Constants.SRM_KEYWORD) || cvParamType.getName().contains("SRM")){
                CvParam cvParam = new CvParam("PRIDE:0000311","SRM/MRM", "PRIDE", "SRM/MRM");
                experimentTypes.add(cvParam);
            }
        }
        return experimentTypes;
    }

    /**
     * Retrieve information about the experiment URL
     * @param fullDatasetLink experiment List URL
     * @return experiment URL
     */
    private static String transformGetDatasetLink(List<FullDatasetLinkType> fullDatasetLink) {
        if(fullDatasetLink != null && fullDatasetLink.size() >0){
            for(FullDatasetLinkType datasetLink: fullDatasetLink)
                if(datasetLink.getCvParam().getAccession().equalsIgnoreCase(Constants.MASSIVEURL_ACCESSION) ||
                   datasetLink.getCvParam().getAccession().equalsIgnoreCase(Constants.PASSELURL_ACCESSION))
                    return datasetLink.getCvParam().getValue();
        }
        return null;
    }

    /**
     * Retrieve the curator keywords
     * @param submitterKeywords
     * @return List<String> List of keywords
     */
    private static List<String> transformCuratorKeywords(List<CvParamType> submitterKeywords) {
        List<String> keywords = new ArrayList<String>();
        for(CvParamType cv: submitterKeywords)
            if(cv.getAccession().equalsIgnoreCase(Constants.CURATORKEY_ACCESSION))
                keywords.add(cv.getValue());
        return keywords;
    }

    /**
     * Read Submitter keywords
     * @param submitterKeywords List<CvParamType>
     * @return List of keys
     */
    private static List<String> transformSubmitterKeywords(List<CvParamType> submitterKeywords) {
        List<String> keywords = new ArrayList<String>();
        for(CvParamType cv: submitterKeywords){
            if(cv.getAccession().equalsIgnoreCase(Constants.SUBMITTERKEY_ACCESSION))
                keywords.add(cv.getValue());
        }
        return keywords;
    }

    /**
     * Return the list of File Name related with the Dataset
     * @param dataFiles List<DatasetFileType>
     * @return          List<String>
     */
    private static List<String> transformDataFiles(List<DatasetFileType> dataFiles) {
        List<String> files = new ArrayList<String>();
        for(DatasetFileType file: dataFiles){
           if(file.getCvParam() != null && file.getCvParam().size() > 0){
               for(CvParamType cv: file.getCvParam()){
                   files.add(cv.getValue());
               }
           }
        }
        return files;
    }

    /**
     * Change the GregorianCalendar Date to Date
     * @param announceDate
     * @return
     */
    private static Date transformDate(XMLGregorianCalendar announceDate) {
        return announceDate.toGregorianCalendar().getTime();
    }

    /**
     * Select Lab Heads from the List of Contacts
     * @param contactList
     * @return List<Submitter>
     */
    private static List<Submitter> selectLabHeadsFromContacts(List<ContactType> contactList) {
        List<Submitter> labHeads = new ArrayList<Submitter>();
        for(ContactType contact: contactList){
            if(contact.getCvParam() != null && contact.getCvParam().size() > 0){
                for(CvParamType cv: contact.getCvParam()){
                    if(cv.getAccession().equalsIgnoreCase(Constants.LABHEAD_ACCESSION)){
                        labHeads.add(transformSubmitter(contact));
                    }
                }
            }
        }
        return labHeads;
    }

    /**
     * Select from a List of Contacts the Submitter
     * @param contactList  Contact List
     * @return Submitter
     */
    private static Submitter selectSubmitterFromContacts(List<ContactType> contactList) {
        Submitter submitter = new Submitter();
        for(ContactType contact: contactList){
            if(contact.getCvParam() != null && contact.getCvParam().size() > 0){
                for(CvParamType cv: contact.getCvParam()){
                    if(cv.getAccession().equalsIgnoreCase(Constants.SUBMITTER_ACCESSION)){
                        return transformSubmitter(contact);
                    }
                }
            }
        }
        return submitter;
    }

    /**
     * Transform Submitter from contact to Submitter
     * @param contact ContactType
     * @return  Submitter
     */
    private static Submitter transformSubmitter(ContactType contact) {
        Submitter submitter = new Submitter();
        for(CvParamType cv: contact.getCvParam()){
            if(cv.getAccession().equalsIgnoreCase(CvTermReference.CONTACT_NAME.getAccession()))
                submitter.setFirstName(cv.getValue());

            if(cv.getAccession().equalsIgnoreCase(CvTermReference.CONTACT_EMAIL.getAccession()))
                submitter.setEmail(cv.getValue());

            if(cv.getAccession().equalsIgnoreCase(CvTermReference.CONTACT_ORG.getAccession()))
                submitter.setAffiliation(cv.getValue());
       }
        return submitter;
    }

    /**
     * Return all the taxonomies related with the File using the NCBI Taxonomy
     * @param species all the species related with the file in CVTems
     * @return The list of the accession in NCBI Taxonomy
     */
    private static List<String> transformTaxonomies(List<SpeciesType> species) {
        List<String> taxonomies = new ArrayList<String>();
        for(SpeciesType specie: species)
            for(CvParamType cv: specie.getCvParam())
                if(cv.getAccession().equalsIgnoreCase(Constants.TAXONOMY_ACCESSION))
                    taxonomies.add(cv.getValue());
        return taxonomies;
    }

    /**
     * TRansform all species to CVParams
     * @param species List of SpeciesType in PX XM file
     * @return A List of cv
     */
    private static List<CvParam> transformSpecies(List<SpeciesType> species) {
        List<CvParam> cvParams = new ArrayList<>();
        if(species != null && species.size() > 0){
            for(SpeciesType specie: species){
                //Remove the species that are wrote in Taxonomy way.
                List<CvParamType> finalCvs = removeCVParamTypeByAccession(specie.getCvParam(), Constants.TAXONOMY_ACCESSION);
                cvParams.addAll(transformCVParamTypeList(finalCvs));
            }
        }
        return cvParams;
    }

    /**
     * THis function remove all the terms that contains an specific accession Term.
     * @param cvParam List of all CVParamsType
     * @param taxonomyAccession the accession ID to be removed
     * @return The final List of terms
     */
    private static List<CvParamType> removeCVParamTypeByAccession(List<CvParamType> cvParam, String taxonomyAccession) {
        List<CvParamType> cvList = new ArrayList<CvParamType>();
        for(CvParamType cv: cvParam)
            if(!cv.getAccession().equalsIgnoreCase(taxonomyAccession))
              cvList.add(cv);
        return cvList;
    }

    /**
     * Transform a List of instruments to a List of CvParmas
     * @param instruments List of instruments from PX submission
     * @return List of CvParams
     */
    private static List<CvParam> transformInstruments(List<InstrumentType> instruments) {
        List<CvParam> cvParams = new ArrayList<>();
        if(instruments != null && instruments.size() >0){
            for(InstrumentType instrument: instruments){
               cvParams.addAll(transformCVParamTypeList(instrument.getCvParam()));
            }
        }
        return cvParams;
    }

    /**
     * Convert List of CVParamsType to CVparams in the model
     * @param params List of CVParams Type
     * @return List of CvParams
     */
    private static List<CvParam> transformCVParamTypeList(List<CvParamType> params){
        List<CvParam> cvParams = new ArrayList<>();
        for(CvParamType cv: params){
            CvParam param = new CvParam(cv.getAccession(), cv.getName(), cv.getUnitName(),cv.getValue());
            cvParams.add(param);
        }
        return cvParams;
    }


}
