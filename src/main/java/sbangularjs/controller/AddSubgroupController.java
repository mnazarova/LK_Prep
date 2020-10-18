package sbangularjs.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import sbangularjs.model.Student;
import sbangularjs.model.StudentSubgroup;
import sbangularjs.model.Subgroup;
import sbangularjs.repository.StudentSubgroupRepository;
import sbangularjs.repository.SubgroupRepository;

import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor(onConstructor = @_(@Autowired))
public class AddSubgroupController {
    private SubgroupRepository subgroupRepository;
    private StudentSubgroupRepository studentSubgroupRepository;

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
