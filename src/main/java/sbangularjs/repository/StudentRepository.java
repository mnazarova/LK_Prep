package sbangularjs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sbangularjs.model.Student;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findByNumberRecordBook(String username);
    List<Student> findByGroupId(Long groupId);

    @Query("select s from Student s where s.id in :studentIds")
    List<Student> findByIds(List<Long> studentIds);
    /*List<Student> findAllBySurnameContainingIgnoreCaseAndEmailContainingIgnoreCase(String surname, String email);
    List<Student> findBySurnameContainingIgnoreCase(String surname);
    List<Student> findByEmailContainingIgnoreCase(String email);*/
}
