package sbangularjs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sbangularjs.model.Student;
import sbangularjs.model.Teacher;

import java.util.List;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
//    Student findByNumberRecordBook(String username);
//    List<Student> findByGroupId(Long groupId);
    List<Teacher> findAll();


//    @Query("select s from Student s where s.id in :studentIds")
//    List<Student> findByIds(List<Long> studentIds);
    Teacher findByUsername(String username);
}
