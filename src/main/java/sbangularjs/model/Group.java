package sbangularjs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "groups")
public class Group {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "group_id")
    private Long id;

    private String number;
    private Boolean active;
    private Integer curSemester;

    @JsonIgnore // need
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="syllabus_id")
    private Syllabus syllabus;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="deanery_id")
    private Deanery deanery;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="deputy_dean_id")
    private DeputyDean deputyDean;

    @JsonIgnore
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Student> students;

    @JsonIgnore
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CertificationAttestation> certificationsAttestation;

    @JsonIgnore
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SessionSheet> sessionSheets;

    @Transient
    private Boolean blank; // for secretaries

    public Group() {}
}
