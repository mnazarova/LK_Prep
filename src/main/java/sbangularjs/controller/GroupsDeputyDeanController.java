package sbangularjs.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import sbangularjs.model.*;
import sbangularjs.repository.*;

import javax.transaction.Transactional;
import java.util.*;

@Controller
@PreAuthorize("hasAuthority('DEPUTY_DEAN')")
@AllArgsConstructor(onConstructor = @_(@Autowired))
public class GroupsDeputyDeanController {
    private DeputyDeanRepository deputyDeanRepository;
    private GroupRepository groupRepository;
    private SyllabusRepository syllabusRepository;
    private SyllabusContentRepository syllabusContentRepository;

    @PatchMapping("/getSyllabusesByDeputyDeanId")
    public ResponseEntity getSyllabusesByDeputyDeanId(@AuthenticationPrincipal User user) {
        DeputyDean deputyDean = deputyDeanRepository.findByUsername(user.getUsername());
        if (deputyDean == null || deputyDean.getId() == null) return new ResponseEntity(HttpStatus.CONFLICT);
        Set<Syllabus> syllabuses = syllabusRepository.findSyllabusesByDeputyDeanId(deputyDean.getId());
        if (syllabuses.isEmpty()) return new ResponseEntity(HttpStatus.NO_CONTENT);
        for (Syllabus syllabus: syllabuses) {
            if (syllabus.getGroups().size() != 0) { // set Deadline
                List<SyllabusContent> sc = syllabusContentRepository
                        .findBySyllabusIdAndSemesterNumber(syllabus.getId(), syllabus.getGroups().get(0).getCurSemester());
                if (sc.size() == 0) continue;
                syllabus.setDeadline(sc.get(0).getDeadline());
            }

        }
        return new ResponseEntity<>(syllabuses, HttpStatus.OK);
    }

    @PatchMapping("/getSemesterNumberSetBySyllabusIdForDeputyDean")
    public ResponseEntity getSemesterNumberSetBySyllabusIdForDeputyDean(@RequestParam Long syllabusId) {

        Set<Integer> semesterNumberSet = syllabusContentRepository.findSemesterNumberSetBySyllabusId(syllabusId);
        semesterNumberSet.remove(0);

        return new ResponseEntity<>(semesterNumberSet, HttpStatus.OK);
    }

    @PatchMapping("/getEstimatedCurSemesterNumberForDeputyDean")
    public ResponseEntity getEstimatedCurSemesterNumberForDeputyDean(@RequestParam Long syllabusId) {
        Syllabus syllabus = syllabusRepository.findSyllabusById(syllabusId);
        if (syllabus == null) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

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
        // бакалавр и специалист с нуля, магистр (Инженер) с 8, аспирант с 12? нет, с 1 по 6
        int start = 0;
        if (syllabus.getQualification().getId() == 2) // Магистратура
            start = 8;
        //else if (syllabus.getQualification().getId() == 4) // Аспирантура
          //  start = 12; // 1-6

        int estimatedCurSemesterNumber = start + diff * 2 + add;

        // год в учебном плане, разность по модулю текущего года и того года, в зависимости от текущей даты
        // с 1.09 (включительно) по 1.02 (не включительно) - осень, с 1.02 (включительно) по 1.09 (не включительно)
        // возвращаем форму обучения, устанавливаем мин и макс семестр, высчитываем текущий
        // если за пределами промежутка, то ошибка, иначе возвращаем список! и след запросом как-то передаём текущий семестра
        // разность 0 (*2)  (осень +1, весна +2)
        // разность 1 (*2)  (осень +1, весна +2)
        // разность 2 (*2)  (осень +1, весна +2)
        // разность 3 (*2)  (осень +1, весна +2)
        // если проверка на бакалавриат очный пройдена успешно, то возвращаем результат семестра

        // разность 4 (*2)  ( осень +1, весна +2)
        // если проверка на специалитет очный или бакалавриат заочный пройдена успешно, то возвращаем результат семестра

        // разность 4 (*2)  ( осень +1, весна +2)
        // разность 5 (*2)  ( осень +1, весна +2)
        // если проверка на магистратуру пройдена успешно, то возвращаем результат семестра
        return new ResponseEntity<>(estimatedCurSemesterNumber, HttpStatus.OK);
    }


    @Transactional
    @PatchMapping("/saveSyllabusForDeputyDean")
    public ResponseEntity saveSyllabusForDeputyDean(@AuthenticationPrincipal User user, @RequestBody Syllabus updatingSyllabus) {
        if (updatingSyllabus == null || updatingSyllabus.getId() == null) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

        List<Group> savingGroupList = new ArrayList<>();
        for (Group updatingGroup: updatingSyllabus.getGroups()) {
            Group oldGroup = groupRepository.findGroupById(updatingGroup.getId());
            oldGroup.setActive(updatingGroup.getActive());
            oldGroup.setCurSemester(updatingGroup.getCurSemester());
            savingGroupList.add(oldGroup);
        }
        groupRepository.saveAll(savingGroupList);

        if (updatingSyllabus.getDeadline() != null && updatingSyllabus.getGroups().size() != 0
                && updatingSyllabus.getGroups().get(0).getCurSemester() != null) {
            List<SyllabusContent> syllabusContentList = syllabusContentRepository
                    .findBySyllabusIdAndSemesterNumber(updatingSyllabus.getId(), updatingSyllabus.getGroups().get(0).getCurSemester());
            if (syllabusContentList.size() != 0 && !updatingSyllabus.getDeadline().equals(syllabusContentList.get(0).getDeadline())) {
                for (SyllabusContent syllabusContent : syllabusContentList)
                    syllabusContent.setDeadline(updatingSyllabus.getDeadline());
                syllabusContentRepository.saveAll(syllabusContentList);
            }
        }

        Syllabus syllabus = syllabusRepository.findSyllabusById(updatingSyllabus.getId());
        if (syllabus == null) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        if (syllabus.getGroups().size() != 0) {
            List<SyllabusContent> sc = syllabusContentRepository
                    .findBySyllabusIdAndSemesterNumber(syllabus.getId(), syllabus.getGroups().get(0).getCurSemester());
            if (sc.size() != 0)
                syllabus.setDeadline(sc.get(0).getDeadline());
        }

        return new ResponseEntity<>(syllabus, HttpStatus.OK);
    }

}
