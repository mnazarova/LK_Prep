package sbangularjs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sbangularjs.model.AttestationContent;

import java.util.List;

public interface AttestationContentRepository extends JpaRepository<AttestationContent, Long> {
    /*@Query("select a from Attestation a where a.deadline > :deadlineDateTime")
    List<AttestationContent> findAllWithDeadlineDateTimeAfter(
            @Param("deadlineDateTime") Date deadlineDateTime);*/

    List<AttestationContent> findAllByCertificationAttestationId(Long id);

    List<AttestationContent> findAll();
}
