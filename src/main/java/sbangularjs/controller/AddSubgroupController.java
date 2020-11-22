package sbangularjs.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import sbangularjs.model.*;
import sbangularjs.repository.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@PreAuthorize("hasAuthority('SECRETARY')")
@AllArgsConstructor(onConstructor = @_(@Autowired))
public class AddSubgroupController {
    private FacultyRepository facultyRepository;
    private DepartmentRepository departmentRepository;

    private SubgroupRepository subgroupRepository;
    private GroupRepository groupRepository;
    private StudentSubgroupRepository studentSubgroupRepository;

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
        if (departments.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(departments, HttpStatus.OK);
    }

    @PatchMapping("/getGroups")
    public ResponseEntity<List<Group>> getGroups(@RequestBody Long departmentId) {
        List<Group> groups = groupRepository.findGroupsByDepartmentId(departmentId);
        return new ResponseEntity<>(groups, HttpStatus.OK);
    }

    @PatchMapping("/createSubgroup")
    public ResponseEntity createSubgroup(@RequestBody Subgroup subgroup) {
        try {
            List<Student> studentList = subgroup.getStudents();
            subgroup.setActive(true);
            subgroupRepository.save(subgroup);
            List<StudentSubgroup> newStudentSubgroupList = new ArrayList<>();
            for (Student student: studentList)
                newStudentSubgroupList.add(new StudentSubgroup(student, subgroup));
            studentSubgroupRepository.saveAll(newStudentSubgroupList);
            return new ResponseEntity(HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("new Subgroup don't created");
        }
    }

}
