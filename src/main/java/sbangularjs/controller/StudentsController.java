package sbangularjs.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import sbangularjs.model.Deanery;
import sbangularjs.model.Group;
import sbangularjs.model.Student;
import sbangularjs.model.User;
import sbangularjs.repository.DeaneryRepository;
import sbangularjs.repository.GroupRepository;
import sbangularjs.repository.StudentRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Controller
@PreAuthorize("hasAuthority('DEANERY')")
@AllArgsConstructor(onConstructor = @_(@Autowired))
public class StudentsController {
    private DeaneryRepository deaneryRepository;
    private GroupRepository groupRepository;
    private StudentRepository studentRepository;

    @PatchMapping("/getStudents")
    public ResponseEntity getStudents(@AuthenticationPrincipal User user, @RequestParam Long groupId) {
        Deanery curDeanery = deaneryRepository.findByUsername(user.getUsername());
        if (curDeanery == null || curDeanery.getId() == null) return new ResponseEntity<>(0, HttpStatus.CONFLICT);
        Group group = groupRepository.findByDeaneryIdAndId(curDeanery.getId(), groupId);
        if (group == null) return new ResponseEntity<>(1, HttpStatus.CONFLICT);

        List<Student> students = studentRepository.findByGroupId(groupId);
        if (students.isEmpty()) return new ResponseEntity(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @Transactional
    @PatchMapping("/saveExpelledStudents")
    public ResponseEntity saveExpelledStudents(@RequestParam Long groupId, @RequestBody List<Student> students) {
        List<Student> updatingStudents = new ArrayList<>();
        for(Student curStudent: students) {
            Student student = studentRepository.findStudentById(curStudent.getId());
            if (curStudent.getExpelled() != null && curStudent.getExpelled() != student.getExpelled()) {
                student.setExpelled(curStudent.getExpelled());
                updatingStudents.add(student);
                // проверить все ведомости, актуальные на данный момент
            }
        }
        studentRepository.saveAll(updatingStudents);
        return new ResponseEntity<>(studentRepository.findByGroupId(groupId), HttpStatus.OK);
    }

}
