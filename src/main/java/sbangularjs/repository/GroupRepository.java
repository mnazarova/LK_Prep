package sbangularjs.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sbangularjs.model.Group;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findAll();
    Group findGroupById(long id);

    /* SECRETARY */
    List<Group> getGroupListByActiveIsTrueAndCurSemesterIsNotNull();

    @Query(value = "select gr from Group gr " +
            "where gr.deanery.faculty.id = :facultyId and gr.active = true and gr.curSemester IS NOT NULL")
    List<Group> findGroupsByActiveIsTrueAndFacultyIdAndCurSemesterNotNull(Long facultyId);

    /* DEANERY */
    @Query(value = "select gr.id from Group gr where gr.deanery.id = :deaneryId")
    List<Long> findGroupIdsByDeaneryId(Long deaneryId);
    List<Group> findGroupsByDeaneryId(Long deaneryId, Sort sort);
    Group findByDeaneryIdAndId(Long deaneryId, Long groupId); // check

    @Query(value = "select gr from Group gr " +
            "where gr.deanery.id = :deaneryId and gr.active = true and gr.syllabus.department.id = :departmentId")
    List<Group> findGroupsByDeaneryIdAndDepartmentIdAndActiveIsTrue(Long deaneryId, Long departmentId, Sort sort);
    List<Group> findGroupsByDeputyDeanId(Long deputyDeanId); // check

}
