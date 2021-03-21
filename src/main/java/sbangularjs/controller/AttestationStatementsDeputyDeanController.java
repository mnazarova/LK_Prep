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
import sbangularjs.model.DeputyDean;
import sbangularjs.model.Group;
import sbangularjs.model.User;
import sbangularjs.repository.AttestationRepository;
import sbangularjs.repository.CertificationAttestationRepository;
import sbangularjs.repository.DeputyDeanRepository;
import sbangularjs.repository.GroupRepository;

import java.util.List;

@Controller
@PreAuthorize("hasAuthority('DEPUTY_DEAN')")
@AllArgsConstructor(onConstructor = @_(@Autowired))
public class AttestationStatementsDeputyDeanController {
    private DeputyDeanRepository deputyDeanRepository;
    private AttestationRepository attestationRepository;
    private GroupRepository groupRepository;
    private CertificationAttestationRepository certificationAttestationRepository;

    @PatchMapping("/getGroupsByAttestationAndByDeputyDeanAndByGroupId")
    public ResponseEntity getGroupsByAttestationAndByDeputyDeanAndByGroupId(@AuthenticationPrincipal User user,
                                @RequestParam Long id, @RequestParam List<Long> groupIds) { // groupId == null - все группы, иначе одна выбранная пользователем группа
        DeputyDean deputyDean = deputyDeanRepository.findByUsername(user.getUsername());
        Attestation attestation = attestationRepository.findAttestationById(id);
        if (deputyDean == null) return new ResponseEntity<>(0, HttpStatus.CONFLICT);

        if (groupIds.size() == 0)
            groupIds = groupRepository.findGroupIdsByDeputyDeanId(deputyDean.getId());

        /*Group group = groupRepository.findGroupById(groupIds.get(0));
        if (attestation == null || attestation.getFaculty() == null || group == null || group.getDeanery() == null ||
                group.getDeanery().getFaculty() == null || !group.getDeanery().getFaculty().getId().equals(attestation.getFaculty().getId()))
            return new ResponseEntity<>(1, HttpStatus.CONFLICT);*/

        List<Long> certificationAttestationIds = certificationAttestationRepository.findAllByAttestationIdAndGroupIds(id, groupIds);
        if(certificationAttestationIds.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        List<SubjectDTO> subList = certificationAttestationRepository.findAllSubjectsGroupDTOByCAIds(certificationAttestationIds);
        if (subList.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(subList, HttpStatus.OK);
    }
}
