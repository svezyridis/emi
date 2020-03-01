package lawfirm.project.cases;

import lawfirm.project.RestResponse;
import lawfirm.project.Validator;
import lawfirm.project.auth.AuthController;
import lawfirm.project.storage.FileUploadController;
import lawfirm.project.storage.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

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
    public RestResponse getCases(@CookieValue(value = "jwt", defaultValue = "token") String token, @RequestParam(value = "userID", defaultValue = "-1") Integer userID,
                                 @RequestParam(value = "caseID", defaultValue = "-1") Integer caseID) {
        if (!validator.simpleValidateToken(token))
            return new RestResponse("error", null, "invalid token");
        List<Case> cases = null;
        if (userID == -1)
            cases = caseRepository.getAllCases();
        else
            cases = caseRepository.getCasesOfUser(userID);
        if (cases == null)
            return new RestResponse("error", null, "cases could not be fetched");
        return new RestResponse("success", cases, "cases successfully fetched");
    }


    @PostMapping(value = "/cases")
    public RestResponse createCase(@CookieValue(value = "jwt", defaultValue = "token") String token, @RequestBody Case newCase) {
        logger.info(newCase.toString());
        if (!validator.simpleValidateToken(token))
            return new RestResponse("error", null, "invalid token");
        if(!validateCase(newCase))
            return new RestResponse("error", null, "missing case attributes");
        Integer result=caseRepository.createCase(newCase);
        if(result==-1)
            return new RestResponse("error", null, "case could not be created");
        return new RestResponse("success", null, "case successfully created");
    }

    @PutMapping(value = "/cases/{caseID}")
    public RestResponse updateCase(@CookieValue(value = "jwt", defaultValue = "token") String token, @RequestBody Case newCase, @PathVariable Integer caseID) {
        if (!validator.simpleValidateToken(token))
            return new RestResponse("error", null, "invalid token");
        if(!validateCase(newCase))
            return new RestResponse("error", null, "missing case attributes");
        Integer result=caseRepository.updateCase(newCase,caseID);
        if(result==-1)
            return new RestResponse("error", null, "case could not be updated");
        return new RestResponse("success", null, "case successfully updated");
    }

    @DeleteMapping(value = "/cases/{caseID}")
    public RestResponse deleteCase(@CookieValue(value = "jwt", defaultValue = "token") String token, @PathVariable Integer caseID) {
        if (!validator.simpleValidateToken(token))
            return new RestResponse("error", null, "invalid token");
        Integer result=caseRepository.deleteCase(caseID);
        if(result==-1)
            return new RestResponse("error", null, "case could not be deleted");
        return new RestResponse("success", null, "case successfully deleted");
    }

    private boolean validateCase(Case newCase) {
        return newCase.getClientID() != null && newCase.getFolderNo() != null && newCase.getNature() != null
                && !newCase.getFolderNo().equals("") && !newCase.getNature().equals("");
    }


}
