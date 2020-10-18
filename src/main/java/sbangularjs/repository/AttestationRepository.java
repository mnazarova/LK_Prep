package sbangularjs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sbangularjs.model.Attestation;

import java.util.Date;
import java.util.List;

public interface AttestationRepository extends JpaRepository<Attestation, Long> {
    List<Attestation> findByActiveTrue();
//    List<Attestation> findByDeadline_Month(Long groupId);
//    List<Attestation> findByDeadlineIsBefore();
    @Query("select a from Attestation a where a.deadline > :deadlineDateTime")
    List<Attestation> findAllWithDeadlineDateTimeAfter(
            @Param("deadlineDateTime") Date deadlineDateTime);
    List<Attestation> findByNameContainingIgnoreCase(String name);
    /*List<Student> findAllBySurnameContainingIgnoreCaseAndEmailContainingIgnoreCase(String surname, String email);
    List<Student> findByEmailContainingIgnoreCase(String email);*/
}