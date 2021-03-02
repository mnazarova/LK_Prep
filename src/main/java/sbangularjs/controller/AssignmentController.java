package sbangularjs.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import sbangularjs.model.*;
import sbangularjs.repository.DeaneryRepository;
import sbangularjs.repository.DepartmentRepository;
import sbangularjs.repository.TeacherRepository;

import javax.transaction.Transactional;
import java.util.List;

@Controller
@PreAuthorize("hasAuthority('DEANERY')")
@AllArgsConstructor(onConstructor = @_(@Autowired))
public class AssignmentController {

    private DeaneryRepository deaneryRepository;
    private DepartmentRepository departmentRepository;
    private TeacherRepository teacherRepository;

    @PatchMapping("/getDepartmentsByFacultyId")
    public ResponseEntity getDepartmentsByFacultyId(@AuthenticationPrincipal User user) {
        Deanery deanery = deaneryRepository.findByUsername(user.getUsername());
        if (deanery == null) return new ResponseEntity<>(HttpStatus.CONFLICT);

        List<Department> departments = departmentRepository.findByFacultyId(deanery.getFaculty().getId(), Sort.by(Sort.Direction.ASC, "shortName"));
        if (departments.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // You many decide to return HttpStatus.NOT_FOUND
        return new ResponseEntity<>(departments, HttpStatus.OK);
    }

    @PatchMapping("/getTeachersByDepartmentId")
    public ResponseEntity getTeachersByDepartmentId(@RequestParam Long departmentId) {
        List<Teacher> teacherList = teacherRepository.findByDepartmentIdAndActiveIsTrue(departmentId);
        if (teacherList == null)
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        return new ResponseEntity<>(teacherList, HttpStatus.OK);
    }

    @Transactional
    @PatchMapping("/saveHeadDepartment")
    public ResponseEntity saveHeadDepartment(@RequestBody Department updatingDepartment) {
        if (updatingDepartment == null || updatingDepartment.getId() == null) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        Teacher oldHeadDepartment = departmentRepository.findDepartmentById(updatingDepartment.getId()).getHeadDepartment();
        if (oldHeadDepartment != null) {
            oldHeadDepartment.setDepartment(null);
            teacherRepository.save(oldHeadDepartment);
        }

        Department department = departmentRepository.findDepartmentById(updatingDepartment.getId());
        if (department == null || updatingDepartment.getHeadDepartment() == null ||
                updatingDepartment.getHeadDepartment().getId() == null)
            return new ResponseEntity<>(department, HttpStatus.NO_CONTENT); // только отвяжется старый зав.каф.

        Teacher newHeadDepartment = teacherRepository.findTeacherById(updatingDepartment.getHeadDepartment().getId());
        if (newHeadDepartment != null) {
            newHeadDepartment.setDepartment(department);
            department.setHeadDepartment(newHeadDepartment);
            teacherRepository.save(newHeadDepartment);
        }

//        Department department2 = departmentRepository.findDepartmentById(updatingDepartment.getId());
        return new ResponseEntity<>(department, HttpStatus.OK);
    }


}
