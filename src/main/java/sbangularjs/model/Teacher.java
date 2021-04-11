package sbangularjs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Teacher {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "teacher_id")
    private Long id;

    private String surname;
    private String name;
    private String initials;
    private String patronymic;

    private boolean active;

    @JsonIgnore // сокрытие данных
    private String email;
    @JsonIgnore // сокрытие данных
    private String username;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="head_department_id")
    private Department department;

    @JsonIgnore
    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlaceWork> placesWork;

    @JsonIgnore
    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AttestationContent> attestationContents;

    @JsonIgnore
    @OneToMany(mappedBy = "setWorksByTeacher", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AttestationContent> attestationContentsSetWorksByTeacher;

    @JsonIgnore
    @OneToMany(mappedBy = "setAttestByTeacher", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AttestationContent> attestationContentsSetAttestByTeacher;

    @JsonIgnore
    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SessionSheetContent> sessionSheetContents;

    @JsonIgnore
    @OneToMany(mappedBy = "setEvaluationByTeacher", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SessionSheetContent> sessionSheetContentsSetEvaluationByTeacher;

    public Teacher() {}
}
