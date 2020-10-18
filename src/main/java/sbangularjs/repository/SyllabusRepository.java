package sbangularjs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sbangularjs.model.Syllabus;

import java.util.List;

public interface SyllabusRepository extends JpaRepository<Syllabus, Long> {
    List<Syllabus> findAll();
    List<Syllabus> findByDepartmentId(Long departmentId);
}
