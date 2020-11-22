package sbangularjs.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sbangularjs.model.*;
import sbangularjs.repository.*;

import java.util.*;

@Controller
@PreAuthorize("hasAuthority('SECRETARY')")
@AllArgsConstructor(onConstructor = @_(@Autowired))
public class LinkController {
    private SubgroupRepository subgroupRepository;
    private GroupRepository groupRepository;

    private AttestationRepository attestationRepository;
    private AttestationContentRepository attestationContentRepository;
    private SyllabusContentRepository syllabusContentRepository;
    private CertificationAttestationRepository certificationAttestationRepository;

    private DisciplineRepository disciplineRepository;
    private FormOfWorkRepository formOfWorkRepository;

    private StudentSubgroupRepository studentSubgroupRepository;
    private StudentRepository studentRepository;
    private TeacherRepository teacherRepository;
    private SecretaryRepository secretaryRepository;


    @PatchMapping("/findByNameSubgroup")
    public ResponseEntity findByNameSubgroup(@RequestBody Subgroup subgroup) {
        List<Subgroup> subgroups = subgroupRepository.findByNameContainingIgnoreCase(subgroup.getName());
        if (subgroups.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // You many decide to return HttpStatus.NOT_FOUND
        return new ResponseEntity<>(subgroups, HttpStatus.OK);
    }

    @RequestMapping(value = "/findNotExpelledStudents",
            method = RequestMethod.PATCH,
            produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
//    @PatchMapping("/findNotExpelledStudents")
    public /*ResponseEntity*/ List<Student> findNotExpelledStudents(@RequestBody Long groupId) {
        List<Student> students = studentRepository.findByGroupId(groupId);
        if (students.isEmpty()) return null;//new ResponseEntity<>(HttpStatus.NO_CONTENT);

        for(int i=students.size()-1;i>=0;i--) {
            if(students.get(i).getExpelled() != null && students.get(i).getExpelled()) // Expelled = TRUE, отчислены только тогда, когда true
                students.remove(students.get(i));
        }

        if (students.isEmpty()) // все студенты отчислены (переведены)
            return null;//new ResponseEntity<>(HttpStatus.NO_CONTENT); // You many decide to return HttpStatus.NOT_FOUND
        return students;//new ResponseEntity<>(students, HttpStatus.OK);
    }

    @RequestMapping(value = "/findNotExpelledStudentsFromSubgroup",
            method = RequestMethod.PATCH,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
//    @PatchMapping("/findNotExpelledStudentsFromSubgroup")
    public /*ResponseEntity*/List<Student> findNotExpelledStudentsFromSubgroup(@RequestBody Subgroup subgroup) {
        List<Long> studentsIdFromSubgroup = studentSubgroupRepository.findStudentsIdBySubgroupId(subgroup.getId()); // ids
        List<Student> students = studentRepository.findByIds(studentsIdFromSubgroup);
        if (students.isEmpty()) return null; // new ResponseEntity<>(null, HttpStatus.NOT_FOUND);//return null;
        for (int i = students.size() - 1; i >= 0; i--)
            if (students.get(i).getExpelled() != null && students.get(i).getExpelled()) // Expelled = TRUE, отчислены только тогда, когда true
                students.remove(students.get(i));
        if (students.isEmpty()) return null; // new ResponseEntity<>(null, HttpStatus.NOT_FOUND);//return null;
        else return students; // new ResponseEntity<>(students, HttpStatus.OK);//return students;
    }

    @PatchMapping("/getSubjectsWithTeachersByDep")
    public ResponseEntity getSubjectsWithTeachersByDep(@AuthenticationPrincipal User user,
                                                       @RequestParam(value = "groupOrSubgroup") Long groupIdOrSubgroupId,
                                                       @RequestParam(value = "attestation") Long attestationId,
                                                       @RequestParam(value = "isSubgroup") Boolean isSubgroup) {
        Secretary curSecretary = secretaryRepository.findByUsername(user.getUsername());
        if (curSecretary == null || curSecretary.getDepartment() == null)
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);

        List<CertificationAttestation> certificationsAttestationList;
        if (isSubgroup)
            certificationsAttestationList = certificationAttestationRepository.findCAsByAttestationIdAndSubgroupIdAndDepId(attestationId,
                                                                                        groupIdOrSubgroupId, curSecretary.getDepartment().getId());
        else
            certificationsAttestationList = certificationAttestationRepository.findCAsByAttestationIdAndGroupIdAndDepId(attestationId,
                                                                                        groupIdOrSubgroupId, curSecretary.getDepartment().getId());
        if (certificationsAttestationList.isEmpty())
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(certificationsAttestationList, HttpStatus.OK);
    }

    @GetMapping("/getAllFormOfWork")
    public ResponseEntity getAllFormOfWork() {
        List<FormOfWork> formsOfWork = formOfWorkRepository.findAll();
        if (formsOfWork.isEmpty())
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(formsOfWork, HttpStatus.OK);
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
        else if (group.getSyllabus().getQualification().getId() == 4) // Аспирантура
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

    @PatchMapping("/getDisciplinesByDep")
    public ResponseEntity getDisciplinesByDep(@AuthenticationPrincipal User user,
                                              @RequestParam(value = "groupId") Long groupId,
                                              @RequestParam(value = "semesterNumber") Integer semesterNumber) {
        Secretary curSecretary = secretaryRepository.findByUsername(user.getUsername());
        Group group = groupRepository.findGroupById(groupId);
        if (curSecretary == null || group == null || group.getSyllabus() == null)
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);


        List<Discipline> disciplines = disciplineRepository
                        .findDisciplinesBySyllabusIdAndSemNumbAndDepId(group.getSyllabus().getId(),
                                             semesterNumber, curSecretary.getDepartment().getId());
        if (disciplines.isEmpty())
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(disciplines, HttpStatus.OK);
    }

    @PatchMapping("/getTeachers")
    public ResponseEntity getTeachers(@RequestBody Long disciplineId) {
        Discipline discipline = disciplineRepository.findDisciplineById(disciplineId);
        if (discipline == null || discipline.getDepartment() == null)
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        Set<Teacher> teachers = teacherRepository.findByDepartmentId(discipline.getDepartment().getId());
        return new ResponseEntity<>(teachers, HttpStatus.OK);
    }

    @PatchMapping("/addCertificationAttestation") // LINK
    public ResponseEntity addCertificationAttestation(@AuthenticationPrincipal User user,
                                                      @RequestParam(value = "groupId") Long groupForSyllabusId,
                                                      @RequestBody CertificationAttestation certificationAttestation) {
        if (certificationAttestation == null || groupForSyllabusId == null)
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);

        Group groupForSyllabus = groupRepository.findGroupById(groupForSyllabusId);
        if (groupForSyllabus == null || groupForSyllabus.getSyllabus() == null)
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        SyllabusContent syllabusContent = syllabusContentRepository
                .findBySyllabusIdAndDisciplineIdAndSemesterNumber(groupForSyllabus.getSyllabus().getId(),
                        certificationAttestation.getSyllabusContent().getDiscipline().getId(),
                        certificationAttestation.getSyllabusContent().getSemesterNumber());
        if (syllabusContent == null)
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);

        CertificationAttestation newCertificationAttestation = new CertificationAttestation();
        newCertificationAttestation.setSyllabusContent(syllabusContent);
        newCertificationAttestation.setTeacher(teacherRepository.findTeacherById(certificationAttestation.getTeacher().getId()));
        newCertificationAttestation.setAttestation(attestationRepository.findAttestationById(certificationAttestation.getAttestation().getId()));
        newCertificationAttestation.setIsSubgroup(certificationAttestation.getIsSubgroup());
        newCertificationAttestation.setForm_of_work(formOfWorkRepository.findFormOfWorkById(certificationAttestation.getForm_of_work().getId()));

        List<Student> students;
        if (certificationAttestation.getIsSubgroup()) {
            Subgroup subgroup = subgroupRepository.findSubgroupById(certificationAttestation.getSubgroup().getId());
            if (subgroup == null)
                return new ResponseEntity<>(null, HttpStatus.CONFLICT);

            newCertificationAttestation.setSubgroup(subgroup);
            students = findNotExpelledStudentsFromSubgroup(subgroup);
        } else {
            Group group = groupRepository.findGroupById(certificationAttestation.getGroup().getId());
            if (group == null)
                return new ResponseEntity<>(null, HttpStatus.CONFLICT);

            newCertificationAttestation.setGroup(group);
            students = findNotExpelledStudents(group.getId());
        }
        certificationAttestationRepository.save(newCertificationAttestation);

        List<AttestationContent> attestationContents = new ArrayList<>();
        for (Student student : students)
            attestationContents.add(new AttestationContent(newCertificationAttestation, student));
        attestationContentRepository.saveAll(attestationContents);

        Long groupIdOrSubgroupId;
        if (certificationAttestation.getIsSubgroup())
            groupIdOrSubgroupId = certificationAttestation.getSubgroup().getId();
        else
            groupIdOrSubgroupId = certificationAttestation.getGroup().getId();
        return getSubjectsWithTeachersByDep(user, groupIdOrSubgroupId, certificationAttestation.getAttestation().getId(), certificationAttestation.getIsSubgroup());
    }

    @PatchMapping("/checkBeforeDeleteDiscipline")
    public ResponseEntity checkBeforeDeleteDiscipline(@RequestParam Long certificationAttestationId) {
        List<AttestationContent> attestationContents = attestationContentRepository.findAllByCertificationAttestationId(certificationAttestationId);
        for (AttestationContent attestationContent : attestationContents)
            if (attestationContent.getDateAttest() != null || attestationContent.getDateWorks() != null)
                return new ResponseEntity<>("Нельзя удалять, так как преподавателем уже были внесены изменения!", HttpStatus.CONFLICT); // status = 409

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PatchMapping("/deleteDiscipline")
    public ResponseEntity deleteDiscipline(@AuthenticationPrincipal User user,
                                           @RequestParam Long certificationAttestationId) {
        CertificationAttestation certificationAttestation = certificationAttestationRepository.findCertificationAttestationById(certificationAttestationId);
        if (certificationAttestation == null)
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);

        List<AttestationContent> attestationContents = attestationContentRepository.findAllByCertificationAttestationId(certificationAttestationId);
        if (attestationContents == null) // хоть один студент должен быть, если не нашлось, то ошибка
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);

        attestationContentRepository.deleteAll(attestationContents);
        certificationAttestationRepository.delete(certificationAttestation);

        Long groupIdOrSubgroupId;
        if (certificationAttestation.getIsSubgroup())
            groupIdOrSubgroupId = certificationAttestation.getSubgroup().getId();
        else
            groupIdOrSubgroupId = certificationAttestation.getGroup().getId();
        return getSubjectsWithTeachersByDep(user, groupIdOrSubgroupId, certificationAttestation.getAttestation().getId(), certificationAttestation.getIsSubgroup());
    }
}
