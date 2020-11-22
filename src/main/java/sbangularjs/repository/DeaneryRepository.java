package sbangularjs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sbangularjs.model.Deanery;

import java.util.List;

public interface DeaneryRepository extends JpaRepository<Deanery, Long> {
    List<Deanery> findAll();
    Deanery findDeaneryById(Long deaneryId);

    /*@Query("select t from Teacher t " +
           "join PlaceWork pw on pw.teacher.id = t.id and pw.department.id = :departmentId")
    Set<Teacher> findByDepartmentId(Long departmentId);*/

    Deanery findByUsername(String username);
}
