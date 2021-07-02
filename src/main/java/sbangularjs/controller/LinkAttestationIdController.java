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
import sbangularjs.DTO.ConnectTeacherStudentDTO;
import sbangularjs.model.*;
import sbangularjs.repository.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@PreAuthorize("hasAuthority('SECRETARY')")
@AllArgsConstructor(onConstructor = @_(@Autowired))
public class LinkAttestationIdController {

    private SecretaryRepository secretaryRepository;
    private StudentRepository studentRepository;
    private TeacherRepository teacherRepository;

    private GroupRepository groupRepository;
    private SyllabusContentRepository syllabusContentRepository;
    private CertificationAttestationRepository certificationAttestationRepository;
    private AttestationContentRepository attestationContentRepository;
    private AttestationRepository attestationRepository;

    @PatchMapping("/attestationGetGroupById")
    public ResponseEntity attestationGetGroupById(@RequestParam Long groupId) {
        Group group = groupRepository.findGroupById(groupId);
        if (group == null || group.getActive() == null || !group.getActive())
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        return new ResponseEntity<>(group, HttpStatus.OK);
    }

    @PatchMapping("/attestationGetAttestationById")
    public ResponseEntity attestationGetAttestationById(@RequestParam Long attestationId) {
        Attestation attestation = attestationRepository.findAttestationById(attestationId);
        if (attestation == null || attestation.getId() == null) return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        return new ResponseEntity<>(attestation, HttpStatus.OK);
    }

    @PatchMapping("/attestationGetTeacherListByDepartmentId")
    public ResponseEntity attestationGetTeacherListByDepartmentId(@AuthenticationPrincipal User user) {
        Secretary curSecretary = secretaryRepository.findByUsername(user.getUsername());
        if (curSecretary == null || curSecretary.getDepartment() == null)
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        List<Teacher> teacherList = teacherRepository.findByDepartmentIdAndActiveIsTrue(curSecretary.getDepartment().getId());
        if (teacherList == null)
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        return new ResponseEntity<>(teacherList, HttpStatus.OK);
    }

    @PatchMapping("/getSyllabusContentListWithAttestationByGroupIdAndDepartmentId")
    public ResponseEntity getSyllabusContentListWithAttestationByGroupIdAndDepartmentId(@AuthenticationPrincipal User user, @RequestParam Long groupId, @RequestParam Long attestationId) {
        Group group = groupRepository.findGroupById(groupId);
        Attestation attestation = attestationRepository.findAttestationById(attestationId);
        Secretary curSecretary = secretaryRepository.findByUsername(user.getUsername());

        if (group == null || group.getCurSemester() == null)
            return new ResponseEntity<>(0, HttpStatus.CONFLICT);
        if (attestation == null || attestation.getId() == null || attestationRepository.checkAttestationDeadlineDateTimeAfter(new Date(), attestationId) == null)
            return new ResponseEntity<>(2, HttpStatus.CONFLICT);
        if (curSecretary == null || curSecretary.getDepartment() == null)
            return new ResponseEntity<>(1, HttpStatus.CONFLICT);
        if (group.getSyllabus() == null || !group.getDeanery().getFaculty().getId().equals(attestation.getFaculty().getId()))
            return new ResponseEntity<>(3, HttpStatus.CONFLICT);

        List<SyllabusContent> syllabusContents = syllabusContentRepository.findSyllabusContentsBySyllabusIdAndDepartmentIdAndCurSemester(
                group.getSyllabus().getId(), curSecretary.getDepartment().getId(), group.getCurSemester());
        if (syllabusContents.isEmpty()) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

        List<Student> students = studentRepository.findNotExpelledStudentsByGroupId(groupId);
        if (students.isEmpty()) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT); // все студенты отчислены, переведены или не добавлены в группу

        for (SyllabusContent sc: syllabusContents)
            sc.setConnectTeacherStudentDTOList(setConnectTeacherStudentDTO(students, groupId, sc, attestationId));
        return new ResponseEntity<>(syllabusContents, HttpStatus.OK);
    }

    private List<ConnectTeacherStudentDTO> setConnectTeacherStudentDTO(List<Student> students, Long groupId, SyllabusContent sc, Long attestationId) {
        List<ConnectTeacherStudentDTO> connectTeacherStudentDTOList = new ArrayList<>();
        for (Student student: students) {
            ConnectTeacherStudentDTO connectTeacherStudentDTO = new ConnectTeacherStudentDTO();

            connectTeacherStudentDTO.setStudentId(student.getId());
            connectTeacherStudentDTO.setStudentFullName(String.format("%s %s %s", student.getSurname() != null ? student.getSurname() :' ',
                    student.getName() != null ? student.getName():' ', student.getPatronymic()  != null ? student.getPatronymic():' '));

            CertificationAttestation certificationAttestation = certificationAttestationRepository.findCertificationAttestationBySyllabusContentIdAndGroupIdAndAttestationId(
                    sc.getId(), groupId, attestationId);
            if (certificationAttestation != null && certificationAttestation.getId() != null) {
                AttestationContent attestationContent = attestationContentRepository.findAttestationContentByCertificationAttestationIdAndStudentId(
                        certificationAttestation.getId(), student.getId());
                if (attestationContent != null && attestationContent.getTeacher() != null)
                    connectTeacherStudentDTO.setAttestationTeacher(attestationContent.getTeacher()); // Аттестация
            }

            connectTeacherStudentDTOList.add(connectTeacherStudentDTO);
        }
        return connectTeacherStudentDTOList;
    }

    @PatchMapping("/checkChangesInAttestationStatement")
    public ResponseEntity checkChangesInAttestationStatement(@RequestBody SyllabusContent syllabusContent, @RequestParam Long groupId, @RequestParam Long attestationId) {
        if (syllabusContent == null || syllabusContent.getId() == null || syllabusContent.getConnectTeacherStudentDTOList() == null)
            return new ResponseEntity<>(-1, HttpStatus.CONFLICT);

        CertificationAttestation certificationAttestation = certificationAttestationRepository.findCertificationAttestationBySyllabusContentIdAndGroupIdAndAttestationId(
                syllabusContent.getId(), groupId, attestationId);
        if (certificationAttestation == null || certificationAttestation.getId() == null) // если ранее не была создана ведомость
            return new ResponseEntity<>(HttpStatus.OK);

        for (ConnectTeacherStudentDTO connectTeacherStudentDTO : syllabusContent.getConnectTeacherStudentDTOList()) {
            AttestationContent attestationContent = attestationContentRepository.findAttestationContentByCertificationAttestationIdAndStudentId(
                    certificationAttestation.getId(), connectTeacherStudentDTO.getStudentId());
            if (connectTeacherStudentDTO.getAttestationTeacher() != null && connectTeacherStudentDTO.getAttestationTeacher().getId() != null) {
                if (attestationContent != null && attestationContent.getTeacher() != null && attestationContent.getTeacher().getId() != null
                        && !attestationContent.getTeacher().getId().equals(connectTeacherStudentDTO.getAttestationTeacher().getId())) { // переназначение преподавателя
                    if (attestationContent.getAttest() != null  || attestationContent.getWorks() != null)
                        return new ResponseEntity<>(0, HttpStatus.CONFLICT);
                }
            }
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    @PatchMapping("/saveAttestationTeachers")
    public ResponseEntity saveAttestationTeachers(@RequestBody SyllabusContent syllabusContent, @RequestParam Long groupId, @RequestParam Long attestationId) {
        if (syllabusContent == null || syllabusContent.getId() == null || syllabusContent.getConnectTeacherStudentDTOList() == null)
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

        CertificationAttestation certificationAttestation = certificationAttestationRepository
                .findCertificationAttestationBySyllabusContentIdAndGroupIdAndAttestationId(syllabusContent.getId(), groupId, attestationId);  // Аттестация
        if (certificationAttestation == null) { // новая ведомость
            certificationAttestation = new CertificationAttestation();
            certificationAttestation.setSyllabusContent(syllabusContentRepository.findSyllabusContentById(syllabusContent.getId()));
            certificationAttestation.setGroup(groupRepository.findGroupById(groupId));
            certificationAttestation.setAttestation(attestationRepository.findAttestationById(attestationId));
        }
        //else // ведомость аттестации уже была создана ранее
           //attestationContents = attestationContentRepository.findAllByCertificationAttestationIdAndActiveTrue(certificationAttestation.getId());

        List<AttestationContent> attestationContents = new ArrayList<>();
        for (ConnectTeacherStudentDTO connectTeacherStudentDTO : syllabusContent.getConnectTeacherStudentDTOList()) {

            AttestationContent attestationContent = attestationContentRepository.findAttestationContentByCertificationAttestationIdAndStudentId(
                    certificationAttestation.getId(), connectTeacherStudentDTO.getStudentId());
            if (attestationContent == null) { // если ранее не назначался преподаватель
                attestationContent = new AttestationContent();
                attestationContent.setCertificationAttestation(certificationAttestation);
                attestationContent.setStudent(studentRepository.findStudentById(connectTeacherStudentDTO.getStudentId()));
                attestationContent.setActive(true);
            }

            if (connectTeacherStudentDTO.getAttestationTeacher() != null && connectTeacherStudentDTO.getAttestationTeacher().getId() != null) { // если назначен преподаватель
                if (attestationContent.getTeacher() != null && attestationContent.getTeacher().getId() != null &&
                    !attestationContent.getTeacher().getId().equals(connectTeacherStudentDTO.getAttestationTeacher().getId())) { // переназначение преподавателя
//                    if (attestationContent.getAttest() != null  || attestationContent.getWorks() != null) {
                        /*Стирание данных аттестационного оценивания*/
                        attestationContent.setAttest(null);
                        attestationContent.setDateAttest(null);
                        attestationContent.setWorks(null);
                        attestationContent.setDateWorks(null);
//                    }
                }
                attestationContent.setTeacher(connectTeacherStudentDTO.getAttestationTeacher());
            }

            attestationContents.add(attestationContent);
        }
        certificationAttestation.setAttestationContents(attestationContents);
        certificationAttestationRepository.save(certificationAttestation);

        SyllabusContent sc = syllabusContentRepository.findSyllabusContentById(syllabusContent.getId());
        List<Student> students = studentRepository.findNotExpelledStudentsByGroupId(groupId);
        if (students.isEmpty()) return new ResponseEntity<>(null, HttpStatus.OK); // все студенты отчислены или переведены
        sc.setConnectTeacherStudentDTOList(setConnectTeacherStudentDTO(students, groupId, sc, attestationId));

        return new ResponseEntity<>(sc, HttpStatus.OK);
    }

}
