package sbangularjs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
public class Attestation {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "attestation_id")
    private Long id;

    private String name;
    private Boolean active;

    @Temporal(TemporalType.TIMESTAMP)
    private Date deadline;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="faculty_id")
    private Faculty faculty;

    @JsonIgnore
    @OneToMany(mappedBy = "attestation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CertificationAttestation> certificationsAttestation;

    public Attestation() {}

    public Attestation(String name, Date deadline) {
        this.name = name;
        this.deadline = deadline;
    }
}