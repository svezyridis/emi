package lawfirm.project.cases.models;

import lawfirm.project.auth.User;

import java.sql.Timestamp;

public class Note {
    String text;
    Integer id;
    Integer caseID;
    Timestamp time;
    Integer userID;
    User user;

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCaseID() {
        return caseID;
    }

    public void setCaseID(Integer caseID) {
        this.caseID = caseID;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Note{" +
                "text='" + text + '\'' +
                ", id=" + id +
                ", caseID=" + caseID +
                ", time=" + time +
                ", userID=" + userID +
                ", user=" + user +
                '}';
    }
}
