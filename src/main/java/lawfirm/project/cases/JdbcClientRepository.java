package lawfirm.project.cases;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcClientRepository implements ClientRepository {
    @Override
    public List<Client> getAllClients() {
        return null;
    }

    @Override
    public Integer createClient(Client client) {
        return null;
    }

    @Override
    public Integer addPhoneNumbers(Integer clientID, List<String> phoneNumbers) {
        return null;
    }

    @Override
    public Integer updateClient(Client client) {
        return null;
    }

    @Override
    public Integer deleteClient(Integer clientID) {
        return null;
    }
}
