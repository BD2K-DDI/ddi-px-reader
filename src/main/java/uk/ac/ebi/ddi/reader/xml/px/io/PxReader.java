package uk.ac.ebi.ddi.reader.xml.px.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.ddi.reader.xml.px.model.ProteomeXchangeDatasetType;


import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;


/**
 * @author ypriverol
 */
public class PxReader {

    private static final Logger logger = LoggerFactory.getLogger(PxReader.class);

    /**
     * internal unmashaller
     */
    private Unmarshaller unmarshaller = null;

    private ProteomeXchangeDatasetType dataset = null;

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
        JAXBElement<ProteomeXchangeDatasetType> datasetType = (JAXBElement<ProteomeXchangeDatasetType>) unmarshaller.unmarshal(xml);
        System.out.println(datasetType);
    }

    public ProteomeXchangeDatasetType getDataset() {
        return dataset;
    }
}
