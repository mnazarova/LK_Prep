package sbangularjs.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sbangularjs.DTO.SubjectDTO;
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
@PreAuthorize("hasAuthority('TEACHER')")
@AllArgsConstructor(onConstructor = @_(@Autowired))
public class SubjectController {
    private TeacherRepository teacherRepository;
    private CertificationAttestationRepository certificationAttestationRepository;
    private AttestationContentRepository attestationContentRepository;
    private AttestationRepository attestationRepository;

    @PatchMapping("/getGroupsByAttestationIdAndTeacherId")
    public ResponseEntity getGroupsByAttestationIdAndTeacherId(@AuthenticationPrincipal User user, @RequestParam Long attestationId) {
        Teacher teacher = teacherRepository.findByUsername(user.getUsername());
        Attestation attestation = attestationRepository.findAttestationById(attestationId);
        if (teacher == null || teacher.getId() == null)
            return new ResponseEntity<>(0, HttpStatus.CONFLICT);
        if (attestation == null || attestation.getId() == null || attestationRepository.checkAttestationDeadlineDateTimeAfter(new Date(), attestationId) == null)
            return new ResponseEntity<>(1, HttpStatus.CONFLICT);

        return new ResponseEntity<>(getSubjectDTOList(attestationId, teacher.getId(), null), HttpStatus.OK);
    }

    private List<SubjectDTO> getSubjectDTOList(Long attestationId, Long teacherId, Long groupId) { // groupId == null - все группы
        List<Long> certificationAttestationIds = certificationAttestationRepository.findCertificationAttestationIdsByAttestationIdAndTeacherId(attestationId, teacherId);
        if (certificationAttestationIds.size() == 0) return null; // return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        List<SubjectDTO> subjectDTOList;
        if (groupId == null)
            subjectDTOList = certificationAttestationRepository.findAllSubjectsGroupDTOByCAIds(certificationAttestationIds); // далее заполнить дату
        else
            subjectDTOList = certificationAttestationRepository.findAllSubjectsGroupDTOByCAIdsAndGroupId(certificationAttestationIds, groupId);
        for (SubjectDTO sub: subjectDTOList)
            if (attestationContentRepository.findUnfinishedByCertificationAttestationId(sub.getCertificationAttestationId(), teacherId) == 0) // нет null полей
                sub.setFinished(true);
            else
                sub.setFinished(false);
        return subjectDTOList;
    }


    @PatchMapping("/subjectSelectedGroup")
    public ResponseEntity subjectSelectedGroup(@AuthenticationPrincipal User user, @RequestParam Long groupId, @RequestParam Long attestationId) {
        Teacher teacher = teacherRepository.findByUsername(user.getUsername());
        return new ResponseEntity<>(getSubjectDTOList(attestationId, teacher.getId(), groupId), HttpStatus.OK);
    }

}
