package lawfirm.project.storage;
import lawfirm.project.RestResponse;
import lawfirm.project.Validator;
import lawfirm.project.cases.repositories.CaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;




@RestController
public class FileUploadController {

    private final StorageService storageService;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @Autowired
    Validator validator;

    @Autowired
    @Qualifier("jdbcCaseRepository")
    private CaseRepository caseRepository;

    @GetMapping("/files/{caseID}/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@CookieValue(value = "jwt", defaultValue = "token") String token,@PathVariable String filename, @PathVariable String caseID) {
        if (!validator.simpleValidateToken(token))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        Resource file = storageService.loadAsResource(filename, caseID);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/files/{caseID}")
    public RestResponse handleFileUpload(@CookieValue(value = "jwt", defaultValue = "token") String token, @RequestParam("file") MultipartFile file, @PathVariable Integer caseID) {
        if (!validator.simpleValidateToken(token))
            return new RestResponse("error", null, "invalid token");
        Integer userID=validator.getUserID(token);
        storageService.store(file,caseID.toString());
        Integer result=caseRepository.addAttachment(caseID,userID, StringUtils.cleanPath(file.getOriginalFilename()));
        if(result==-1){
            return new RestResponse("error", null, "file could not be added to database");
        }
        return new RestResponse("success",null,"file saved successfully");
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(StorageException.class)
    public RestResponse handleStorageException(StorageException exc) {
        return new RestResponse("error",null,exc.getMessage());
    }

}
