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
import sbangularjs.model.*;
import sbangularjs.repository.DeputyDeanRepository;
import sbangularjs.repository.GroupRepository;
import sbangularjs.repository.SessionSheetRepository;
import sbangularjs.repository.SyllabusContentRepository;

import java.util.*;

@Controller
@PreAuthorize("hasAuthority('DEPUTY_DEAN')")
@AllArgsConstructor(onConstructor = @_(@Autowired))
public class DeputyDeanSessionStatementsController {
    private DeputyDeanRepository deputyDeanRepository;
    private GroupRepository groupRepository;
    private SyllabusContentRepository syllabusContentRepository;
    private SessionSheetRepository sessionSheetRepository;

    @PatchMapping("/getSemesterNumberSetByGroupIdForDeputyDean")
    public ResponseEntity getSemesterNumberSetByGroupIdForDeputyDean(@AuthenticationPrincipal User user, @RequestParam Long groupId) {
        DeputyDean deputyDean = deputyDeanRepository.findByUsername(user.getUsername());
        if (deputyDean == null) return new ResponseEntity<>(0, HttpStatus.CONFLICT);

        Group group = groupRepository.findGroupById(groupId);
        if (group == null || group.getSyllabus() == null || group.getSyllabus().getId() == null ||
                group.getDeputyDean() == null || group.getDeputyDean().getId() == null || !group.getDeputyDean().getId().equals(deputyDean.getId()))
            return new ResponseEntity<>(1, HttpStatus.CONFLICT);
        Set<Integer> semesterNumberSet = syllabusContentRepository.findSemesterNumberSetBySyllabusId(group.getSyllabus().getId());
        semesterNumberSet.remove(0);

        return new ResponseEntity<>(semesterNumberSet, HttpStatus.OK);
    }

    @PatchMapping("/getCurSemesterByGroupIdForDeputyDean")
    public ResponseEntity getCurSemesterByGroupIdForDeputyDean(@RequestParam Long groupId) {
        Group group = groupRepository.findGroupById(groupId);
        if (group.getCurSemester() != null)
            return new ResponseEntity<>(group.getCurSemester(), HttpStatus.OK);

        Syllabus syllabus = group.getSyllabus(); // проверяется в предыдущем методе
        int startYear = syllabus.getYear();
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        int curYear = calendar.get(Calendar.YEAR);
        int curMonth = calendar.get(Calendar.MONTH);
        int diff = curYear - startYear;
        if (curMonth < Calendar.SEPTEMBER) // новый год. январь (включительно) - август (включительно)
            diff--;

        int add = 1; // осенний семестр
        if (curMonth > Calendar.JANUARY && curMonth < Calendar.SEPTEMBER) // весенний семестр (1-7) с февраля по август (включая)
            add = 2; // 1 или 2, в зависимости от времени года

        // с какого семестра начинается счёт
        // бакалавр и специалист с нуля, магистр (Инженер) с 8, аспирант с 12? нет, с 1 по 6 семестры у аспирантов!!!
        int start = 0;
        if (syllabus.getQualification().getId() == 2) // Магистратура
            start = 8;
        //else if (syllabus.getQualification().getId() == 4) // Аспирантура
        //  start = 12; // 1-6 семестры у аспирантов!!!

        int curSemesterNumber = start + diff * 2 + add;
        return new ResponseEntity<>(curSemesterNumber, HttpStatus.OK);
    }

    @PatchMapping("/getSessionStatementsByGroupIdAndSemesterNumberForDeputyDean")
    public ResponseEntity getSessionStatementsByGroupIdAndSemesterNumberForDeputyDean(@RequestParam Long groupId, @RequestParam Integer semesterNumber, @RequestParam Boolean isAdditional) {
        List<Long> sessionSheetIds = sessionSheetRepository.findSessionSheetIdsByGroupIdAndSemesterNumberAndIsAdditional(groupId, semesterNumber, isAdditional);
        if(sessionSheetIds.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        List<SessionDTO> sessionDTOList = sessionSheetRepository.findAllSessionDTOBySessionSheetIds(sessionSheetIds);
        return new ResponseEntity<>(sessionDTOList, HttpStatus.OK);
    }

}
