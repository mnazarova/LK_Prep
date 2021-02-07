package sbangularjs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sbangularjs.model.SplitAttestationForm;

import java.util.List;

public interface SplitAttestationFormRepository extends JpaRepository<SplitAttestationForm, Long> {
    List<SplitAttestationForm> findAll();
    SplitAttestationForm findSplitAttestationFormById(Long id);
    SplitAttestationForm findByName(String splitAttestationFormName);
}
