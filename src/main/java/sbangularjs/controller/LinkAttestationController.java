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
import sbangularjs.model.*;
import sbangularjs.repository.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@PreAuthorize("hasAuthority('SECRETARY')")
@AllArgsConstructor(onConstructor = @_(@Autowired))
public class LinkAttestationController {

    private GroupRepository groupRepository;
    private StudentRepository studentRepository;
    private SecretaryRepository secretaryRepository;
    private SyllabusContentRepository syllabusContentRepository;
    private AttestationRepository attestationRepository;
    private CertificationAttestationRepository certificationAttestationRepository;
    private AttestationContentRepository attestationContentRepository;

    @PatchMapping("/getActingAttestation")
    public ResponseEntity getActingAttestation() {
        List<Attestation> attestations = attestationRepository.findAllWithDeadlineDateTimeAfter(new Date());
        if (attestations.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(attestations, HttpStatus.OK);
    }

    @PatchMapping("/attestationSelectedGroup")
    public ResponseEntity attestationSelectedGroup(@AuthenticationPrincipal User user, @RequestParam Long groupId, Long attestationId) {
        Group group = groupRepository.findGroupById(groupId);
        if (group == null)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // You many decide to return HttpStatus.NOT_FOUND
        List<Group> groupList = new ArrayList<>();
        groupList.add(group);
        return new ResponseEntity<>(setBlankForGroups(user, groupList, attestationId).get(0), HttpStatus.OK);
    }


    @PatchMapping("/changeAttestationSelected")
    public ResponseEntity changeAttestationSelected(@AuthenticationPrincipal User user, Long attestationId) { // смена аттестации и вывод списка групп
        Attestation attestation = attestationRepository.findAttestationById(attestationId);
        if (attestation == null || attestation.getFaculty() == null)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        List<Group> groupList = groupRepository.findGroupsByActiveIsTrueAndFacultyIdAndCurSemesterNotNull(attestation.getFaculty().getId());
        if (groupList.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // You many decide to return HttpStatus.NOT_FOUND
        return new ResponseEntity<>(setBlankForGroups(user, groupList, attestationId), HttpStatus.OK);
    }

    private List<Group> setBlankForGroups(User user, List<Group> groupList, Long attestationId) {
        Secretary curSecretary = secretaryRepository.findByUsername(user.getUsername());
//        if (curSecretary == null || curSecretary.getDepartment() == null)
//            return new ResponseEntity<>(1, HttpStatus.CONFLICT);
        for (Group gr: groupList) {
            if (gr.getCurSemester() == null || gr.getSyllabus() == null) {
                gr.setBlank(null);
                continue;
            }
            List<SyllabusContent> syllabusContents = syllabusContentRepository.findSyllabusContentsBySyllabusIdAndDepartmentIdAndCurSemester(
                    gr.getSyllabus().getId(), curSecretary.getDepartment().getId(), gr.getCurSemester());
            if (syllabusContents.isEmpty()) { // нет предметов в текущем семестре у группы на кафедре секретаря
                gr.setBlank(false);
                continue;
            }
            List<Student> students = studentRepository.findNotExpelledStudentsByGroupId(gr.getId());
            if (students.isEmpty()) {
                gr.setBlank(null);
                continue;
            }

            gr.setBlank(checkAllSyllabusContentAndStudents(syllabusContents, students, gr.getId(), attestationId)); // true - exist blank field
        }
        return groupList;
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
