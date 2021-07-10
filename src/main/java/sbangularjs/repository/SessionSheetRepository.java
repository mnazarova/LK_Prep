package sbangularjs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sbangularjs.DTO.SessionDTO;
import sbangularjs.model.SessionSheet;

import java.util.Date;
import java.util.List;

public interface SessionSheetRepository extends JpaRepository<SessionSheet, Long> {

    List<SessionSheet> findAll();
    SessionSheet findSessionSheetById(Long sessionSheetId);
    SessionSheet findSessionSheetBySyllabusContentIdAndGroupIdAndSplitAttestationFormIdAndIsAdditional(Long syllabusContentId, Long groupId, Long splitAttestationFormId, Boolean isAdditional);
    List<SessionSheet> findSessionSheetBySyllabusContentIdAndGroupId(Long syllabusContentId, Long groupId);

    /*@Query("select ca from CertificationAttestation ca " +
            "where ca.attestation.id = :attestationId and ca.group.id = :groupId and ca.syllabusContent.discipline.department.id = :departmentId")
    List<CertificationAttestation> findCAsByAttestationIdAndGroupIdAndDepId(Long attestationId, Long groupId, Long departmentId);*/

    /* TEACHER AND DEANERY */
    @Query(value = "select new sbangularjs.DTO.SessionDTO(ss.id, ss.group.id, ss.group.number, " +
            "ss.syllabusContent.discipline.id, ss.syllabusContent.discipline.name, ss.syllabusContent.deadline, " +
            "ss.splitAttestationForm.id, ss.splitAttestationForm.name, ss.isAdditional) " +
            "from SessionSheet ss where ss.id in :sessionSheetIds")
    List<SessionDTO> findAllSessionDTOBySessionSheetIds(List<Long> sessionSheetIds);

    /* TEACHER */
    @Query("select DISTINCT ss.id from SessionSheetContent ssc join SessionSheet ss on ss.id = ssc.sessionSheet.id " +
            "and ss.isAdditional = false and ssc.teacher.id = :teacherId and ssc.active = true " +
            "where ss.group.curSemester = ss.syllabusContent.semesterNumber and ss.syllabusContent.deadline > :now ") // = проверено: только 00.00.00
    List<Long> findSessionSheetIdsByTeacherIdAndIsAdditionalFalse(Long teacherId, Date now);

    @Query("select DISTINCT ss.id from SessionSheetContent ssc join SessionSheet ss on ss.id = ssc.sessionSheet.id " +
            "and ss.isAdditional = true and ssc.teacher.id = :teacherId and ssc.active = true " +
            "where ss.syllabusContent.deadline >= :firstSessionDay and ss.syllabusContent.deadline < :firstNonSessionDay " +
            "and ss.syllabusContent.deadline < :now ")
    List<Long> findSessionSheetIdsByTeacherIdAndIsAdditionalTrue(Long teacherId, Date now, Date firstSessionDay, Date firstNonSessionDay);

    @Query(value = "select new sbangularjs.DTO.SessionDTO(ss.id, ss.group.id, ss.group.number, " +
            "ss.syllabusContent.discipline.id, ss.syllabusContent.discipline.name, ss.syllabusContent.deadline, " +
            "ss.splitAttestationForm.id, ss.splitAttestationForm.name, ss.isAdditional) " +
            "from SessionSheet ss where ss.id in :sessionSheetIds and ss.group.id = :groupId")
    List<SessionDTO> findAllSessionDTOBySessionSheetIdsAndGroupId(List<Long> sessionSheetIds, Long groupId);

    /* For Head Of Department */
    @Query("select DISTINCT ss.id from SessionSheetContent ssc join SessionSheet ss on ss.id = ssc.sessionSheet.id " +
           "and ss.isAdditional = false and ssc.active = true where ss.syllabusContent.discipline.department.id = :departmentId " +
           "and ss.group.curSemester = ss.syllabusContent.semesterNumber and ss.syllabusContent.deadline > :now ")
    List<Long> findSessionSheetIdsByDepartmentIdAndDeadline(Long departmentId, Date now);

    @Query("select DISTINCT ss.id from SessionSheetContent ssc join SessionSheet ss on ss.id = ssc.sessionSheet.id " +
            "and ss.isAdditional = true and ssc.active = true where ss.syllabusContent.discipline.department.id = :departmentId " +
            "and ss.syllabusContent.deadline >= :firstSessionDay and ss.syllabusContent.deadline < :firstNonSessionDay " +
            "and ss.syllabusContent.deadline < :now ")
    List<Long> findSessionSheetIdsByDepartmentIdAndIsAdditionalTrue(Long departmentId, Date now, Date firstSessionDay, Date firstNonSessionDay);

    /* DEANERY */
    @Query("select DISTINCT ss.id from SessionSheetContent ssc join SessionSheet ss on ss.id = ssc.sessionSheet.id and ssc.active = true " +
           "where ss.isAdditional = :isAdditional and ss.group.id = :groupId and ss.syllabusContent.semesterNumber = :semesterNumber")
    List<Long> findSessionSheetIdsByGroupIdAndSemesterNumberAndIsAdditional(Long groupId, Integer semesterNumber, Boolean isAdditional);

}
