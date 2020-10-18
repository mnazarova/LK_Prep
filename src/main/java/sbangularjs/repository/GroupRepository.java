package sbangularjs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sbangularjs.model.Group;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Integer> {
//    @Query(value = "select new sbangularjs.DTO.GroupDto(gr.id, gr.number) from Group gr")
//    List<GroupDto> findAllDTO();
    List<Group> findAll();

    @Query(value = "select gr from Group gr" +
            " join Syllabus s on s.id = gr.syllabus.id and s.department.id = :departmentId\n")
    List<Group> findGroupsByDepartmentId(Long departmentId);
    /*
    select gr.group_id, gr.number
    from groups gr
    join syllabus s on s.syllabus_id = gr.syllabus_id and s.department_id = 3
     */

//    List<Group> findBySyllabus();
}
