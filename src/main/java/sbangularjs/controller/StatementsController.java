package sbangularjs.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import sbangularjs.model.Attestation;
import sbangularjs.model.Deanery;
import sbangularjs.model.User;
import sbangularjs.repository.AttestationRepository;
import sbangularjs.repository.DeaneryRepository;

import java.util.Date;
import java.util.List;

@Controller
@PreAuthorize("hasAuthority('DEANERY')")
@AllArgsConstructor(onConstructor = @_(@Autowired))
public class StatementsController {
    private AttestationRepository attestationRepository;
    private DeaneryRepository deaneryRepository;

    @GetMapping("/getActiveAttestationByFacultyId")
    public ResponseEntity<List<Attestation>> getActiveAttestationByFacultyId(@AuthenticationPrincipal User user) {
        Deanery deanery = deaneryRepository.findByUsername(user.getUsername());
        if (deanery == null) return new ResponseEntity<>(HttpStatus.CONFLICT);

        List<Attestation> attestations = attestationRepository.findByFacultyId(deanery.getFaculty().getId(), Sort.by(Sort.Direction.DESC, "deadline"));
        if (attestations.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT); // You many decide to return HttpStatus.NOT_FOUND

        /* set active = false*/
        List<Attestation> overdueAttestations = attestationRepository.findAllOverdueAttestationsWithActiveTrue(new Date());
        for (Attestation overdueAttestation: overdueAttestations)
            overdueAttestation.setActive(false);
        attestationRepository.saveAll(overdueAttestations);

        return new ResponseEntity<>(attestations, HttpStatus.OK);
    }

}
