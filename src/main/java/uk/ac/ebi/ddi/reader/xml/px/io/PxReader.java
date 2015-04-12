package uk.ac.ebi.ddi.reader.xml.px.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.ddi.reader.xml.px.model.CvParamType;
import uk.ac.ebi.ddi.reader.xml.px.model.InstrumentType;
import uk.ac.ebi.ddi.reader.xml.px.model.ProteomeXchangeDatasetType;
import uk.ac.ebi.ddi.reader.xml.px.model.SpeciesType;


import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
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
        return dataset.getValue().getDatasetSummary().getHostingRepository().value();
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


    public List<CvParamType> getPtms() {
        return dataset.getValue().getModificationList().getCvParam();
    }

    public List<SpeciesType> getSpecies() {
        return dataset.getValue().getSpeciesList().getSpecies();
    }
}
