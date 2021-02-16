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
import sbangularjs.model.*;
import sbangularjs.repository.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@PreAuthorize("hasAuthority('SECRETARY')")
@AllArgsConstructor(onConstructor = @_(@Autowired))
public class LinkSessionController {

    private GroupRepository groupRepository;
    private SecretaryRepository secretaryRepository;
    private SyllabusContentRepository syllabusContentRepository;
    private SessionSheetRepository sessionSheetRepository;
    private SessionSheetContentRepository sessionSheetContentRepository;

    @PatchMapping("/sessionGetGroupListByActiveIsTrue")
    public ResponseEntity sessionGetGroupListByActiveIsTrue(@AuthenticationPrincipal User user) {
        List<Group> groupList = groupRepository.getGroupListByActiveIsTrue();
        if (groupList.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // You many decide to return HttpStatus.NOT_FOUND
        return new ResponseEntity<>(setBlankForGroups(user, groupList), HttpStatus.OK);
    }

    private List<Group> setBlankForGroups(User user, List<Group> groupList) {
        Secretary curSecretary = secretaryRepository.findByUsername(user.getUsername());
//        if (curSecretary == null || curSecretary.getDepartment() == null)
//            return new ResponseEntity<>(1, HttpStatus.CONFLICT);
        for (Group gr: groupList) {
            if (gr.getCurSemester() == null || gr.getSyllabus() == null) {
                gr.setBlank(null);
                continue;
            }
            List<SyllabusContent> syllabusContents = syllabusContentRepository.findSyllabusContentsBySyllabusIdAndDepartmentIdAndCurSemester(
                    gr.getSyllabus().getId(), curSecretary.getDepartment().getId(), gr.getCurSemester());
            if (syllabusContents.isEmpty()) { // нет предметов в текущем семестре у группы на кафедре секретаря
                gr.setBlank(false);
                continue;
            }
            List<Student> students = gr.getStudents();
            for(int i=students.size()-1;i>=0;i--) {
                if(students.get(i).getExpelled() != null && students.get(i).getExpelled()) // Expelled = TRUE, отчислены только тогда, когда true
                    students.remove(students.get(i));
            }
            if (students.isEmpty()) {
                gr.setBlank(null);
                continue;
            }

            gr.setBlank(checkAllSyllabusContentAndStudents(syllabusContents, students, gr.getId())); // true - exist blank field
        }
        return groupList;
    }

    private boolean checkAllSyllabusContentAndStudents(List<SyllabusContent> syllabusContents, List<Student> students, Long groupId) {
        for (SyllabusContent sc: syllabusContents) {
            for (Student student: students) {
                if (sc.getAttestationForm().getName().contains("Экзамен")) {
                    if (findNullTeacher(sc.getId(), groupId, 6L, student.getId())) // Допуск
                        return true;
                    if (findNullTeacher(sc.getId(), groupId, 1L, student.getId())) // Экзамен
                        return true;
                }
                if (sc.getAttestationForm().getName().contains("Дифф. зачет"))
                    if (findNullTeacher(sc.getId(), groupId, 2L, student.getId())) return true; // Дифф. зачет
                if (sc.getAttestationForm().getName().contains("Зачет")) // регистрозависимый метод
                    if (findNullTeacher(sc.getId(), groupId, 3L, student.getId())) return true; // Зачет

                if (sc.getAttestationForm().getName().contains("КР")) // регистрозависимый метод
                    if (findNullTeacher(sc.getId(), groupId, 4L, student.getId())) return true; // КР
                if (sc.getAttestationForm().getName().contains("КП")) // регистрозависимый метод
                    if (findNullTeacher(sc.getId(), groupId, 5L, student.getId())) return true; // КП

            }
        }
        return false;
    }

    private boolean findNullTeacher(Long syllabusContentId, Long groupId, Long splitAttestationFormId, Long studentId) {
        SessionSheet sessionSheet = sessionSheetRepository.findSessionSheetBySyllabusContentIdAndGroupIdAndSplitAttestationFormId(
                syllabusContentId, groupId, splitAttestationFormId);
        if (sessionSheet == null || sessionSheet.getId() == null)
            return true;
        SessionSheetContent sessionSheetContent = sessionSheetContentRepository.findSessionSheetContentBySessionSheetIdAndStudentId(sessionSheet.getId(), studentId);
        return sessionSheetContent == null || sessionSheetContent.getTeacher() == null || sessionSheetContent.getTeacher().getId() == null; // false - если преподаватель назначен
    }

    @PatchMapping("/sessionSelectedGroup")
    public ResponseEntity sessionSelectedGroup(@AuthenticationPrincipal User user, @RequestParam Long groupId) {
        Group group = groupRepository.findGroupById(groupId);
        if (group == null)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // You many decide to return HttpStatus.NOT_FOUND
        List<Group> groupList = new ArrayList<>();
        groupList.add(group);
        return new ResponseEntity<>(setBlankForGroups(user, groupList).get(0), HttpStatus.OK);
    }
}
