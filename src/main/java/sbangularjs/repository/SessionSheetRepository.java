package sbangularjs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sbangularjs.model.SessionSheet;

import java.util.List;

public interface SessionSheetRepository extends JpaRepository<SessionSheet, Long> {

    List<SessionSheet> findAll();
    SessionSheet findSessionSheetById(Long sessionSheetId);
    SessionSheet findSessionSheetBySyllabusContentIdAndGroupIdAndSplitAttestationFormId(Long syllabusContentId, Long groupId, Long splitAttestationFormId);


    /*@Query("select ca from CertificationAttestation ca " +
            "where ca.attestation.id = :attestationId and ca.group.id = :groupId and ca.syllabusContent.discipline.department.id = :departmentId")
    List<CertificationAttestation> findCAsByAttestationIdAndGroupIdAndDepId(Long attestationId, Long groupId, Long departmentId);*/


}
