package sbangularjs.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import sbangularjs.model.*;
import sbangularjs.repository.*;

import java.util.Date;
import java.util.List;

@Controller
@PreAuthorize("hasAuthority('SECRETARY')")
@AllArgsConstructor(onConstructor = @_(@Autowired))
public class ListAttestationController {

    private GroupRepository groupRepository;
    private StudentRepository studentRepository;
    private SecretaryRepository secretaryRepository;
    private SyllabusContentRepository syllabusContentRepository;
    private AttestationRepository attestationRepository;
    private CertificationAttestationRepository certificationAttestationRepository;
    private AttestationContentRepository attestationContentRepository;

    @PatchMapping("/getActingAttestation")
    public ResponseEntity getActingAttestation(@AuthenticationPrincipal User user) {
        Secretary curSecretary = secretaryRepository.findByUsername(user.getUsername());
        if (curSecretary == null || curSecretary.getDepartment() == null)
            return new ResponseEntity<>(0, HttpStatus.CONFLICT);
        List<Attestation> attestations = attestationRepository.findAllWithDeadlineDateTimeAfter(new Date());
        if (attestations.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        for (Attestation attestation: attestations) {
            List<Group> groupList = groupRepository.findGroupsByActiveIsTrueAndFacultyIdAndCurSemesterNotNull(attestation.getFaculty().getId());

            attestation.setFinished(true);
            for (Group gr: groupList) {
                if (gr.getId() == null || gr.getCurSemester() == null || gr.getSyllabus() == null || gr.getSyllabus().getId() == null)
                    continue;
                List<Student> students = studentRepository.findNotExpelledStudentsByGroupId(gr.getId());
                List<SyllabusContent> syllabusContents = syllabusContentRepository.findSyllabusContentsBySyllabusIdAndDepartmentIdAndCurSemester(
                        gr.getSyllabus().getId(), curSecretary.getDepartment().getId(), gr.getCurSemester());
                if (syllabusContents.isEmpty() || students.isEmpty()) // нет предметов в текущем семестре у группы на кафедре секретаря или нет студентов
                    continue;

                if (checkAllSyllabusContentAndStudents(syllabusContents, students, gr.getId(), attestation.getId())) { // true - присутствуют пустые поля
                    attestation.setFinished(false);
                    break;
                }
            }
        }

        return new ResponseEntity<>(attestations, HttpStatus.OK);
    }

    private boolean checkAllSyllabusContentAndStudents(List<SyllabusContent> syllabusContents, List<Student> students, Long groupId, Long attestationId) {
        for (SyllabusContent sc: syllabusContents) {
            for (Student student: students) {
                CertificationAttestation certificationAttestation = certificationAttestationRepository.findCertificationAttestationBySyllabusContentIdAndGroupIdAndAttestationId(
                        sc.getId(), groupId, attestationId);  // Аттестация
                if (certificationAttestation == null || certificationAttestation.getId() == null) return true;

                AttestationContent attestationContent = attestationContentRepository.findAttestationContentByCertificationAttestationIdAndStudentId(
                        certificationAttestation.getId(), student.getId());
                if (attestationContent == null || attestationContent.getTeacher() == null || attestationContent.getTeacher().getId() == null) // false - если преподаватель назначен
                    return true;
            }
        }
        return false;
    }

}
