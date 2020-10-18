package sbangularjs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
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

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="syllabus_id")
    private Syllabus syllabus;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="discipline_id")
    private Discipline discipline;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="attestation_form_id")
    private AttestationForm attestationForm;

    @JsonIgnore
    @OneToMany(mappedBy = "syllabusContent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CertificationAttestation> certificationsAttestation;

    public SyllabusContent() {}
}
