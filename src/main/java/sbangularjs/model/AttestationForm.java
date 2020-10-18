package sbangularjs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class AttestationForm { // Форма аттестации
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "attestation_form_id")
    private Long id;

    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "attestationForm", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SyllabusContent> syllabusesContent;

    public AttestationForm() {}
}
