package sbangularjs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Faculty { // Факультет
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "faculty_id")
    private Long id;

    private String shortName;

    private String fullName;

    @JsonIgnore
    @OneToMany(mappedBy = "faculty", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Department> departments;

    @JsonIgnore
    @OneToMany(mappedBy = "faculty", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attestation> attestations;

    public Faculty() {}
}
