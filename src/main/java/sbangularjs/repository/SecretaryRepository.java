package sbangularjs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sbangularjs.model.Secretary;
import sbangularjs.model.Teacher;

import java.util.List;
import java.util.Set;

public interface SecretaryRepository extends JpaRepository<Secretary, Long> {
    List<Secretary> findAll();
    Secretary findSecretaryById(Long secretaryId);

    Secretary findByUsername(String username);
}
