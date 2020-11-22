package sbangularjs.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import sbangularjs.model.Student;
import sbangularjs.repository.StudentRepository;

import java.util.ArrayList;
import java.util.List;

@Controller
@PreAuthorize("hasAuthority('DEANERY')")
@AllArgsConstructor(onConstructor = @_(@Autowired))
public class StudentsController {
    private StudentRepository studentRepository;

    @PatchMapping("/findStudents")
    public ResponseEntity<List<Student>> findStudents(@RequestBody Long groupId) {
        List<Student> students = studentRepository.findByGroupId(groupId);
        if (students.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @PatchMapping("/saveExpelledStudents")
    public ResponseEntity saveExpelledStudents(@RequestBody List<Student> students) {
        // TODO студенты, у которых нужно поменять значение expelled, а не все
        List<Student> updatingStudents = new ArrayList<>();
        for(Student curStudent: students) {
            Student student = studentRepository.findByNumberRecordBook(curStudent.getNumberRecordBook());
            student.setExpelled(curStudent.getExpelled());
            updatingStudents.add(student);
        }
        studentRepository.saveAll(updatingStudents);
        return new ResponseEntity(HttpStatus.OK);
    }

}
