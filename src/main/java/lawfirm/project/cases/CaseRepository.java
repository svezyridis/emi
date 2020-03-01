package lawfirm.project.cases;

import java.util.List;

public interface CaseRepository {
    List<Case> getAllCases();

    List<Case> getCasesOfUser(Integer userID);

    Integer createCase(Case newCase);

    Integer updateCase(Case newCase,Integer caseID);

    Integer deleteCase(Integer caseID);
}
