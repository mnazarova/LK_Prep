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
import sbangularjs.model.DeputyDean;
import sbangularjs.model.User;
import sbangularjs.repository.AttestationRepository;
import sbangularjs.repository.DeputyDeanRepository;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Controller
@PreAuthorize("hasAuthority('DEPUTY_DEAN')")
@AllArgsConstructor(onConstructor = @_(@Autowired))
public class ViewAttestationsDeputyDeanController {
    private AttestationRepository attestationRepository;
    private DeputyDeanRepository deputyDeanRepository;

    @GetMapping("/getActiveAttestationByFacultyIdForDeputyDean")
    public ResponseEntity getActiveAttestationByFacultyIdForDeputyDean(@AuthenticationPrincipal User user) {
        DeputyDean deputyDean = deputyDeanRepository.findByUsername(user.getUsername());
        if (deputyDean == null) return new ResponseEntity(HttpStatus.CONFLICT);

        Set<Attestation> attestations = attestationRepository.findAllAttestationsByDeputyDeanId(deputyDean.getId()/*, Sort.by(Sort.Direction.DESC, "deadline")*/);
        if (attestations.isEmpty()) return new ResponseEntity(HttpStatus.NO_CONTENT); // You many decide to return HttpStatus.NOT_FOUND

        /* set active = false*/
        List<Attestation> overdueAttestations = attestationRepository.findAllOverdueAttestationsWithActiveTrue(new Date());
        for (Attestation overdueAttestation: overdueAttestations)
            overdueAttestation.setActive(false);
        attestationRepository.saveAll(overdueAttestations);

        return new ResponseEntity<>(attestations, HttpStatus.OK);
    }

}
