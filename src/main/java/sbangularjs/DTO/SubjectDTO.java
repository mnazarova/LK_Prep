package sbangularjs.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SubjectDTO { // For Attestation

    private Long certificationAttestationId;
//    private Teacher teacher;
    private Long attestationId;

    private Long groupId;
    private String groupNumber;

    private Long disciplineId;
    private String disciplineName;

    private Date lastEdit; // скорее всего, убрать
    private boolean finished; // устанавливать true, если все поля content data not null (отдельно устанавливать?)
// готово к отправке
    public SubjectDTO() {}

    public SubjectDTO(Long certificationAttestationId, Long attestationId,
                      Long disciplineId, String disciplineName,
                      Long groupId, String groupNumber) {
        this.certificationAttestationId = certificationAttestationId;
        this.attestationId = attestationId;
//        this.teacher = teacher;
        this.disciplineId = disciplineId;
        this.disciplineName = disciplineName;

        this.groupId = groupId;
        this.groupNumber = groupNumber;
    }

}
