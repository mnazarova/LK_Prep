package sbangularjs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sbangularjs.model.Discipline;

import java.util.List;

public interface DisciplineRepository extends JpaRepository<Discipline, Long> {
    List<Discipline> findAll();

    @Query(value = "select d from Discipline d" +
            " join SyllabusContent sc on sc.discipline.id = d.id and sc.syllabus.id = :syllabusId and sc.semesterNumber = :semesterNumber")
    List<Discipline> findDisciplinesBySyllabusIdAndSemNumb(Long syllabusId, Integer semesterNumber);
}
