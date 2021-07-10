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
@PreAuthorize("hasAuthority('DEPUTY_DEAN')")
@AllArgsConstructor(onConstructor = @_(@Autowired))
public class DeputyDeanSessionStatementController {
    private DeputyDeanRepository deputyDeanRepository;
    private GroupRepository groupRepository;
    private SessionSheetRepository sessionSheetRepository;
    private EvaluationRepository evaluationRepository;
    private SessionSheetContentRepository sessionSheetContentRepository;

    @PatchMapping("/getSelectedContentSessionSheetForDeputyDean")
    public ResponseEntity getSelectedContentSessionSheetForDeputyDean(@RequestParam Long sessionSheetId) {
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

    @PatchMapping("/getContentSessionSheetForDeputyDean")
    public ResponseEntity getContentSessionSheetForDeputyDean(@AuthenticationPrincipal User user, @RequestParam Long groupId, @RequestParam Long sessionSheetId) {
        DeputyDean deputyDean = deputyDeanRepository.findByUsername(user.getUsername());
        if (deputyDean == null || deputyDean.getId() == null) return new ResponseEntity<>(0, HttpStatus.CONFLICT);

        // если зам. декана перешёл на страницу группы, не принадлежащей ему, нельзя будет посмотреть данные ведомости!
        Group group = groupRepository.findByDeputyDeanIdAndId(deputyDean.getId(), groupId); // группа - зам. декана
        if (group == null) return new ResponseEntity<>(1, HttpStatus.CONFLICT);

        SessionSheet sessionSheet = sessionSheetRepository.findSessionSheetById(sessionSheetId);
        if (sessionSheet == null || sessionSheet.getGroup() == null || sessionSheet.getGroup().getId() == null ||
                !sessionSheet.getGroup().getId().equals(groupId)) // группа в адресной строке - группа в ведомости
            return new ResponseEntity<>(2, HttpStatus.CONFLICT);

//        if (!certificationAttestation.getAttestation().getId().equals(attestationId)) return new ResponseEntity<>(3, HttpStatus.CONFLICT);

        List<SessionSheetContent> sessionSheetContents = sessionSheetContentRepository.findAllBySessionSheetIdAndActiveIsTrue(sessionSheetId, Sort.by(Sort.Direction.ASC, "student.surname"));
        if (sessionSheetContents.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // You many decide to return HttpStatus.NOT_FOUND

        /* Установка Допуска, если Экзамен */
        if (sessionSheet.getSplitAttestationForm().getId() == 1) {
            SessionSheet ss = sessionSheetRepository.findSessionSheetBySyllabusContentIdAndGroupIdAndSplitAttestationFormIdAndIsAdditional(
                    sessionSheet.getSyllabusContent().getId(), sessionSheet.getGroup().getId(), 6L, sessionSheet.getIsAdditional());
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
