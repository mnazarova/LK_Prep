package sbangularjs.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import sbangularjs.model.Department;
import sbangularjs.model.Faculty;
import sbangularjs.model.Group;
import sbangularjs.model.Student;
import sbangularjs.repository.DepartmentRepository;
import sbangularjs.repository.FacultyRepository;
import sbangularjs.repository.GroupRepository;
import sbangularjs.repository.StudentRepository;

import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor(onConstructor = @_(@Autowired))
public class StudentsController {
    private FacultyRepository facultyRepository;
    private DepartmentRepository departmentRepository;
    private GroupRepository groupRepository;
    private StudentRepository studentRepository;

    @GetMapping("/getAllFaculties")
    public ResponseEntity<List<Faculty>> getAllFaculties() {
        List<Faculty> faculties = facultyRepository.findAll();
        if (faculties.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // You many decide to return HttpStatus.NOT_FOUND
        return new ResponseEntity<>(faculties, HttpStatus.OK);
    }

    @PatchMapping("/getDepartments")
    public ResponseEntity<List<Department>> getDepartments(@RequestBody Long facultyId) {
        List<Department> departments = departmentRepository.findByFacultyId(facultyId);
        /*for(Department fk:departments) {
            fk.setFaculty(null);
            fk.setSyllabuses(null);
        }*/
        if (departments.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(departments, HttpStatus.OK);
    }

    @PatchMapping("/getGroups")
    public ResponseEntity<List<Group>> getGroups(@RequestBody Long departmentId) {
        List<Group> groups = groupRepository.findGroupsByDepartmentId(departmentId);
        return new ResponseEntity<>(groups, HttpStatus.OK);
    }


    @PatchMapping("/findStudents")
    public ResponseEntity<List<Student>> findStudents(@RequestBody Long groupId) {
        List<Student> students = studentRepository.findByGroupId(groupId);
        if (students.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @PatchMapping("/findNotExpelledStudents")
    public ResponseEntity<List<Student>> findNotExpelledStudents(@RequestBody Long groupId) {
        List<Student> students = studentRepository.findByGroupId(groupId);
        if (students.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        for(int i=students.size()-1;i>=0;i--) {
            if(students.get(i).getExpelled() != null && students.get(i).getExpelled()) // Expelled = TRUE, отчислены только тогда, когда true
                students.remove(students.get(i));
        }
        if (students.isEmpty()) // все студенты отчислены (переведены)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // You many decide to return HttpStatus.NOT_FOUND
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @PatchMapping("/saveExpelledStudents")
    public ResponseEntity saveExpelledStudents(@RequestBody List<Student> students) {
        // студенты, у которых нужно поменять значение expelled
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
