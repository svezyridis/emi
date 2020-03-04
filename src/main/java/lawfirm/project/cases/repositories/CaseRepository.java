package lawfirm.project.cases.repositories;

import lawfirm.project.cases.models.Attachment;
import lawfirm.project.cases.models.Case;
import lawfirm.project.cases.models.Note;

import java.util.List;

public interface CaseRepository {
    List<Case> getAllCases();

    List<Case> getCasesOfUser(Integer userID);

    Integer createCase(Case newCase);

    Integer updateCase(Case newCase,Integer caseID);

    Integer deleteCase(Integer caseID);

    Case getCase(Integer caseID);

    List<Attachment> getCaseAttachments(Integer caseID);

    List<Note> getCaseNotes(Integer caseID);

    Integer createNote(Note note);

    List<Case> getCasesOfClient(Integer clientID);

    Integer addAttachment(Integer caseID,Integer userID,String filename);
}
