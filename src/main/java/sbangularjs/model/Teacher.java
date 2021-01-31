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
//    @JsonIgnore // сокрытие данных
//    private String password;

    @JsonIgnore
    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CertificationAttestation> certificationsAttestation;

    @JsonIgnore
    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlaceWork> placesWork;

    public Teacher() {}
}
