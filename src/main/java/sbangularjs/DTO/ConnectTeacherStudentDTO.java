package sbangularjs.DTO;

import lombok.Getter;
import lombok.Setter;
import sbangularjs.model.Teacher;

@Getter
@Setter
public class ConnectTeacherStudentDTO {

    private Long studentId;
    private String studentFullName;

    private Teacher attestationTeacher;
    // Допуск
    private Teacher admittanceTeacher;
    // Экзамен/Зачёт/Диф.зачёт
    private Teacher examTeacher;
    // КП/КР
    private Teacher krOrKpTeacher;

    public ConnectTeacherStudentDTO() {}

    public ConnectTeacherStudentDTO(Long studentId, String studentFullName, Teacher attestationTeacher,
                                    Teacher admittanceTeacher, Teacher examTeacher,
                                    Teacher krOrKpTeacher) {
        this.studentId = studentId;
        this.studentFullName = studentFullName;

        this.attestationTeacher = attestationTeacher;
        this.admittanceTeacher = admittanceTeacher;
        this.examTeacher = examTeacher;
        this.krOrKpTeacher = krOrKpTeacher;
    }
}
