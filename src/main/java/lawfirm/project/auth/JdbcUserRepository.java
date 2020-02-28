package lawfirm.project.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
@Repository
public class JdbcUserRepository implements UserRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setFirstName(rs.getString("firstName"));
            user.setLastName(rs.getString("lastName"));
            user.setRole(rs.getString("role"));
            user.setPassword(rs.getString("password"));
            user.setUsername(rs.getString("username"));
            user.setId(rs.getInt("ID"));
            user.setRoleID(rs.getInt("roleID"));
            return user;
        }
    }



    @Override
    public Integer createUser(User newUser) {
        return null;
    }

    @Override
    public List<Role> getRoles() {
        return null;
    }

    @Override
    public User findByUsername(String username) {
        String sql = "SELECT USER.ID as ID, firstName, lastName, username, password, roleID, name as role from USER INNER JOIN ROLE ON USER.roleID=ROLE.ID where username=?";
        try {
            return jdbcTemplate.queryForObject(sql,
                    new Object[]{username},
                    new UserRowMapper()
            );
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Integer updatePassword(Integer userID, String newPassword) {
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        String sql = "SELECT USER.ID as ID, firstName, lastName, username, password, roleID, name as role from USER INNER JOIN ROLE ON USER.roleID=ROLE.ID";
        try {
            return jdbcTemplate.query(sql,
                    new UserRowMapper()
            );
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
}
