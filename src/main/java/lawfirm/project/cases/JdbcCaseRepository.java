package lawfirm.project.cases;

import lawfirm.project.auth.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
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

}
