package sbangularjs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sbangularjs.model.SyllabusContent;

import java.util.List;
import java.util.Set;

public interface SyllabusContentRepository extends JpaRepository<SyllabusContent, Long> {
    List<SyllabusContent> findAll();

    @Query(value = "select sc.semesterNumber from SyllabusContent sc" +
                   " where sc.syllabus.id = :syllabusId order by sc.semesterNumber")
    Set<Integer> findSemesterNumbersBySyllabusId(Long syllabusId);
}
