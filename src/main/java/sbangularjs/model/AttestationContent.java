package sbangularjs.model;

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
    private Boolean active;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateWorks;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateAttest;

    @ManyToOne(fetch = FetchType.EAGER) // или @JsonIgnoreProperties ({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name="student_id")
    private Student student;

//    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER) // или @JsonIgnoreProperties ({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name="certification_attestation_id")
    private CertificationAttestation certificationAttestation; // ведомость аттестации

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="teacher_id")
    private Teacher teacher;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="set_works_by_teacher")
    private Teacher setWorksByTeacher; // какой преподаватель установил поле "Работает"

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="set_attest_by_teacher")
    private Teacher setAttestByTeacher; // какой преподаватель установил поле "Аттестован"

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
