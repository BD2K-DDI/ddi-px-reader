package uk.ac.ebi.ddi.reader.model;

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
}
