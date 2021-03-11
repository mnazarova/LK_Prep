package sbangularjs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sbangularjs.model.DeputyDean;

import java.util.List;

public interface DeputyDeanRepository extends JpaRepository<DeputyDean, Long> {
    List<DeputyDean> findAll();
    DeputyDean findDeputyDeanById(Long deputyDeanId);

    DeputyDean findByUsername(String username);
}
