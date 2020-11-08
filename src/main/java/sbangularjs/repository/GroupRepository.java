package sbangularjs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sbangularjs.model.Group;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findAll();
    Group findGroupById(long id);

    @Query(value = "select gr from Group gr" +
            " join Syllabus s on s.id = gr.syllabus.id and s.department.id = :departmentId")
    List<Group> findGroupsByDepartmentId(Long departmentId);
}
