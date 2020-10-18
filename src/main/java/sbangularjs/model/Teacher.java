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
    private String patronymic;

    private String email;

    private String username;
    private String password;
    private boolean active;

    @JsonIgnore
    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CertificationAttestation> certificationsAttestation;

    public Teacher() {}
}
