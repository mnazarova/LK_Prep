package sbangularjs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sbangularjs.model.DeaneryGroupOrSubgroup;

import java.util.List;

public interface DeaneryGroupOrSubgroupRepository extends JpaRepository<DeaneryGroupOrSubgroup, Long> {
    List<DeaneryGroupOrSubgroup> findAll();

    DeaneryGroupOrSubgroup findByDeaneryIdAndGroupId(Long deaneryId, Long groupId);
    DeaneryGroupOrSubgroup findByDeaneryIdAndSubgroupId(Long deaneryId, Long subgroupId);
}
