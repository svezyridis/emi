package lawfirm.project.cases.models;

import java.sql.Date;
import java.util.List;

public class Case {
    Integer id;
    Integer userID;
    String nature;
    String folderNo;
    Date assignmentDate;
    Date completionDate;
    Integer clientID;
    Client client;
    List<Attachment> attachments;
    List<Note> notes;

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public String getFolderNo() {
        return folderNo;
    }

    public void setFolderNo(String folderNo) {
        this.folderNo = folderNo;
    }

    public Date getAssignmentDate() {
        return assignmentDate;
    }

    public void setAssignmentDate(Date assignmentDate) {
        this.assignmentDate = assignmentDate;
    }

    public Date getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
    }

    public Integer getClientID() {
        return clientID;
    }

    public void setClientID(Integer clientID) {
        this.clientID = clientID;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public String toString() {
        return "Case{" +
                "id=" + id +
                ", userID=" + userID +
                ", nature='" + nature + '\'' +
                ", folderNo='" + folderNo + '\'' +
                ", assignmentDate=" + assignmentDate +
                ", completionDate=" + completionDate +
                ", clientID=" + clientID +
                ", client=" + client +
                '}';
    }
}
