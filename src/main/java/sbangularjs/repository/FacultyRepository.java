package sbangularjs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sbangularjs.model.Faculty;

import java.util.List;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    List<Faculty> findAll();
    /*List<OrderDone> findAllByClientIdAndSum(Integer clientId, Integer sum);
    List<OrderDone> findAllByClientId(Integer clientId);*/
}
