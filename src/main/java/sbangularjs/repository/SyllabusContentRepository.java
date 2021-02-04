package sbangularjs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sbangularjs.model.SyllabusContent;

import java.util.List;
import java.util.Set;

public interface SyllabusContentRepository extends JpaRepository<SyllabusContent, Long> {
    List<SyllabusContent> findAll();

    SyllabusContent findBySyllabusIdAndDisciplineIdAndSemesterNumber(Long syllabusId, Long disciplineId, Integer semesterNumber);

    @Query(value = "select sc from SyllabusContent sc join Discipline d " +
            "on sc.discipline.id = d.id and d.department.id = :departmentId "+// and sc.discipline.department.id = :departmentId")
            "where sc.syllabus.id = :syllabusId and sc.semesterNumber = :curSemester")
    List<SyllabusContent> findSyllabusContentsBySyllabusIdAndDepartmentIdAndCurSemester(Long syllabusId, Long departmentId, Integer curSemester);

    @Query(value = "select sc.semesterNumber from SyllabusContent sc" +
                   " where sc.syllabus.id = :syllabusId order by sc.semesterNumber")
    Set<Integer> findSemesterNumbersBySyllabusId(Long syllabusId);
}
