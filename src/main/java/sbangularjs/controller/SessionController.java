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
import sbangularjs.DTO.SessionDTO;
import sbangularjs.DTO.SubjectDTO;
import sbangularjs.model.Attestation;
import sbangularjs.model.SessionSheet;
import sbangularjs.model.Teacher;
import sbangularjs.model.User;
import sbangularjs.repository.*;

import java.util.Date;
import java.util.List;

@Controller
@PreAuthorize("hasAuthority('TEACHER')")
@AllArgsConstructor(onConstructor = @_(@Autowired))
public class SessionController {
    private TeacherRepository teacherRepository;
    private SessionSheetRepository sessionSheetRepository;
    private SessionSheetContentRepository sessionSheetContentRepository;

    @PatchMapping("/getSessionDTOGroupsByTeacherIdAndGroupId")
    public ResponseEntity getSessionDTOGroupsByTeacherIdAndGroupId(@AuthenticationPrincipal User user, @RequestParam Long groupId) {
        Teacher teacher = teacherRepository.findByUsername(user.getUsername());
        if (teacher == null || teacher.getId() == null)
            return new ResponseEntity<>(0, HttpStatus.CONFLICT); // if (attestationRepository.checkAttestationDeadlineDateTimeAfter(new Date(), attestationId) == null)

        return new ResponseEntity<>(getSessionDTOList(teacher.getId(), groupId), HttpStatus.OK);
    }

    private List<SessionDTO> getSessionDTOList(Long teacherId, Long groupId) { // groupId == null - все группы данного преподавателя
        List<Long> sessionSheetIds = sessionSheetRepository.findSessionSheetIdsByTeacherIdAndDeadline(teacherId, new Date());
        if (sessionSheetIds.size() == 0) return null; // return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        List<SessionDTO> sessionDTOList;
        if (groupId == null)
            sessionDTOList = sessionSheetRepository.findAllSessionDTOBySessionSheetIds(sessionSheetIds);
        else
            sessionDTOList = sessionSheetRepository.findAllSessionDTOBySessionSheetIdsAndGroupId(sessionSheetIds, groupId);
        for (SessionDTO sessionDTO: sessionDTOList)
            if (sessionSheetContentRepository.findUnfinishedBySessionSheetIdAndTeacherId(sessionDTO.getSessionSheetId(), teacherId) == 0) // нет null полей
                sessionDTO.setFinished(true);
            else
                sessionDTO.setFinished(false);
        return sessionDTOList;
    }

    /*@PatchMapping("/getGroupsForHeadOfDepartment")
    public ResponseEntity getGroupsForHeadOfDepartment(@AuthenticationPrincipal User user, @RequestParam Long attestationId, @RequestParam Long groupId) {
        // groupId == null - все группы с дисциплинами данной кафедры
        Teacher teacher = teacherRepository.findByUsername(user.getUsername());
        Attestation attestation = attestationRepository.findAttestationById(attestationId);
        if (teacher == null || teacher.getId() == null)
            return new ResponseEntity<>(0, HttpStatus.CONFLICT);
        if (teacher.getDepartment() == null || teacher.getDepartment().getId() == null)
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        if (attestation == null || attestation.getId() == null || attestationRepository.checkAttestationDeadlineDateTimeAfter(new Date(), attestationId) == null)
            return new ResponseEntity<>(1, HttpStatus.CONFLICT);

        List<Long> certificationAttestationIds;
        if (groupId == null)
            certificationAttestationIds = certificationAttestationRepository.findCertificationAttestationIdsByAttestationIdAndDepartmentId(attestationId, teacher.getDepartment().getId());
        else
            certificationAttestationIds = certificationAttestationRepository.findCertificationAttestationIdsByAttestationIdAndDepartmentIdAngGroupId(
                    attestationId, teacher.getDepartment().getId(), groupId);

        if (certificationAttestationIds.size() == 0) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        List<SubjectDTO> subjectDTOList = certificationAttestationRepository.findAllSubjectsGroupDTOByCAIds(certificationAttestationIds);
        for (SubjectDTO sub: subjectDTOList)
            if (attestationContentRepository.findUnfinishedByCertificationAttestationId(sub.getCertificationAttestationId()) == 0) // нет null полей
                sub.setFinished(true);
            else
                sub.setFinished(false);
        return new ResponseEntity<>(subjectDTOList, HttpStatus.OK);
    }*/

}
