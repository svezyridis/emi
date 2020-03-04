package lawfirm.project.cases.repositories;

import lawfirm.project.auth.User;
import lawfirm.project.cases.models.Attachment;
import lawfirm.project.cases.models.Case;
import lawfirm.project.cases.models.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class JdbcCaseRepository implements CaseRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private class CaseRowMapper implements RowMapper<Case> {
        @Override
        public Case mapRow(ResultSet rs, int rowNum) throws SQLException {
            Case newCase = new Case();
            newCase.setUserID(rs.getInt("userID"));
            newCase.setNature(rs.getString("nature"));
            newCase.setAssignmentDate(rs.getDate("assignmentDate"));
            newCase.setCompletionDate(rs.getDate("completionDate"));
            newCase.setFolderNo(rs.getString("folderNo"));
            newCase.setClientID(rs.getInt("clientID"));
            newCase.setId(rs.getInt("ID"));
            return newCase;
        }
    }

    private class AttachmentRowMapper implements RowMapper<Attachment> {
        @Override
        public Attachment mapRow(ResultSet rs, int rowNum) throws SQLException {
            Attachment attachment=new Attachment();
            attachment.setFilename(rs.getString("filename"));
            attachment.setCaseID(rs.getInt("caseID"));
            attachment.setAddedOn(rs.getTimestamp("addedOn"));
            User user=new User();
            user.setFirstName(rs.getString("firstName"));
            user.setLastName(rs.getString("lastName"));
            attachment.setUser(user);
            return  attachment;
        }
    }

    private class NoteRowMapper implements RowMapper<Note> {
        @Override
        public Note mapRow(ResultSet rs, int rowNum) throws SQLException {
            Note note=new Note();
            note.setId(rs.getInt("ID"));
            note.setCaseID(rs.getInt("caseID"));
            note.setText(rs.getString("todoText"));
            note.setTime(rs.getTimestamp("time"));
            User user=new User();
            user.setFirstName(rs.getString("firstName"));
            user.setLastName(rs.getString("lastName"));
            note.setUser(user);
            return note;
        }
    }

    @Override
    public List<Case> getAllCases() {

        String sql = "SELECT * from CASES";
        try {
            return jdbcTemplate.query(sql,
                    new CaseRowMapper());
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Case> getCasesOfUser(Integer userID) {
        String sql = "SELECT * from CASES WHERE userID=?";
        try {
            return jdbcTemplate.query(sql,
                    new Object[]{userID},
                    new CaseRowMapper());
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Integer createCase(Case newCase) {
        String sql = "INSERT INTO CASES (userID, nature, folderNo, assignmentDate, completionDate, clientID) VALUES (?,?,?,?,?,?)";
        Integer result = -1;
        try {
            result = jdbcTemplate.update(sql, newCase.getUserID(), newCase.getNature(), newCase.getFolderNo(),
                    newCase.getAssignmentDate(), newCase.getCompletionDate(), newCase.getClientID());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Integer updateCase(Case newCase, Integer caseID) {
        String sql = "UPDATE CASES SET userID=ifnull(?,userID),nature=ifnull(?,nature),folderNo=ifnull(?,folderNo),assignmentDate=ifnull(?,assignmentDate), completionDate=ifnull(?,completionDate),clientID=ifnull(?,clientID) WHERE ID=?";
        Integer result = -1;
        try {
            result = jdbcTemplate.update(sql, newCase.getUserID(), newCase.getNature(), newCase.getFolderNo(),
                    newCase.getAssignmentDate(), newCase.getCompletionDate(), newCase.getClientID(),caseID);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Integer deleteCase(Integer caseID) {
        String sql = "DELETE FROM CASES WHERE ID=?";
        Integer result = -1;
        try {
            result = jdbcTemplate.update(sql,caseID);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Case getCase(Integer caseID) {
        String sql= "SELECT * FROM CASES WHERE ID=?";

        try {
            return jdbcTemplate.queryForObject(sql,
                    new Object[]{caseID},
                    new CaseRowMapper()
            );
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Attachment> getCaseAttachments(Integer caseID) {
        String sql = "SELECT ATTACHMENT.ID, filename, caseID, addedOn, userID, firstName, lastName, username, password, roleID from ATTACHMENT INNER JOIN USER ON ATTACHMENT.userID=USER.ID WHERE caseID=?";
        try {
            return jdbcTemplate.query(sql,
                    new Object[]{caseID},
                    new AttachmentRowMapper());
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Note> getCaseNotes(Integer caseID) {
        String sql = "SELECT NOTE.ID, caseID, userID, todoText, time, firstName, lastName, username, password, roleID from NOTE INNER JOIN USER ON NOTE.userID=USER.ID WHERE caseID=?";
        try {
            return jdbcTemplate.query(sql,
                    new Object[]{caseID},
                    new NoteRowMapper());
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Integer createNote(Note note) {
        String sql = "INSERT INTO NOTE (caseID, userID, todoText, time) VALUES (?,?,?,NOW())";
        Integer result = -1;
        try {
            result = jdbcTemplate.update(sql,note.getCaseID(),note.getUserID(),note.getText());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<Case> getCasesOfClient(Integer clientID) {
        String sql = "SELECT * from CASES WHERE clientID=?";
        try {
            return jdbcTemplate.query(sql,
                    new Object[]{clientID},
                    new CaseRowMapper());
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Integer addAttachment(Integer caseID,Integer userID,String filename) {
        String sql = "INSERT INTO ATTACHMENT  (filename, caseID, addedOn, userID) VALUES (?,?,now(),?)";
        Integer result = -1;
        try {
            result = jdbcTemplate.update(sql,filename,caseID,userID);
        } catch (DataAccessException e) {
            e.printStackTrace();
            if (e.getRootCause().getMessage().startsWith("Duplicate entry")) {
                return 0;
            }
        }
        return result;
    }

}
