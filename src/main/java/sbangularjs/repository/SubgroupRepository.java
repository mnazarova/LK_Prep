package sbangularjs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sbangularjs.model.Subgroup;

import java.util.List;

public interface SubgroupRepository extends JpaRepository<Subgroup, Long> {
    List<Subgroup> findByActive(boolean active);
    Subgroup findSubgroupById(Long subgroupId);
    List<Subgroup> findByNameContainingIgnoreCase(String surname);

    @Query(value = "select sbg from Subgroup sbg" +
            " join DeaneryGroupOrSubgroup DGS on DGS.subgroup.id = sbg.id and DGS.deanery.id = :deaneryId and DGS.isSubgroup = true")
    List<Subgroup> findSubgroupsByDeaneryId(Long deaneryId);

    @Query(value = "select sbg.id from Subgroup sbg" +
            " join DeaneryGroupOrSubgroup DGS on DGS.subgroup.id = sbg.id and DGS.deanery.id = :deaneryId and DGS.isSubgroup = true")
    List<Long> findSubgroupIdsByDeaneryId(Long deaneryId);
}
