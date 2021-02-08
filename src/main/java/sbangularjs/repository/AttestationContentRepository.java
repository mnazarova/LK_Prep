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

    /* TEACHER */
    List<AttestationContent> findAllByCertificationAttestationIdAndTeacherId(Long certificationAttestationId, Long teacherId);

    /*@Query("select count(ac) from AttestationContent ac " +
            "where ac.certificationAttestation.id = :certificationAttestationId and ac.teacher.id = :teacherId " +
            "and ac.works is not null and ac.attest is not null")
    Integer findFinishedByCertificationAttestationId(Long certificationAttestationId, Long teacherId);*/

    @Query("select count(ac) from AttestationContent ac " +
            "where ac.certificationAttestation.id= :certificationAttestationId and ac.teacher.id = :teacherId " +
            "and (ac.works = null or ac.attest = null)")
    Integer findUnfinishedByCertificationAttestationId(Long certificationAttestationId, Long teacherId);

    /*  */
    List<AttestationContent> findAllByCertificationAttestationId(Long id);

}
