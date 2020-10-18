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
import sbangularjs.model.StudentSubgroup;
import sbangularjs.model.Subgroup;
import sbangularjs.repository.StudentRepository;
import sbangularjs.repository.StudentSubgroupRepository;
import sbangularjs.repository.SubgroupRepository;

import java.util.List;

@Controller
@PreAuthorize("hasAuthority('SECRETARY')")
@AllArgsConstructor(onConstructor = @_(@Autowired))
public class LinkController {
    private SubgroupRepository subgroupRepository;
    private StudentSubgroupRepository studentSubgroupRepository;
    private StudentRepository studentRepository;

    @PatchMapping("/findByNameSubgroup")
    public ResponseEntity findByNameSubgroup(@RequestBody Subgroup subgroup) {
        List<Subgroup> subgroups = subgroupRepository.findByNameContainingIgnoreCase(subgroup.getName());
        if (subgroups.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // You many decide to return HttpStatus.NOT_FOUND
        return new ResponseEntity<>(subgroups, HttpStatus.OK);
    }

    @PatchMapping("/findNotExpelledStudentsFromSubgroup")
    public ResponseEntity findNotExpelledStudentsFromSubgroup(@RequestBody Subgroup subgroup) {
        List<Long> studentsIdFromSubgroup = studentSubgroupRepository.findStudentsIdBySubgroupId(subgroup.getId()); // ids
        List<Student> students = studentRepository.findByIds(studentsIdFromSubgroup);
        if (students.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        for (int i=students.size()-1; i>=0; i--)
            if (students.get(i).getExpelled() != null && students.get(i).getExpelled()) // Expelled = TRUE, отчислены только тогда, когда true
                students.remove(students.get(i));
        if (students.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        else return new ResponseEntity<>(students, HttpStatus.OK);
    }

}
