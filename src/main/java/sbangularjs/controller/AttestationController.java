package sbangularjs.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import sbangularjs.model.Attestation;
import sbangularjs.repository.AttestationRepository;

import java.util.Date;
import java.util.List;

@Controller
@PreAuthorize("hasAnyAuthority('TEACHER')")
@AllArgsConstructor(onConstructor = @_(@Autowired))
public class AttestationController {
    private AttestationRepository attestationRepository;

    @GetMapping("/getAttestation")
    public ResponseEntity<List<Attestation>> getAttestation() {
        List<Attestation> attestations = attestationRepository.findAllWithDeadlineDateTimeAfter(new Date());
        if (attestations.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // You many decide to return HttpStatus.NOT_FOUND
        return new ResponseEntity<>(attestations, HttpStatus.OK);
    }

}
