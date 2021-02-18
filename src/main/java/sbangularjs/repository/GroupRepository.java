package sbangularjs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sbangularjs.model.Group;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findAll();
    Group findGroupById(long id);
    List<Group> getGroupListByActiveIsTrueAndCurSemesterIsNotNull();

    @Query(value = "select gr from Group gr " +
            "where gr.syllabus.department.faculty.id = :facultyId and gr.active = true and gr.curSemester IS NOT NULL")
    List<Group> findGroupsByActiveIsTrueAndFacultyIdAndCurSemesterNotNull(Long facultyId);

    @Query(value = "select gr from Group gr" +
            " join DeaneryGroup DG on DG.group.id = gr.id and DG.deanery.id = :deaneryId")
    List<Group> findGroupsByDeaneryId(Long deaneryId);

    @Query(value = "select gr.id from Group gr join DeaneryGroup DG" +
            " on DG.group.id = gr.id and DG.deanery.id = :deaneryId")
    List<Long> findGroupIdsByDeaneryId(Long deaneryId);
}
