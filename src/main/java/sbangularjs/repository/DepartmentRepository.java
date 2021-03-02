package sbangularjs.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import sbangularjs.model.Department;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    List<Department> findAll();
    Department findDepartmentById(Long departmentId);

    List<Department> findByFacultyId(Long facultyId, Sort sort);
}
