package sbangularjs.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import sbangularjs.model.Attestation;
import sbangularjs.model.Teacher;
import sbangularjs.model.User;
import sbangularjs.repository.AttestationContentRepository;
import sbangularjs.repository.AttestationRepository;
import sbangularjs.repository.CertificationAttestationRepository;
import sbangularjs.repository.TeacherRepository;

import java.util.Date;
import java.util.List;

@Controller
@PreAuthorize("hasAnyAuthority('TEACHER')")
@AllArgsConstructor(onConstructor = @_(@Autowired))
public class AttestationController {
    private TeacherRepository teacherRepository;
    private AttestationRepository attestationRepository;
    private CertificationAttestationRepository certificationAttestationRepository;
    private AttestationContentRepository attestationContentRepository;

    @GetMapping("/getAttestation")
    public ResponseEntity getAttestation(@AuthenticationPrincipal User user) {
        Teacher teacher = teacherRepository.findByUsername(user.getUsername());
        if (teacher == null || teacher.getId() == null)
            return new ResponseEntity<>(0, HttpStatus.CONFLICT);
        List<Attestation> attestations = attestationRepository.findAllWithDeadlineDateTimeAfter(new Date());
        if (attestations.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // You many decide to return HttpStatus.NOT_FOUND

        for (Attestation attestation: attestations) {
            List<Long> certificationAttestationIds = certificationAttestationRepository.findCertificationAttestationIdsByAttestationIdAndTeacherId(attestation.getId(), teacher.getId());
           // if (certificationAttestationIds.size() == 0) attestation.setFinished(true);
            attestation.setFinished(true);
            for (Long caId: certificationAttestationIds) {
                if (attestationContentRepository.findUnfinishedByCertificationAttestationIdAndTeacherId(caId, teacher.getId()) != 0) { // присутствуют null полей
                    attestation.setFinished(false);
                    break;
                }
            }
        }

        return new ResponseEntity<>(attestations, HttpStatus.OK);
    }

}
