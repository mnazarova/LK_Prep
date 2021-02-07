package sbangularjs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sbangularjs.model.AttestationContent;

import java.util.List;

public interface AttestationContentRepository extends JpaRepository<AttestationContent, Long> {
    List<AttestationContent> findAll();
    AttestationContent findAttestationContentById(Long attestationContentId);

    /* SECRETARY */
    AttestationContent findAttestationContentByCertificationAttestationIdAndStudentId(Long certificationAttestationId, Long studentId);

    /*  */
    List<AttestationContent> findAllByCertificationAttestationId(Long id);
    List<AttestationContent> findAllByCertificationAttestationIdAndTeacherId(Long id, Long teacherId);

    @Query("select count(all ac) from AttestationContent ac " +
            " where ac.certificationAttestation.id= :certificationAttestationId and (ac.works=null or ac.attest=null)")
    Integer findUnfinishedByCertificationAttestationId(Long certificationAttestationId);
}
