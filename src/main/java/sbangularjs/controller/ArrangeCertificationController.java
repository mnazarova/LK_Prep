package sbangularjs.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import sbangularjs.model.Attestation;
import sbangularjs.model.Deanery;
import sbangularjs.model.User;
import sbangularjs.repository.AttestationRepository;
import sbangularjs.repository.DeaneryRepository;

@Controller
@PreAuthorize("hasAuthority('DEANERY')")
@AllArgsConstructor(onConstructor = @_(@Autowired))
public class ArrangeCertificationController {
    private AttestationRepository attestationRepository;
    private DeaneryRepository deaneryRepository;

    @PatchMapping("/createCertification")
    public ResponseEntity createCertification(@RequestBody Attestation attestation, @AuthenticationPrincipal User user) {
        try { // только dekanat_ может создавать ведомости
            Deanery deanery = deaneryRepository.findByUsername(user.getUsername());
            if (deanery == null || deanery.getDeaneryGroupList() == null || deanery.getDeaneryGroupList().size() == 0 || deanery.isDeputyDean() != false) // null or true
                return new ResponseEntity(HttpStatus.CONFLICT);
            attestation.setFaculty(deanery.getDeaneryGroupList().get(0).getGroup().getSyllabus().getDepartment().getFaculty());
            attestation.setActive(true);
            attestationRepository.save(attestation);
            return new ResponseEntity(HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("new Attestation don't created");
        }
    }

}
