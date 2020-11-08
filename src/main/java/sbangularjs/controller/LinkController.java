package sbangularjs.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import sbangularjs.model.Discipline;
import sbangularjs.model.Group;
import sbangularjs.model.Student;
import sbangularjs.model.Subgroup;
import sbangularjs.repository.*;

import java.util.*;

@Controller
@PreAuthorize("hasAuthority('SECRETARY')")
@AllArgsConstructor(onConstructor = @_(@Autowired))
public class LinkController {
    private SubgroupRepository subgroupRepository;
    private DisciplineRepository disciplineRepository;
    private GroupRepository groupRepository;
    private SyllabusContentRepository syllabusContentRepository;
    private StudentSubgroupRepository studentSubgroupRepository;
    private StudentRepository studentRepository;

    @PatchMapping("/findByNameSubgroup")
    public ResponseEntity findByNameSubgroup(@RequestBody Subgroup subgroup) {
        List<Subgroup> subgroups = subgroupRepository.findByNameContainingIgnoreCase(subgroup.getName());
        if (subgroups.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // You many decide to return HttpStatus.NOT_FOUND
        return new ResponseEntity<>(subgroups, HttpStatus.OK);
    }

    @PatchMapping("/findNotExpelledStudentsFromSubgroup")
    public ResponseEntity findNotExpelledStudentsFromSubgroup(@RequestBody Subgroup subgroup) {
        List<Long> studentsIdFromSubgroup = studentSubgroupRepository.findStudentsIdBySubgroupId(subgroup.getId()); // ids
        List<Student> students = studentRepository.findByIds(studentsIdFromSubgroup);
        if (students.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        for (int i=students.size()-1; i>=0; i--)
            if (students.get(i).getExpelled() != null && students.get(i).getExpelled()) // Expelled = TRUE, отчислены только тогда, когда true
                students.remove(students.get(i));
        if (students.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        else return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @PatchMapping("/getGroupsOfSelectedStudents")
    public ResponseEntity getGroupsOfSelectedStudents(@RequestBody List<Student> students) {
       if (students.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        Set<Group> groups = studentRepository.findGroupsByStudents(students);
        if (groups.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        else return new ResponseEntity<>(groups, HttpStatus.OK);
    }

    @PatchMapping("/getSemesterNumbers")
    public ResponseEntity getSemesterNumbers(@RequestBody Long groupId) {
        Group group = groupRepository.findGroupById(groupId);
        Set<Integer> listSemesterNumbers = syllabusContentRepository.findSemesterNumbersBySyllabusId(group.getSyllabus().getId());
        if (listSemesterNumbers.contains(0))
            listSemesterNumbers.remove(0);
        return new ResponseEntity<>(listSemesterNumbers, HttpStatus.OK);
    }

    @PatchMapping("/getCurSemNumb")
    public ResponseEntity getCurSemNumb(@RequestBody Long groupId) { // передаётся id группы
        Group group = groupRepository.findGroupById(groupId);
        if (group.getSyllabus() == null)
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

        int startYear = group.getSyllabus().getYear();
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        int curYear = calendar.get(Calendar.YEAR);
        int curMonth = calendar.get(Calendar.MONTH);
        int diff = curYear - startYear;

        int add = 1; // осенний семестр
        if (curMonth > Calendar.JANUARY && curMonth < Calendar.AUGUST) // весенний семестр (1-6) с февраля по июль (включая)
            add = 2; // 1 или 2, в зависимости от времени года
//        else
//            add = 1;

        // с какого семестра начинается счёт
        // бакалавр и специалист с нуля, магистр (Инженер) с 9, аспирант с 13?
        int start = 0;
//        if (group.getSyllabus().getQualification() == 1 || group.getSyllabus().getQualification() == 3)
//            start = 0;
        if (group.getSyllabus().getQualification().getId() == 2) // Магистратура
            start = 9;
        else
            if (group.getSyllabus().getQualification().getId() == 4) // Аспирантура
                start = 13;

        int curSemNumb = start + diff * 2 + add;

        // предпоследняя цифра или год уч плана, разность по модулю текущего года и той цифры, в зависимости от текущей даты
        // (с 1.08 по 1.02 (не включительно) - осень, с 1.02 - 1.08 (не вкл))
        // возвращаем форму обучения, устанавливаем мин и макс семестр, высчитываем текущий
        // если за пределами промежутка, то ошибка, иначе возвращаем список !!! и след запросом как-то передаём текущий сем
        // разность 0 (*2)  ( осень +1, весна +2)
        // разность 1 (*2)  ( осень +1, весна +2)
        // разность 2 (*2)  ( осень +1, весна +2)
        // разность 3 (*2)  ( осень +1, весна +2)
        // если проверка на бакалавриат очный пройдена успешно, то всё оки, возвращаем результат семестра

        // разность 4 (*2)  ( осень +1, весна +2)
        // если проверка на бакалавриат очный пройдена успешно, то всё оки, возвращаем результат семестра


        // разность 4 (*2)  ( осень +1, весна +2)
        // разность 5 (*2)  ( осень +1, весна +2)
        // если проверка на магистратуру пройдена успешно, то всё оки, возвращаем результат семестра
        return new ResponseEntity<>(curSemNumb, HttpStatus.OK);
    }

    @PatchMapping("/getDisciplines")
    public ResponseEntity getDisciplines(@RequestParam(value = "groupId") Long groupId, @RequestParam(value = "semesterNumber") Integer semesterNumber) {
        Group group = groupRepository.findGroupById(groupId);
        if (group == null || group.getSyllabus() == null)
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);


        List<Discipline> disciplines = disciplineRepository.findDisciplinesBySyllabusIdAndSemNumb(group.getSyllabus().getId(), semesterNumber);
        return new ResponseEntity<>(disciplines, HttpStatus.OK);
    }


}
