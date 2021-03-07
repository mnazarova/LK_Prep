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
import sbangularjs.repository.AttestationContentRepository;
import sbangularjs.repository.CertificationAttestationRepository;
import sbangularjs.repository.DeaneryRepository;
import sbangularjs.repository.GroupRepository;

import java.util.List;

@Controller
@PreAuthorize("hasAuthority('DEANERY')")
@AllArgsConstructor(onConstructor = @_(@Autowired))
public class StateController {
    private DeaneryRepository deaneryRepository;
    private GroupRepository groupRepository;
    private CertificationAttestationRepository certificationAttestationRepository;
    private AttestationContentRepository attestationContentRepository;

    @PatchMapping("/getContentAttestationForDeanery")
    public ResponseEntity getContentAttestationForDeanery(@AuthenticationPrincipal User user,
                                                                          @RequestParam Long attestationId, @RequestParam Long id) {
        Deanery curDeanery = deaneryRepository.findByUsername(user.getUsername());
        if (curDeanery == null) return new ResponseEntity<>(0, HttpStatus.CONFLICT);

        CertificationAttestation certificationAttestation = certificationAttestationRepository.findCertificationAttestationById(id);
        if (certificationAttestation == null) return new ResponseEntity<>(1, HttpStatus.CONFLICT);

        if (!certificationAttestation.getAttestation().getId().equals(attestationId)) return new ResponseEntity<>(2, HttpStatus.CONFLICT);

        // если представитель деканата перешёл на страницу группы, не принадлежащей ему, нельзя будет посмотреть данные по аттестации!
        Group group = groupRepository.findByDeaneryIdAndId(curDeanery.getId(), certificationAttestation.getGroup().getId());
        if (group == null) return new ResponseEntity<>(1, HttpStatus.CONFLICT);

        List<AttestationContent> attestationContents = attestationContentRepository.findAllByCertificationAttestationId(id);
        if (attestationContents.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // You many decide to return HttpStatus.NOT_FOUND
        return new ResponseEntity<>(attestationContents, HttpStatus.OK);
    }

}
