package sbangularjs.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Date;
import java.util.List;

@Controller
@PreAuthorize("hasAuthority('TEACHER')")
@AllArgsConstructor(onConstructor = @_(@Autowired))
public class ContentSessionController {
    private AttestationContentRepository attestationContentRepository;
    private CertificationAttestationRepository certificationAttestationRepository;
    private SessionSheetRepository sessionSheetRepository;
    private SessionSheetContentRepository sessionSheetContentRepository;
    private TeacherRepository teacherRepository;

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
            sessionSheetContents = sessionSheetContentRepository.findAllBySessionSheetId(sessionSheetId);
        else
            sessionSheetContents = sessionSheetContentRepository.findAllBySessionSheetIdAndTeacherId(sessionSheetId, teacher.getId());
        return new ResponseEntity<>(sessionSheetContents, HttpStatus.OK);
    }

    /*@Transactional
    @PatchMapping("/saveContentSession")
    public ResponseEntity saveContentSession(@AuthenticationPrincipal User user, @RequestBody List<AttestationContent> attestationContents) {
        if(attestationContents == null || attestationContents.size() == 0) return new ResponseEntity(HttpStatus.NO_CONTENT);

        for (AttestationContent attestationContent: attestationContents) {
            AttestationContent curOld = attestationContentRepository.findAttestationContentById(attestationContent.getId());
            if (attestationContent.getAttest() != curOld.getAttest()) {
                attestationContent.setDateAttest(new Date());
                attestationContent.setSetAttestByTeacher(teacherRepository.findByUsername(user.getUsername()));
            }

            if (attestationContent.getWorks() != curOld.getWorks()) {
                attestationContent.setDateWorks(new Date());
                attestationContent.setSetWorksByTeacher(teacherRepository.findByUsername(user.getUsername()));
            }
        }
        attestationContentRepository.saveAll(attestationContents);
        return new ResponseEntity<>(attestationContents, HttpStatus.OK);
    }*/

}
