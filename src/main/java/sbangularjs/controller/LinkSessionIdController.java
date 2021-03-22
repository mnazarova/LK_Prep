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
import sbangularjs.DTO.ConnectTeacherStudentDTO;
import sbangularjs.model.*;
import sbangularjs.repository.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Controller
@PreAuthorize("hasAuthority('SECRETARY')")
@AllArgsConstructor(onConstructor = @_(@Autowired))
public class LinkSessionIdController {

    private SecretaryRepository secretaryRepository;
    private StudentRepository studentRepository;
    private TeacherRepository teacherRepository;

    private GroupRepository groupRepository;
    private SyllabusContentRepository syllabusContentRepository;
    private SessionSheetRepository sessionSheetRepository;
    private SessionSheetContentRepository sessionSheetContentRepository;
    private SplitAttestationFormRepository splitAttestationFormRepository;

    @PatchMapping("/sessionGetGroupById")
    public ResponseEntity sessionGetGroupById(@RequestParam Long groupId) {
        Group group = groupRepository.findGroupById(groupId);
        if (group == null || group.getActive() == null || !group.getActive())
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        return new ResponseEntity<>(group, HttpStatus.OK);
    }

    @PatchMapping("/sessionGetTeacherListByDepartmentId")
    public ResponseEntity sessionGetTeacherListByDepartmentId(@AuthenticationPrincipal User user) {
        Secretary curSecretary = secretaryRepository.findByUsername(user.getUsername());
        if (curSecretary == null || curSecretary.getDepartment() == null)
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        List<Teacher> teacherList = teacherRepository.findByDepartmentIdAndActiveIsTrue(curSecretary.getDepartment().getId());
        if (teacherList == null)
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        return new ResponseEntity<>(teacherList, HttpStatus.OK);
    }

    @PatchMapping("/getSyllabusContentListWithoutAttestationByGroupIdAndDepartmentId")
    public ResponseEntity getSyllabusContentListWithoutAttestationByGroupIdAndDepartmentId(@AuthenticationPrincipal User user, @RequestParam Long groupId) {
        Secretary curSecretary = secretaryRepository.findByUsername(user.getUsername());
        Group group = groupRepository.findGroupById(groupId);
        if (group == null || group.getCurSemester() == null)
            return new ResponseEntity<>(0, HttpStatus.CONFLICT);

        if (curSecretary == null || curSecretary.getDepartment() == null || group.getSyllabus() == null)
            return new ResponseEntity<>(1, HttpStatus.CONFLICT);

        List<SyllabusContent> syllabusContents = syllabusContentRepository.findSyllabusContentsBySyllabusIdAndDepartmentIdAndCurSemester(
                group.getSyllabus().getId(), curSecretary.getDepartment().getId(), group.getCurSemester());
        if (syllabusContents.isEmpty())
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

        List<Student> students = studentRepository.findNotExpelledStudentsByGroupId(groupId);
        if (students.isEmpty()) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT); // нет студентов в группе (или все отчислены, переведены)

        for (SyllabusContent sc: syllabusContents) {
            sc.setConnectTeacherStudentDTOList(setConnectTeacherStudentDTO(students, groupId, sc));
        }
        return new ResponseEntity<>(syllabusContents, HttpStatus.OK);
    }

    private List<ConnectTeacherStudentDTO> setConnectTeacherStudentDTO(List<Student> students, Long groupId, SyllabusContent sc) {
        List<ConnectTeacherStudentDTO> connectTeacherStudentDTOList = new ArrayList<>();
        for (Student student: students) {
            ConnectTeacherStudentDTO connectTeacherStudentDTO = new ConnectTeacherStudentDTO();

            connectTeacherStudentDTO.setStudentId(student.getId());
            connectTeacherStudentDTO.setStudentFullName(String.format("%s %s %s", student.getSurname() != null ? student.getSurname() :' ',
                    student.getName() != null ? student.getName():' ', student.getPatronymic()  != null ? student.getPatronymic():' '));

            /*установка преподавателей*/
            if (sc.getAttestationForm().getName().contains("Экзамен")) {
                connectTeacherStudentDTO.setAdmittanceTeacher(searchAndSetTeacher(sc.getId(), groupId, 6L, student.getId())); // Допуск
                connectTeacherStudentDTO.setExamTeacher(searchAndSetTeacher(sc.getId(), groupId, 1L, student.getId())); // Экзамен
            }

            if (sc.getAttestationForm().getName().contains("Дифф. зачет"))
                connectTeacherStudentDTO.setExamTeacher(searchAndSetTeacher(sc.getId(), groupId, 2L, student.getId())); // Дифф. зачет
            if (sc.getAttestationForm().getName().contains("Зачет")) // регистрозависимый метод
                connectTeacherStudentDTO.setExamTeacher(searchAndSetTeacher(sc.getId(), groupId, 3L, student.getId())); // Зачет

            if (sc.getAttestationForm().getName().contains("КР")) // регистрозависимый метод
                connectTeacherStudentDTO.setKrOrKpTeacher(searchAndSetTeacher(sc.getId(), groupId, 4L, student.getId())); // КР
            if (sc.getAttestationForm().getName().contains("КП")) // регистрозависимый метод
                connectTeacherStudentDTO.setKrOrKpTeacher(searchAndSetTeacher(sc.getId(), groupId, 5L, student.getId())); // КП
            /*установка преподавателей*/

            connectTeacherStudentDTOList.add(connectTeacherStudentDTO);
        }
        return connectTeacherStudentDTOList;
    }

    private Teacher searchAndSetTeacher(Long syllabusContentId, Long groupId, Long splitAttestationFormId, Long studentId) {
        SessionSheet sessionSheet = sessionSheetRepository.findSessionSheetBySyllabusContentIdAndGroupIdAndSplitAttestationFormId(
                syllabusContentId, groupId, splitAttestationFormId);
        if (sessionSheet != null) {
            SessionSheetContent sessionSheetContent = sessionSheetContentRepository.findSessionSheetContentBySessionSheetIdAndStudentId(
                    sessionSheet.getId(), studentId);
            if (sessionSheetContent != null)
                return sessionSheetContent.getTeacher();
        }
        return null;
    }

    @Transactional
    @PatchMapping("/saveTeachersByDiscipline")
    public ResponseEntity saveTeachersByDiscipline(@RequestBody SyllabusContent syllabusContent, @RequestParam Long groupId) {
//        try {
            if (syllabusContent == null || syllabusContent.getId() == null || syllabusContent.getConnectTeacherStudentDTOList() == null)
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

        SessionSheet sessionSheet;
        List<SessionSheetContent> sessionSheetContents;
        if (syllabusContent.getAttestationForm().getName().contains("Экзамен")) { // регистрозависимый метод
//                saveOrUpdateSessionSheet(6L, ConnectTeacherStudentDTO.class.getDeclaredMethod("getAdmittanceTeacher"), syllabusContent.getId(), groupId, syllabusContent.getConnectTeacherStudentDTOList());

            sessionSheet = findOrSetSessionSheet(syllabusContent.getId(), groupId, 6L);  // Допуск
            sessionSheetContents = new ArrayList<>();
            for (ConnectTeacherStudentDTO connectTeacherStudentDTO : syllabusContent.getConnectTeacherStudentDTOList()) {
                SessionSheetContent sessionSheetContent = findOrSetSessionSheetContent(sessionSheet, connectTeacherStudentDTO.getStudentId());

                if (connectTeacherStudentDTO.getAdmittanceTeacher() != null)
                    sessionSheetContent.setTeacher(connectTeacherStudentDTO.getAdmittanceTeacher());

                sessionSheetContents.add(sessionSheetContent);
            }
            sessionSheet.setSessionSheetContents(sessionSheetContents);
            sessionSheetRepository.save(sessionSheet);
            
            saveOrUpdateExamOrPass(1L, syllabusContent.getId(), groupId, syllabusContent.getConnectTeacherStudentDTOList()); // Экзамен
        }

        if (syllabusContent.getAttestationForm().getName().contains("Дифф. зачет")) // регистрозависимый метод
            saveOrUpdateExamOrPass(2L, syllabusContent.getId(), groupId, syllabusContent.getConnectTeacherStudentDTOList());
        if (syllabusContent.getAttestationForm().getName().contains("Зачет")) // регистрозависимый метод
            saveOrUpdateExamOrPass(3L, syllabusContent.getId(), groupId, syllabusContent.getConnectTeacherStudentDTOList());

        if (syllabusContent.getAttestationForm().getName().contains("КР")) // регистрозависимый метод
            saveOrUpdateKrOrKp(4L, syllabusContent.getId(), groupId, syllabusContent.getConnectTeacherStudentDTOList());
        if (syllabusContent.getAttestationForm().getName().contains("КП")) // регистрозависимый метод
            saveOrUpdateKrOrKp(5L, syllabusContent.getId(), groupId, syllabusContent.getConnectTeacherStudentDTOList());

        SyllabusContent sc = syllabusContentRepository.findSyllabusContentById(syllabusContent.getId());
        List<Student> students = studentRepository.findNotExpelledStudentsByGroupId(groupId); // не отчисленные к данному моменту студенты группы
        if (students.isEmpty()) return new ResponseEntity<>(null, HttpStatus.OK); // все студенты отчислены или переведены
        sc.setConnectTeacherStudentDTOList(setConnectTeacherStudentDTO(students, groupId, sc));

        return new ResponseEntity<>(sc, HttpStatus.OK);

//        getSyllabusContentListWithoutAttestationByGroupIdAndDepartmentId(user, groupId)
        /*}
        catch (NoSuchMethodException e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }*/
    }

    private void saveOrUpdateExamOrPass(Long splitAttestationFormId, Long syllabusContentId, Long groupId, List<ConnectTeacherStudentDTO> connectTeacherStudentDTOList) {
        SessionSheet sessionSheet = findOrSetSessionSheet(syllabusContentId, groupId, splitAttestationFormId);
        List<SessionSheetContent> sessionSheetContents = new ArrayList<>();
        for (ConnectTeacherStudentDTO connectTeacherStudentDTO : connectTeacherStudentDTOList) {
            SessionSheetContent sessionSheetContent = findOrSetSessionSheetContent(sessionSheet, connectTeacherStudentDTO.getStudentId());

            if (connectTeacherStudentDTO.getExamTeacher() != null)
                sessionSheetContent.setTeacher(connectTeacherStudentDTO.getExamTeacher());

            sessionSheetContents.add(sessionSheetContent);
        }
        sessionSheet.setSessionSheetContents(sessionSheetContents);
        sessionSheetRepository.save(sessionSheet);
    }

    private void saveOrUpdateKrOrKp(Long splitAttestationFormId, Long syllabusContentId, Long groupId, List<ConnectTeacherStudentDTO> connectTeacherStudentDTOList) {
        SessionSheet sessionSheet = findOrSetSessionSheet(syllabusContentId, groupId, splitAttestationFormId);
        List<SessionSheetContent> sessionSheetContents = new ArrayList<>();
        for (ConnectTeacherStudentDTO connectTeacherStudentDTO : connectTeacherStudentDTOList) {
            SessionSheetContent sessionSheetContent = findOrSetSessionSheetContent(sessionSheet, connectTeacherStudentDTO.getStudentId());

            if (connectTeacherStudentDTO.getKrOrKpTeacher() != null)
                sessionSheetContent.setTeacher(connectTeacherStudentDTO.getKrOrKpTeacher());

            sessionSheetContents.add(sessionSheetContent);
        }
        sessionSheet.setSessionSheetContents(sessionSheetContents);
        sessionSheetRepository.save(sessionSheet);
    }

    private SessionSheetContent findOrSetSessionSheetContent(SessionSheet sessionSheet, Long connectTeacherStudentDTOStudentId) {
        SessionSheetContent sessionSheetContent = new SessionSheetContent();
        if (sessionSheet!= null && sessionSheet.getId() != null) { // Сессионная ведомость была создана
            sessionSheetContent = sessionSheetContentRepository.findSessionSheetContentBySessionSheetIdAndStudentId(
                    sessionSheet.getId(), connectTeacherStudentDTOStudentId);
            if (sessionSheetContent == null)
                sessionSheetContent = new SessionSheetContent();
        }
        sessionSheetContent.setSessionSheet(sessionSheet);
        sessionSheetContent.setStudent(studentRepository.findStudentById(connectTeacherStudentDTOStudentId));

        /*Стирание данных сессионного оценивания*/
        sessionSheetContent.setEvaluation(null);
        sessionSheetContent.setDate(null);

        sessionSheetContent.setActive(true);

        return sessionSheetContent;
    }

    private SessionSheet findOrSetSessionSheet(Long syllabusContentId, Long groupId, Long splitAttestationFormId) {
        SessionSheet sessionSheet = sessionSheetRepository.findSessionSheetBySyllabusContentIdAndGroupIdAndSplitAttestationFormId(
                syllabusContentId, groupId, splitAttestationFormId);
        if (sessionSheet == null) { // новая ведомость
            sessionSheet = new SessionSheet();
            sessionSheet.setSyllabusContent(syllabusContentRepository.findSyllabusContentById(syllabusContentId));
            sessionSheet.setGroup(groupRepository.findGroupById(groupId));
            sessionSheet.setSplitAttestationForm(splitAttestationFormRepository.findSplitAttestationFormById(splitAttestationFormId));
        }
        return sessionSheet;
    }

    /*private boolean saveOrUpdateSessionSheet(Long splitAttestationFormId, Method addingTeacher, Long syllabusContentId,
                                             Long groupId, List<ConnectTeacherStudentDTO> connectTeacherStudentDTOList) {
        SessionSheet sessionSheet = sessionSheetRepository.findSessionSheetBySyllabusContentIdAndGroupIdAndSplitAttestationFormId(
                syllabusContentId, groupId, splitAttestationFormId); // Допуск
        if (sessionSheet == null) {
            sessionSheet = new SessionSheet();
            sessionSheet.setSyllabusContent(syllabusContentRepository.findSyllabusContentById(syllabusContentId));
            sessionSheet.setGroup(groupRepository.findGroupById(groupId));
            sessionSheet.setSplitAttestationForm(splitAttestationFormRepository.findSplitAttestationFormById(splitAttestationFormId));
        }

        List<SessionSheetContent> sessionSheetContents = new ArrayList<>();
        for (ConnectTeacherStudentDTO connectTeacherStudentDTO : connectTeacherStudentDTOList) {
            SessionSheetContent sessionSheetContent = new SessionSheetContent();
            if (sessionSheet.getId() != null) { // Сессионная ведомость была создана
                sessionSheetContent = sessionSheetContentRepository.findSessionSheetContentBySessionSheetIdAndStudentId(
                        sessionSheet.getId(), connectTeacherStudentDTO.getStudentId());
                if (sessionSheetContent == null)
                    sessionSheetContent = new SessionSheetContent();
            }
            sessionSheetContent.setSessionSheet(sessionSheet);
            sessionSheetContent.setStudent(studentRepository.findStudentById(connectTeacherStudentDTO.getStudentId()));
            addingTeacher.invoke();
            if (connectTeacherStudentDTO.addingTeacher() != null)
                sessionSheetContent.setTeacher(connectTeacherStudentDTO.addingTeacher());

            sessionSheetContents.add(sessionSheetContent);
        }
        sessionSheet.setSessionSheetContents(sessionSheetContents);
        sessionSheetRepository.save(sessionSheet);
        return true; // success
    }*/

}
