package sbangularjs.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sbangularjs.DTO.SessionDTO;
import sbangularjs.model.Teacher;
import sbangularjs.model.User;
import sbangularjs.repository.*;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@Controller
@PreAuthorize("hasAuthority('TEACHER')")
@AllArgsConstructor(onConstructor = @_(@Autowired))
public class SessionController {
    private TeacherRepository teacherRepository;
    private SessionSheetRepository sessionSheetRepository;
    private SessionSheetContentRepository sessionSheetContentRepository;

    @PatchMapping("/getSessionDTOGroupsByTeacherIdAndGroupId")
    public ResponseEntity getSessionDTOGroupsByTeacherIdAndGroupId(@AuthenticationPrincipal User user, @RequestParam Long groupId) {
        Teacher teacher = teacherRepository.findByUsername(user.getUsername());
        if (teacher == null || teacher.getId() == null)
            return new ResponseEntity<>(0, HttpStatus.CONFLICT); // if (attestationRepository.checkAttestationDeadlineDateTimeAfter(new Date(), attestationId) == null)

        return new ResponseEntity<>(getSessionDTOList(teacher.getId(), groupId), HttpStatus.OK);
    }

    private List<SessionDTO> getSessionDTOList(Long teacherId, Long groupId) { // groupId == null - все группы данного преподавателя
        List<Long> sessionSheetIds = sessionSheetRepository.findSessionSheetIdsByTeacherIdAndIsAdditionalFalse(teacherId, new Date());

        /* Доп. ведомости */
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        int curYear = calendar.get(Calendar.YEAR), curMonth = calendar.get(Calendar.MONTH), day = 1;
        if (curMonth < Calendar.MARCH) { // Зимняя сессия (Январь, Февраль)
            Date firstJanuary = new GregorianCalendar(curYear, Calendar.JANUARY, day).getTime(), firstMarch = new GregorianCalendar(curYear, Calendar.MARCH, day).getTime();
            sessionSheetIds.addAll(sessionSheetRepository.findSessionSheetIdsByTeacherIdAndIsAdditionalTrue(teacherId, new Date(), firstJanuary, firstMarch));;
        }
        if (curMonth > Calendar.MAY && curMonth < Calendar.OCTOBER) { // Весенняя сессия (без выпускников) (5-8) с Июня по Сентябрь (включая)
            Date firstJune = new GregorianCalendar(curYear, Calendar.JUNE, day).getTime(), firstOctober = new GregorianCalendar(curYear, Calendar.OCTOBER, day).getTime();
            sessionSheetIds.addAll(sessionSheetRepository.findSessionSheetIdsByTeacherIdAndIsAdditionalTrue(teacherId, new Date(), firstJune, firstOctober));;
        }
        /* Доп. ведомости */

        if (sessionSheetIds.size() == 0) return null; // return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        List<SessionDTO> sessionDTOList;
        if (groupId == null)
            sessionDTOList = sessionSheetRepository.findAllSessionDTOBySessionSheetIds(sessionSheetIds);
        else
            sessionDTOList = sessionSheetRepository.findAllSessionDTOBySessionSheetIdsAndGroupId(sessionSheetIds, groupId);

        for (SessionDTO sessionDTO: sessionDTOList)
            if (sessionSheetContentRepository.findUnfinishedBySessionSheetIdAndTeacherId(sessionDTO.getSessionSheetId(), teacherId) == 0) // нет null полей
                sessionDTO.setFinished(true);
            else
                sessionDTO.setFinished(false);
        return sessionDTOList;
    }

    @PatchMapping("/getSessionDTOGroupsForHeadOfDepartment")
    public ResponseEntity getSessionDTOGroupsForHeadOfDepartment(@AuthenticationPrincipal User user, @RequestParam Long groupId) { // groupId == null - все группы с дисциплинами данной кафедры
        Teacher teacher = teacherRepository.findByUsername(user.getUsername());
        if (teacher == null || teacher.getId() == null)
            return new ResponseEntity<>(0, HttpStatus.CONFLICT);
        if (teacher.getDepartment() == null || teacher.getDepartment().getId() == null)
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

        List<Long> sessionSheetIds = sessionSheetRepository.findSessionSheetIdsByDepartmentIdAndDeadline(teacher.getDepartment().getId(), new Date());
        /* Доп. ведомости */
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        int curYear = calendar.get(Calendar.YEAR), curMonth = calendar.get(Calendar.MONTH), day = 1;
        if (curMonth < Calendar.MARCH) { // Зимняя сессия (Январь, Февраль)
            Date firstJanuary = new GregorianCalendar(curYear, Calendar.JANUARY, day).getTime(), firstMarch = new GregorianCalendar(curYear, Calendar.MARCH, day).getTime();
            sessionSheetIds.addAll(sessionSheetRepository.findSessionSheetIdsByDepartmentIdAndIsAdditionalTrue(teacher.getDepartment().getId(), new Date(), firstJanuary, firstMarch));;
        }
        if (curMonth > Calendar.MAY && curMonth < Calendar.OCTOBER) { // Весенняя сессия (без выпускников) (5-8) с Июня по Сентябрь (включая)
            Date firstJune = new GregorianCalendar(curYear, Calendar.JUNE, day).getTime(), firstOctober = new GregorianCalendar(curYear, Calendar.OCTOBER, day).getTime();
            sessionSheetIds.addAll(sessionSheetRepository.findSessionSheetIdsByDepartmentIdAndIsAdditionalTrue(teacher.getDepartment().getId(), new Date(), firstJune, firstOctober));;
        }
        /* Доп. ведомости */
        if (sessionSheetIds.size() == 0) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

        List<SessionDTO> sessionDTOList;
        if (groupId == null)
            sessionDTOList = sessionSheetRepository.findAllSessionDTOBySessionSheetIds(sessionSheetIds);
        else
            sessionDTOList = sessionSheetRepository.findAllSessionDTOBySessionSheetIdsAndGroupId(sessionSheetIds, groupId);

        for (SessionDTO sessionDTO: sessionDTOList)
            if (sessionSheetContentRepository.findUnfinishedBySessionSheetId(sessionDTO.getSessionSheetId()) == 0) // нет null полей
                sessionDTO.setFinished(true);
            else
                sessionDTO.setFinished(false);
        return new ResponseEntity<>(sessionDTOList, HttpStatus.OK);
    }

}
