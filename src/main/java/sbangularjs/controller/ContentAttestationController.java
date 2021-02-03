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
import org.springframework.web.bind.annotation.RequestParam;
import sbangularjs.model.AttestationContent;
import sbangularjs.model.CertificationAttestation;
import sbangularjs.model.Teacher;
import sbangularjs.model.User;
import sbangularjs.repository.AttestationContentRepository;
import sbangularjs.repository.CertificationAttestationRepository;
import sbangularjs.repository.TeacherRepository;

import java.util.Date;
import java.util.List;

@Controller
@PreAuthorize("hasAuthority('TEACHER')")
@AllArgsConstructor(onConstructor = @_(@Autowired))
public class ContentAttestationController {
    private AttestationContentRepository attestationContentRepository;
    private CertificationAttestationRepository certificationAttestationRepository;
    private TeacherRepository teacherRepository;

    @PatchMapping("/getContentAttestation")
    public ResponseEntity<List<AttestationContent>> getContentAttestation(@AuthenticationPrincipal User user,
                                                                          @RequestParam Long id,
                                                                          @RequestParam Long attestationId) {
        CertificationAttestation certificationAttestation = certificationAttestationRepository.findCertificationAttestationById(id);
        Teacher teacher = teacherRepository.findByUsername(user.getUsername());
        if (certificationAttestation == null || teacher == null || !certificationAttestation.getAttestation().getId().equals(attestationId))
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        /*if (certificationAttestation == null || !certificationAttestation.getTeacher().getUsername().equals(user.getUsername())
        || !certificationAttestation.getAttestation().getId().equals(attestationId))
            return new ResponseEntity<>(HttpStatus.CONFLICT);*/

        List<AttestationContent> attestationContents = attestationContentRepository.findAllByCertificationAttestationIdAndTeacherId(id, teacher.getId());
        if (attestationContents.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // You many decide to return HttpStatus.NOT_FOUND
        return new ResponseEntity<>(attestationContents, HttpStatus.OK);
    }

    @PatchMapping("/saveContentAttestation")
    public ResponseEntity saveContentAttestation(@RequestBody List<AttestationContent> attestationContents) {
        if(attestationContents == null)
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        for (AttestationContent attestationContent: attestationContents) {
            AttestationContent curOld = attestationContentRepository.findAttestationContentById(attestationContent.getId());
//            System.out.println(attestationContent.getAttest());
//            System.out.println(curOld.getAttest());
            if (attestationContent.getAttest() != curOld.getAttest()) {
                attestationContent.setDateAttest(new Date());
            }
//            System.out.println(attestationContent.getWorks());
//            System.out.println(curOld.getWorks());
            if (attestationContent.getWorks() != curOld.getWorks()) {
                attestationContent.setDateWorks(new Date());
            }
            attestationContent.setCertificationAttestation(curOld.getCertificationAttestation());
        }
        try {
            attestationContentRepository.saveAll(attestationContents);
            return new ResponseEntity(HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ContentAttestation didn't saved");
        }
/*        List<Student> updatingStudents = new ArrayList<>();
        for(Student curStudent: students) {
            Student student = studentRepository.findByNumberRecordBook(curStudent.getNumberRecordBook());
            student.setExpelled(curStudent.getExpelled());
            updatingStudents.add(student);
        }
        studentRepository.saveAll(updatingStudents);
        return new ResponseEntity(HttpStatus.OK);*/
    }

}
