package sbangularjs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import sbangularjs.DTO.ConnectTeacherStudentDTO;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
public class SyllabusContent { // Содержание учебного плана
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "syllabus_content_id")
    private Long id;

    private Integer semesterNumber;

    @Temporal(TemporalType.TIMESTAMP)
    private Date deadline;

//    @JsonIgnore
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="syllabus_id")
    private Syllabus syllabus;

//    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="discipline_id")
    private Discipline discipline;

//    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="attestation_form_id")
    private AttestationForm attestationForm;

    @JsonIgnore
    @OneToMany(mappedBy = "syllabusContent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CertificationAttestation> certificationsAttestation;

    @JsonIgnore
    @OneToMany(mappedBy = "syllabusContent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SessionSheet> sessionSheets;

    @Transient
    private List<ConnectTeacherStudentDTO> ConnectTeacherStudentDTOList;

    public SyllabusContent() {}
}
