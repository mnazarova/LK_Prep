package sbangularjs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class SessionSheet { // Сессионная ведомость
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "session_sheet_id")
    private Long id;

    private Long idSpec1;
    private Long idSpec2;
    private Boolean isAdditional;

//    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="syllabus_content_id")
    private SyllabusContent syllabusContent;

//    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="group_id")
    private Group group;

//    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="split_attestation_form_id")
    private SplitAttestationForm splitAttestationForm;

    @JsonIgnore
    @OneToMany(mappedBy = "sessionSheet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SessionSheetContent> sessionSheetContents;

    public SessionSheet() {}
}
