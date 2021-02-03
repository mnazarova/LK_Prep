package sbangularjs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sbangularjs.model.DeaneryGroup;

import java.util.List;

public interface DeaneryGroupRepository extends JpaRepository<DeaneryGroup, Long> {
    List<DeaneryGroup> findAll();

    DeaneryGroup findByDeaneryIdAndGroupId(Long deaneryId, Long groupId);
//    DeaneryGroup findByDeaneryIdAndSubgroupId(Long deaneryId, Long subgroupId);
}
