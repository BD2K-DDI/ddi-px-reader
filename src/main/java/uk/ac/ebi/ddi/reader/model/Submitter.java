package uk.ac.ebi.ddi.reader.model;

/**
 * @author ypriverol
 */
public class Submitter {

    //First name
    private String firstName;

    //Last Name
    private String lastName;

    //email
    private String email;

    //Affiliation
    private String affiliation;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public String getName(){
        String name = firstName;
        if(lastName != null && lastName.length() > 0){
            name = name + " " + lastName;
        }
        return name;
    }
}
