package sbangularjs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sbangularjs.model.Teacher;

import java.util.List;
import java.util.Set;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    List<Teacher> findAll();
    Teacher findTeacherById(Long teacherId);

    @Query("select t from Teacher t " +
           "join PlaceWork pw on pw.teacher.id = t.id and pw.department.id = :departmentId")
    Set<Teacher> findByDepartmentId(Long departmentId);

    Teacher findByUsername(String username);
}
