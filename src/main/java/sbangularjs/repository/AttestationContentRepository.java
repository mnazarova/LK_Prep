package sbangularjs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sbangularjs.model.AttestationContent;

import java.util.List;

public interface AttestationContentRepository extends JpaRepository<AttestationContent, Long> {
    /*@Query("select a from Attestation a where a.deadline > :deadlineDateTime")
    List<AttestationContent> findAllWithDeadlineDateTimeAfter(
            @Param("deadlineDateTime") Date deadlineDateTime);*/
    List<AttestationContent> findAll();
    AttestationContent findAttestationContentById(Long attestationContentId);

    List<AttestationContent> findAllByCertificationAttestationId(Long id);
    List<AttestationContent> findAllByCertificationAttestationIdAndTeacherId(Long id, Long teacherId);


    @Query("select count(all ac) from AttestationContent ac " +
            " where ac.certificationAttestation.id= :certificationAttestationId and (ac.works=null or ac.attest=null)")
    Integer findUnfinishedByCertificationAttestationId(Long certificationAttestationId);
}
