package sbangularjs.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SessionDTO { // Subject For Session

    private Long sessionSheetId;

    private Long groupId;
    private String groupNumber;

    private Long disciplineId;
    private String disciplineName;
    private Date deadlineDiscipline;

    private Long splitAttestationFormId;
    private String splitAttestationFormName;

    private boolean finished; // устанавливать true, если все поля content data not null

    public SessionDTO() {}

    public SessionDTO(Long sessionSheetId, Long groupId, String groupNumber,
                      Long disciplineId, String disciplineName, Date deadlineDiscipline,
                      Long splitAttestationFormId, String splitAttestationFormName) {
        this.sessionSheetId = sessionSheetId;

        this.groupId = groupId;
        this.groupNumber = groupNumber;

        this.disciplineId = disciplineId;
        this.disciplineName = disciplineName;
        this.deadlineDiscipline = deadlineDiscipline;

        this.splitAttestationFormId = splitAttestationFormId;
        this.splitAttestationFormName = splitAttestationFormName;
    }

}
