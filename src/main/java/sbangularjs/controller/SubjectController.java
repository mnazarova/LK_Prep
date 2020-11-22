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
import sbangularjs.model.Teacher;
import sbangularjs.model.User;
import sbangularjs.repository.AttestationContentRepository;
import sbangularjs.repository.CertificationAttestationRepository;
import sbangularjs.repository.TeacherRepository;

import java.util.List;

@Controller
@PreAuthorize("hasAuthority('TEACHER')")
@AllArgsConstructor(onConstructor = @_(@Autowired))
public class SubjectController {
    private TeacherRepository teacherRepository;
    private CertificationAttestationRepository certificationAttestationRepository;
    private AttestationContentRepository attestationContentRepository;

    @PatchMapping("/getGroupsByAttestationAndByTeacher")
    public ResponseEntity<List<SubjectDTO>> getGroupsByAttestationAndByTeacher(@AuthenticationPrincipal User user,
                                                                               @RequestParam(value = "id") Long id) {
        Teacher curTeacher = teacherRepository.findByUsername(user.getUsername());
        List<Long> certificationAttestationIds = certificationAttestationRepository
                .findAllByAttestationIdAndTeacherIdAndIsSubgroup(id, curTeacher.getId(), false);
        /*List<Long> certificationAttestationIds = new ArrayList<>();
        for (CertificationAttestation ca : certificationAttestations){
            certificationAttestationIds.add(ca.getId());
        }*/
        if(certificationAttestationIds == null)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        List<SubjectDTO> subList = certificationAttestationRepository.findAllSubjectsGroupDTO(certificationAttestationIds); // далее заполнить дату
        if (subList.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // You many decide to return HttpStatus.NOT_FOUND
        setFinished(subList);
        return new ResponseEntity<>(subList, HttpStatus.OK);
    }

    private void setFinished(List<SubjectDTO> subList) {
        for (SubjectDTO sub: subList)
            if (attestationContentRepository.findUnfinishedByCertificationAttestationId(sub.getCertificationAttestationId()) == 0) // нет null полей
                sub.setFinished(false);
            else
                sub.setFinished(true);
    }

    @PatchMapping("/getSubgroupsByAttestationAndByTeacher")
    public ResponseEntity<List<SubjectDTO>> getSubgroupsByAttestationAndByTeacher(
            @AuthenticationPrincipal User user,
            @RequestParam(value = "id") Long attestationId) {
        Teacher curTeacher = teacherRepository.findByUsername(user.getUsername());
//        List<CertificationAttestation> certificationAttestations = certificationAttestationRepository
//                .findAllByAttestationIdAndTeacherIdAndIsSubgroup(attestationId, curTeacher.getId(), true);
        List<Long> certificationAttestationIds = certificationAttestationRepository
                .findAllByAttestationIdAndTeacherIdAndIsSubgroup(attestationId, curTeacher.getId(), true);
        if(certificationAttestationIds.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        List<SubjectDTO> subList = certificationAttestationRepository.findAllSubjectsSubgroupDTO(certificationAttestationIds);
        if (subList.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // You many decide to return HttpStatus.NOT_FOUND
        setFinished(subList);
        return new ResponseEntity<>(subList, HttpStatus.OK);
    }

}
