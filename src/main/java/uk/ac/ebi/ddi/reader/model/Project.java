package uk.ac.ebi.ddi.reader.model;

import java.util.Collections;
import java.util.Date;
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

    private List<String> keywords;

    private List<CvParam> quantificationMethods;

    private List<CvParam> software;

    private String doi;

    private Submitter submitter;

    private List<Submitter> labHeads;

    private List<String> dataFiles;
    private String datasetLink;

    /**
     * Default constructor create a List of every list-based attribute
     */
    public Project() {
        dataFiles             = Collections.emptyList();
        labHeads              = Collections.emptyList();
        software              = Collections.emptyList();
        quantificationMethods = Collections.emptyList();;
        projectTags           = Collections.emptyList();
        experimentTypes       = Collections.emptyList();
        ptms                  = Collections.emptyList();
        tissues               = Collections.emptyList();
        diseases              = Collections.emptyList();
        cellTypes             = Collections.emptyList();
        instruments           = Collections.emptyList();
        references            = Collections.emptyList();
        taxonomies            = Collections.emptyList();
        species               = Collections.emptyList();

    }

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

    public List<String> getKeywords() {
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

    public void setAccession(String accession) {
        this.accession = accession;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public void setSpecies(List<CvParam> species) {
        this.species = species;
    }

    public void setTaxonomies(List<String> taxonomies) {
        this.taxonomies = taxonomies;
    }

    public void setReferences(List<Reference> references) {
        this.references = references;
    }

    public void setSubmissionDate(Date submissionDate) {
        this.submissionDate = submissionDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public void setSampleProcessingProtocol(String sampleProcessingProtocol) {
        this.sampleProcessingProtocol = sampleProcessingProtocol;
    }

    public void setDataProcessingProtocol(String dataProcessingProtocol) {
        this.dataProcessingProtocol = dataProcessingProtocol;
    }

    public void setInstruments(List<CvParam> instruments) {
        this.instruments = instruments;
    }

    public void setCellTypes(List<CvParam> cellTypes) {
        this.cellTypes = cellTypes;
    }

    public void setDiseases(List<CvParam> diseases) {
        this.diseases = diseases;
    }

    public void setTissues(List<CvParam> tissues) {
        this.tissues = tissues;
    }

    public void setPtms(List<CvParam> ptms) {
        this.ptms = ptms;
    }

    public void setExperimentTypes(List<CvParam> experimentTypes) {
        this.experimentTypes = experimentTypes;
    }

    public void setProjectTags(List<String> projectTags) {
        this.projectTags = projectTags;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public void setQuantificationMethods(List<CvParam> quantificationMethods) {
        this.quantificationMethods = quantificationMethods;
    }

    public void setSoftware(List<CvParam> software) {
        this.software = software;
    }

    public void setDoi(String doi) {
        this.doi = doi;
    }

    public void setSubmitter(Submitter submitter) {
        this.submitter = submitter;
    }

    public void setLabHeads(List<Submitter> labHeads) {
        this.labHeads = labHeads;
    }

    public void setDataFiles(List<String> dataFiles) {
        this.dataFiles = dataFiles;
    }

    public void addCuratorKey(String reviewLevel) {
        if(reviewLevel != null)
            projectTags.add(reviewLevel);
    }

    public String getDatasetLink() {
        return datasetLink;
    }

    public void setDatasetLink(String datasetLink) {
        this.datasetLink = datasetLink;
    }
}
