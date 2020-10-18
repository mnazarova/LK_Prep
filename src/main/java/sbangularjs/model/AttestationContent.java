package sbangularjs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
public class AttestationContent { // Содержание аттестации
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "attestation_content_id")
    private Long id;

    private Boolean works; // Работает?
    private Boolean attest; // Аттестован?

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateWorks;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateAttest;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="certification_attestation_id")
    private CertificationAttestation certificationAttestation; // ведомость аттестации

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="student_id")
    private Student student;

    public AttestationContent() {}
}
