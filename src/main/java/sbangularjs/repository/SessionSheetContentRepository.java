package sbangularjs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sbangularjs.model.SessionSheetContent;

import java.util.List;

public interface SessionSheetContentRepository extends JpaRepository<SessionSheetContent, Long> {

    List<SessionSheetContent> findAll();
    SessionSheetContent findSessionSheetContentById(Long sessionSheetContentId);
    SessionSheetContent findSessionSheetContentBySessionSheetIdAndStudentId(Long sessionSheetId, Long studentId);

    /*TEACHER*/
    @Query("select count(ssc) from SessionSheetContent ssc where " +
    "ssc.sessionSheet.id= :sessionSheetId and ssc.teacher.id = :teacherId and ssc.evaluation = null")
    Integer findUnfinishedBySessionSheetIdAndTeacherId(Long sessionSheetId, Long teacherId);

    @Query("select count(ssc) from SessionSheetContent ssc where " +
    "ssc.sessionSheet.id= :sessionSheetId and ssc.evaluation = null")
    Integer findUnfinishedBySessionSheetId(Long sessionSheetId);

    List<SessionSheetContent> findAllBySessionSheetIdAndTeacherIdAndActiveIsTrue(Long sessionSheetId, Long teacherId);

    /* For Head Of Department */
    List<SessionSheetContent> findAllBySessionSheetIdAndActiveIsTrue(Long sessionSheetId);
}
