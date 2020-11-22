package sbangularjs.DTO;

import lombok.Getter;
import lombok.Setter;
import sbangularjs.model.Teacher;

import java.util.Date;

@Getter
@Setter
public class SubjectDTO {

    private Long subgroupId;
    private String subgroupName;

    private Long certificationAttestationId;
    private Teacher teacher;
    private String form_of_workName;
    private Long attestationId;

    private Long groupId;
    private String groupNumber;

    private Long disciplineId;
    private String disciplineName;

    private Date lastEdit; // скорее всего, убрать
    private boolean finished; // устанавливать true, если все поля content data not null (отдельно устанавливать?)
// готово к отправке
    public SubjectDTO() {}

    /*public SubjectDTO(Long teacherId, Long attestationId,
                      Long disciplineId, String disciplineName, Long groupId,
                      String groupNumber, Date lastEdit, boolean finished) {
        this.teacher = teacher;
        this.attestationId = attestationId;

        this.disciplineId = disciplineId;
        this.disciplineName = disciplineName;
        this.groupId = groupId;
        this.groupNumber = groupNumber;
        this.lastEdit = lastEdit;
        this.finished = finished;
    }*/

    public SubjectDTO(Long certificationAttestationId, String form_of_workName, Long attestationId,
                      Teacher teacher/*Long teacherId, String teacherFullName*/, Long disciplineId, String disciplineName,
                      Long groupId, String groupNumber) {
        this.certificationAttestationId = certificationAttestationId;
        this.form_of_workName = form_of_workName;
        this.attestationId = attestationId;

        this.teacher = teacher;

        this.disciplineId = disciplineId;
        this.disciplineName = disciplineName;

        this.groupId = groupId;
        this.groupNumber = groupNumber;
    }

    public SubjectDTO(Long subgroupId, String subgroupName,
                      Long certificationAttestationId, String form_of_workName, Long attestationId,
                      Teacher teacher /*Long teacherId, String teacherFullName*/, Long disciplineId, String disciplineName) {
        this.subgroupId = subgroupId;
        this.subgroupName = subgroupName;

        this.certificationAttestationId = certificationAttestationId;
        this.form_of_workName = form_of_workName;
        this.attestationId = attestationId;

        this.teacher = teacher;

        this.disciplineId = disciplineId;
        this.disciplineName = disciplineName;
    }
}
