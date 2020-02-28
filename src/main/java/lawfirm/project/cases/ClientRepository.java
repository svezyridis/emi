package lawfirm.project.cases;

import java.util.List;

public interface ClientRepository {
    List<Client> getAllClients();

    Integer createClient(Client client);

    Integer addPhoneNumbers(Integer clientID, List<String> phoneNumbers);

    Integer updateClient(Client client);

    Integer deleteClient(Integer clientID);
}
