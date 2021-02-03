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

    // По умолчанию ставить false
    private Boolean works; // Работает?
    private Boolean attest; // Аттестован?

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateWorks;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateAttest;

    @ManyToOne(fetch = FetchType.EAGER) // или @JsonIgnoreProperties ({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name="student_id")
    private Student student;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY) // или @JsonIgnoreProperties ({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name="certification_attestation_id")
    private CertificationAttestation certificationAttestation; // ведомость аттестации

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="teacher_id")
    private Teacher teacher;

    public AttestationContent() {
//        works = false;
//        attest = false;
    }

    public AttestationContent(CertificationAttestation ca, Student stud) {
        certificationAttestation = ca;
        student = stud;
//        works = false;
//        attest = false;
    }
}
