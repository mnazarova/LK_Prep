package sbangularjs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sbangularjs.model.Syllabus;

import java.util.List;
import java.util.Set;

public interface SyllabusRepository extends JpaRepository<Syllabus, Long> {

    Syllabus findSyllabusById(Long syllabusId);
    List<Syllabus> findAll();
    List<Syllabus> findByDepartmentId(Long departmentId);

    @Query("SELECT s FROM Syllabus s JOIN Group gr ON gr.syllabus.id = s.id AND gr.deanery.id = :deaneryId ORDER BY gr.active DESC, s.year DESC, s.id")
    Set<Syllabus> findSyllabusesByDeaneryId(Long deaneryId/*, Sort sort*/);

    @Query("SELECT s FROM Syllabus s JOIN Group gr ON gr.syllabus.id = s.id AND gr.deputyDean.id = :deputyDeanId ORDER BY gr.active DESC, s.year DESC, s.id")
    Set<Syllabus> findSyllabusesByDeputyDeanId(Long deputyDeanId);

}
