package lawfirm.project.cases;

import lawfirm.project.RestResponse;
import lawfirm.project.Validator;
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
    public RestResponse getCases(@CookieValue(value = "jwt", defaultValue = "token") String token) {
        if (!validator.simpleValidateToken(token))
            return new RestResponse("error", null, "invalid token");
        List<Client> clients = clientRepository.getAllClients();
        if (clients == null)
            return new RestResponse("error", null, "clients could not be fetched");
        return new RestResponse("success", clients, "clients successfully fetched");
    }

    @PostMapping(value = "/clients")
    public RestResponse createCase(@CookieValue(value = "jwt", defaultValue = "token") String token, @RequestBody Client client) {
        if (!validator.simpleValidateToken(token))
            return new RestResponse("error", null, "invalid token");
        if (!validateClient(client))
            return new RestResponse("error", null, "missing client attributes");
        Integer clientID = clientRepository.createClient(client);
        if (clientID == -1)
            return new RestResponse("error", null, "client could not be created");
        Integer result = clientRepository.addPhoneNumbers(clientID, client.getPhoneNumbers());
        if (result == -1)
            return new RestResponse("error", null, "phone numbers could not be added");
        return new RestResponse("success", null, "client successfully created");
    }

    @PutMapping(value = "/clients")
    public RestResponse updateCase(@CookieValue(value = "jwt", defaultValue = "token") String token, @RequestBody Client client) {
        if (!validator.simpleValidateToken(token))
            return new RestResponse("error", null, "invalid token");
        if (!validateClient(client))
            return new RestResponse("error", null, "missing case attributes");
        Integer result = clientRepository.updateClient(client);
        if (result == -1)
            return new RestResponse("error", null, "client could not be updated");
        return new RestResponse("success", null, "case successfully updated");
    }

    @DeleteMapping(value = "/cases")
    public RestResponse deleteCase(@CookieValue(value = "jwt", defaultValue = "token") String token, @RequestParam(value = "caseID", defaultValue = "-1") Integer caseID) {
        if (!validator.simpleValidateToken(token))
            return new RestResponse("error", null, "invalid token");
        if (caseID == -1)
            return new RestResponse("error", null, "case id not provided");
        Integer result = caseRepository.deleteCase(caseID);
        if (result == -1)
            return new RestResponse("error", null, "case could not be deleted");
        return new RestResponse("success", null, "case successfully deleted");
    }

    private boolean validateCase(Case newCase) {
        return newCase.getClientID() != null && newCase.getFolderNo() != null && newCase.getNature() != null
                && !newCase.getFolderNo().equals("") && !newCase.getNature().equals("");
    }
}
