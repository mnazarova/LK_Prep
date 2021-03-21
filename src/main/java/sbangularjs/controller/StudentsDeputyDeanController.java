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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@PreAuthorize("hasAuthority('DEPUTY_DEAN')")
@AllArgsConstructor(onConstructor = @_(@Autowired))
public class StudentsDeputyDeanController {
    private DeputyDeanRepository deputyDeanRepository;
    private GroupRepository groupRepository;
    private StudentRepository studentRepository;

    private AttestationRepository attestationRepository;
    private AttestationContentRepository attestationContentRepository;

    private SyllabusContentRepository syllabusContentRepository;
    private SessionSheetRepository sessionSheetRepository;
    private SessionSheetContentRepository sessionSheetContentRepository;

    @PatchMapping("/getStudentsForDeputyDean")
    public ResponseEntity getStudentsForDeputyDean(@AuthenticationPrincipal User user, @RequestParam Long groupId) {
        DeputyDean deputyDean = deputyDeanRepository.findByUsername(user.getUsername());
        if (deputyDean == null || deputyDean.getId() == null) return new ResponseEntity<>(0, HttpStatus.CONFLICT);
        Group group = groupRepository.findByDeputyDeanIdAndId(deputyDean.getId(), groupId);
        if (group == null) return new ResponseEntity<>(1, HttpStatus.CONFLICT);

        List<Student> students = studentRepository.findByGroupId(groupId);
        if (students.isEmpty()) return new ResponseEntity(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @Transactional
    @PatchMapping("/saveExpelledStudentsForDeputyDean")
    public ResponseEntity saveExpelledStudentsForDeputyDean(@RequestParam Long groupId, @RequestBody List<Student> students) {
        List<Student> updatingStudents = new ArrayList<>();
        for(Student curStudent: students) {
            Student student = studentRepository.findStudentById(curStudent.getId());
            if (curStudent.getExpelled() != null && curStudent.getExpelled() != student.getExpelled()) {
                student.setExpelled(curStudent.getExpelled()); // expelled - true, activeContent - false; RESTORE: expelled - false, activeContent - true;
                updatingStudents.add(student);
                changeActiveOfContent(groupId, student, !student.getExpelled()); // проверка всех актуальных ведомостей
            }
        }
        studentRepository.saveAll(updatingStudents);
        return new ResponseEntity<>(studentRepository.findByGroupId(groupId), HttpStatus.OK);
    }

    private void changeActiveOfContent(Long groupId, Student student, Boolean active) {

        // sessionContent
        List<SyllabusContent> syllabusContents = syllabusContentRepository.findBySyllabusIdAndSemesterNumber(
                student.getGroup().getSyllabus().getId(), student.getGroup().getCurSemester()); // дисциплины текущего семестра
        for (SyllabusContent syllabusContent: syllabusContents) {
            List<SessionSheet> sessionSheets = sessionSheetRepository.findSessionSheetBySyllabusContentIdAndGroupId(syllabusContent.getId(), student.getGroup().getId());
            for (SessionSheet sessionSheet : sessionSheets) {
                SessionSheetContent sessionSheetContent = sessionSheetContentRepository.findSessionSheetContentBySessionSheetIdAndStudentId(sessionSheet.getId(), student.getId());
                if (sessionSheetContent != null) {
                    sessionSheetContent.setActive(active);
                    sessionSheetContentRepository.save(sessionSheetContent);
                }
            }
        }

        Group group = groupRepository.findGroupById(groupId);
        if (group == null || group.getDeanery() == null || group.getDeanery().getFaculty() == null)
            return;

        // attestationContent
        List<Attestation> attestations = attestationRepository.findAllWithDeadlineDateTimeAfter(new Date()); // актуальные аттестации
        for (Attestation attestation: attestations) {
            if (!attestation.getFaculty().getId().equals(group.getDeanery().getFaculty().getId())) continue;
            for (CertificationAttestation certificationAttestation: attestation.getCertificationsAttestation()) {
                if (!certificationAttestation.getGroup().getId().equals(student.getGroup().getId())) continue;
                AttestationContent attestationContent = attestationContentRepository.findAttestationContentByCertificationAttestationIdAndStudentId(
                        certificationAttestation.getId(), student.getId());
                if (attestationContent != null) {
                    attestationContent.setActive(active);
                    attestationContentRepository.save(attestationContent);
                }

            }
        }

    }

}
