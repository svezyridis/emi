package lawfirm.project.cases;

import java.util.List;

public class Client {
    Integer ID;
    String name;
    String email;
    String lastName;
    String phoneNumber;
    Integer noOfCases;

    public Integer getNoOfCases() {
        return noOfCases;
    }

    public void setNoOfCases(Integer noOfCases) {
        this.noOfCases = noOfCases;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}


