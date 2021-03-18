package sbangularjs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sbangularjs.model.Student;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findByNumberRecordBook(String username);
    Student findStudentById(Long id);
    List<Student> findByGroupId(Long groupId);

    /*@Query("select s from Student s where s.id in :studentIds")
    List<Student> findByIds(List<Long> studentIds);

    @Query("select distinct g from Group g " +
            " join Student s on s.group.id = g.id and s in :students")
    Set<Group> findGroupsByStudents(List<Student> students);*/
}
