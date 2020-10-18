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

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="syllabus_content_id")
    private SyllabusContent syllabusContent;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="teacher_id")
    private Teacher teacher;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="attestation_id")
    private Attestation attestation;

    @JsonIgnore
    @OneToMany(mappedBy = "certificationAttestation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AttestationContent> attestationContents;

    public CertificationAttestation() {}
}
