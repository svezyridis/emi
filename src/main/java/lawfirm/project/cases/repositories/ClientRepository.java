package lawfirm.project.cases.repositories;

import lawfirm.project.cases.models.Attachment;
import lawfirm.project.cases.models.Client;
import lawfirm.project.cases.models.Note;

import java.util.List;

public interface ClientRepository {
    List<Client> getAllClients();

    Integer createClient(Client client);

    Integer addPhoneNumbers(Integer clientID, List<String> phoneNumbers);

    Integer updateClient(Client client, Integer clientID);

    Integer deleteClient(Integer clientID);

}
