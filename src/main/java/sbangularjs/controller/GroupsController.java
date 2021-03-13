package sbangularjs.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import sbangularjs.model.*;
import sbangularjs.repository.DeaneryRepository;
import sbangularjs.repository.GroupRepository;
import sbangularjs.repository.SyllabusContentRepository;

import javax.transaction.Transactional;
import java.util.*;

@Controller
@PreAuthorize("hasAuthority('DEANERY')")
@AllArgsConstructor(onConstructor = @_(@Autowired))
public class GroupsController {
    private DeaneryRepository deaneryRepository;
    private GroupRepository groupRepository;
    private SyllabusContentRepository syllabusContentRepository;

    @PatchMapping("/getGroupsByDeaneryId")
    public ResponseEntity getGroupsByDeaneryId(@AuthenticationPrincipal User user) {
        Deanery curDeanery = deaneryRepository.findByUsername(user.getUsername());
        if (curDeanery == null || curDeanery.getId() == null) return new ResponseEntity(HttpStatus.CONFLICT);
        List<Group> groups = groupRepository.findGroupsByDeaneryId(curDeanery.getId(),
                Sort.by(Sort.Direction.DESC, "active").and(Sort.by("curSemester")));
        if (groups.isEmpty()) return new ResponseEntity(HttpStatus.NO_CONTENT);
        for (Group group: groups) {
            List<SyllabusContent> sc = syllabusContentRepository
                    .findBySyllabusIdAndSemesterNumber(group.getSyllabus().getId(), group.getCurSemester());
            if (sc.size() == 0) continue;
            group.setDeadline(sc.get(0).getDeadline());
        }
        return new ResponseEntity<>(groups, HttpStatus.OK);
    }

    @PatchMapping("/getSemesterNumberSetByGroupId")
    public ResponseEntity getSemesterNumberSetByGroupId(@RequestParam Long groupId) {

        Set<Integer> semesterNumberSet = syllabusContentRepository.findSemesterNumberSetByGroupId(groupId);
        /*if (semesterNumberSet.contains(0)) */semesterNumberSet.remove(0);

        return new ResponseEntity<>(semesterNumberSet, HttpStatus.OK);
    }

    @PatchMapping("/getEstimatedCurSemesterNumber")
    public ResponseEntity getEstimatedCurSemesterNumber(@RequestParam Long groupId) {
        Group group = groupRepository.findGroupById(groupId);
        if (group.getSyllabus() == null) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

        int startYear = group.getSyllabus().getYear();
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
        // бакалавр и специалист с нуля, магистр (Инженер) с 9, аспирант с 13?
        int start = 0;
        if (group.getSyllabus().getQualification().getId() == 2) // Магистратура
            start = 9;
        else if (group.getSyllabus().getQualification().getId() == 4) // Аспирантура
            start = 13;

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
    @PatchMapping("/saveGroup")
    public ResponseEntity saveGroup(/*@AuthenticationPrincipal User user, */@RequestBody Group updatingGroup) {
        if (updatingGroup == null || updatingGroup.getId() == null) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

        Group group = groupRepository.findGroupById(updatingGroup.getId());
        if (group == null) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        group.setActive(updatingGroup.getActive());
        group.setCurSemester(updatingGroup.getCurSemester());
        if (updatingGroup.getDeadline() != null && updatingGroup.getCurSemester() != null) {
            List<SyllabusContent> syllabusContentList = syllabusContentRepository
                    .findBySyllabusIdAndSemesterNumber(group.getSyllabus().getId(), updatingGroup.getCurSemester());
            for (SyllabusContent syllabusContent: syllabusContentList)
                syllabusContent.setDeadline(updatingGroup.getDeadline());
            syllabusContentRepository.saveAll(syllabusContentList);
        }
        groupRepository.save(group);

        List<SyllabusContent> sc = syllabusContentRepository.findBySyllabusIdAndSemesterNumber(group.getSyllabus().getId(), group.getCurSemester());
        if (sc.size() != 0)
            group.setDeadline(sc.get(0).getDeadline());

//        List<Group> groups = groupRepository.findGroupsByDeaneryId(deaneryRepository.findByUsername(user.getUsername()).getId(),
//                Sort.by(Sort.Direction.DESC, "active").and(Sort.by("curSemester")));

//        Group checkGroup = groupRepository.findGroupById(group.getId());

        return new ResponseEntity<>(group, HttpStatus.OK);
    }

}
