package sbangularjs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sbangularjs.DTO.SubjectDTO;
import sbangularjs.model.CertificationAttestation;

import java.util.List;
public interface CertificationAttestationRepository extends JpaRepository<CertificationAttestation, Long> {

    List<CertificationAttestation> findAll();
    CertificationAttestation findCertificationAttestationById(Long certificationAttestationId);


    @Query("select ca.id from AttestationContent at " +
            "join CertificationAttestation ca on ca.id = at.certificationAttestation.id " +
            "and ca.attestation.id = :attestationId and at.teacher.id = :teacherId")
    List<Long> findAllByAttestationIdAndTeacherId(Long attestationId, Long teacherId);

    @Query(value = "select new sbangularjs.DTO.SubjectDTO(ca.id, ca.attestation.id, " +
            " d.id, d.name, g.id, g.number) " +
            " from CertificationAttestation ca " +
            " join Discipline d on d.id = ca.syllabusContent.discipline.id " +
            " join Group g on g.id = ca.group.id " +
            " where ca.id in :certificationAttestationIds ")
    List<SubjectDTO> findAllSubjectsGroupDTO(List<Long> certificationAttestationIds);

    /* SECRETARY */

    @Query("select ca from CertificationAttestation ca " +
            "where ca.attestation.id = :attestationId and ca.group.id = :groupId and ca.syllabusContent.discipline.department.id = :departmentId")
    List<CertificationAttestation> findCAsByAttestationIdAndGroupIdAndDepId(Long attestationId, Long groupId, Long departmentId);

    /* DEANERY */

    @Query("select ca.id from CertificationAttestation ca where ca.attestation.id = :attestationId " +
            " and ca.group.id in :groupIds")
    List<Long> findAllByAttestationIdAndGroupIds(Long attestationId, List<Long> groupIds);

}
