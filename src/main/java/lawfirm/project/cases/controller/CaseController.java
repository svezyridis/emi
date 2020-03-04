package lawfirm.project.cases.controller;

import lawfirm.project.RestResponse;
import lawfirm.project.Validator;
import lawfirm.project.cases.models.Note;
import lawfirm.project.cases.repositories.CaseRepository;
import lawfirm.project.cases.models.Case;
import lawfirm.project.storage.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CaseController {

    Logger logger = LoggerFactory.getLogger(CaseController.class);

    @Autowired
    @Qualifier("jdbcCaseRepository")
    private CaseRepository caseRepository;

    @Autowired
    Validator validator;

    @Autowired
    StorageService storageService;

    @RequestMapping(value = "/cases")
    public RestResponse getCases(@CookieValue(value = "jwt", defaultValue = "token") String token,@RequestParam(value = "clientID",defaultValue = "-1") Integer clientID, @RequestParam(value = "userID", defaultValue = "-1") Integer userID,
                                 @RequestParam(value = "caseID", defaultValue = "-1") Integer caseID) {
        if (!validator.simpleValidateToken(token))
            return new RestResponse("error", null, "invalid token");
        List<Case> cases = null;
        if (userID != -1)
            cases = caseRepository.getCasesOfUser(userID);
        else if(clientID!=-1)
            cases =caseRepository.getCasesOfClient(clientID);
        else if (caseID != -1) {
            Case myCase = caseRepository.getCase(caseID);
            myCase.setAttachments(caseRepository.getCaseAttachments(myCase.getId()));
            myCase.setNotes(caseRepository.getCaseNotes(myCase.getId()));
            return new RestResponse("success", myCase, "case successfully fetched");
        }
        else
            cases = caseRepository.getAllCases();

        if (cases == null)
            return new RestResponse("error", null, "cases could not be fetched");
        return new RestResponse("success", cases, "cases successfully fetched");
    }


    @PostMapping(value = "/cases")
    public RestResponse createCase(@CookieValue(value = "jwt", defaultValue = "token") String token, @RequestBody Case newCase) {
        logger.info(newCase.toString());
        if (!validator.simpleValidateToken(token))
            return new RestResponse("error", null, "invalid token");
        if (!validateCase(newCase))
            return new RestResponse("error", null, "missing case attributes");
        Integer result = caseRepository.createCase(newCase);
        if (result == -1)
            return new RestResponse("error", null, "case could not be created");
        return new RestResponse("success", null, "case successfully created");
    }

    @PostMapping(value = "/notes")
    public RestResponse createCase(@CookieValue(value = "jwt", defaultValue = "token") String token, @RequestBody Note note) {
        logger.info(note.toString());
        if (!validator.simpleValidateToken(token))
            return new RestResponse("error", null, "invalid token");
        if (!validateNote(note))
            return new RestResponse("error", null, "missing note attributes");
        Integer result = caseRepository.createNote(note);
        if (result == -1)
            return new RestResponse("error", null, "note could not be created");
        return new RestResponse("success", null, "note successfully created");
    }

    private boolean validateNote(Note note) {
        return note.getUserID() != null && !note.getText().equals(null) && !note.getText().equals("") && note.getCaseID() != null;
    }

    @PutMapping(value = "/cases/{caseID}")
    public RestResponse updateCase(@CookieValue(value = "jwt", defaultValue = "token") String token, @RequestBody Case newCase, @PathVariable Integer caseID) {
        if (!validator.simpleValidateToken(token))
            return new RestResponse("error", null, "invalid token");
        if (!validateCase(newCase))
            return new RestResponse("error", null, "missing case attributes");
        Integer result = caseRepository.updateCase(newCase, caseID);
        if (result == -1)
            return new RestResponse("error", null, "case could not be updated");
        return new RestResponse("success", null, "case successfully updated");
    }

    @DeleteMapping(value = "/cases/{caseID}")
    public RestResponse deleteCase(@CookieValue(value = "jwt", defaultValue = "token") String token, @PathVariable Integer caseID) {
        if (!validator.simpleValidateToken(token))
            return new RestResponse("error", null, "invalid token");
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
