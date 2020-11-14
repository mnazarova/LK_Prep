package sbangularjs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class CertificationAttestation { // Ведомость аттестации
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "certification_attestation_id")
    private Long id;

//    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="syllabus_content_id")
    private SyllabusContent syllabusContent;

//    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="teacher_id")
    private Teacher teacher;

//    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="attestation_id")
    private Attestation attestation;

    @JsonIgnore
    @OneToMany(mappedBy = "certificationAttestation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AttestationContent> attestationContents;

    private Boolean isSubgroup; // Подгруппа?

//    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="group_id")
    private Group group;

//    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="subgroup_id")
    private Subgroup subgroup;

//  @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="form_of_work_id")
    private FormOfWork form_of_work;

    public CertificationAttestation() {}
}
