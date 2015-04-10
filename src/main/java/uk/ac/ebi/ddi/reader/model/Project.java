package uk.ac.ebi.ddi.reader.model;

import java.util.Collection;
import java.util.Date;
import java.util.Dictionary;
import java.util.List;

/**
 * General Project information of about the Project
 * @author ypriverol
 */
public class Project {

    private String accession;

    private String repositoryName;

    private String title;

    private String projectDescription;

    private List<CvParam> species;

    private List<String>  taxonomies;

    private List<Reference> references;
    private Date submissionDate;
    private Date publicationDate;
    private String sampleProcessingProtocol;
    private String dataProcessingProtocol;
    private List<CvParam> instruments;
    private List<CvParam> cellTypes;
    private List<CvParam> diseases;
    private List<CvParam> tissues;
    private List<CvParam> ptms;
    private List<CvParam> experimentTypes;
    private List<String> projectTags;
    private String keywords;
    private List<CvParam> quantificationMethods;
    private List<CvParam> software;
    private String doi;
    private Submitter submitter;
    private List<Submitter> labHeads;
    private List<String> dataFiles;

    public boolean isPublicProject() {
        return true;
    }

    public String getAccession() {
        return accession;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public String getTitle() {
        return title;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public List<CvParam> getSpecies() {
        return species;
    }

    public List<String> getTaxonomies() {
        return taxonomies;
    }

    public List<Reference> getReferences() {
        return references;
    }

    public Date getSubmissionDate() {
        return submissionDate;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public String getSampleProcessingProtocol() {
        return sampleProcessingProtocol;
    }

    public String getDataProcessingProtocol() {
        return dataProcessingProtocol;
    }

    public List<CvParam> getInstruments() {
        return instruments;
    }

    public List<CvParam> getCellTypes() {
        return cellTypes;
    }

    public List<CvParam> getDiseases() {
        return diseases;
    }

    public List<CvParam> getTissues() {
        return tissues;
    }

    public List<CvParam> getPtms() {
        return ptms;
    }

    public List<CvParam> getExperimentTypes() {
        return experimentTypes;
    }

    public List<String> getProjectTags() {
        return projectTags;
    }

    public String getKeywords() {
        return keywords;
    }

    public List<CvParam> getQuantificationMethods() {
        return quantificationMethods;
    }

    public List<CvParam> getSoftware() {
        return software;
    }

    public String getDoi() {
        return doi;
    }

    public Submitter getSubmitter() {
        return submitter;
    }

    public List<Submitter> getLabHeads() {
        return labHeads;
    }

    public List<String> getDataFiles() {
        return dataFiles;
    }
}
