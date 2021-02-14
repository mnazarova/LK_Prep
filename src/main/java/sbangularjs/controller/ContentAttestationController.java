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
import sbangularjs.model.AttestationContent;
import sbangularjs.model.CertificationAttestation;
import sbangularjs.model.Teacher;
import sbangularjs.model.User;
import sbangularjs.repository.AttestationContentRepository;
import sbangularjs.repository.CertificationAttestationRepository;
import sbangularjs.repository.TeacherRepository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Controller
@PreAuthorize("hasAuthority('TEACHER')")
@AllArgsConstructor(onConstructor = @_(@Autowired))
public class ContentAttestationController {
    private AttestationContentRepository attestationContentRepository;
    private CertificationAttestationRepository certificationAttestationRepository;
    private TeacherRepository teacherRepository;

    @PatchMapping("/getContentAttestation")
    public ResponseEntity getContentAttestation(@AuthenticationPrincipal User user, @RequestParam Long certificationAttestationId, @RequestParam Long attestationId, @RequestParam Boolean isHead) {
        CertificationAttestation certificationAttestation = certificationAttestationRepository.findCertificationAttestationById(certificationAttestationId);
        Teacher teacher = teacherRepository.findByUsername(user.getUsername());
        if (teacher == null || teacher.getId() == null)
            return new ResponseEntity<>(0, HttpStatus.CONFLICT);
        if (certificationAttestation == null || certificationAttestation.getAttestation() == null || certificationAttestation.getAttestation().getId() == null
                                             || !certificationAttestation.getAttestation().getId().equals(attestationId))
            return new ResponseEntity<>(1, HttpStatus.CONFLICT);
        if (isHead && (teacher.getDepartment() == null || teacher.getDepartment().getId() == null)) // если не зав. каф.
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        List<AttestationContent> attestationContents;
        if (isHead) // зав.каф.
            attestationContents = attestationContentRepository.findAllByCertificationAttestationId(certificationAttestationId);
        else
            attestationContents = attestationContentRepository.findAllByCertificationAttestationIdAndTeacherId(certificationAttestationId, teacher.getId());
        return new ResponseEntity<>(attestationContents, HttpStatus.OK);
    }

    @Transactional
    @PatchMapping("/saveContentAttestation")
    public ResponseEntity saveContentAttestation(@AuthenticationPrincipal User user, @RequestBody List<AttestationContent> attestationContents) {
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
        /*Teacher teacher = teacherRepository.findByUsername(user.getUsername());
        List<AttestationContent> returnAttestationContents = attestationContentRepository.findAllByCertificationAttestationIdAndTeacherId(
                attestationContents.get(0).getCertificationAttestation().getId(), teacher.getId());*/
        return new ResponseEntity<>(attestationContents, HttpStatus.OK);
    }

}
