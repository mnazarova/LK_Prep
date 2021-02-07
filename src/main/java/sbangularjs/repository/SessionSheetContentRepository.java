package sbangularjs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sbangularjs.model.SessionSheetContent;

import java.util.List;

public interface SessionSheetContentRepository extends JpaRepository<SessionSheetContent, Long> {

    List<SessionSheetContent> findAll();
    SessionSheetContent findSessionSheetContentById(Long sessionSheetContentId);
    SessionSheetContent findSessionSheetContentBySessionSheetIdAndStudentId(Long sessionSheetId, Long studentId);
}
