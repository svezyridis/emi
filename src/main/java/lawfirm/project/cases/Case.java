package lawfirm.project.cases;

import java.sql.Date;

public class Case {
    Integer id;
    Integer userID;
    String nature;
    String folderNo;
    Date assignmentDate;
    Date completionDate;
    Integer clientID;
    Client client;
}
