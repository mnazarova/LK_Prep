package sbangularjs.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import sbangularjs.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username, Sort sort);
    User findByUsername(String username);
}
