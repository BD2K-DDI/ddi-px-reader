package uk.ac.ebi.ddi.px.xml.px.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.ddi.px.xml.px.model.*;


import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;


/**
 * @author ypriverol
 */
public class PxReader {

    private static final Logger logger = LoggerFactory.getLogger(PxReader.class);

    /**
     * internal unmashaller
     */
    private Unmarshaller unmarshaller = null;

    private JAXBElement<ProteomeXchangeDatasetType> dataset = null;

    private List<CvParamType> species;
    private String reviewLevel;


    /**
     *
     * @param xml
     * @throws javax.xml.bind.JAXBException
     */
    public PxReader(InputStream xml) throws JAXBException {
        if (xml == null) {
            throw new IllegalArgumentException("Xml file to be indexed must not be null");
        }
        // create unmarshaller
        this.unmarshaller = PXUnmarshallerFactory.getInstance().initializeUnmarshaller();

        dataset = (JAXBElement<ProteomeXchangeDatasetType>) unmarshaller.unmarshal(xml);

    }

    /**
     * Retrieve the accession of the dataset
     * @return Accession
     */
    public String getAccession(){
        return dataset.getValue().getId();
    }

    /**
     * Return the repository Name
     * @return name of repo
     */
    public String getRepositoryName(){
        if(dataset.getValue() != null &&
                dataset.getValue().getDatasetSummary() != null &&
                dataset.getValue().getDatasetSummary().getHostingRepository() != null &&
                dataset.getValue().getDatasetSummary().getHostingRepository().value() != null)
            return dataset.getValue().getDatasetSummary().getHostingRepository().value();
        return null;
    }

    /**
     * Get dataset Description
     * @return description
     */
    public String getTitle() {
        return dataset.getValue().getDatasetSummary().getTitle();
    }

    /**
     * Return the dataset description
     * @return description
     */
    public String getDescription() {
        return dataset.getValue().getDatasetSummary().getDescription();
    }

    /**
     * Return the List of instruments related with the dataset
     * @return List<InstrumentType>
     */
    public List<InstrumentType> getInstruments() {
        return dataset.getValue().getInstrumentList().getInstrument();
    }

    /**
     * Get PTMs from PX Submission
     * @return List of PTMs
     */
    public List<CvParamType> getPtms() {
        return dataset.getValue().getModificationList().getCvParam();
    }

    /**
     * Get the List of Species including taxonomies and name of species
     * @return
     */
    public List<SpeciesType> getSpecies() {
        return dataset.getValue().getSpeciesList().getSpecies();
    }

    /**
     * Get The list of Contacts including submitter and Lab heads
     * @return List<ContactType>
     */
    public List<ContactType> getContactList() {
        return dataset.getValue().getContactList().getContact();
    }

    public XMLGregorianCalendar getAnnounceDate() {
        return dataset.getValue().getDatasetSummary().getAnnounceDate();
    }

    /**
     * Return the List of Data Files related with the Datatset
     * @return List<DatasetFileType>
     */
    public  List<DatasetFileType> getDataFiles() {
        if(dataset.getValue().getDatasetFileList() != null)
          return dataset.getValue().getDatasetFileList().getDatasetFile();
        return Collections.emptyList();
    }

    /**
     * Get PX keywords
     * @return List<CvParamType>
     */
    public List<CvParamType> getSubmitterKeywords() {
        if(dataset.getValue().getKeywordList() != null && dataset.getValue().getKeywordList().getCvParam() != null && dataset.getValue().getKeywordList().getCvParam().size() > 0){
            return dataset.getValue().getKeywordList().getCvParam();
        }
        return Collections.emptyList();
    }

    /**
     * Retrieve the Review Level as a Curator keyword
     * @return the keyword
     */
    public String getReviewLevel() {
        if(dataset.getValue().getDatasetSummary() != null){
            if(dataset.getValue().getDatasetSummary().getReviewLevel() != null){
                return dataset.getValue().getDatasetSummary().getReviewLevel().getCvParam().getValue();
            }
        }
        return null;
    }

    /**
     * FullDataset Link List
     * @return
     */
    public List<FullDatasetLinkType> getFullDatasetLink() {
        if(dataset.getValue().getFullDatasetLinkList() != null && dataset.getValue().getFullDatasetLinkList().getFullDatasetLink() != null && dataset.getValue().getFullDatasetLinkList().getFullDatasetLink().size() > 0)
         return dataset.getValue().getFullDatasetLinkList().getFullDatasetLink();
        return Collections.emptyList();
    }

    /**
     * List of Publications from PX
     * @return List of publications
     */
    public List<PublicationType> getReferences() {
        if(dataset.getValue().getPublicationList() != null &&
                dataset.getValue().getPublicationList().getPublication() != null &&
                dataset.getValue().getPublicationList().getPublication().size() > 0){
            return dataset.getValue().getPublicationList().getPublication();
        }
        return Collections.emptyList();
    }
}
