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
    SessionSheet findSessionSheetBySyllabusContentIdAndGroupIdAndSplitAttestationFormId(Long syllabusContentId, Long groupId, Long splitAttestationFormId);
    List<SessionSheet> findSessionSheetBySyllabusContentIdAndGroupId(Long syllabusContentId, Long groupId);

    /*@Query("select ca from CertificationAttestation ca " +
            "where ca.attestation.id = :attestationId and ca.group.id = :groupId and ca.syllabusContent.discipline.department.id = :departmentId")
    List<CertificationAttestation> findCAsByAttestationIdAndGroupIdAndDepId(Long attestationId, Long groupId, Long departmentId);*/

    /* TEACHER AND DEANERY */
    @Query(value = "select new sbangularjs.DTO.SessionDTO(ss.id, ss.group.id, ss.group.number, " +
            "ss.syllabusContent.discipline.id, ss.syllabusContent.discipline.name, ss.syllabusContent.deadline, " +
            "ss.splitAttestationForm.id, ss.splitAttestationForm.name) " +
            "from SessionSheet ss where ss.id in :sessionSheetIds")
    List<SessionDTO> findAllSessionDTOBySessionSheetIds(List<Long> sessionSheetIds);

    /* TEACHER */
    @Query("select ss.id from SessionSheetContent ssc " +
            "join SessionSheet ss on ss.id = ssc.sessionSheet.id " +
            "and ssc.teacher.id = :teacherId and ssc.active = true where ss.syllabusContent.deadline > :deadline")
    List<Long> findSessionSheetIdsByTeacherIdAndDeadline(Long teacherId, Date deadline);


    @Query(value = "select new sbangularjs.DTO.SessionDTO(ss.id, ss.group.id, ss.group.number, " +
            "ss.syllabusContent.discipline.id, ss.syllabusContent.discipline.name, ss.syllabusContent.deadline, " +
            "ss.splitAttestationForm.id, ss.splitAttestationForm.name) " +
            "from SessionSheet ss where ss.id in :sessionSheetIds and ss.group.id = :groupId")
    List<SessionDTO> findAllSessionDTOBySessionSheetIdsAndGroupId(List<Long> sessionSheetIds, Long groupId);

    /* For Head Of Department */
    @Query("select ss.id from SessionSheetContent ssc " +
            "join SessionSheet ss on ss.id = ssc.sessionSheet.id and ssc.active = true " +
            "where ss.syllabusContent.discipline.department.id = :departmentId and ss.syllabusContent.deadline > :deadline")
    List<Long> findSessionSheetIdsByDepartmentIdAndDeadline(Long departmentId, Date deadline);

    @Query("select ss.id from SessionSheet ss where ss.syllabusContent.discipline.department.id = :departmentId " +
            "and ss.syllabusContent.deadline > :deadline and ss.group.id = :groupId")
    List<Long> findSessionSheetIdsDepartmentIdAndDeadlineAngGroupId(Long departmentId, Date deadline, Long groupId);

    /* DEANERY */
    @Query("select ss.id from SessionSheet ss where ss.syllabusContent.deadline > :deadline and ss.group.id in :groupIds")
    List<Long> findSessionSheetIdsByDeadlineAndGroupIds(Date deadline, List<Long> groupIds);

}
