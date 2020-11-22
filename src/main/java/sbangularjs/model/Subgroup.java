package sbangularjs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Subgroup {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "subgroup_id")
    private Long id;

    private String name;

    private Boolean active;

    @Transient
    private List<Student> students;

    @JsonIgnore
    @OneToMany(mappedBy = "subgroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudentSubgroup> studentsSubgroup;

    @JsonIgnore
    @OneToMany(mappedBy = "subgroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CertificationAttestation> certificationsAttestation;

    @JsonIgnore
    @OneToMany(mappedBy = "subgroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeaneryGroupOrSubgroup> deaneryGroupOrSubgroupList;

    public Subgroup() {}

    public Subgroup(String name, Boolean active) {
        this.name = name;
        this.active = active;
    }
}
