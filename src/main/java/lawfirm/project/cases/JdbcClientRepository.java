package lawfirm.project.cases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class JdbcClientRepository implements ClientRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private class ClientRowMapper implements RowMapper<Client> {
        @Override
        public Client mapRow(ResultSet rs, int rowNum) throws SQLException {
            Client client = new Client();
            client.setID(rs.getInt("ID"));
            client.setEmail(rs.getString("email"));
            client.setName(rs.getString("name"));
            client.setLastName(rs.getString("lastName"));
            client.setPhoneNumber(rs.getString("phone"));
            client.setNoOfCases(rs.getInt("noOfCases"));
            return client;
        }
    }

    @Override
    public List<Client> getAllClients() {
        String sql = "SELECT CLIENT.ID, email, phone, name, lastName, COUNT(C.ID) as noOfCases from CLIENT LEFT JOIN CASES C on CLIENT.ID = C.clientID group by CLIENT.ID";
        try {
            return jdbcTemplate.query(sql,
                    new ClientRowMapper());
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Integer createClient(Client client) {
        String sql = "INSERT INTO CLIENT (email, phone, name, lastName) VALUES (?,?,?,?)";
        Integer result = -1;
        try {
            result = jdbcTemplate.update(sql, client.getEmail(),client.getPhoneNumber(),client.getName(),client.getLastName());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Integer addPhoneNumbers(Integer clientID, List<String> phoneNumbers) {
        return null;
    }

    @Override
    public Integer updateClient(Client client,Integer clientID) {
        String sql = "UPDATE CLIENT SET name=ifnull(?,name),lastName=ifnull(?,lastName),email=ifnull(?,email),phone=ifnull(?,phone) WHERE ID=?";
        Integer result = -1;
        try {
            result = jdbcTemplate.update(sql,client.getName(),client.getLastName(),client.getEmail(),client.getPhoneNumber(),clientID);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Integer deleteClient(Integer clientID) {
        String sql = "DELETE FROM CLIENT WHERE ID=?";
        Integer result = -1;
        try {
            result = jdbcTemplate.update(sql,clientID);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return result;
    }
}
