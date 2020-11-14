package sbangularjs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class FormOfWork { // Форма работы
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "form_of_work_id")
    private Long id;

    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "form_of_work", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CertificationAttestation> certificationsAttestation;

    public FormOfWork() {}
}
