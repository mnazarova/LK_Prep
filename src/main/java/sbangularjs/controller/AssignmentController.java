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
import sbangularjs.repository.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Controller
@PreAuthorize("hasAuthority('DEANERY')")
@AllArgsConstructor(onConstructor = @_(@Autowired))
public class AssignmentController {

    private UserRepository userRepository;
    private DeaneryRepository deaneryRepository;
    private DeputyDeanRepository deputyDeanRepository;
    private DepartmentRepository departmentRepository;
    private TeacherRepository teacherRepository;
    private GroupRepository groupRepository;

    /*@PatchMapping("/getDepartmentsByFacultyId")
    public ResponseEntity getDepartmentsByFacultyId(@AuthenticationPrincipal User user) {
        Deanery deanery = deaneryRepository.findByUsername(user.getUsername());
        if (deanery == null || deanery.getId() == null || deanery.getFaculty() == null
                || deanery.getFaculty().getId() == null) return new ResponseEntity<>(HttpStatus.CONFLICT);

        List<Department> departments = departmentRepository.findByFacultyId(deanery.getFaculty().getId(), Sort.by(Sort.Direction.ASC, "shortName"));
        if (departments.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // You many decide to return HttpStatus.NOT_FOUND
        return new ResponseEntity<>(departments, HttpStatus.OK);
    }*/

    @PatchMapping("/getDepartmentsWithGroupsByFacultyId")
    public ResponseEntity getDepartmentsWithGroupsByFacultyId(@AuthenticationPrincipal User user) {
        Deanery deanery = deaneryRepository.findByUsername(user.getUsername());
        if (deanery == null || deanery.getId() == null || deanery.getFaculty() == null
                || deanery.getFaculty().getId() == null) return new ResponseEntity<>(HttpStatus.CONFLICT);

        List<Department> departments = departmentRepository.findByFacultyId(deanery.getFaculty().getId(), Sort.by(Sort.Direction.ASC, "shortName"));
        if (departments.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // You many decide to return HttpStatus.NOT_FOUND
        for (Department department: departments)
            department.setGroupsAssignmentDeputyDean(groupRepository.findGroupsByDeaneryIdAndDepartmentIdAndActiveIsTrue(
                    deanery.getId(), department.getId(), Sort.by(Sort.Direction.ASC, "id")));
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
    public ResponseEntity saveHeadDepartment(@AuthenticationPrincipal User user, @RequestBody Department updatingDepartment) {
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

        department.setGroupsAssignmentDeputyDean(groupRepository.findGroupsByDeaneryIdAndDepartmentIdAndActiveIsTrue(
                deaneryRepository.findByUsername(user.getUsername()).getId(), department.getId(), Sort.by(Sort.Direction.ASC, "id")));

//        Department department2 = departmentRepository.findDepartmentById(updatingDepartment.getId());
        return new ResponseEntity<>(department, HttpStatus.OK);
    }

    @Transactional
    @PatchMapping("/saveDeputyDean")
    public ResponseEntity saveDeputyDean(@AuthenticationPrincipal User user, @RequestBody Department updatingDepartment) {
        if (updatingDepartment == null || updatingDepartment.getId() == null) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        List<DeputyDean> deletingDeputyDeanList = new ArrayList<>();
        for (Group group: updatingDepartment.getGroupsAssignmentDeputyDean()) {
            Group oldGroup = groupRepository.findGroupById(group.getId());
            if (group.getDeputyDean() != null && group.getDeputyDean().getId() != null) {
                if (oldGroup.getDeputyDean() == null) { // у группы ранее не было зам. декана
                    String username = teacherRepository.findTeacherById(group.getDeputyDean().getId()).getUsername();
                    DeputyDean deputyDean = deputyDeanRepository.findByUsername(username);
                    if (deputyDean == null) { // ранее не был зам. декана
                        deputyDean = new DeputyDean();
                        deputyDean.setId(group.getDeputyDean().getId());
                        deputyDean.setActive(true);
                        deputyDean.setUsername(username);
                        deputyDean.setSurname(group.getDeputyDean().getSurname());
                        deputyDean.setName(group.getDeputyDean().getName());
                        deputyDean.setPatronymic(group.getDeputyDean().getPatronymic());
                        deputyDean.setInitials(group.getDeputyDean().getInitials());
                    }

                    oldGroup.setDeputyDean(deputyDean);
                    groupRepository.save(oldGroup);

                    List<Group> groups = deputyDean.getGroups();
                    if (groups == null)
                        deputyDean.setGroups(Collections.singletonList(oldGroup));
                    else
                        deputyDean.getGroups().add(oldGroup);
                    deputyDeanRepository.save(deputyDean);

                    User usr = userRepository.findByUsername(username);
                    if (usr == null) {
                        usr = new User();
                        usr.setId(deputyDean.getId());
                        usr.setActive(true);
                        usr.setUsername(deputyDean.getUsername());
                        usr.setPassword(deputyDean.getUsername());
                        usr.setRoles(Collections.singleton(Role.DEPUTY_DEAN));
                    }
                    else {
                        Set<Role> roles = usr.getRoles();
                        roles.add(Role.DEPUTY_DEAN);
                        usr.setRoles(roles);
                    }
                    userRepository.save(usr);
                }
                else
                    if (oldGroup.getDeputyDean() != null && oldGroup.getDeputyDean().getId() != null
                        && !oldGroup.getDeputyDean().getId().equals(group.getDeputyDean().getId())) { // раньше был назначен зам. декана и его изменили
                        deletingDeputyDeanList.add(oldGroup.getDeputyDean()); // добавляем в список удаляемых зама, который был раньше

                        String username = teacherRepository.findTeacherById(group.getDeputyDean().getId()).getUsername();
                        DeputyDean deputyDean = deputyDeanRepository.findByUsername(username);
                        if (deputyDean == null) { // ранее не был зам. декана
                            deputyDean = new DeputyDean();
                            deputyDean.setId(group.getDeputyDean().getId());
                            deputyDean.setActive(true);
                            deputyDean.setUsername(username);
                            deputyDean.setSurname(group.getDeputyDean().getSurname());
                            deputyDean.setName(group.getDeputyDean().getName());
                            deputyDean.setPatronymic(group.getDeputyDean().getPatronymic());
                            deputyDean.setInitials(group.getDeputyDean().getInitials());
                        }

                        oldGroup.setDeputyDean(deputyDean);
                        groupRepository.save(oldGroup);

                        List<Group> groups = deputyDean.getGroups();
                        if (groups == null)
                            deputyDean.setGroups(Collections.singletonList(oldGroup));
                        else
                            deputyDean.getGroups().add(oldGroup);
                        deputyDeanRepository.save(deputyDean);

                        User usr = userRepository.findByUsername(username);
                        if (usr == null) {
                            usr = new User();
                            usr.setId(deputyDean.getId());
                            usr.setActive(true);
                            usr.setUsername(deputyDean.getUsername());
                            usr.setPassword(deputyDean.getUsername());
                            usr.setRoles(Collections.singleton(Role.DEPUTY_DEAN));
                        }
                        else {
                            Set<Role> roles = usr.getRoles();
                            roles.add(Role.DEPUTY_DEAN);
                            usr.setRoles(roles);
                        }
                        userRepository.save(usr);
                    }

            }

        }

        for (DeputyDean deputyDean: deletingDeputyDeanList) {
            List<Group> groups = groupRepository.findGroupsByDeputyDeanId(deputyDean.getId());
            if (groups.size() == 0) { // удаляем роль зам.декана
                User usr = userRepository.findByUsername(deputyDean.getUsername());
                if (usr != null) {
                    usr.getRoles().remove(Role.DEPUTY_DEAN);
                    userRepository.save(usr);
                    if (usr.getRoles().isEmpty())
                        userRepository.delete(usr);
                }
                deputyDeanRepository.delete(deputyDean);
            }
        }

        Department department = departmentRepository.findDepartmentById(updatingDepartment.getId());
        department.setGroupsAssignmentDeputyDean(groupRepository.findGroupsByDeaneryIdAndDepartmentIdAndActiveIsTrue(
                deaneryRepository.findByUsername(user.getUsername()).getId(), department.getId(), Sort.by(Sort.Direction.ASC, "id")));
        return new ResponseEntity<>(department, HttpStatus.OK);
    }

}
