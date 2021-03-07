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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import sbangularjs.model.*;
import sbangularjs.repository.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@PreAuthorize("hasAuthority('TEACHER')")
@AllArgsConstructor(onConstructor = @_(@Autowired))
public class ContentSessionController {
    private TeacherRepository teacherRepository;
    private EvaluationRepository evaluationRepository;
    private SessionSheetRepository sessionSheetRepository;
    private SessionSheetContentRepository sessionSheetContentRepository;

    @PatchMapping("/getContentSession")
    public ResponseEntity getContentSession(@AuthenticationPrincipal User user, @RequestParam Long sessionSheetId, @RequestParam Boolean isHead) {
        SessionSheet sessionSheet = sessionSheetRepository.findSessionSheetById(sessionSheetId);
        Teacher teacher = teacherRepository.findByUsername(user.getUsername());
        if (teacher == null || teacher.getId() == null)
            return new ResponseEntity<>(0, HttpStatus.CONFLICT);
        if (sessionSheet == null) // проверка дедлайна дисциплины. sessionSheet.getAttestation() == null || !certificationAttestation.getAttestation().getId().equals(attestationId)
            return new ResponseEntity<>(1, HttpStatus.CONFLICT);

        /*если не зав. каф.*/
        if (isHead && (teacher.getDepartment() == null || teacher.getDepartment().getId() == null)) return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        List<SessionSheetContent> sessionSheetContents;
        if (isHead) // зав.каф.
            sessionSheetContents = sessionSheetContentRepository.findAllBySessionSheetIdAndActiveIsTrue(
                    sessionSheetId, Sort.by(Sort.Direction.ASC, "student.surname"));
        else
            sessionSheetContents = sessionSheetContentRepository.findAllBySessionSheetIdAndTeacherIdAndActiveIsTrue(
                    sessionSheetId, teacher.getId(), Sort.by(Sort.Direction.ASC, "student.surname"));

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

    @PatchMapping("/getSelected")
    public ResponseEntity getSelected(@RequestParam Long sessionSheetId) {
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

    @Transactional
    @PatchMapping("/saveContentSession")
    public ResponseEntity saveContentSession(@AuthenticationPrincipal User user, @RequestParam Long sessionSheetId, @RequestBody List<SessionSheetContent> sessionSheetContents) {
        if(sessionSheetContents.size() == 0)
            return new ResponseEntity(HttpStatus.NO_CONTENT);

        SessionSheet sessionSheet = sessionSheetRepository.findSessionSheetById(sessionSheetId);

        for (SessionSheetContent sessionSheetContent: sessionSheetContents) {
            SessionSheetContent curOld = sessionSheetContentRepository.findSessionSheetContentById(sessionSheetContent.getId());

            if (sessionSheetContent.getEvaluation() != null && sessionSheetContent.getEvaluation().getId() != null) {
                if (curOld.getEvaluation() == null || !sessionSheetContent.getEvaluation().getId().equals(curOld.getEvaluation().getId())) {
                    sessionSheetContent.setDate(new Date());
                    sessionSheetContent.setSetEvaluationByTeacher(teacherRepository.findByUsername(user.getUsername()));
                }
            }

            if (sessionSheet.getSplitAttestationForm().getId() == 1) { // Ведомость "Экзамен"
                SessionSheet ss = sessionSheetRepository.findSessionSheetBySyllabusContentIdAndGroupIdAndSplitAttestationFormId(
                        sessionSheet.getSyllabusContent().getId(), sessionSheet.getGroup().getId(), 6L); // Допуск
                if (ss != null && ss.getId() != null && sessionSheetContent.getStudent() != null && sessionSheetContent.getStudent().getId() != null) {
                    SessionSheetContent ssc = sessionSheetContentRepository.findSessionSheetContentBySessionSheetIdAndStudentId(
                            ss.getId(), sessionSheetContent.getStudent().getId());
                    if (ssc != null && sessionSheetContent.getAdmittance() != null && sessionSheetContent.getAdmittance().getId() != null) {
                        if (ssc.getEvaluation() == null || !sessionSheetContent.getAdmittance().getId().equals(ssc.getEvaluation().getId())) {
                            ssc.setEvaluation(sessionSheetContent.getAdmittance()); // Установка "допуска" в ведомости "Экзамен"
                            ssc.setDate(new Date());
                            ssc.setSetEvaluationByTeacher(teacherRepository.findByUsername(user.getUsername()));
                            sessionSheetContentRepository.save(ssc);
                        }
                    }
                }
            }

            if (sessionSheet.getSplitAttestationForm().getId() == 6) { // Ведомость "Допуск"
                SessionSheet ss = sessionSheetRepository.findSessionSheetBySyllabusContentIdAndGroupIdAndSplitAttestationFormId(
                        sessionSheet.getSyllabusContent().getId(), sessionSheet.getGroup().getId(), 1L); // Экзамен
                if (ss != null && ss.getId() != null && sessionSheetContent.getStudent() != null && sessionSheetContent.getStudent().getId() != null) {
                    SessionSheetContent ssc = sessionSheetContentRepository.findSessionSheetContentBySessionSheetIdAndStudentId(
                            ss.getId(), sessionSheetContent.getStudent().getId());
                    if (ssc != null && sessionSheetContent.getEvaluation() != null && sessionSheetContent.getEvaluation().getId() != null) { // изменилось значение "допуска"
                        if (curOld.getEvaluation() == null || !sessionSheetContent.getEvaluation().getId().equals(curOld.getEvaluation().getId())) {
                            ssc.setEvaluation(null); // Обнуление оценки по экзамену, если из ведомости допуска изменили значение поля
                            ssc.setSetEvaluationByTeacher(null);
                            ssc.setDate(null);
                            sessionSheetContentRepository.save(ssc);
                        }
                    }
                }
            }

        }
        sessionSheetContentRepository.saveAll(sessionSheetContents);
        return new ResponseEntity<>(sessionSheetContents, HttpStatus.OK);
    }

}
