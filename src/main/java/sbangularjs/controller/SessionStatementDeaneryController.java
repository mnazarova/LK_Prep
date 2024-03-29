package sbangularjs.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sbangularjs.model.*;
import sbangularjs.repository.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@PreAuthorize("hasAuthority('DEANERY')")
@AllArgsConstructor(onConstructor = @_(@Autowired))
public class SessionStatementDeaneryController {
    private DeaneryRepository deaneryRepository;
    private GroupRepository groupRepository;
    private SessionSheetRepository sessionSheetRepository;
    private EvaluationRepository evaluationRepository;
    private SessionSheetContentRepository sessionSheetContentRepository;

    @PatchMapping("/getSelectedContentSessionSheet")
    public ResponseEntity getSelectedContentSessionSheet(@RequestParam Long sessionSheetId) {
        SessionSheet sessionSheet = sessionSheetRepository.findSessionSheetById(sessionSheetId);
        if (sessionSheet == null || sessionSheet.getSplitAttestationForm() == null || sessionSheet.getSplitAttestationForm().getId() == null)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        List<Evaluation> evaluations = new ArrayList<>();
        if (sessionSheet.getSplitAttestationForm().getId() == 3) { // Зачет
            evaluations.add(evaluationRepository.findEvaluationById(1L));
            evaluations.add(evaluationRepository.findEvaluationById(2L));
        }
        if (sessionSheet.getSplitAttestationForm().getId() == 2) // Дифф. зачет
            for (long i = 2; i <= 5; i++)
                evaluations.add(evaluationRepository.findEvaluationById(i));
        if (sessionSheet.getSplitAttestationForm().getId() == 4 || sessionSheet.getSplitAttestationForm().getId() == 5) // КР или КП
            for (long i = 11; i <= 14; i++)
                evaluations.add(evaluationRepository.findEvaluationById(i));

        if (sessionSheet.getSplitAttestationForm().getId() == 6) { // Допуск
            evaluations.add(evaluationRepository.findEvaluationById(6L));
            evaluations.add(evaluationRepository.findEvaluationById(7L));
        }
        if (sessionSheet.getSplitAttestationForm().getId() == 1) // Экзамен
            for (long i = 8; i <= 13; i++)
                evaluations.add(evaluationRepository.findEvaluationById(i));

        return new ResponseEntity<>(evaluations, HttpStatus.OK);
    }

    @PatchMapping("/getContentSessionSheetForDeanery")
    public ResponseEntity getContentSessionSheetForDeanery(@AuthenticationPrincipal User user, @RequestParam Long sessionSheetId) {
        Deanery curDeanery = deaneryRepository.findByUsername(user.getUsername());
        if (curDeanery == null) return new ResponseEntity<>(0, HttpStatus.CONFLICT);

        SessionSheet sessionSheet = sessionSheetRepository.findSessionSheetById(sessionSheetId);
        if (sessionSheet == null) return new ResponseEntity<>(1, HttpStatus.CONFLICT);

//        if (!certificationAttestation.getAttestation().getId().equals(attestationId)) return new ResponseEntity<>(2, HttpStatus.CONFLICT);

        // если представитель деканата перешёл на страницу группы, не принадлежащей ему, нельзя будет посмотреть данные ведомости!
        Group group = groupRepository.findByDeaneryIdAndId(curDeanery.getId(), sessionSheet.getGroup().getId());
        if (group == null) return new ResponseEntity<>(1, HttpStatus.CONFLICT);

        List<SessionSheetContent> sessionSheetContents = sessionSheetContentRepository.findAllBySessionSheetIdAndActiveIsTrue(sessionSheetId, Sort.by(Sort.Direction.ASC, "student.surname"));
        if (sessionSheetContents.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // You many decide to return HttpStatus.NOT_FOUND

        /* Установка Допуска, если Экзамен */
        if (sessionSheet.getSplitAttestationForm().getId() == 1) {
            SessionSheet ss = sessionSheetRepository.findSessionSheetBySyllabusContentIdAndGroupIdAndSplitAttestationFormId(
                    sessionSheet.getSyllabusContent().getId(), sessionSheet.getGroup().getId(), 6L);
            if (ss != null && ss.getId() != null) {
                for (SessionSheetContent sessionSheetContent : sessionSheetContents) {
                    SessionSheetContent ssc = sessionSheetContentRepository.findSessionSheetContentBySessionSheetIdAndStudentId(
                            ss.getId(), sessionSheetContent.getStudent().getId());
                    if (ssc == null || ssc.getEvaluation() == null)
                        continue;
                    sessionSheetContent.setAdmittance(ssc.getEvaluation());
                }
            }
        }

        return new ResponseEntity<>(sessionSheetContents, HttpStatus.OK);
    }

}
