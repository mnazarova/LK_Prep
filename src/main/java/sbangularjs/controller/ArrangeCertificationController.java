package sbangularjs.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import sbangularjs.model.Attestation;
import sbangularjs.repository.AttestationRepository;

@Controller
@AllArgsConstructor(onConstructor = @_(@Autowired))
public class ArrangeCertificationController {
    private AttestationRepository attestationRepository;

    @PatchMapping("/createCertification")
    public ResponseEntity createCertification(@RequestBody Attestation attestation) {
        try {
            attestation.setActive(true);
            attestationRepository.save(attestation);
            return new ResponseEntity(HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("new Attestation don't created");
        }
    }

}