package sbangularjs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sbangularjs.DTO.SubjectDTO;
import sbangularjs.model.CertificationAttestation;

import java.util.List;
public interface CertificationAttestationRepository extends JpaRepository<CertificationAttestation, Long> {

    List<CertificationAttestation> findAll();
    CertificationAttestation findCertificationAttestationById(Long certificationAttestationId);


    @Query("select ca.id from CertificationAttestation ca where ca.attestation.id = :attestationId and " +
            " ca.teacher.id = :teacherId and ca.isSubgroup = :isSubgroup")
    List<Long> findAllByAttestationIdAndTeacherIdAndIsSubgroup(Long attestationId, Long teacherId, boolean isSubgroup);

    @Query(value = "select new sbangularjs.DTO.SubjectDTO(ca.id, ca.teacher.id, ca.attestation.id," +
            "d.id, d.name, g.id, g.number) " +
            " from CertificationAttestation ca " +
            " join Discipline d on d.id = ca.syllabusContent.discipline.id " +
            " join Group g on g.id = ca.group.id " +
            " where ca.id in :certificationAttestationIds ")
    List<SubjectDTO> findAllSubjectsGroupDTO(List<Long> certificationAttestationIds);

    @Query(value = "select new sbangularjs.DTO.SubjectDTO(sg.id, sg.name, ca.id, ca.teacher.id, ca.attestation.id, d.id, d.name) " +
            " from CertificationAttestation ca " +
            " join Discipline d on d.id = ca.syllabusContent.discipline.id " +
            " join Subgroup sg on sg.id = ca.subgroup.id " +
            " where ca.id in :certificationAttestationIds ")
    List<SubjectDTO> findAllSubjectSubgroupDTO(List<Long> certificationAttestationIds);

//    List<CertificationAttestation> findByAttestationIdAndSubgroupId(Long attestationId, Long subgroupId);
//    List<CertificationAttestation> findByAttestationIdAndGroupId(Long attestationId, Long groupId);

    @Query("select ca from CertificationAttestation ca " +
            "where ca.attestation.id = :attestationId and ca.subgroup.id = :subgroupId and ca.syllabusContent.discipline.department.id = :departmentId")
    List<CertificationAttestation> findCAsByAttestationIdAndSubgroupIdAndDepId(Long attestationId, Long subgroupId, Long departmentId);

    @Query("select ca from CertificationAttestation ca " +
            "where ca.attestation.id = :attestationId and ca.group.id = :groupId and ca.syllabusContent.discipline.department.id = :departmentId")
    List<CertificationAttestation> findCAsByAttestationIdAndGroupIdAndDepId(Long attestationId, Long groupId, Long departmentId);


}
