package sbangularjs.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SubjectDTO {

    private Long subgroupId;
    private String subgroupNumber;

    private Long certificationAttestationId;
    private Long teacherId;
    private Long attestationId;

    private Long groupId;
    private String groupNumber;

    private Long disciplineId;
    private String disciplineName;

    private Date lastEdit; // скорее всего, убрать
    private boolean finished; // устанавливать true, если все поля content data not null (отдельно устанавливать?)
// готово к отправке
    public SubjectDTO() {}

    public SubjectDTO(Long teacherId, Long attestationId,
                      Long disciplineId, String disciplineName, Long groupId,
                      String groupNumber, Date lastEdit, boolean finished) {
        this.teacherId = teacherId;
        this.attestationId = attestationId;

        this.disciplineId = disciplineId;
        this.disciplineName = disciplineName;
        this.groupId = groupId;
        this.groupNumber = groupNumber;
        this.lastEdit = lastEdit;
        this.finished = finished;
    }

    public SubjectDTO(Long certificationAttestationId, Long teacherId, Long attestationId, Long disciplineId, String disciplineName,
                      Long groupId, String groupNumber) {
        this.certificationAttestationId = certificationAttestationId;
        this.teacherId = teacherId;
        this.attestationId = attestationId;

        this.disciplineId = disciplineId;
        this.disciplineName = disciplineName;

        this.groupId = groupId;
        this.groupNumber = groupNumber;
    }

    public SubjectDTO(Long subgroupId, String subgroupNumber, Long certificationAttestationId,
                      Long teacherId, Long attestationId, Long disciplineId, String disciplineName) {
        this.subgroupId = subgroupId;
        this.subgroupNumber = subgroupNumber;

        this.certificationAttestationId = certificationAttestationId;
        this.teacherId = teacherId;
        this.attestationId = attestationId;

        this.disciplineId = disciplineId;
        this.disciplineName = disciplineName;
    }
}
