package sbangularjs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sbangularjs.model.StudentSubgroup;

import java.util.List;

public interface StudentSubgroupRepository extends JpaRepository<StudentSubgroup, Long> {
    List<StudentSubgroup> findByStudentId(Long studentId);

    @Query("select ss.student.id from StudentSubgroup ss where ss.subgroup.id = :subgroupId")
    List<Long> findStudentsIdBySubgroupId(Long subgroupId);

    List<StudentSubgroup> findBySubgroupId(Long subgroupId);
}
