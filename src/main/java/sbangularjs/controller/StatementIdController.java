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
import sbangularjs.model.Deanery;
import sbangularjs.model.Group;
import sbangularjs.model.User;
import sbangularjs.repository.CertificationAttestationRepository;
import sbangularjs.repository.DeaneryRepository;
import sbangularjs.repository.GroupRepository;

import java.util.List;

@Controller
@PreAuthorize("hasAuthority('DEANERY')")
@AllArgsConstructor(onConstructor = @_(@Autowired))
public class StatementIdController {
    private DeaneryRepository deaneryRepository;
    private GroupRepository groupRepository;
    private CertificationAttestationRepository certificationAttestationRepository;

    @PatchMapping("/getGroupListByDeanery")
    public ResponseEntity<List<Group>> getGroupListByDeanery(@AuthenticationPrincipal User user) {
        Deanery curDeanery = deaneryRepository.findByUsername(user.getUsername());
        if (curDeanery == null)
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        List<Group> groupList = groupRepository.findGroupsByDeaneryId(curDeanery.getId());
        if (groupList.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(groupList, HttpStatus.OK);
    }

    /*@PatchMapping("/getSubroupListByDeanery")
    public ResponseEntity<List<Subgroup>> getSubroupListByDeanery(@AuthenticationPrincipal User user) {
        // УБРАТЬ!
        Deanery curDeanery = deaneryRepository.findByUsername(user.getUsername());
        if (curDeanery != null) {
            List<Subgroup> subgroupList = subgroupRepository.findSubgroupsByDeaneryId(curDeanery.getId());
            if (subgroupList.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(subgroupList, HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.CONFLICT);
    }*/

    @PatchMapping("/getGroupsByAttestationAndByDeanery")
    public ResponseEntity<List<SubjectDTO>> getGroupsByAttestationAndByDeanery(@AuthenticationPrincipal User user,
                                                                               @RequestParam Long id) {
        Deanery curDeanery = deaneryRepository.findByUsername(user.getUsername());
        if (curDeanery == null) return new ResponseEntity<>(HttpStatus.CONFLICT);

        List<Long> groupIds = groupRepository.findGroupIdsByDeaneryId(curDeanery.getId());

        List<Long> certificationAttestationIds = certificationAttestationRepository.findAllByAttestationIdAndGroupIds(id, groupIds);
        if(certificationAttestationIds.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        List<SubjectDTO> subList = certificationAttestationRepository.findAllSubjectsGroupDTOByCAIds(certificationAttestationIds);
        if (subList.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(subList, HttpStatus.OK);
    }

    /*@PatchMapping("/getSubgroupsByAttestationAndByDeanery")
    public ResponseEntity<List<SubjectDTO>> getSubgroupsByAttestationAndByDeanery(
            @AuthenticationPrincipal User user,
            @RequestParam(value = "id") Long attestationId) {
        // УБРАТЬ!
        Deanery curDeanery = deaneryRepository.findByUsername(user.getUsername());
        if (curDeanery == null)
            return new ResponseEntity<>(HttpStatus.CONFLICT);

        List<Long> subgroupIds = subgroupRepository.findSubgroupIdsByDeaneryId(curDeanery.getId());

        List<Long> certificationAttestationIds = certificationAttestationRepository.findAllByAttestationIdAndSubgroupIds(attestationId, subgroupIds);
        if(certificationAttestationIds == null) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        List<SubjectDTO> subList = certificationAttestationRepository.findAllSubjectsSubgroupDTO(certificationAttestationIds);
        if (subList == null) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(subList, HttpStatus.OK);
    }*/

    @PatchMapping("/selectedOnGroupList")
    public ResponseEntity<List<SubjectDTO>> selectedOnGroupList(@AuthenticationPrincipal User user,
                                                                @RequestParam Long id, @RequestParam List<Long> groupId) {
        Deanery curDeanery = deaneryRepository.findByUsername(user.getUsername());
        if (curDeanery == null) return new ResponseEntity<>(HttpStatus.CONFLICT);

        List<Long> certificationAttestationIds = certificationAttestationRepository.findAllByAttestationIdAndGroupIds(id, groupId);
        if(certificationAttestationIds.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        List<SubjectDTO> subList = certificationAttestationRepository.findAllSubjectsGroupDTOByCAIds(certificationAttestationIds);
        if (subList.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(subList, HttpStatus.OK);
    }

    /*@PatchMapping("/selectedOnSubgroupList")
    УБРАТЬ!
    public ResponseEntity<List<SubjectDTO>> selectedOnSubgroupList(@AuthenticationPrincipal User user,
                                                                   @RequestParam(value = "id") Long id, @RequestParam List<Long> subgroupId) {
        Deanery curDeanery = deaneryRepository.findByUsername(user.getUsername());
        if (curDeanery == null)
            return new ResponseEntity<>(HttpStatus.CONFLICT);

        List<Long> certificationAttestationIds = certificationAttestationRepository.findAllByAttestationIdAndSubgroupIds(id, subgroupId);
        if(certificationAttestationIds == null) return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        List<SubjectDTO> subList = certificationAttestationRepository.findAllSubjectsSubgroupDTO(certificationAttestationIds);
        if (subList == null) return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(subList, HttpStatus.OK);
    }*/
}
