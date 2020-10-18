package sbangularjs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sbangularjs.model.Subgroup;

import java.util.List;

public interface SubgroupRepository extends JpaRepository<Subgroup, Long> {
    List<Subgroup> findByActive(boolean active);
    Subgroup findById(long id);
    List<Subgroup> findByNameContainingIgnoreCase(String surname);
}