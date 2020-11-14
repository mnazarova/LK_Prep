package sbangularjs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sbangularjs.model.FormOfWork;

import java.util.List;

public interface FormOfWorkRepository extends JpaRepository<FormOfWork, Long> {
    List<FormOfWork> findAll();
    FormOfWork findFormOfWorkById(Long formOfWorkId);
}
