package lawfirm.project.cases.controller;

import lawfirm.project.RestResponse;
import lawfirm.project.Validator;
import lawfirm.project.cases.repositories.ClientRepository;
import lawfirm.project.cases.models.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ClientController {

    Logger logger = LoggerFactory.getLogger(ClientController.class);

    @Autowired
    @Qualifier("jdbcClientRepository")
    private ClientRepository clientRepository;

    @Autowired
    Validator validator;

    @RequestMapping(value = "/clients")
    public RestResponse getClients(@CookieValue(value = "jwt", defaultValue = "token") String token) {
        if (!validator.simpleValidateToken(token))
            return new RestResponse("error", null, "invalid token");
        List<Client> clients = clientRepository.getAllClients();
        if (clients == null)
            return new RestResponse("error", null, "clients could not be fetched");
        return new RestResponse("success", clients, "clients successfully fetched");
    }

    @PostMapping(value = "/clients")
    public RestResponse createClient(@CookieValue(value = "jwt", defaultValue = "token") String token, @RequestBody Client client) {
        if (!validator.simpleValidateToken(token))
            return new RestResponse("error", null, "invalid token");
        if (!validateClient(client))
            return new RestResponse("error", null, "missing client attributes");
        Integer clientID = clientRepository.createClient(client);
        if (clientID == -1)
            return new RestResponse("error", null, "client could not be created");
        return new RestResponse("success", null, "client successfully created");
    }

    private boolean validateClient(Client client) {
        return true;
    }

    @PutMapping(value = "/clients/{clientID}")
    public RestResponse updateClient(@CookieValue(value = "jwt", defaultValue = "token") String token, @RequestBody Client client, @PathVariable Integer clientID) {
        if (!validator.simpleValidateToken(token))
            return new RestResponse("error", null, "invalid token");
        if (!validateClient(client))
            return new RestResponse("error", null, "missing case attributes");
        Integer result = clientRepository.updateClient(client, clientID);
        if (result == -1)
            return new RestResponse("error", null, "client could not be updated");
        return new RestResponse("success", null, "client successfully updated");
    }

    @DeleteMapping(value = "/clients/{clientID}")
    public RestResponse deleteClient(@CookieValue(value = "jwt", defaultValue = "token") String token, @PathVariable Integer clientID) {
        if (!validator.simpleValidateToken(token))
            return new RestResponse("error", null, "invalid token");
        Integer result = clientRepository.deleteClient(clientID);
        if (result == -1)
            return new RestResponse("error", null, "client could not be deleted");
        return new RestResponse("success", null, "client successfully deleted");
    }

}
